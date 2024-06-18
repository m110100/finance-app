package com.mono.app.repository;

import com.mono.app.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Query("SELECT w FROM Wallet w INNER JOIN User u ON w.user.id = u.id " +
            "WHERE w.name = :name AND u.id = :userId AND (w.isEnabled = true AND u.isEnabled = true)")
    Optional<Wallet> findUserWallet(String name, Long userId);

    @Query("SELECT w FROM Wallet w INNER JOIN User u ON w.user.id = u.id " +
            "WHERE u.id = :userId AND (w.isEnabled = true AND u.isEnabled = true) ORDER BY w.id ASC")
    List<Wallet> findAllUserWallets(Long userId);
}
