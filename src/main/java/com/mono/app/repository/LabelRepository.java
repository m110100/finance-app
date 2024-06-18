package com.mono.app.repository;

import com.mono.app.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    @Query("SELECT l FROM Label l INNER JOIN User u ON l.user.id = u.id " +
            "WHERE l.name = :name AND u.id = :userId AND (l.isEnabled = true AND u.isEnabled)")
    Optional<Label> findUserLabel(String name, Long userId);

    @Query("SELECT l FROM Label l INNER JOIN User u ON l.user.id = u.id " +
            "WHERE u.id = :userId AND (l.isEnabled = true AND u.isEnabled)")
    Set<Label> findAllUserLabels(Long userId);
}
