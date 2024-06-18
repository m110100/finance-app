package com.mono.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_enabled", columnDefinition = "boolean default false", nullable = false)
    private Boolean isEnabled = true;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Wallet> wallets;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Goal> goals;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<SubCategory> subCategories;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Label> labels;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;
}
