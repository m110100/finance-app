package com.mono.app.service;

import com.mono.app.dto.request.TransactionRequest;
import com.mono.app.dto.response.TransactionResponse;
import com.mono.app.exceptions.BusinessFault;
import com.mono.app.exceptions.types.ErrorCode;
import com.mono.app.exceptions.types.ErrorType;
import com.mono.app.model.*;
import com.mono.app.model.composite_keys.TransactionLabelCompositeKey;
import com.mono.app.model.composite_keys.TransactionSubCategoryCompositeKey;
import com.mono.app.model.enums.TransactionType;
import com.mono.app.repository.TransactionLabelRepository;
import com.mono.app.repository.TransactionRepository;
import com.mono.app.repository.TransactionSubCategoryRepository;
import com.mono.app.service.types.TransactionService;
import com.mono.app.utils.mapper.types.Mapper;
import com.mono.app.utils.security.AuthenticatedUserResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService<Transaction, TransactionResponse, TransactionRequest> {
    private final TransactionRepository repository;
    private final TransactionLabelRepository transactionLabelRepository;
    private final TransactionSubCategoryRepository transactionSubCategoryRepository;
    private final Mapper<Transaction, TransactionResponse> mapper;
    private final WalletService walletService;
    private final CategoryService categoryService;
    private final LabelService labelService;
    private final AuthenticatedUserResolver resolver;

    @Autowired
    public TransactionServiceImpl(
            TransactionRepository repository,
            TransactionLabelRepository transactionLabelRepository,
            TransactionSubCategoryRepository transactionSubCategoryRepository,
            @Qualifier("transactionMapper") Mapper<Transaction, TransactionResponse> mapper,
            WalletService walletService,
            CategoryService categoryService,
            LabelService labelService,
            AuthenticatedUserResolver resolver) {
        this.repository = repository;
        this.transactionLabelRepository = transactionLabelRepository;
        this.transactionSubCategoryRepository = transactionSubCategoryRepository;
        this.mapper = mapper;
        this.walletService = walletService;
        this.categoryService = categoryService;
        this.labelService = labelService;
        this.resolver = resolver;
    }

    @Override
    public TransactionResponse addTransaction(TransactionRequest request) {
        final User user = resolver.getAuthenticatedUser();

        final Optional<Wallet> existingWallet = walletService.getUserWallet(request.wallet(), user.getId());
        Optional<Category> existingCategory = Optional.empty();

        if (existingWallet.isEmpty()) {
            throw new BusinessFault(
                    "Счет не найден",
                    ErrorCode.E001,
                    ErrorType.ERROR);
        }

        if (request.category() != null && !request.category().isEmpty()) {
            existingCategory = categoryService.getCategoryByName(request.category());

            if (existingCategory.isEmpty()) {
                throw new BusinessFault(
                        "Категория не найдена",
                        ErrorCode.E001,
                        ErrorType.ERROR
                );
            }
        }

        final Wallet wallet = existingWallet.get();
        final Category category = existingCategory.orElse(null);
        final TransactionType type = request.type().name().equals(TransactionType.INCOME.name()) ?
                TransactionType.INCOME : TransactionType.EXPENSE;

        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .date(request.date())
                .note(request.note())
                .type(type)
                .category(category)
                .wallet(wallet)
                .isEnabled(true)
                .build();

        transaction = repository.save(transaction);

        if (request.labels() != null && !request.labels().isEmpty()) {
            Transaction tempTransaction = transaction;

            Set<TransactionLabel> labels = request.labels().stream()
                    .map(label -> labelService.getUserLabel(label.name(), user.getId()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(label -> new TransactionLabel(
                            new TransactionLabelCompositeKey(tempTransaction.getId(), label.getId()),
                            tempTransaction,
                            label,
                            true)
                    )
                    .collect(Collectors.toSet());

            transactionLabelRepository.saveAll(labels);
            transaction.setLabels(labels);
        }

        if (category != null && request.subCategories() != null && !request.subCategories().isEmpty()) {
            Transaction tempTransaction = transaction;

            Set<TransactionSubCategory> subCategories = request.subCategories().stream()
                    .map(subCategory -> categoryService.getSubCategoryByName(subCategory.name(),
                            user.getId()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(subCategory -> new TransactionSubCategory(
                            new TransactionSubCategoryCompositeKey(tempTransaction.getId(),
                                    subCategory.getId()),
                            tempTransaction,
                            subCategory,
                            true)
                    )
                    .collect(Collectors.toSet());

            transactionSubCategoryRepository.saveAll(subCategories);
            transaction.setSubCategories(subCategories);
        }

        transaction = repository.save(transaction); // Обновление транзакции после установки связей.

        // Обновление данных кошелька
        if (transaction.getType().name().equals(TransactionType.INCOME.name())) {
            walletService.addIncome(wallet, transaction.getAmount());
        } else if (transaction.getType().name().equals(TransactionType.EXPENSE.name())) {
            walletService.addExpense(wallet, transaction.getAmount());
        }

        return mapper.toDTO(transaction);
    }

    @Override
    public List<TransactionResponse> getAllWalletIncomes(String walletName) {
        final User user = resolver.getAuthenticatedUser();
        final Optional<Wallet> existingWallet = walletService.getUserWallet(walletName, user.getId());

        if (existingWallet.isEmpty()) {
            throw new BusinessFault(
                    "Wallet not found",
                    ErrorCode.E001,
                    ErrorType.ERROR);
        }

        final Wallet wallet = existingWallet.get();

        return repository.findUserWalletIncomes(wallet.getId()).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getAllWalletExpenses(String walletName) {
        final User user = resolver.getAuthenticatedUser();
        final Optional<Wallet> existingWallet = walletService.getUserWallet(walletName, user.getId());

        if (existingWallet.isEmpty()) {
            throw new BusinessFault(
                    "Wallet not found",
                    ErrorCode.E001,
                    ErrorType.ERROR);
        }

        final Wallet wallet = existingWallet.get();

        return repository.findUserWalletExpenses(wallet.getId()).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getAllWalletTransactions(String walletName) {
        final User user = resolver.getAuthenticatedUser();
        final Optional<Wallet> existingWallet = walletService.getUserWallet(walletName, user.getId());

        if (existingWallet.isEmpty()) {
            throw new BusinessFault(
                    "Wallet not found",
                    ErrorCode.E001,
                    ErrorType.ERROR);
        }

        final Wallet wallet = existingWallet.get();

        return repository.findUserWalletTransactions(wallet.getId()).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TransactionResponse> getAllWalletTransactionsWithPagination(String walletName, Integer offset, Integer limit) {
        final User user = resolver.getAuthenticatedUser();
        final Optional<Wallet> existingWallet = walletService.getUserWallet(walletName, user.getId());

        if (existingWallet.isEmpty()) {
            throw new BusinessFault(
                    "Wallet not found",
                    ErrorCode.E001,
                    ErrorType.ERROR);
        }

        final Wallet wallet = existingWallet.get();

        PageRequest pageRequest = PageRequest.of(offset, limit);
        Page<Transaction> transactionSlice = repository.findUserWalletTransactionsWithPagination(wallet.getId(), pageRequest);

        return transactionSlice.map(mapper::toDTO);
    }
}
