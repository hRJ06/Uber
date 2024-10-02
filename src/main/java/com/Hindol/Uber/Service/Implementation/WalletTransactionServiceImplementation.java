package com.Hindol.Uber.Service.Implementation;

import com.Hindol.Uber.Entity.WalletTransaction;
import com.Hindol.Uber.Repository.WalletTransationRepository;
import com.Hindol.Uber.Service.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImplementation implements WalletTransactionService {
    private final WalletTransationRepository walletTransationRepository;
    private final ModelMapper modelMapper;
    @Override
    public void createNewWalletTransaction(WalletTransaction walletTransaction) {
        walletTransationRepository.save(walletTransaction);
    }
}
