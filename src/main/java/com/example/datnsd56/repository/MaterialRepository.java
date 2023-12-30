package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
    @Query(value = "select * from Material where name = ?1 ",
            nativeQuery = true)
    Page<Material> findByName(String name, Pageable pageable);
    boolean existsByName(String name);
}
