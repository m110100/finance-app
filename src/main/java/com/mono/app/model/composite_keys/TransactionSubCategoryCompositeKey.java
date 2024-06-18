package com.mono.app.model.composite_keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class TransactionSubCategoryCompositeKey implements Serializable {
    @Column(name = "transaction_id")
    Long transactionId;

    @Column(name = "sub_category_id")
    Long subCategoryId;
}
