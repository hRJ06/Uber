package com.Hindol.Uber.Service.Implementation;

import com.Hindol.Uber.Entity.RideRequest;
import com.Hindol.Uber.Exception.ResourceNotFoundException;
import com.Hindol.Uber.Repository.RideRequestRepository;
import com.Hindol.Uber.Service.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImplementation implements RideRequestService {
    private final RideRequestRepository rideRequestRepository;
    @Override
    public RideRequest findRideRequestById(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId).orElseThrow(() -> new ResourceNotFoundException("No Ride Request found with ID : " + rideRequestId));
    }

    @Override
    public void update(RideRequest rideRequest) {
        rideRequestRepository.findById(rideRequest.getId()).orElseThrow(() -> new ResourceNotFoundException("No Ride Request with ID : " + rideRequest.getId()));
        rideRequestRepository.save(rideRequest);
    }
}
