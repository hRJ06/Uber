package com.Hindol.Uber.Strategy;

import com.Hindol.Uber.Entity.Enum.PaymentMethod;
import com.Hindol.Uber.Strategy.Implementation.CashPaymentStrategy;
import com.Hindol.Uber.Strategy.Implementation.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {
    private final WalletPaymentStrategy walletPaymentStrategy;
    private final CashPaymentStrategy cashPaymentStrategy;

    public PaymentStrategy getPaymentStrategy(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case WALLET -> walletPaymentStrategy;
            case  CASH -> cashPaymentStrategy;
            default -> throw new RuntimeException("Invalid payment method");
        };
    }
}

