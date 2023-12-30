package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Color;
import com.example.datnsd56.entity.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Integer> {
    @Query(value = "select * from Roles where name = ?1 ",
            nativeQuery = true)
    Page<Roles> findByName(String name, Pageable pageable);

    Roles findRolesByName(String roles);
    boolean existsByName(String Name);
}
