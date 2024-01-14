package com.example.datnsd56.repository;

import com.example.datnsd56.entity.OrderItem;
import com.example.datnsd56.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ThongKeRepository extends JpaRepository<OrderItem,Integer> {
@Query(value = "\tSELECT  O.fullname,O.account_id,O.phone,O.address,O.sale_method,O.email,O.voucher_id,O.Shipping_fee,O.code,O.customer_id,O.update_date,O.total,O.create_date,O.order_status,OI.product_details_id,OI.id,OI.price,OI.quantity,OI.status,OI.rate_id,OI.order_id FROM order_items AS OI JOIN Orderss AS O ON O.id = OI.order_id;",nativeQuery = true)
List<OrderItem> getAllQ();
@Query(value = "SELECT  O.fullname,O.account_id,O.phone,O.address,O.sale_method,O.email,O.voucher_id,O.Shipping_fee,O.code,\n" +
        "\tO.customer_id,O.update_date,O.total,O.create_date,O.order_status,OI.product_details_id,OI.id,OI.price,OI.quantity,\n" +
        "\tOI.status,OI.rate_id,OI.order_id FROM order_items AS OI JOIN Orderss AS O ON O.id = OI.order_id WHERE  O.order_status = '2';",nativeQuery = true)
List<OrderItem>getAllByStatusdanggiao();
@Query(value = "SELECT  O.fullname,O.account_id,O.phone,O.address,O.sale_method,O.email,O.voucher_id,O.Shipping_fee,O.code,\n" +
        "\tO.customer_id,O.update_date,O.total,O.create_date,O.order_status,OI.product_details_id,OI.id,OI.price,OI.quantity,\n" +
        "\tOI.status,OI.rate_id,OI.order_id FROM order_items AS OI JOIN Orderss AS O ON O.id = OI.order_id WHERE  O.order_status = '0';",nativeQuery = true)
List<OrderItem>getAllByStatushuy();
    @Query(value = "SELECT  O.fullname,O.account_id,O.phone,O.address,O.sale_method,O.email,O.voucher_id,O.Shipping_fee,O.code,\n" +
        "\tO.customer_id,O.update_date,O.total,O.create_date,O.order_status,OI.product_details_id,OI.id,OI.price,OI.quantity,\n" +
        "\tOI.status,OI.rate_id,OI.order_id FROM order_items AS OI JOIN Orderss AS O ON O.id = OI.order_id WHERE  O.order_status = '0';",nativeQuery = true)
    Page<Orders>getAllByStatushuy1(Pageable pageable);


    @Query(value = "SELECT  O.fullname,O.account_id,O.phone,O.address,O.sale_method,O.email,O.voucher_id,O.Shipping_fee,O.code,\n" +
        "\tO.customer_id,O.update_date,O.total,O.create_date,O.order_status,OI.product_details_id,OI.id,OI.price,OI.quantity,\n" +
        "\tOI.status,OI.rate_id,OI.order_id FROM order_items AS OI JOIN Orderss AS O ON O.id = OI.order_id WHERE  O.order_status = '1';",nativeQuery = true)
List<OrderItem>getAllByStatusdahoathanh();
@Query(value = "\n" +
        "\n" +
        "\tSELECT\n" +
        "    O.fullname,\n" +
        "    O.account_id,\n" +
        "    O.phone,\n" +
        "    O.address,\n" +
        "    O.sale_method,\n" +
        "    O.email,\n" +
        "    O.voucher_id,\n" +
        "    O.Shipping_fee,\n" +
        "    O.code,\n" +
        "    O.customer_id,\n" +
        "    O.update_date,\n" +
        "    O.total,\n" +
        "    O.create_date,\n" +
        "    O.order_status,\n" +
        "    OI.product_details_id,\n" +
        "    OI.id AS OrderItemID,\n" +
        "    OI.price,\n" +
        "    OI.quantity,\n" +
        "    OI.status,\n" +
        "    OI.rate_id,\n" +
        "    OI.order_id\n" +
        "FROM\n" +
        "    order_items AS OI\n" +
        "JOIN\n" +
        "    Orderss AS O ON O.id = OI.order_id\n" +
        "JOIN (\n" +
        "    SELECT TOP 5\n" +
        "        product_details_id,\n" +
        "        SUM(quantity) AS TotalQuantitySold\n" +
        "    FROM\n" +
        "        order_items\n" +
        "    GROUP BY\n" +
        "        product_details_id\n" +
        "    ORDER BY\n" +
        "        TotalQuantitySold DESC\n" +
        ") AS TopProducts ON OI.product_details_id = TopProducts.product_details_id\n" +
        "ORDER BY\n" +
        "    TopProducts.TotalQuantitySold DESC;\n",nativeQuery = true)
List<OrderItem>getAllTop5();



@Query(value = "SELECT\n" +
        "    COUNT(DISTINCT O.id) AS TotalOrders\n" +
        "FROM\n" +
        "    Orderss AS O\n" +
        "WHERE\n" +
        "    CONVERT(date, O.create_date) = CONVERT(date, GETDATE());",nativeQuery = true)
BigDecimal getTotalProducts();
@Query(value = "SELECT\n" +
        "    SUM(total) AS TotalRevenue\n" +
        "FROM\n" +
        "    Orderss\n" +
        "WHERE\n" +
        "    CONVERT(date, create_date) = CONVERT(date, GETDATE())\n" +
        "    AND order_status = '1';",nativeQuery = true)
BigDecimal getTotalManey();

    @Query(value = "SELECT\n" +
            "    COUNT(DISTINCT id) AS TotalCanceledOrders\n" +
            "FROM\n" +
            "    Orderss\n" +
            "WHERE\n" +
            "    order_status = '0';", nativeQuery = true)
    BigDecimal getTotalHuy();
    @Query(value = "SELECT\n" +
        "    COUNT(DISTINCT id) AS TotalCanceledOrders\n" +
        "FROM\n" +
        "    Orderss\n" +
        "WHERE\n" +
        "    order_status = '1';", nativeQuery = true)
    BigDecimal getTotalHt();
    @Query(value = "SELECT\n" +
            "    COUNT(DISTINCT id) AS TotalOrders\n" +
            "FROM\n" +
            "    Orderss;", nativeQuery = true)
    BigDecimal getTotalAll();

    @Query(value = "SELECT\n" +
            "    SUM(total) AS TotalRevenue\n" +
            "FROM\n" +
            "    Orderss\n" +
            "WHERE\n" +
            "    order_status = '1';", nativeQuery = true)
    BigDecimal getTotalAllManey();
    @Query(value = "SELECT \n" +
            "     O.fullname, O.account_id, O.phone, O.address, O.sale_method, O.email, O.voucher_id, O.Shipping_fee,\n" +
            "    O.code, O.customer_id, O.update_date, O.total, O.create_date, O.order_status,\n" +
            "    OI.product_details_id, OI.id, OI.price, OI.quantity, OI.status, OI.rate_id, OI.order_id\n" +
            "FROM \n" +
            "    order_items AS OI\n" +
            "JOIN \n" +
            "    Orderss AS O ON O.id = OI.order_id\n" +
            "WHERE \n" +
            "    O.create_date BETWEEN :tuNgay AND :denNgay", nativeQuery = true)
    List<OrderItem> getAllByTime(@Param("tuNgay") String tuNgay, @Param("denNgay") String denNgay);



//-
}
