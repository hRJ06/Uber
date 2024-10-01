package com.Hindol.Uber.Service.Implementation;

import com.Hindol.Uber.Service.DistanceService;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
public class DistanceServiceOSRMImplementation implements DistanceService {
    @Override
    public Double calculateDistance(Point pickUpLocation, Point dropOffLocation) {
        return 0.0;
    }
}
