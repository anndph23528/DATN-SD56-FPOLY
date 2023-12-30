package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.Image;
import com.example.datnsd56.entity.Products;
import com.example.datnsd56.repository.ImageRepository;
import com.example.datnsd56.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;


    @Override
    public Page<Image> getAll(Integer page) {
        Pageable pageable = PageRequest.of(page, 5);
        return imageRepository.findAll(pageable);
    }

    @Override
    public void add(Image image) {
        imageRepository.save(image);
    }

    @Override
    public Image getById(Integer id) {
        return imageRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Integer id) {
        imageRepository.existsById(id);
    }

    @Override
    public void update(Image image) {
        imageRepository.save(image);
    }

    @Override
    public List<Image> getImagesForProducts(Integer id, Integer imageId) {
        return imageRepository.getImageByProductId(id,imageId);
    }

//    @Override
//    public List<Image> getImagesForProducts(Integer id) {
//        List<Image> list = imageRepository.getImageByProductId(id);
//        return list;
//    }

//    @Override
//    public List<Image> getall() {
//        return imageRepository.findAll();
//    }

}
