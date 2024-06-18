package com.mono.app.model;

import com.mono.app.model.composite_keys.TransactionSubCategoryCompositeKey;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "transaction", "subCategory"})
@Entity
@Table(name = "transaction_sub_category")
public class TransactionSubCategory {
    @EmbeddedId
    TransactionSubCategoryCompositeKey id;

    @ManyToOne
    @MapsId("transactionId")
    @JoinColumn(name = "transaction_id", nullable = false)
    Transaction transaction;

    @ManyToOne
    @MapsId("subCategoryId")
    @JoinColumn(name = "sub_category_id", nullable = false)
    SubCategory subCategory;

    @Column(name = "is_enabled", columnDefinition = "boolean default true", nullable = false)
    private Boolean isEnabled = true;
}
