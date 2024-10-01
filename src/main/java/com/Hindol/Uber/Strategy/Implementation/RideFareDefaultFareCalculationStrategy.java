package com.Hindol.Uber.Strategy.Implementation;

import com.Hindol.Uber.Entity.RideRequest;
import com.Hindol.Uber.Service.DistanceService;
import com.Hindol.Uber.Strategy.RideFareCalculatorStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFareDefaultFareCalculationStrategy implements RideFareCalculatorStrategy {
    private final DistanceService distanceService;
    @Override
    public double calculateFare(RideRequest rideRequest) {
        double distance = distanceService.calculateDistance(rideRequest.getPickUpLocation(), rideRequest.getDropOffLocation());
        return distance * RIDE_FARE_MULTIPLIER;
    }
}
