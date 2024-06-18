package com.mono.app.service;

import com.mono.app.dto.request.WalletRequest;
import com.mono.app.dto.response.WalletResponse;
import com.mono.app.exceptions.BusinessFault;
import com.mono.app.exceptions.types.ErrorCode;
import com.mono.app.exceptions.types.ErrorType;
import com.mono.app.model.User;
import com.mono.app.model.Wallet;
import com.mono.app.repository.WalletRepository;
import com.mono.app.utils.mapper.types.Mapper;
import com.mono.app.utils.security.AuthenticatedUserResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class WalletService {
    private final WalletRepository repository;
    private final AuthenticatedUserResolver resolver;
    private final Mapper<Wallet, WalletResponse> mapper;

    @Autowired
    public WalletService(
            WalletRepository repository,
            AuthenticatedUserResolver resolver,
            @Qualifier("walletMapper") Mapper<Wallet, WalletResponse> mapper
    ) {
        this.repository = repository;
        this.resolver = resolver;
        this.mapper = mapper;
    }

    public void addIncome(Wallet wallet, BigDecimal amount) {
        wallet.setCurrentBalance(wallet.getCurrentBalance().add(amount));
        repository.save(wallet);
    }

    public void addExpense(Wallet wallet, BigDecimal amount) {
        wallet.setCurrentBalance(wallet.getCurrentBalance().subtract(amount));
        repository.save(wallet);
    }

    public List<WalletResponse> getAllUserWallets() {
        User user = resolver.getAuthenticatedUser();

        return repository.findAllUserWallets(user.getId()).stream()
                .map(mapper::toDTO)
                .collect(toList());
    }

    public Optional<Wallet> getUserWallet(String name, Long userId) {
        return repository.findUserWallet(name, userId);
    }

    public WalletResponse addWallet(WalletRequest request) {
        User user = resolver.getAuthenticatedUser();

        final Optional<Wallet> existingWallet = repository.findUserWallet(request.name(), user.getId());

        if (existingWallet.isPresent()) {
            throw new BusinessFault(
                    String.format("Wallet with name %s already exists", request.name()),
                    ErrorCode.E002,
                    ErrorType.ERROR);
        } else {
            Wallet wallet = Wallet.builder()
                    .name(request.name())
                    .startBalance(new BigDecimal(request.startBalance()))
                    .currentBalance(new BigDecimal(request.startBalance()))
                    .isEnabled(true)
                    .user(user)
                    .build();

            wallet = repository.save(wallet);

            return mapper.toDTO(wallet);
        }
    }
}
