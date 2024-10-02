package com.Hindol.Uber.Service;

import com.Hindol.Uber.DTO.DriverDTO;
import com.Hindol.Uber.DTO.RiderDTO;
import com.Hindol.Uber.Entity.Driver;
import com.Hindol.Uber.Entity.Ride;
import com.Hindol.Uber.Entity.Rider;

public interface RatingService {
    DriverDTO rateDriver(Ride ride, Integer rating);
    RiderDTO rateRider(Ride ride, Integer rating);
    void createNewRating(Ride ride);
}
