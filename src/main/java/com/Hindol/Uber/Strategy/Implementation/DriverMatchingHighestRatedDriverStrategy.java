package com.Hindol.Uber.Strategy.Implementation;

import com.Hindol.Uber.Entity.Driver;
import com.Hindol.Uber.Entity.RideRequest;
import com.Hindol.Uber.Repository.DriverRepository;
import com.Hindol.Uber.Strategy.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverMatchingHighestRatedDriverStrategy implements DriverMatchingStrategy {
    private final DriverRepository driverRepository;
    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {
        return driverRepository.findTenNearByTopRatedDrivers(rideRequest.getPickUpLocation());
    }
}
