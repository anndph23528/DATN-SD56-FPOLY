package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.Color;
import com.example.datnsd56.entity.Image;
import com.example.datnsd56.entity.ProductDetails;
import com.example.datnsd56.entity.Products;
import com.example.datnsd56.entity.Roles;
import com.example.datnsd56.entity.Size;
import com.example.datnsd56.repository.ColorRepository;
import com.example.datnsd56.repository.ImageRepository;
import com.example.datnsd56.repository.ProductDetailsRepository;
import com.example.datnsd56.repository.ProductsRepository;
import com.example.datnsd56.repository.SizeRepository;
import com.example.datnsd56.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
//import java.math.Double;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductsServiceImpl implements ProductsService {
    @Autowired
    private ProductsRepository productRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @Override
    public Page<Products> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<Products> page1 = productRepository.findAll(pageable);
        return page1;

    }

    @Override
    public List<Products> getAllPro() {
        return productRepository.findAll();
    }

    @Override
    public List<Products> getAllPros() {
        return productRepository.getAllPros();
    }

    @Override
    public void addProduct(Products products, List<Color> colorList, List<Size> sizeList, MultipartFile[] files) throws IOException, SQLException {
        productRepository.save(products);
        for (MultipartFile file : files) {
            Image anhSanPham = new Image();
            byte[] bytes = file.getBytes();
            Blob blob = new SerialBlob(bytes);
            anhSanPham.setProductId(products);
            anhSanPham.setUrl(blob);
            imageRepository.save(anhSanPham);
        }
        List<ProductDetails> listDetail = new ArrayList<>();
        for (Color color : colorList) {
            for (Size size : sizeList) {
                ProductDetails details = new ProductDetails();
                details.setProductId(products);
                details.setStatus(false);
                details.setQuantity(1);
                details.setColorId(color);
                details.setSizeId(size);
                details.setSellPrice(BigDecimal.valueOf(1));
                details.setCreateDate(LocalDate.now());
                listDetail.add(details);
            }
        }
        productDetailsRepository.saveAll(listDetail);
    }

    @Override
    public List<ProductDetails> getAllDetail(Integer id) {
        return productDetailsRepository.getAllDetail(id);
    }

    @Override
    public Products getById(Integer id) {
        Products products = productRepository.findById(id).orElse(null);
        return products;
    }


    @Override
    public void addProductDetail(List<Integer> id, List<Integer> soLuong, List<BigDecimal> donGia) {
        if (id.size() != soLuong.size() || id.size() != donGia.size()) {
            // Handle the error, for example, log an error message or throw an exception.
            System.err.println("Input lists have different sizes");
            System.err.println("Id size: " + id.size() + ", quantity size: " + soLuong.size() + ", sellprice size: " + donGia.size());
        } else {
            for (int i = 0; i < id.size(); i++) {
                Integer ids = id.get(i);
                Integer soLuongs = soLuong.get(i);
                BigDecimal donGias = donGia.get(i);
                Optional<ProductDetails> productDetails = getOne(ids);
                if (productDetails.isPresent()) {
                    ProductDetails details = productDetails.get();
                    details.setStatus(true);
                    details.setQuantity(soLuongs);
                    details.setSellPrice(donGias);
                    details.setUpdateDate(LocalDate.now());
                    productDetailsRepository.save(details);
                }
            }
        }
    }

    @Override
    public void updateProductDetail(List<Integer> id, List<Integer> soLuong, List<BigDecimal> donGia) {
        for (int i = 0; i < id.size(); i++) {
            Integer ids = id.get(i);
            Integer soLuongs = soLuong.get(i);
            BigDecimal donGias = donGia.get(i);
            Optional<ProductDetails> productDetails = getOne(ids);
            if (productDetails.isPresent()) {
                ProductDetails details = productDetails.get();
                details.setStatus(true);
                details.setQuantity(soLuongs);
                details.setSellPrice(donGias);
                details.setUpdateDate(LocalDate.now());
                productDetailsRepository.save(details);
            }
        }
    }

    @Override
    public Optional<ProductDetails> getOne(Integer id) {
        return Optional.of(productDetailsRepository.findById(id).get());
    }



    @Override
    public void delete(Integer id) {
        Products products = getById(id);
        productRepository.delete(products);
    }



    @Override
    public void updateProduct(Products products, MultipartFile[] files) throws IOException, SQLException {
//        products.setUpdateDate(LocalDate.now());
//        productRepository.save(products);
        List<Image> existingImages = imageRepository.getImageByProductIds(products.getId());

        // Cập nhật thông tin sản phẩm
        products.setUpdateDate(LocalDate.now());
        productRepository.save(products);

        // Kiểm tra xem có ảnh mới được chọn không
        if (files != null && files.length > 0) {
            // Nếu có ảnh mới, xóa tất cả ảnh cũ của sản phẩm
            imageRepository.deleteAll(existingImages);

            // Lưu ảnh mới vào danh sách
            for (MultipartFile file : files) {
                byte[] bytes = file.getBytes();
                Blob blob = new SerialBlob(bytes);
                Image newImage = new Image();
                newImage.setProductId(products);
                newImage.setUrl(blob);
                imageRepository.save(newImage);
            }
        } else {
            // Nếu không có ảnh mới, giữ nguyên ảnh cũ
            Products currentProducts = productRepository.findById(products.getId()).orElse(null);

            if (currentProducts != null) {
                products.setImages(currentProducts.getImages());
            }
        }

    }

    @Override
    public List<Integer> findSelectedSizeIds(Integer id) {
        return productDetailsRepository.findSelectedSizeIds(id);
    }

    public List<Integer> getSelectedSizeIds(Integer id) {
        List<ProductDetails> detailsList = productDetailsRepository.getProductDetailsById(id);
        List<Integer> selectedSizes = new ArrayList<>();

        for (ProductDetails details : detailsList) {
            if (details.getSizeId() != null) {
                selectedSizes.add(details.getSizeId().getId());
            }
        }

        return selectedSizes;
    }
    @Override
    public List<Integer> findSelectedColorIds(Integer id) {
        return productDetailsRepository.findSelectedColorIds(id);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);

    }

    @Override
    public Page<Products> findByName(String name) {
        Pageable page=PageRequest.of(0,5);
        Page<Products> list=productRepository.findByName(name,page);
        return list;
    }

    public List<Integer> getSelectedColorIds(Integer id) {
        List<ProductDetails> detailsList = productDetailsRepository.getProductDetailsById(id);
        List<Integer> selectedColors = new ArrayList<>();

        for (ProductDetails details : detailsList) {
            if (details.getSizeId() != null) {
                selectedColors.add(details.getSizeId().getId());
            }
        }

        return selectedColors;
    }
    public void ProductsService(ProductsRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Products findProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }



    public ProductDetails updatePrice(Integer id, BigDecimal price) {
        ProductDetails product = (ProductDetails) productDetailsRepository.getAllDetail(id);
        product.setSellPrice(price);
        return productDetailsRepository.save(product);
    }

}
