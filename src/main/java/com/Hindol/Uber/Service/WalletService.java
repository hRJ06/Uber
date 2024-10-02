package com.Hindol.Uber.Service;

import com.Hindol.Uber.Entity.Enum.TransactionMethod;
import com.Hindol.Uber.Entity.Ride;
import com.Hindol.Uber.Entity.User;
import com.Hindol.Uber.Entity.Wallet;

public interface WalletService {
    Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);
    void deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);
    void withdrawAllMyMoneyFromWallet();
    Wallet findWalletById(Long walletId);
    Wallet createNewWallet(User user);
    Wallet findByUser(User user);
}
