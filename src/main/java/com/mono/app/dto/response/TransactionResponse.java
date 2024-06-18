package com.mono.app.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mono.app.model.enums.TransactionType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public record TransactionResponse(
        TransactionType type,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal amount,
        String wallet,
        Date date,
        String note,
        String category,
        Set<TransactionSubCategoryResponse> subCategories,
        Set<TransactionLabelResponse> labels
) { }
