package com.Hindol.Uber.Strategy.Implementation;

import com.Hindol.Uber.Entity.RideRequest;
import com.Hindol.Uber.Service.DistanceService;
import com.Hindol.Uber.Strategy.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFareSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {
    private final DistanceService distanceService;
    private static final double SURGE_FACTOR = 2;
    @Override
    public double calculateFare(RideRequest rideRequest) {
        double distance = distanceService.calculateDistance(rideRequest.getPickUpLocation(), rideRequest.getDropOffLocation());
        return distance * RIDE_FARE_MULTIPLIER * SURGE_FACTOR;
    }
}
