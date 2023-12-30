package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    @Query(value = "select * from Category  where name = ?1 ",
            nativeQuery = true)
    Page<Category> findByName(String name, Pageable pageable);
    boolean existsByName(String name);
}
