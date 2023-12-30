package com.example.datnsd56.repository;

import com.example.datnsd56.entity.ProductDetails;
import com.example.datnsd56.entity.Products;
import com.example.datnsd56.entity.Size;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//import java.math.Double;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Integer> {
    //@Query("SELECT v FROM ProductDetails v JOIN v.productId s \n" +
//        "WHERE (:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')))\n" +
//        "  AND (:minTien IS NULL OR v.sellPrice >= :minTien)\n" +
//        "  AND (:maxTien IS NULL OR v.sellPrice <= :maxTien)")
    @Transactional
    @Modifying
    @Query(value = "update [Product_details] set status = 0 where id = ?", nativeQuery = true)
    void deletects(@Param("id") Integer id);

    @Query(value = "SELECT v.id,v.color_id,v.size_id,v.quantity,v.sell_price,v.create_date,v.update_date,v.product_id,v.status FROM Product_details v JOIN Products s ON v.product_id = s.id  \n" +
            "            WHERE v.quantity = ?1 or v.sell_price= ?1 or v.id = ?1", nativeQuery = true)
//    @Query("SELECT v FROM ProductDetails v JOIN v.productId s\n" +
//            "WHERE v.quantity = ?1 or v.sellPrice= ?1")
    Page<ProductDetails> findProductDetailsBySellPrice(Double sellPrice, Pageable pageable);

//    List<ProductDetails> findByProductId(Integer productId);


    @Query(value = "select * from Product_details where status = 0", nativeQuery = true)
    List<ProductDetails> listPending();

    @Query(value = "select * from Product_details where product_id=?1", nativeQuery = true)
    List<ProductDetails> getAllDetail(Integer id);

    @Query(value = "select * from Product_details ", nativeQuery = true)
    List<ProductDetails> getProductDetailsByProductId(Integer id);

    //    @Query(value = "select * fgetrom Product_details where product_id=?1", nativeQuery = true)
////    List<ProductDetails> getAllDetail(Integer id);
//   List< Products> findProductDetailsByProductId(Integer id);
    @Query(value = "select sell_price from Product_details where product_id=?1 and color_id=?2 and size_id=?3  ", nativeQuery = true)
    BigDecimal getDetail(Integer productId, Integer color, Integer size);
    @Query(value = "select quantity from Product_details where product_id=?1 and color_id=?2 and size_id=?3  ", nativeQuery = true)
    Integer getQuantity(Integer productId, Integer color, Integer size);

    @Query(value = "select * from Product_details where product_id=?1 and color_id=?2 and size_id=?3  ", nativeQuery = true)
    ProductDetails getCart(Integer productId,Integer color, Integer size);
//    @Query(value = "select * from Product_details where   color_id=?1 and size_id=?1  ", nativeQuery = true)
//    @Min(value = 1, message = "lon hon 0") Double getPrice(String color, String size);

    @Query(value = "select p.sell_price ,p.id,p.color_id,p.create_date,p.product_id,p.quantity,p.size_id,p.status,p.update_date from Product_details AS p join Color as c on p.color_id=c.id join Size as s on s.id=p.size_id join Products as pr on pr.id=p.product_id  where  p.product_id=?1 ", nativeQuery = true)
    List<ProductDetails> getProductDetailsById(Integer id);

    @Query(value = "select p.sell_price from Product_details AS p where color_id=?1 and size_id=?1 and id=?1 ", nativeQuery = true)
    List<ProductDetails> findProductDetailsByColorIdAndSizeIdAndAndProductId(Integer colorId, Integer sizeId);

    //    @UniqueElements/
    @Query(value = "SELECT  * FROM Product_details     where id =?1 ;  ", nativeQuery = true)
    ProductDetails getByIds(Integer id);

    @Query(value = "select * from Product_details where product_id = ?1", nativeQuery = true)
    List<ProductDetails> findBySanPhamId(Integer idSanPham);

    @Transactional
    @Modifying
    @Query(value = "update Products c set c.status = 0 where c.id = :idll")
    void delete(Integer id);

    @Query(value = "select sp from ProductDetails sp where sp.productId.id = :id")
    List<ProductDetails> detailByIdSP(Integer id);

    @Query(value = "SELECT sp from ProductDetails sp where sp.productId.id = :id")
    List<ProductDetails> getAllByIdSP(Integer id);


    @Query(value = "SELECT pd.size_id FROM Product_details pd WHERE pd.product_id = ?1 AND pd.size_id IS NOT NULL", nativeQuery = true)
    List<Integer> findSelectedSizeIds(@Param("id") Integer id);

    @Query(value = "SELECT pd.color_id FROM Product_details pd WHERE pd.product_id = ?1 AND pd.color_id IS NOT NULL", nativeQuery = true)
    List<Integer> findSelectedColorIds(@Param("id") Integer id);

    @Query(value = "SELECT Id FROM Product_details WHERE status = 1 AND product_id  = :productId AND size_id = :sizeId AND color_id = :colorId", nativeQuery = true)
    Integer getProductDetailId(@Param("productId") Integer productId, @Param("sizeId") Integer sizeId, @Param("colorId") Integer colorId);

}
