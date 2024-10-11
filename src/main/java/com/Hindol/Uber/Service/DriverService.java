package com.Hindol.Uber.Service;

import com.Hindol.Uber.DTO.DriverDTO;
import com.Hindol.Uber.DTO.RideDTO;
import com.Hindol.Uber.DTO.RiderDTO;
import com.Hindol.Uber.Entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;


public interface DriverService {
    RideDTO acceptRide(Long rideRequestId);
    RideDTO cancelRide(Long rideId);
    RideDTO startRide(Long rideId, String otp);
    RideDTO endRide(Long rideId);
    RiderDTO rateRider(Long rideId,Integer rating);
    DriverDTO getMyProfile();
    Page<RideDTO> getAllMyRides(PageRequest pageRequest);
    Driver getCurrentDriver();
    Driver updateDriverAvailability(Driver driver, boolean available);
    Driver createNewDriver(Driver driver);
    DriverDTO updateDriver(Map<String, Object> fieldsToBeUpdated);
}
