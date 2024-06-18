package com.mono.app.model;

import com.mono.app.model.composite_keys.TransactionLabelCompositeKey;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "transaction", "label"})
@Entity
@Table(name = "transaction_label")
public class TransactionLabel {
    @EmbeddedId
    TransactionLabelCompositeKey id;

    @ManyToOne
    @MapsId("transactionId")
    @JoinColumn(name = "transaction_id", nullable = false)
    Transaction transaction;

    @ManyToOne
    @MapsId("labelId")
    @JoinColumn(name = "label_id", nullable = false)
    Label label;

    @Column(name = "is_enabled", columnDefinition = "boolean default true", nullable = false)
    private Boolean isEnabled = true;
}
