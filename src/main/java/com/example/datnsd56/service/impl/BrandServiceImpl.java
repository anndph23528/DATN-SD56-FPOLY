package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.Brand;
import com.example.datnsd56.entity.Category;
import com.example.datnsd56.repository.BrandRepository;
import com.example.datnsd56.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository repository;

    @Override
    public Page<Brand> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<Brand> page1 = repository.findAll(pageable);
        return page1;
    }

    @Override
    public List<Brand> getAllBrand() {
        return repository.findAll();
    }

    @Override
    public void add(Brand brand) {
        brand.setCreateDate(LocalDate.now());
        brand.setUpdateDate(LocalDate.now());

        repository.save(brand);
    }

    @Override
    public Brand getById(Integer id) {
        Brand brand = repository.findById(id).orElse(null);
        return brand;
    }

    @Override
    public void delete(Integer id) {
        Brand brand = getById(id);
        repository.delete(brand);
    }

    @Override
    public void update(Brand brand) {
        brand.setCreateDate(LocalDate.now());
        brand.setUpdateDate(LocalDate.now());


        repository.save(brand);
    }

    @Override
    public Page<Brand> findByName(String name) {
        Pageable page=PageRequest.of(0,5);
        Page<Brand> list=repository.findByName(name,page);
        return list;
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);

    }
}
