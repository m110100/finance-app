package com.mono.app.repository;

import com.mono.app.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.id = :id AND t.isEnabled = true")
    Optional<Transaction> findTransactionById(Long id);

    @Query("SELECT t FROM Transaction t WHERE t.wallet.id = :walletId AND t.isEnabled = true AND t.type = 'INCOME' " +
            "ORDER BY t.date DESC")
    List<Transaction> findUserWalletIncomes(Long walletId);

    @Query("SELECT t FROM Transaction t WHERE t.wallet.id = :walletId AND t.isEnabled = true AND t.type = 'EXPENSE' " +
            "ORDER BY t.date DESC")
    List<Transaction> findUserWalletExpenses(Long walletId);

    @Query("SELECT t FROM Transaction t WHERE t.wallet.id = :walletId AND t.isEnabled = true " +
            "ORDER BY t.date DESC")
    List<Transaction> findUserWalletTransactions(Long walletId);

    @Query("SELECT t FROM Transaction t WHERE t.wallet.id = :walletId AND t.isEnabled = true " +
            "ORDER BY t.date DESC")
    Page<Transaction> findUserWalletTransactionsWithPagination(Long walletId, Pageable pageable);
}
