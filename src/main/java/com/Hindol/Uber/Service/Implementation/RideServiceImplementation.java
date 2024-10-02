package com.Hindol.Uber.Service.Implementation;

import com.Hindol.Uber.Entity.Driver;
import com.Hindol.Uber.Entity.Enum.RideRequestStatus;
import com.Hindol.Uber.Entity.Enum.RideStatus;
import com.Hindol.Uber.Entity.Ride;
import com.Hindol.Uber.Entity.RideRequest;
import com.Hindol.Uber.Entity.Rider;
import com.Hindol.Uber.Exception.ResourceNotFoundException;
import com.Hindol.Uber.Repository.RideRepository;
import com.Hindol.Uber.Service.RideRequestService;
import com.Hindol.Uber.Service.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImplementation implements RideService {
    private final RideRepository rideRepository;
    private final ModelMapper modelMapper;
    private final RideRequestService rideRequestService;
    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId).orElseThrow(() -> new ResourceNotFoundException("No Ride found with ID : " + rideId));
    }

    @Override
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);
        Ride ride = modelMapper.map(rideRequest, Ride.class);
        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setDriver(driver);
        /* TODO: Remove OTP field for Driver */
        ride.setOtp(generateRandomOTP());
        ride.setId(null);
        rideRequestService.update(rideRequest);
        return rideRepository.save(ride);
    }

    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideStatus(rideStatus);
        return rideRepository.save(ride);
    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepository.findByRider(rider, pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepository.findByDriver(driver, pageRequest);
    }

    private String generateRandomOTP() {
        Random random = new Random();
        int otpInt = random.nextInt(10000);
        return String.format("%04d", otpInt);
    }
}
