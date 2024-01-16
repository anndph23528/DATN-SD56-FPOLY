package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products,Integer>, JpaSpecificationExecutor<Products> {
//    @Query(value = "select p.id,p.brand_id,p.category_id,p.create_date,p.material_id,p.shoe_sole_id,p.status,p.update_date,p.code,p.description,p.name, pd.sell_price from Products as p inner join Product_details pd ON p.id = pd.product_id where pd.product_id=?1", nativeQuery = true)
////    List<ProductDetails> getAllDetail(Integer id);
//    List<Products> getProductDetailsById(Integer id);
    @Modifying
    @Transactional
    @Query(value = "select DISTINCT  p.id,p.brand_id,p.category_id,p.create_date,p.material_id,p.shoe_sole_id,p.status,p.update_date,p.code,p.description,p.name from Products as p join Product_details as pd on p.id=pd.product_id where p.status=1",nativeQuery = true)
List<Products> getAllPros();

    @Modifying
    @Transactional
    @Query(value = "select DISTINCT  p.id,p.brand_id,p.category_id,p.create_date,p.material_id,p.shoe_sole_id,p.status,p.update_date,p.code,p.description,p.name from Products as p join Product_details as pd on p.id=pd.product_id where p.status=1",nativeQuery = true)
    List<Products> getAllPros1(Specification<Products> spec);
    boolean existsByName(String name);
    @Query(value = "select * from Products  where name = ?1 ",
            nativeQuery = true)
    Page<Products> findByName(String name, Pageable pageable);
}
