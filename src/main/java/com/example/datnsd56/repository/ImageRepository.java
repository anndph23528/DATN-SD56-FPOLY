package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Image;
import com.example.datnsd56.entity.Products;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    @Query(value = "select * from Image where product_id = ?1 and id=?2", nativeQuery = true)
    List<Image> getImageByProductId(Integer productId,Integer imageId);
    @Query(value = "select * from Image where product_id = ?1", nativeQuery = true)
    List<Image> getImageByProductIds(Integer productId);
        // Các phương thức khác
        @Modifying
        @Transactional
        @Query(value = "DELETE FROM Image WHERE id = :id", nativeQuery = true)
        void deletess(Integer id);




}
