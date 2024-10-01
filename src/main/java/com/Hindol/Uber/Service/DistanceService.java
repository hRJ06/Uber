package com.Hindol.Uber.Service;

import org.locationtech.jts.geom.Point;

public interface DistanceService {
    Double calculateDistance(Point pickUpLocation, Point dropOffLocation);
}
