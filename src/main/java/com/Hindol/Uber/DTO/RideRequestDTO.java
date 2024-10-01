package com.Hindol.Uber.DTO;

import com.Hindol.Uber.Entity.Enum.PaymentMethod;
import com.Hindol.Uber.Entity.Enum.RideRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDTO {
    private Long id;
    private PointDTO pickUpLocation;
    private PointDTO dropOffLocation;
    private LocalDateTime requestedTime;
    private RiderDTO rider;
    private Double fare;
    private PaymentMethod paymentMethod;
    private RideRequestStatus rideRequestStatus;
}
