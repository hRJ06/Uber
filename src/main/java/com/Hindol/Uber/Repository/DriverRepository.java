package com.Hindol.Uber.Repository;

import com.Hindol.Uber.Entity.Driver;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    @Query("SELECT d.*, ST_Distance(d.current_location, :pickUpLocation) AS distance " +
            "FROM drivers AS d " +
            "where d.available = true AND ST_DWithin(d.current_location, :pickUpLocation, 10000) " +
            "ORDER BY distance " +
            "LIMIT 10"
    )
    List<Driver> findTenNearestDrivers(Point pickUpLocation);
}