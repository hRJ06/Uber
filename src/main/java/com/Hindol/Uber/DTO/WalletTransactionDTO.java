package com.Hindol.Uber.DTO;

import com.Hindol.Uber.Entity.Enum.TransactionMethod;
import com.Hindol.Uber.Entity.Enum.TransactionType;
import com.Hindol.Uber.Entity.Ride;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WalletTransactionDTO {
    private Long id;
    private Double amount;
    private TransactionType transactionType;
    private TransactionMethod transactionMethod;
    private Ride ride;
    private String transactionId;
    private LocalDateTime timeStamp;
    private WalletDTO wallet;
}
