package com.mono.app.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

public record WalletRequest(
        String name,
        String startBalance
) { }
