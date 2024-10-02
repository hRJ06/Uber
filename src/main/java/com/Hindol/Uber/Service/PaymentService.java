package com.Hindol.Uber.Service;

import com.Hindol.Uber.Entity.Enum.PaymentStatus;
import com.Hindol.Uber.Entity.Payment;
import com.Hindol.Uber.Entity.Ride;

public interface PaymentService {
    void processPayment(Ride ride);
    Payment createNewPayment(Ride ride);
    void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus);


}
