package com.example.datnsd56.service;

import com.example.datnsd56.entity.Color;
import com.example.datnsd56.entity.ShoeSole;
import com.example.datnsd56.entity.Size;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SizeService {
    Page<Size> getAll(Integer pageNo);

    List<Size> getAllSZ();

    void add(Size size );
    List<Size> getColorId(Integer id);

    Size getById(Integer id);

    void delete(Integer id);

    void update(Size size);
    Page<Size> findByName(String name);
    boolean existsByName(String name);

}
