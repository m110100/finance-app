package com.mono.app.utils.mapper;

import com.mono.app.dto.request.TransactionRequest;
import com.mono.app.dto.response.TransactionLabelResponse;
import com.mono.app.dto.response.TransactionResponse;
import com.mono.app.dto.response.TransactionSubCategoryResponse;
import com.mono.app.model.Transaction;
import com.mono.app.model.TransactionSubCategory;
import com.mono.app.utils.mapper.types.Mapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@Component("transactionMapper")
public class TransactionMapper implements Mapper<Transaction, TransactionResponse> {
    @Override
    public TransactionResponse toDTO(Transaction dao) {
        String categoryName = dao.getCategory() != null ? dao.getCategory().getName() : null;
        Set<TransactionSubCategoryResponse> subCategories = dao.getSubCategories() != null ?
                dao.getSubCategories().stream()
                        .map(transactionSubCategory -> new TransactionSubCategoryResponse(
                                transactionSubCategory.getSubCategory().getName()))
                        .collect(Collectors.toSet())
                : null;
        Set<TransactionLabelResponse> labels = dao.getLabels() != null ?
                dao.getLabels().stream()
                        .map(transactionLabel -> new TransactionLabelResponse(
                                transactionLabel.getLabel().getName()))
                        .collect(Collectors.toSet())
                : null;

        return new TransactionResponse(
                dao.getType(),
                dao.getAmount(),
                dao.getWallet().getName(),
                dao.getDate(),
                dao.getNote(),
                categoryName,
                subCategories,
                labels
        );
    }
}
