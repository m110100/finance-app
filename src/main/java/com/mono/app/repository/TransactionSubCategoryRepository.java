package com.mono.app.repository;

import com.mono.app.model.TransactionSubCategory;
import com.mono.app.model.composite_keys.TransactionSubCategoryCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionSubCategoryRepository
        extends JpaRepository<TransactionSubCategory, TransactionSubCategoryCompositeKey> {

}
