package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Integer> {
    @Query(value = "select * from Brand  where name = ?1 ",
            nativeQuery = true)
    Page<Brand> findByName(String name, Pageable pageable);
    boolean existsByName(String Name);
}
