package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.Roles;
import com.example.datnsd56.repository.RolesRepository;
import com.example.datnsd56.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesSeviceImpl implements RolesService {
    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public Page<Roles> getAllbypage(Pageable pageable) {
        return rolesRepository.findAll(pageable);
    }

    @Override
    public List<Roles> getAll() {
        return rolesRepository.findAll();
    }

    @Override
    public Roles detail(Integer id) {
        Roles roles = rolesRepository.findById(id).orElse(null);
        return roles;
    }

    @Override
    public Roles add(Roles roles) {
        return  rolesRepository.save(roles);
    }

    @Override
    public void update(Roles roles) {
        rolesRepository.save(roles);

    }

    @Override
    public void delete(Integer id) {
        Roles roles = detail(id);
        rolesRepository.delete(roles);
    }

    @Override
    public Roles findbyname1(String roles) {
      Roles r1 = rolesRepository.findRolesByName("user");
        return r1;
    }
    @Override
    public Page<Roles> findByName(String name) {
        Pageable page=PageRequest.of(0,5);
        Page<Roles> list=rolesRepository.findByName(name,page);
        return list;
    }

    @Override
    public boolean existsByName(String Name) {
        return rolesRepository.existsByName(Name);
    }
}
