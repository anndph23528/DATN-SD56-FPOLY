package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Material;
import com.example.datnsd56.repository.MaterialRepository;
import com.example.datnsd56.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class MaterialServiceImpl implements MaterialService {
    @Autowired
    private MaterialRepository repository;

    @Override
    public Material add(Material material) {
        material.setCreateDate(LocalDate.now());
        material.setUpdateDate(LocalDate.now());
        return repository.save(material);
    }

    @Override
    public void remove(Integer id) {
        Material material = repository.findById(id).orElse(null);
        repository.delete(material);
    }

    @Override
    public Page<Material> pageMaterial(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<Material> page = repository.findAll(pageable);
        return page;
    }

    @Override
    public List<Material> getAllMater() {
        return repository.findAll();
    }

    @Override
    public Material getById(Integer id) {
        Material material = repository.findById(id).orElse(null);
        return material;
    }

    @Override
    public Page<Material> findByName(String name) {
        Pageable page=PageRequest.of(0,5);
        Page<Material> list=repository.findByName(name,page);
        return list;
    }

    @Override
    public void update(Material material) {
        material.setCreateDate(LocalDate.now());
        material.setUpdateDate(LocalDate.now());


        repository.save(material);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);

    }
}
