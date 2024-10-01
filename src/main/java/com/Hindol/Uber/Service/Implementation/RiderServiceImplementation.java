package com.Hindol.Uber.Service.Implementation;

import com.Hindol.Uber.DTO.DriverDTO;
import com.Hindol.Uber.DTO.RideDTO;
import com.Hindol.Uber.DTO.RideRequestDTO;
import com.Hindol.Uber.DTO.RiderDTO;
import com.Hindol.Uber.Entity.Enum.RideRequestStatus;
import com.Hindol.Uber.Entity.RideRequest;
import com.Hindol.Uber.Entity.Rider;
import com.Hindol.Uber.Entity.User;
import com.Hindol.Uber.Exception.ResourceNotFoundException;
import com.Hindol.Uber.Repository.RideRequestRepository;
import com.Hindol.Uber.Repository.RiderRepository;
import com.Hindol.Uber.Service.RiderService;
import com.Hindol.Uber.Strategy.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderServiceImplementation implements RiderService {

    private final ModelMapper modelMapper;
    private final RideStrategyManager rideStrategyManager;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;

    @Override
    public RideRequestDTO requestRide(RideRequestDTO rideRequestDTO) {
        Rider rider = getCurrentDriver();
        RideRequest rideRequest = modelMapper.map(rideRequestDTO, RideRequest.class);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare);
        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);
        rideStrategyManager.driverMatchingStrategy(rider.getRating()).findMatchingDriver(rideRequest);
        return modelMapper.map(savedRideRequest, RideRequestDTO.class);
    }

    @Override
    public RideDTO cancelRide(Long rideId) {
        return null;
    }

    @Override
    public RideDTO startRide(Long rideId) {
        return null;
    }

    @Override
    public RideDTO endRide(Long rideId) {
        return null;
    }

    @Override
    public DriverDTO rateDriver(Long rideId, Integer rating) {
        return null;
    }

    @Override
    public RiderDTO getMyProfile() {
        return null;
    }

    @Override
    public List<RideDTO> getAllMyRides() {
        return List.of();
    }

    @Override
    public void createNewDriver(User user) {
        Rider rider = Rider.builder()
                .user(user)
                .rating(0.0)
                .build();
        riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentDriver() {
        /* TODO: Implement Spring Security*/
        return riderRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("No Rider found with ID : 1"));
    }
}
