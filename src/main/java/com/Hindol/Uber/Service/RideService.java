package com.Hindol.Uber.Service;

import com.Hindol.Uber.DTO.RideRequestDTO;
import com.Hindol.Uber.Entity.Driver;
import com.Hindol.Uber.Entity.Enum.RideStatus;
import com.Hindol.Uber.Entity.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideService {
    Ride getRideById(Long rideId);
    void matchWithDrivers(RideRequestDTO rideRequestDTO);
    Ride createNewRide(RideRequestDTO rideRequestDTO, Driver driver);
    Ride updateRideStatus(Long rideId, RideStatus rideStatus);
    Page<Ride> getAllRidesOfRider(Long riderId, PageRequest pageRequest);
    Page<Ride> getAllRidesOfDriver(Long driverId, PageRequest pageRequest);
}
