package com.Hindol.Uber.Strategy;

import com.Hindol.Uber.Strategy.Implementation.DriverMatchingHighestRatedDriverStrategy;
import com.Hindol.Uber.Strategy.Implementation.DriverMatchingNearestDriverStrategy;
import com.Hindol.Uber.Strategy.Implementation.RideFareDefaultFareCalculationStrategy;
import com.Hindol.Uber.Strategy.Implementation.RideFareSurgePricingFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {
    private final DriverMatchingHighestRatedDriverStrategy highestRatedDriverStrategy;
    private final DriverMatchingNearestDriverStrategy nearestDriverStrategy;
    private final RideFareDefaultFareCalculationStrategy defaultFareCalculationStrategy;
    private final RideFareSurgePricingFareCalculationStrategy surgePricingFareCalculationStrategy;

    public DriverMatchingStrategy driverMatchingStrategy(double riderRating) {
        if(riderRating >= 4.8) {
            return highestRatedDriverStrategy;
        }
        return nearestDriverStrategy;
    }

    public RideFareCalculationStrategy rideFareCalculationStrategy() {
        LocalTime surgeStartTime = LocalTime.of(18, 0);
        LocalTime surgeEndTime = LocalTime.of(21, 0);
        LocalTime currentTime = LocalTime.now();
        boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);
        if(isSurgeTime) {
            return surgePricingFareCalculationStrategy;
        }
        return defaultFareCalculationStrategy;
    }
}
