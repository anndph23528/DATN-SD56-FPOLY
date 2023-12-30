package com.example.datnsd56.service;
import com.example.datnsd56.entity.Image;
import com.example.datnsd56.entity.Products;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;

public interface ImageService {
    Page<Image> getAll(Integer page);

    void add(Image image);

    Image getById(Integer id);

    void delete(Integer id);

    void update(Image image);

    List<Image> getImagesForProducts(Integer id,Integer imageId);

}
