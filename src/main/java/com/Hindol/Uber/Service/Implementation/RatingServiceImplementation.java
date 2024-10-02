package com.Hindol.Uber.Service.Implementation;

import com.Hindol.Uber.DTO.DriverDTO;
import com.Hindol.Uber.DTO.RiderDTO;
import com.Hindol.Uber.Entity.Driver;
import com.Hindol.Uber.Entity.Rating;
import com.Hindol.Uber.Entity.Ride;
import com.Hindol.Uber.Entity.Rider;
import com.Hindol.Uber.Exception.ResourceNotFoundException;
import com.Hindol.Uber.Repository.DriverRepository;
import com.Hindol.Uber.Repository.RatingRepository;
import com.Hindol.Uber.Repository.RiderRepository;
import com.Hindol.Uber.Service.RatingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImplementation implements RatingService {
    private final RatingRepository ratingRepository;
    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;

    @Override
    public DriverDTO rateDriver(Ride ride, Integer rating) {
        Driver driver = ride.getDriver();
        Rating ratingObj = ratingRepository.findByRide(ride).orElseThrow(() -> new ResourceNotFoundException("No Rating found for Ride ID : " + ride.getId()));
        ratingObj.setDriverRating(rating);
        ratingRepository.save(ratingObj);
        Double newRating = ratingRepository.findByDriver(driver)
                .stream()
                .mapToDouble(Rating::getDriverRating)
                .average().orElse(0.0);
        driver.setRating(newRating);
        Driver savedDriver = driverRepository.save(driver);
        return modelMapper.map(savedDriver, DriverDTO.class);
    }

    @Override
    public RiderDTO rateRider(Ride ride, Integer rating) {
        Rider rider = ride.getRider();
        Rating ratingObj = ratingRepository.findByRide(ride).orElseThrow(() -> new ResourceNotFoundException("No Rating found for Ride ID : " + ride.getId()));
        ratingObj.setRiderRating(rating);
        ratingRepository.save(ratingObj);
        Double newRating = ratingRepository.findByRider(rider)
                .stream()
                .mapToDouble(Rating::getDriverRating)
                .average().orElse(0.0);
        rider.setRating(newRating);
        Rider savedRider = riderRepository.save(rider);
        return modelMapper.map(savedRider, RiderDTO.class);
    }

    @Override
    public void createNewRating(Ride ride) {
        Rating rating = Rating.builder()
                .rider(ride.getRider())
                .driver(ride.getDriver())
                .ride(ride)
                .build();
        ratingRepository.save(rating);
    }
}
