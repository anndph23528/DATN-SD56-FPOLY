package com.example.datnsd56.service;

import com.example.datnsd56.entity.Color;
import com.example.datnsd56.entity.Material;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ColorService {
    Page<Color> getAll(Integer pageNo);
    List<Color> getAllColor();
    void add(Color color);

    Color getById(Integer id);
List<Color> getColorId(Integer id);
    void delete(Integer id);

    void update(Color color);
    Page<Color> findByName(String name);
    boolean existsByName(String name);
}
