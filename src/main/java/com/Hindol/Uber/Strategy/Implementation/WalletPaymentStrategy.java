package com.Hindol.Uber.Strategy.Implementation;

import com.Hindol.Uber.Entity.Driver;
import com.Hindol.Uber.Entity.Enum.PaymentStatus;
import com.Hindol.Uber.Entity.Enum.TransactionMethod;
import com.Hindol.Uber.Entity.Payment;
import com.Hindol.Uber.Entity.Rider;
import com.Hindol.Uber.Repository.PaymentRepository;
import com.Hindol.Uber.Service.WalletService;
import com.Hindol.Uber.Strategy.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {
    private final WalletService walletService;
    private final PaymentRepository paymentRepository;
    @Override
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();
        walletService.deductMoneyFromWallet(rider.getUser(), payment.getAmount(), null, payment.getRide(), TransactionMethod.RIDE);
        double driverCut = payment.getAmount() * PLATFORM_COMMISSION;
        walletService.addMoneyToWallet(driver.getUser(), driverCut, null, payment.getRide(), TransactionMethod.RIDE);
        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    }
}
