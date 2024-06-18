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
public class TransactionLabelCompositeKey implements Serializable {
    @Column(name = "transaction_id")
    Long transactionId;

    @Column(name = "label_id")
    Long labelId;
}
