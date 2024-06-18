package com.mono.app.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_enabled", columnDefinition = "boolean default true", nullable = false)
    private Boolean isEnabled = true;

    @Column(name = "name", nullable = false)
    private String name;
}
