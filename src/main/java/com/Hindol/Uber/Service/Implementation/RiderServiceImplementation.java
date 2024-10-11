package com.Hindol.Uber.Service.Implementation;

import com.Hindol.Uber.DTO.*;
import com.Hindol.Uber.Entity.*;
import com.Hindol.Uber.Entity.Enum.RideRequestStatus;
import com.Hindol.Uber.Entity.Enum.RideStatus;
import com.Hindol.Uber.Exception.ResourceNotFoundException;
import com.Hindol.Uber.Repository.RideRequestRepository;
import com.Hindol.Uber.Repository.RiderRepository;
import com.Hindol.Uber.Service.*;
import com.Hindol.Uber.Strategy.RideStrategyManager;
import com.Hindol.Uber.Util.EmailUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiderServiceImplementation implements RiderService {

    private final ModelMapper modelMapper;
    private final RideStrategyManager rideStrategyManager;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;
    private final RideService rideService;
    private final DriverService driverService;
    private final RatingService ratingService;
    private final EmailSenderService emailSenderService;
    private final UserService userService;
    @Override
    @Transactional
    public RideRequestDTO requestRide(RideRequestDTO rideRequestDTO) {
        log.info("Requesting Ride");
        Rider rider = this.getCurrentRider();
        RideRequest rideRequest = modelMapper.map(rideRequestDTO, RideRequest.class);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        rideRequest.setRider(rider);

        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare);

        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);
        List<Driver> driverList = rideStrategyManager.driverMatchingStrategy(rider.getRating()).findMatchingDriver(rideRequest);

        String riderName = rider.getUser().getName();
        String subject = EmailUtil.generateRideRequestEmailSubject(riderName);

        for(Driver driver : driverList) {
            String body = EmailUtil.generateRideRequestEmail(rideRequest, driver);
            String driverEmail = driver.getUser().getEmail();
            emailSenderService.sendEmail(driverEmail, subject, body);
        }
        log.info("Successfully requested Ride");
        return modelMapper.map(savedRideRequest, RideRequestDTO.class);
    }

    @Override
    public RideDTO cancelRide(Long rideId) {
        log.info("Cancelling Ride");
        Rider rider = this.getCurrentRider();
        Ride ride = rideService.getRideById(rideId);
        if(!ride.getRider().equals(rider)) {
            throw new RuntimeException("Rider does not own this ride with ID : " + rideId);
        }
        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)) {
            throw new RuntimeException("Ride cannot be cancelled, invalid status : " + ride.getRideStatus());
        }
        Ride updatedRide = rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        driverService.updateDriverAvailability(ride.getDriver(), true);
        log.info("Successfully cancelled Ride");
        return modelMapper.map(updatedRide, RideDTO.class);
    }

    @Override
    public DriverDTO rateDriver(Long rideId, Integer rating) {
        log.info("Rating Driver for Ride with ID : {}", rideId);
        Ride ride = rideService.getRideById(rideId);
        Rider rider = getCurrentRider();
        if(!rider.equals(ride.getRider())) {
            throw new RuntimeException("Rider is not owner of ride");
        }
        if(!ride.getRideStatus().equals(RideStatus.ENDED)) {
            throw new RuntimeException("Ride status is not ended, hence cannot rate driver");
        }
        log.info("Successfully rated Driver");
        return ratingService.rateDriver(ride, rating);
    }

    @Override
    public RiderDTO getMyProfile() {
        log.info("Getting Rider profile");
        Rider rider = getCurrentRider();
        log.info("Successfully fetched Rider profile");
        return modelMapper.map(rider, RiderDTO.class);
    }

    @Override
    public Page<RideDTO> getAllMyRides(PageRequest pageRequest) {
        log.info("Fetching all rides of Rider");
        Rider currentRider = getCurrentRider();
        Page<RideDTO> rideDTOS =  rideService.getAllRidesOfRider(currentRider, pageRequest).map(ride -> modelMapper.map(ride, RideDTO.class));
        log.info("Successfully fetched all rides of Rider");
        return rideDTOS;
    }

    @Override
    public void createNewDriver(User user) {
        Rider rider = Rider.builder()
                .user(user)
                .rating(0.0)
                .build();
        riderRepository.save(rider);
    }

    @Override
    public RiderDTO updateRider(Map<String, Object> fieldsToBeUpdated) {
        Rider rider = getCurrentRider();
        Long riderId = rider.getId();
        log.info("Updating Rider by ID : {}", riderId);
        userService.updateUserById(rider.getUser().getId(), fieldsToBeUpdated);
        Rider updatedRider = getCurrentRider();
        log.info("Successfully updated Rider By ID : {}", riderId);
        return modelMapper.map(updatedRider, RiderDTO.class);
    }

    @Override
    public Rider getCurrentRider() {
        /* log.info("CONTEXT - {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal()); */
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return riderRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("No Rider associated with User having ID : " + user.getId()));
    }

}
