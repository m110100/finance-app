package com.mono.app.repository;

import com.mono.app.model.TransactionLabel;
import com.mono.app.model.composite_keys.TransactionLabelCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLabelRepository extends JpaRepository<TransactionLabel, TransactionLabelCompositeKey> {

}
