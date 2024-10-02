package com.Hindol.Uber.Strategy;

import com.Hindol.Uber.Entity.Payment;

public interface PaymentStrategy {
    Double PLATFORM_COMMISSION = 0.3;
    void processPayment(Payment payment);
}
