package com.example.datnsd56.service;

import com.example.datnsd56.entity.ProductDetails;
import com.example.datnsd56.entity.Products;
import com.example.datnsd56.entity.Size;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
//import java.math.Double;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductDetailsService {
    Page<ProductDetails> getAll(Integer pageNo);
    int getProductQuantity(int productId);

    List<ProductDetails> getAllCTSP();
   boolean isQuantityAvailable(Integer productId, Integer sizeId, Integer colorId, Integer requestedQuantity) ;

    Page<ProductDetails> search(  Double sellPrice);
    Page<ProductDetails> getAllProductDetails(Pageable pageable);
    Page<ProductDetails> searchByPriceRange(Double minPrice, Double maxPrice, Pageable pageable);
    Page<ProductDetails> searchByName(String productName, Pageable pageable);
    // Tìm kiếm theo tên
//    Page<ProductDetails> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);
    ProductDetails add(ProductDetails productDetails, MultipartFile[] files) throws IOException, SQLException;
    ProductDetails getByIds(Integer id);
    Optional<ProductDetails> findBySanPhamId(Integer idSanPham);
    ProductDetails save(ProductDetails productDetails);
    ProductDetails getById(Integer id);
    Optional<ProductDetails> findById(Integer id);
    Products getOneProdcut(Integer id);
    List<ProductDetails> findProductDetailsBySellPrice(Integer sellprine);
    List<ProductDetails> getProductDetailsById(Integer id);

    List<ProductDetails> listPending();
    void delete(Integer id);
    void deletess(Integer id);
    List<ProductDetails> getProductsByProductId(Integer productId);
    BigDecimal getPrice(Integer id, Integer colorId, Integer sizeId);
    Integer getQuantity(Integer id, Integer colorId, Integer sizeId);

    ProductDetails getCart(Integer productId, Integer color, Integer size);
    void update(ProductDetails productDetails);

    List<ProductDetails> findProductDetailsByColorIdAndSizeIdAndAndProductId(Integer colorId,Integer sizeId,Integer d);
@Min(value = 1, message = "lon hon 0") Double getprice(String color, String size);
}
