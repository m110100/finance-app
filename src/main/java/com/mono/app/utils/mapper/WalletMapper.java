package com.mono.app.utils.mapper;

import com.mono.app.dto.request.WalletRequest;
import com.mono.app.dto.response.WalletResponse;
import com.mono.app.model.User;
import com.mono.app.model.Wallet;
import com.mono.app.utils.mapper.types.Mapper;
import com.mono.app.utils.security.AuthenticatedUserResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("walletMapper")
public class WalletMapper implements Mapper<Wallet, WalletResponse> {

    @Override
    public WalletResponse toDTO(Wallet dao) {
        return new WalletResponse(
                dao.getName(),
                dao.getStartBalance(),
                dao.getCurrentBalance()
        );
    }
}
