package com.mono.app.model;

import com.mono.app.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "subCategories", "labels"})
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "note", nullable = true)
    private String note;

    @Column(name = "type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TransactionType type;

    @Column(name = "is_enabled", columnDefinition = "boolean default true", nullable = false)
    private Boolean isEnabled = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER)
    Set<TransactionSubCategory> subCategories;

    @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER)
    Set<TransactionLabel> labels;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;
}