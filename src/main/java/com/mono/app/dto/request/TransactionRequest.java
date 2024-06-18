package com.mono.app.dto.request;

import com.mono.app.model.enums.TransactionType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public record TransactionRequest(
        TransactionType type,
        BigDecimal amount,
        String wallet,
        Date date,
        String note,
        String category,
        Set<TransactionLabelRequest> labels,
        Set<TransactionSubCategoryRequest> subCategories
) { }
