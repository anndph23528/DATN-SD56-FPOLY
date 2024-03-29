package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.Image;
import com.example.datnsd56.entity.ProductDetails;
import com.example.datnsd56.entity.Products;
import com.example.datnsd56.entity.Size;
import com.example.datnsd56.repository.ImageRepository;
import com.example.datnsd56.repository.ProductDetailsRepository;
import com.example.datnsd56.repository.ProductsRepository;
import com.example.datnsd56.service.ProductDetailsService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
//import java.math.Double;
//import java.math.Double;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductDetailsServiceImpl implements ProductDetailsService {
    @Autowired
    private ProductDetailsRepository productDetailsRepository;
    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Page<ProductDetails> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(Sort.Direction.DESC, "id"));
        return productDetailsRepository.findAll(pageable);
    }
    @Override
    public Page<ProductDetails> searchByPriceRange(Double minPrice, Double maxPrice, Pageable pageable) {
        return productDetailsRepository.findBySellPriceBetween(minPrice, maxPrice, pageable);
    }

    // Phương thức tìm kiếm theo tên
    @Override
    public Page<ProductDetails> searchByName(String productName, Pageable pageable) {
        return productDetailsRepository.findByProductId_NameContainingIgnoreCase(productName, pageable);
    }

    // Phương thức lấy tất cả sản phẩm chi tiết
//    @Override
//    public Page<ProductDetails> getAllProductDetails(Pageable pageable) {
//        return productDetailsRepository.findAll(pageable);
//    }
    @Override
    public Page<ProductDetails> getAllProductDetails(Pageable pageable) {
        return productDetailsRepository.findAll(pageable);
    }

    @Override
    // Trong ProductService hoặc ProductRepository
    public boolean isQuantityAvailable(Integer productId, Integer sizeId, Integer colorId, Integer requestedQuantity) {
        // Lấy thông tin chi tiết sản phẩm
        ProductDetails productDetail = getCart(productId, colorId, sizeId);

        // Kiểm tra sản phẩm có tồn tại không
        if (productDetail == null) {
            return false; // Sản phẩm không tồn tại
        }

        // Kiểm tra số lượng còn lại
        int availableQuantity = productDetail.getQuantity();
        return availableQuantity >= requestedQuantity;
    }


    public int checkQuantity(Integer productId, Integer colorId, Integer sizeId, Integer requestedQuantity) {
        ProductDetails productDetails = productDetailsRepository.getCart(productId, colorId, sizeId);

        if (productDetails != null) {
            int availableQuantity = productDetails.getQuantity();
            return (availableQuantity >= requestedQuantity) ? (availableQuantity - requestedQuantity) : -1;
        } else {
            return -1;
        }
    }





    @Override
    public int getProductQuantity(int productId) {

            Optional<ProductDetails> productDetails = productDetailsRepository.findById(productId);
            return (productDetails != null) ? productDetails.get().getQuantity() : 0;

    }

    @Override
    public List<ProductDetails> getAllCTSP() {
        return productDetailsRepository.findAll();
    }

    @Override
    public Page<ProductDetails> search( Double sellPrice) {
        Pageable pageable = PageRequest.of(0, 10);
        return productDetailsRepository.findProductDetailsBySellPrice( sellPrice, pageable);
    }


    @Override
    public ProductDetails add(ProductDetails productDetails, MultipartFile[] files) throws IOException, SQLException {
        return null;
    }

    @Override
    public ProductDetails getByIds(Integer id) {
        return productDetailsRepository.getByIds(id);

    }


    @Override
    public Optional<ProductDetails> findBySanPhamId(Integer idSanPham) {
        Optional<ProductDetails> chiTietSanPham = productDetailsRepository.findById(idSanPham);

        if (chiTietSanPham.isPresent()){
            return chiTietSanPham;
        }

        return Optional.empty();
    }

    @Override
    public ProductDetails save(ProductDetails productDetails) {
        return productDetailsRepository.save(productDetails);
    }

//    @Override
//    public ProductDetails add(ProductDetails productDetails, MultipartFile[] files) throws IOException, SQLException {
//        productDetails.setCreateDate(LocalDate.now());
//        productDetails.setUpdateDate(LocalDate.now());
//        productDetailsRepository.save(productDetails);
//        for (MultipartFile file : files) {
//            Image anhSanPham = new Image();
//            byte[] bytes = file.getBytes();
//            Blob blob = new SerialBlob(bytes);
//            anhSanPham.setProductDetailId(productDetails);
//            anhSanPham.setUrl(blob);
//            imageRepository.save(anhSanPham);
//        }
//        return productDetails;
//    }

    @Override
    public ProductDetails getById(Integer id) {
        return productDetailsRepository.findById(id).orElse(null);
    }
    @Transactional
    public void deleteAllProductDetails() {
        productsRepository.deleteAll();
    }
    @Override
    public Optional<ProductDetails> findById(Integer id) {
        Optional<ProductDetails> chiTietSanPham = productDetailsRepository.findById(id);

        if (chiTietSanPham.isPresent()){
            return chiTietSanPham;
        }
        return Optional.empty();
    }

    @Override
    public List<ProductDetails> listPending() {
        return productDetailsRepository.listPending();
    }

    @Override
    public Products getOneProdcut(Integer id) {
        return productsRepository.findById(id).orElse(null);
    }

    @Override
    public List<ProductDetails> findProductDetailsBySellPrice(Integer sellprine) {
        return null;
    }

    @Override
    public List<ProductDetails> getProductDetailsById(Integer id) {
        return productDetailsRepository.getProductDetailsById(id);
    }

    @Override
    public void delete(Integer id) {
        productDetailsRepository.deletects(id);
    }

    @Override
    public void deletess(Integer id) {
        productDetailsRepository.deleteById(id);
    }

    @Override
    public List<ProductDetails> getProductsByProductId(Integer productId) {
        return productDetailsRepository.getProductDetailsByProductId(productId);
    }

    @Override
    public ProductDetails getCart(Integer productId, Integer color, Integer size) {
        ProductDetails productDetails=productDetailsRepository.getCart(productId,color,size);
        return productDetails;
    }

    @Override
    public BigDecimal getPrice(Integer id, Integer colorId, Integer sizeId) {
        BigDecimal price = productDetailsRepository.getDetail(id, colorId, sizeId);
        return price;
    }

    @Override
    public Integer getQuantity(Integer id, Integer colorId, Integer sizeId) {
        return productDetailsRepository.getQuantity(id,colorId,sizeId);
    }
@Transactional
    @Override
    public void update(ProductDetails productDetails) {

//        List<Image> existingImages = imageRepository.getImageByProductId(productDetails.getId());

        // Cập nhật thông tin sản phẩm

        productDetails.setCreateDate(LocalDate.now());
        productDetails.setUpdateDate(LocalDate.now());
        productDetailsRepository.save(productDetails);

        // Kiểm tra xem có ảnh mới được chọn không
//        if (files != null && files.length > 0) {
//            // Nếu có ảnh mới, xóa tất cả ảnh cũ của sản phẩm
//            imageRepository.deleteAll(existingImages);
//
//            // Lưu ảnh mới vào danh sách
//            for (MultipartFile file : files) {
//                byte[] bytes = file.getBytes();
//                Blob blob = new SerialBlob(bytes);
//                Image newImage = new Image();
//                newImage.setProductId(productDetails.getProductId());
//                newImage.setUrl(blob);
//                imageRepository.save(newImage);
//            }
//        } else {
//            // Nếu không có ảnh mới, giữ nguyên ảnh cũ
//            Products currentProducts = productsRepository.findById(productDetails.getProductId().getId()).orElse(null);
//
//            if (currentProducts != null) {
//                productDetails.getProductId().setImages(currentProducts.getImages());
//            }
//        }

    }

    @Override
    public List<ProductDetails> findProductDetailsByColorIdAndSizeIdAndAndProductId(Integer colorId, Integer sizeId, Integer id) {
        return productDetailsRepository.findProductDetailsByColorIdAndSizeIdAndAndProductId(colorId,sizeId);
    }

    @Override
    public Double getprice(String color, String size) {
        return null;
    }

//    @Override
//    public @Min(value = 1, message = "lon hon 0") Double getprice(String color, String size) {
//        @Min(value = 1, message = "lon hon 0") Double productDetails=  productDetailsRepository.getPrice(color,size);
//
//        return productDetails;
//    }

//    @Override
//    public List<ProductDetails> getProductsByProductId(Integer productId) {
//        return productDetailsRepository.findByProductId(productId);
//    }


}
