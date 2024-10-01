package com.Hindol.Uber.Strategy;

import com.Hindol.Uber.Entity.Driver;
import com.Hindol.Uber.Entity.RideRequest;

import java.util.List;

public interface DriverMatchingStrategy {
    List<Driver> findMatchingDriver(RideRequest rideRequest);
}
