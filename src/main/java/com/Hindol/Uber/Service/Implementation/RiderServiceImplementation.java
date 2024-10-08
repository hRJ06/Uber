package com.Hindol.Uber.Service.Implementation;

import com.Hindol.Uber.DTO.DriverDTO;
import com.Hindol.Uber.DTO.RideDTO;
import com.Hindol.Uber.DTO.RideRequestDTO;
import com.Hindol.Uber.DTO.RiderDTO;
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
    @Override
    @Transactional
    public RideRequestDTO requestRide(RideRequestDTO rideRequestDTO) {
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
            log.info("Driver - {}", driver);
            String body = EmailUtil.generateRideRequestEmail(rideRequest, driver);
            String driverEmail = driver.getUser().getEmail();
            emailSenderService.sendEmail(driverEmail, subject, body);
        }
        return modelMapper.map(savedRideRequest, RideRequestDTO.class);
    }

    @Override
    public RideDTO cancelRide(Long rideId) {
        Rider rider = this.getCurrentRider();
        Ride ride = rideService.getRideById(rideId);
        if(!ride.getRider().equals(rider)) {
            throw new RuntimeException("Rider does not own this ride with ID : " + rideId);
        }
        if(ride.getRideStatus().equals(RideStatus.CONFIRMED)) {
            throw new RuntimeException("Ride cannot be cancelled, invalid status : " + ride.getRideStatus());
        }
        Ride updatedRide = rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        driverService.updateDriverAvailability(ride.getDriver(), true);
        return modelMapper.map(updatedRide, RideDTO.class);
    }

    @Override
    public DriverDTO rateDriver(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Rider rider = getCurrentRider();
        if(!rider.equals(ride.getRider())) {
            throw new RuntimeException("Rider is not owner of ride");
        }
        if(!ride.getRideStatus().equals(RideStatus.ENDED)) {
            throw new RuntimeException("Ride status is not ended, hence cannot rate driver");
        }
        return ratingService.rateDriver(ride, rating);
    }

    @Override
    public RiderDTO getMyProfile() {
        Rider rider = getCurrentRider();
        return modelMapper.map(rider, RiderDTO.class);
    }

    @Override
    public Page<RideDTO> getAllMyRides(PageRequest pageRequest) {
        Rider currentRider = getCurrentRider();
        return rideService.getAllRidesOfRider(currentRider, pageRequest).map(ride -> modelMapper.map(ride, RideDTO.class));
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
    public Rider getCurrentRider() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return riderRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("No Rider associated with User having ID : " + user.getId()));
    }
}
