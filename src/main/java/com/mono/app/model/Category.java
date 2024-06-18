package com.mono.app.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "is_enabled", columnDefinition = "boolean default true", nullable = false)
    private Boolean isEnabled = true;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private Set<SubCategory> subCategories;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<Transaction> transactions;

}
