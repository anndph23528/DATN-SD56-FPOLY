package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Color;
import com.example.datnsd56.entity.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {
    @Query(value = "SELECT DISTINCT \n" +
        "  c.id,\n" +
        "  c.name,\n" +
        "  c.code,\n" +
        "  c.status,\n" +
        "  c.create_date,\n" +
        "  c.update_date\n" +
        "FROM\n" +
        "  Color AS c\n" +
        "INNER JOIN\n" +
        "  Product_details AS p\n" +
        "ON\n" +
        "  c.id = p.color_id WHERE   CAST(p.product_id AS int) = ?;",nativeQuery = true)
    List<Color> findColorByid(Integer id);
    @Query(value = "select * from Color where name = ?1 ",
            nativeQuery = true)
    Page<Color> findByName(String name, Pageable pageable);
    boolean existsByName(String Name);
}
