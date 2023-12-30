package com.example.datnsd56.service;

import com.example.datnsd56.entity.Category;
import com.example.datnsd56.entity.Color;
import com.example.datnsd56.entity.Material;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    Page<Category> getAll(Integer pageNo);
    List<Category> getAllCate();
    void add(Category category);

    Category getById(Integer id);

    void delete(Integer id);

    void update(Category category);
    Page<Category> findByName(String name);
    boolean existsByName(String name);

}
