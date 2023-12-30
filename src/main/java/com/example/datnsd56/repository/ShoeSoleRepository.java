package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Material;
import com.example.datnsd56.entity.ShoeSole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoeSoleRepository extends JpaRepository<ShoeSole, Integer> {
    @Query(value = "select * from Shoe_sole  where name = ?1 ",
            nativeQuery = true)
    Page<ShoeSole> findByName(String name, Pageable pageable);
    boolean existsByName(String name);
}
