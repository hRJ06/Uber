package com.Hindol.Uber.Repository;

import com.Hindol.Uber.Entity.Driver;
import com.Hindol.Uber.Entity.Rating;
import com.Hindol.Uber.Entity.Ride;
import com.Hindol.Uber.Entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByRider(Rider rider);
    List<Rating> findByDriver(Driver driver);

    Optional<Rating> findByRide(Ride ride);
}
