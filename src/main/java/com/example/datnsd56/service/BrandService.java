package com.example.datnsd56.service;

import com.example.datnsd56.entity.Brand;
import com.example.datnsd56.entity.Category;
import com.example.datnsd56.entity.Material;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BrandService {
    Page<Brand> getAll(Integer pageNo);
    List<Brand> getAllBrand();
    void add(Brand brand);

    Brand getById(Integer id);

    void delete(Integer id);

    void update(Brand brand);
    Page<Brand> findByName(String name);
    boolean existsByName(String name);

}
