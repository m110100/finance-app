package com.mono.app.repository;

import com.mono.app.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    @Query("SELECT sc FROM SubCategory sc INNER JOIN User u ON sc.user.id = u.id " +
            "WHERE sc.name = :name AND u.id = :userId AND (sc.isEnabled = true AND u.isEnabled)")
    Optional<SubCategory> findUserSubCategory(String name, Long userId);

    @Query("SELECT sc FROM SubCategory sc " +
            "INNER JOIN Category c ON sc.category.id = c.id " +
            "INNER JOIN User u ON sc.user.id = u.id " +
            "WHERE u.id = :userId AND (sc.isEnabled = true AND u.isEnabled)")
    Set<SubCategory> findAllUserSubCategories(Long userId);
}
