package com.Hindol.Uber.Service.Implementation;

import com.Hindol.Uber.DTO.DriverDTO;
import com.Hindol.Uber.DTO.RideDTO;
import com.Hindol.Uber.DTO.RiderDTO;
import com.Hindol.Uber.Entity.*;
import com.Hindol.Uber.Entity.Enum.RideRequestStatus;
import com.Hindol.Uber.Entity.Enum.RideStatus;
import com.Hindol.Uber.Exception.ResourceNotFoundException;
import com.Hindol.Uber.Exception.RuntimeConflictException;
import com.Hindol.Uber.Repository.DriverRepository;
import com.Hindol.Uber.Service.*;
import com.Hindol.Uber.Util.EmailUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DriverServiceImplementation implements DriverService {
    private static final Logger log = LoggerFactory.getLogger(DriverServiceImplementation.class);
    private final RideRequestService rideRequestService;
    private final DriverRepository driverRepository;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RatingService ratingService;
    private final EmailSenderService emailSenderService;
    private final UserService userService;
    @Override
    @Transactional
    public RideDTO acceptRide(Long rideRequestId) {
        RideRequest rideRequest  = rideRequestService.findRideRequestById(rideRequestId);
        if(!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)) {
            throw new RuntimeException("Ride Request cannot be accepted, status is " + rideRequest.getRideRequestStatus());
        }
        Driver currentDriver = getCurrentDriver();
        if(!currentDriver.getAvailable()) {
            throw new RuntimeException("Driver cannot accept ride due to unavailability");
        }
        Driver updatedDriver = updateDriverAvailability(currentDriver, false);
        Ride ride = rideService.createNewRide(rideRequest, updatedDriver);
        String riderEmail = ride.getRider().getUser().getEmail();
        String rider_email_subject = EmailUtil.generateRideConfirmationEmailSubjectForRider(updatedDriver);
        String rider_email_body = EmailUtil.generateRideConfirmationEmailForRider(ride, updatedDriver);
        emailSenderService.sendEmail(riderEmail, rider_email_subject, rider_email_body);

        String driverEmail = currentDriver.getUser().getEmail();
        String driver_email_subject = EmailUtil.generateRideConfirmationEmailSubjectForDriver(ride.getRider());
        String driver_email_body = EmailUtil.generateRideConfirmationEmailForDriver(ride, ride.getRider());
        emailSenderService.sendEmail(driverEmail, driver_email_subject, driver_email_body);
        return modelMapper.map(ride, RideDTO.class);
    }

    @Override
    public RideDTO cancelRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver cannot cancel a ride as he has not accepted it earlier");
        }
        if(ride.getRideStatus().equals(RideStatus.CONFIRMED)) {
            throw new RuntimeException("Ride cannot be cancelled, invalid status : " + ride.getRideStatus());
        }
        Ride updatedRide = rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        updateDriverAvailability(driver, true);
        return modelMapper.map(updatedRide, RideDTO.class);
    }

    @Override
    public RideDTO startRide(Long rideId, String otp) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }
        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)) {
            throw new RuntimeException("Ride status is not confirmed, hence cannot be started, status : " + ride.getRideStatus());
        }
        if(!otp.equals(ride.getOtp())) {
            throw new RuntimeException("OTP is not valid, OTP : " + otp);
        }
        ride.setStartedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ONGOING);
        paymentService.createNewPayment(savedRide);
        ratingService.createNewRating(savedRide);
        return modelMapper.map(savedRide, RideDTO.class);
    }

    @Override
    @Transactional
    public RideDTO endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }
        if(!ride.getRideStatus().equals(RideStatus.ONGOING)) {
            throw new RuntimeException("Ride status is not ongoing, hence cannot be ended, status : " + ride.getRideStatus());
        }
        ride.setEndedAt(LocalDateTime.now());
        Ride updatedRide = rideService.updateRideStatus(ride, RideStatus.ENDED);
        updateDriverAvailability(driver, true);
        paymentService.processPayment(ride);
        return modelMapper.map(updatedRide, RideDTO.class);
    }

    @Override
    public RiderDTO rateRider(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())) {
            throw new RuntimeConflictException("Driver is not the owner of ride");
        }
        if(!ride.getRideStatus().equals(RideStatus.ENDED)) {
            throw new RuntimeException("Ride status is not ended, hence cannot rate rider");
        }
        return ratingService.rateRider(ride, rating);
    }

    @Override
    public DriverDTO getMyProfile() {
        Driver currentDriver = getCurrentDriver();
        return modelMapper.map(currentDriver, DriverDTO.class);
    }

    @Override
    public Page<RideDTO> getAllMyRides(PageRequest pageRequest) {
        Driver currentDriver = getCurrentDriver();
        return rideService.getAllRidesOfDriver(currentDriver, pageRequest).map(ride -> modelMapper.map(ride, RideDTO.class));
    }

    @Override
    public Driver getCurrentDriver() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return driverRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("No Driver associated with User having ID : " + user.getId()));
    }

    @Override
    public Driver updateDriverAvailability(Driver driver, boolean available) {
        driver.setAvailable(available);
        return driverRepository.save(driver);
    }

    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public DriverDTO updateDriver(Map<String, Object> fieldsToBeUpdated) {
        Driver driver = getCurrentDriver();
        Long driverId = driver.getId();
        log.info("Updating Driver by ID : {}", driverId);
        userService.updateUserById(driver.getUser().getId(), fieldsToBeUpdated);
        Driver updatedDriver = getCurrentDriver();
        log.info("Successfully updated Driver By ID : {}", driverId);
        return modelMapper.map(updatedDriver, DriverDTO.class);
    }
}
