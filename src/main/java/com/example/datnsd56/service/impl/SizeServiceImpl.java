package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.Color;
import com.example.datnsd56.entity.Size;
import com.example.datnsd56.repository.SizeRepository;
import com.example.datnsd56.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {
    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public Page<Size> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<Size> page1 = sizeRepository.findAll(pageable);
        return page1;
    }

    @Override
    public List<Size> getAllSZ() {
        return sizeRepository.findAll();
    }

    @Override
    public void add(Size size) {
        size.setCreateDate(LocalDate.now());
        size.setUpdateDate(LocalDate.now());
        sizeRepository.save(size);
    }

    @Override
    public List<Size> getColorId(Integer id) {
        return sizeRepository.findSizeByid(id);
    }

    @Override
    public Size getById(Integer id) {
        Size size =sizeRepository.findById(id).orElse(null);
        return size;
    }

    @Override
    public void delete(Integer id) {
        sizeRepository.deleteById(id);
    }

    @Override
    public void update(Size size) {
        size.setCreateDate(LocalDate.now());
        size.setUpdateDate(LocalDate.now());
        sizeRepository.save(size);
    }

    @Override
    public Page<Size> findByName(String name) {
        Pageable page=PageRequest.of(0,5);
        Page<Size> list=sizeRepository.findByName(name,page);
        return list;
    }

    @Override
    public boolean existsByName(String name) {
        return sizeRepository.existsByName(name);
    }
}
