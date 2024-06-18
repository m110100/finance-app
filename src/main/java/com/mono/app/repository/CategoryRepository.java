package com.mono.app.repository;

import com.mono.app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.name = :name AND c.isEnabled = true")
    Optional<Category> findCategoryByName(String name);

    @Query("SELECT c FROM Category c WHERE c.isEnabled = true")
    Set<Category> findAllCategories();
}
