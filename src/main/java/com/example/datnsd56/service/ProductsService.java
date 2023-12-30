package com.example.datnsd56.service;

import com.example.datnsd56.entity.Color;
import com.example.datnsd56.entity.Material;
import com.example.datnsd56.entity.ProductDetails;
import com.example.datnsd56.entity.Products;
import com.example.datnsd56.entity.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
//import java.math.Double;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductsService {
    Page<Products> getAll(Integer pageNo);

    List<Products> getAllPro();
    List<Products> getAllPros();
    void addProduct(Products products, List<Color> colorList, List<Size> sizeList, MultipartFile[] files) throws IOException, SQLException;

    List<ProductDetails> getAllDetail(Integer id);
    Products findProductById(Integer id);
    Products getById(Integer id);
    ProductDetails updatePrice(Integer id, BigDecimal price);
    void addProductDetail(List<Integer> id, List<Integer> soLuong, List<BigDecimal> donGia);

    void updateProductDetail(List<Integer> id, List<Integer> soLuong, List<BigDecimal> donGia);

    Optional<ProductDetails> getOne(Integer id);

    void delete(Integer id);
//    List<Products> getProductDetailsById(Integer id);

    void updateProduct(Products products, MultipartFile[] files) throws IOException, SQLException;
    List<Integer> findSelectedSizeIds(@Param("id") Integer id);
    List<Integer> findSelectedColorIds(@Param("id") Integer id);
    boolean existsByName(String name);
    Page<Products> findByName(String name);
}
