package com.Hindol.Uber.DTO;

import lombok.Data;

import java.util.List;

@Data
public class WalletDTO {
    private Long id;
    private UserDTO user;
    private Double balance;
    private List<WalletTransactionDTO> transactions;
}
