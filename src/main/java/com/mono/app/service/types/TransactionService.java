package com.mono.app.service.types;

import com.mono.app.exceptions.UnexpectedFault;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Set;

public interface TransactionService<T, U, V> {
    U addTransaction(V transaction);

    List<U> getAllWalletIncomes(String walletName);

    List<U> getAllWalletExpenses(String walletName);

    List<U> getAllWalletTransactions(String walletName);

    Page<U> getAllWalletTransactionsWithPagination(String walletName, Integer offset, Integer limit);
}
