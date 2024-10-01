package com.Hindol.Uber.Service;

import com.Hindol.Uber.Entity.RideRequest;

public interface RideRequestService {
    RideRequest findRideRequestById(Long rideRequestId);

    void update(RideRequest rideRequest);
}
