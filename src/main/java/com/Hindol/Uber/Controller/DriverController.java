package com.Hindol.Uber.Controller;

import com.Hindol.Uber.DTO.*;
import com.Hindol.Uber.Service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/drivers")
@RequiredArgsConstructor
@Secured("ROLE_DRIVER")
public class DriverController {
    private final DriverService driverService;
    @PostMapping("/acceptRide/{rideRequestId}")
    public ResponseEntity<RideDTO> acceptRide(@PathVariable Long rideRequestId) {
        return ResponseEntity.ok(driverService.acceptRide(rideRequestId));
    }

    @PostMapping("/startRide/{rideId}")
    public ResponseEntity<RideDTO> startRide(@PathVariable Long rideId, @RequestBody RideStartDTO rideStartDTO) {
        return ResponseEntity.ok(driverService.startRide(rideId, rideStartDTO.getOtp()));
    }

    @PostMapping("/endRide/{rideId}")
    public ResponseEntity<RideDTO> endRide(@PathVariable Long rideId) {
        return ResponseEntity.ok(driverService.endRide(rideId));
    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RideDTO> cancelRide(@PathVariable Long rideId) {
        return ResponseEntity.ok(driverService.cancelRide(rideId));
    }

    @PostMapping("/rateRider")
    public ResponseEntity<RiderDTO> rateRider(@RequestBody RatingDTO ratingDTO) {
        return ResponseEntity.ok(driverService.rateRider(ratingDTO.getRideId(), ratingDTO.getRating()));
    }

    @GetMapping("/getMyProfile")
    public ResponseEntity<DriverDTO> getMyProfile() {
        return ResponseEntity.ok(driverService.getMyProfile());
    }

    @GetMapping("/getMyRides")
    public ResponseEntity<List<RideDTO>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffSet,
                                                       @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffSet, pageSize, Sort.by(Sort.Direction.DESC,"createdTime", "id"));
        return ResponseEntity.ok(driverService.getAllMyRides(pageRequest).getContent());
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<DriverDTO> updateProfile(@RequestBody Map<String, Object> fieldsToBeUpdated) {
        return ResponseEntity.ok(driverService.updateDriver(fieldsToBeUpdated));
    }
}
