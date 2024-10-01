package com.Hindol.Uber.Strategy;

import com.Hindol.Uber.Entity.RideRequest;

public interface RideFareCalculatorStrategy {
    double RIDE_FARE_MULTIPLIER = 10;
    double calculateFare(RideRequest rideRequest);
}
