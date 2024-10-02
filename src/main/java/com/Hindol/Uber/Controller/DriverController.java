package com.Hindol.Uber.Controller;

import com.Hindol.Uber.DTO.*;
import com.Hindol.Uber.Service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;
    @PostMapping("/acceptRide/{rideRequestId}")
    public ResponseEntity<RideDTO> acceptRide(@PathVariable Long rideRequestId) {
        return ResponseEntity.ok(driverService.acceptRide(rideRequestId));
    }

    @PostMapping("/startRide/{rideRequestId}")
    public ResponseEntity<RideDTO> startRide(@PathVariable Long rideRequestId, @RequestBody RideStartDTO rideStartDTO) {
        return ResponseEntity.ok(driverService.startRide(rideRequestId, rideStartDTO.getOtp()));
    }

    @PostMapping("/endRide/{rideRequestId}")
    public ResponseEntity<RideDTO> endRide(@PathVariable Long rideRequestId) {
        return ResponseEntity.ok(driverService.endRide(rideRequestId));
    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RideDTO> cancelRide(@PathVariable Long rideId) {
        return ResponseEntity.ok(driverService.cancelRide(rideId));
    }

    @PostMapping("/rateRider")
    public ResponseEntity<RiderDTO> rateDriver(@RequestBody RatingDTO ratingDTO) {
        return ResponseEntity.ok(driverService.rateRider(ratingDTO.getRideId(), ratingDTO.getRating()));
    }

    @GetMapping("/getMyProfile")
    public ResponseEntity<DriverDTO> getMyProfile() {
        return ResponseEntity.ok(driverService.getMyProfile());
    }

    @GetMapping("/getMyRides")
    public ResponseEntity<Page<RideDTO>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffSet,
                                                       @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffSet, pageSize, Sort.by(Sort.Direction.DESC,"createdTime", "id"));
        return ResponseEntity.ok(driverService.getAllMyRides(pageRequest));
    }

    @PostMapping("/rateRider/{riderId}/{rating}")
    public ResponseEntity<RiderDTO> rateRider(@PathVariable Long riderId, @PathVariable Integer rating) {
        return ResponseEntity.ok(driverService.rateRider(riderId, rating));
    }
}
