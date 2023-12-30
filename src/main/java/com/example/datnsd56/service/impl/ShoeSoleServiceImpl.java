package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.Color;
import com.example.datnsd56.entity.Material;
import com.example.datnsd56.entity.ShoeSole;
import com.example.datnsd56.repository.ShoeSoleRepository;
import com.example.datnsd56.service.ShoeSoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ShoeSoleServiceImpl implements ShoeSoleService {
    @Autowired
    private ShoeSoleRepository repository;

    @Override
    public Page<ShoeSole> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<ShoeSole> page1 = repository.findAll(pageable);
        return page1;
    }

    @Override
    public List<ShoeSole> getAllSole() {
        return repository.findAll();
    }

    @Override
    public void add(ShoeSole shoeSole) {
        shoeSole.setCreateDate(LocalDate.now());
        shoeSole.setUpdateDate(LocalDate.now());

        repository.save(shoeSole);
    }

    @Override
    public ShoeSole getById(Integer id) {
        ShoeSole shoeSole = repository.findById(id).orElse(null);
        return shoeSole;
    }

    @Override
    public void delete(Integer id) {
        ShoeSole shoeSole = getById(id);
        repository.delete(shoeSole);
    }

    @Override
    public void update(ShoeSole shoeSole) {
        shoeSole.setCreateDate(LocalDate.now());
        shoeSole.setUpdateDate(LocalDate.now());


        repository.save(shoeSole);
    }

    @Override
    public Page<ShoeSole> findByName(String name) {
        Pageable page=PageRequest.of(0,5);
        Page<ShoeSole> list=repository.findByName(name,page);
        return list;
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);

    }
}
