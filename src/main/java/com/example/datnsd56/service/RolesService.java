package com.example.datnsd56.service;

import com.example.datnsd56.entity.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RolesService {
    Page<Roles> getAllbypage(Pageable pageable);
    List<Roles> getAll();
    Roles detail(Integer id);
    Roles add(Roles roles);
    void update(Roles roles);
    void delete(Integer id);
    Page<Roles> findByName(String name);
    boolean existsByName(String Name);
    Roles findbyname1(String roles);
}
