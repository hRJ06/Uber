package com.Hindol.Uber.Repository;

import com.Hindol.Uber.Entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransationRepository extends JpaRepository<WalletTransaction, Long> {
}
