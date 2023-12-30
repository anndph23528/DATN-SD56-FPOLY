package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.Color;
import com.example.datnsd56.repository.ColorRepository;
import com.example.datnsd56.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {
    @Autowired
    private ColorRepository repository;

    @Override
    public Page<Color> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<Color> page1 = repository.findAll(pageable);
        return page1;
    }

    @Override
    public List<Color> getAllColor() {
        return repository.findAll();
    }

    @Override
    public void add(Color color) {
        color.setCreateDate(LocalDate.now());
        color.setUpdateDate(LocalDate.now());

        repository.save(color);
    }

    @Override
    public Color getById(Integer id) {
        Color color = repository.findById(id).orElse(null);
        return color;
    }

    @Override
    public List<Color> getColorId(Integer id) {
        return repository.findColorByid(id);
    }

    @Override
    public void delete(Integer id) {
        Color color = getById(id);
        repository.delete(color);
    }

    @Override
    public void update(Color color) {
        color.setCreateDate(LocalDate.now());
        color.setUpdateDate(LocalDate.now());


        repository.save(color);
    }

    @Override
    public Page<Color> findByName(String name) {
        Pageable page=PageRequest.of(0,5);
        Page<Color> list=repository.findByName(name,page);
        return list;
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }
}
