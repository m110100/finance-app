package com.mono.app.repository;

import com.mono.app.model.Role;
import com.mono.app.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.name = :name AND r.isEnabled = true")
    Optional<Role> findByName(RoleType name);

    @Query(value = """
        SELECT r FROM Role r JOIN FETCH r.users u\s
        WHERE u.id = :id AND r.isEnabled = true
    """)
    Optional<Role> findByUserId(Long id);
}
