package com.example.datnsd56.service;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Material;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MaterialService {
    Material add(Material material);

    void remove(Integer id);

    Page<Material> pageMaterial(Integer pageNo);

    List<Material> getAllMater();

    Material getById(Integer id);
    Page<Material> findByName(String name);
    void update(Material material);
    boolean existsByName(String name);

}
