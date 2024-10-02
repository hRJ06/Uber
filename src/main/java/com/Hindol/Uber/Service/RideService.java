package com.Hindol.Uber.Service;

import com.Hindol.Uber.Entity.Driver;
import com.Hindol.Uber.Entity.Enum.RideStatus;
import com.Hindol.Uber.Entity.Ride;
import com.Hindol.Uber.Entity.RideRequest;
import com.Hindol.Uber.Entity.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideService {
    Ride getRideById(Long rideId);
    Ride createNewRide(RideRequest rideRequest, Driver driver);
    Ride updateRideStatus(Ride ride, RideStatus rideStatus);
    Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest);
    Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest);
}
