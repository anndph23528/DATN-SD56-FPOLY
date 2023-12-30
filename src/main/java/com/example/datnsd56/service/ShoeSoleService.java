package com.example.datnsd56.service;

import com.example.datnsd56.entity.Material;
import com.example.datnsd56.entity.ShoeSole;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShoeSoleService {
    Page<ShoeSole> getAll(Integer pageNo);
    List<ShoeSole> getAllSole();
    void add(ShoeSole shoeSole);

    ShoeSole getById(Integer id);

    void delete(Integer id);

    void update(ShoeSole shoeSole);
    Page<ShoeSole> findByName(String name);
    boolean existsByName(String name);
}
