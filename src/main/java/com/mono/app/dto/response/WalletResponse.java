package com.mono.app.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

public record WalletResponse(
    String name,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal startBalance,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal currentBalance
) { }
