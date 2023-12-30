package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.Category;
import com.example.datnsd56.entity.Color;
import com.example.datnsd56.repository.CategoryRepository;
import com.example.datnsd56.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository repository;
    @Override
    public Page<Category> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<Category> page1 = repository.findAll(pageable);
        return page1;
    }

    @Override
    public List<Category> getAllCate() {
        return repository.findAll();
    }

    @Override
    public void add(Category category) {
        category.setCreateDate(LocalDate.now());
        category.setUpdateDate(LocalDate.now());

        repository.save(category);
    }

    @Override
    public Category getById(Integer id) {
        Category category = repository.findById(id).orElse(null);
        return category;
    }

    @Override
    public void delete(Integer id) {
        Category category = getById(id);
        repository.delete(category);
    }

    @Override
    public void update(Category category) {
        category.setCreateDate(LocalDate.now());
        category.setUpdateDate(LocalDate.now());


        repository.save(category);
    }

    @Override
    public Page<Category> findByName(String name) {
        Pageable page=PageRequest.of(0,5);
        Page<Category> list=repository.findByName(name,page);
        return list;
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);

    }
}
