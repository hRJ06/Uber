package com.Hindol.Uber.Controller;

import com.Hindol.Uber.DTO.*;
import com.Hindol.Uber.Service.RiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/rider")
@RequiredArgsConstructor
@Slf4j
@Secured("ROLE_RIDER")
public class RiderController {
    private final RiderService riderService;
    @PostMapping("/requestRide")
    public ResponseEntity<RideRequestDTO> requestRide(@RequestBody RideRequestDTO rideRequestDTO) {
        log.info("ENDPOINT: /rider/requestRide | TYPE: POST");
        return ResponseEntity.ok(riderService.requestRide(rideRequestDTO));
    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RideDTO> cancelRide(@PathVariable Long rideId) {
        log.info("ENDPOINT: /rider/cancelRide/<rideId> | TYPE: POST | PARAM - rideId={}", rideId);
        return ResponseEntity.ok(riderService.cancelRide(rideId));
    }

    @PostMapping("/rateDriver")
    public ResponseEntity<DriverDTO> rateDriver(@RequestBody RatingDTO ratingDTO) {
        log.info("ENDPOINT: /rider/rateDriver | TYPE: POST");
        return ResponseEntity.ok(riderService.rateDriver(ratingDTO.getRideId(), ratingDTO.getRating()));
    }

    @GetMapping("/getMyProfile")
    public ResponseEntity<RiderDTO> getMyProfile() {
        log.info("ENDPOINT: /rider/getMyProfile | TYPE: GET");
        return ResponseEntity.ok(riderService.getMyProfile());
    }

    @GetMapping("/getMyRides")
    public ResponseEntity<List<RideDTO>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffSet,
                                                       @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        log.info("ENDPOINT: /rider/getMyRides | TYPE: GET | PARAMS: pageOffSet={} pageSize={}", pageOffSet, pageSize);
        PageRequest pageRequest = PageRequest.of(pageOffSet, pageSize, Sort.by(Sort.Direction.DESC,"createdTime", "id"));
        return ResponseEntity.ok(riderService.getAllMyRides(pageRequest).getContent());
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<RiderDTO> updateProfile(@RequestBody Map<String, Object> fieldsToBeUpdated) {
        log.info("ENDPOINT: /rider/updateProfile | TYPE: PUT");
        return ResponseEntity.ok(riderService.updateRider(fieldsToBeUpdated));
    }

}
