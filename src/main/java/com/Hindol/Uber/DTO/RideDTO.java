package com.Hindol.Uber.DTO;

import com.Hindol.Uber.Entity.Enum.PaymentMethod;
import com.Hindol.Uber.Entity.Enum.RideStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RideDTO {
    private Long id;
    private PointDTO pickUpLocation;
    private PointDTO dropOffLocation;
    private LocalDateTime createdTime;
    private RiderDTO rider;
    private DriverDTO driver;
    private PaymentMethod paymentMethod;
    private RideStatus rideStatus;
    private String otp;
    private Double fare;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
