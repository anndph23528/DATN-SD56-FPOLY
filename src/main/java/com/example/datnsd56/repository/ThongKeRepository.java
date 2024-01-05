package com.example.datnsd56.repository;

import com.example.datnsd56.entity.OrderItem;
import com.example.datnsd56.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
        "\tOI.status,OI.rate_id,OI.order_id FROM order_items AS OI JOIN Orderss AS O ON O.id = OI.order_id WHERE  O.order_status = '1';",nativeQuery = true)
List<OrderItem>getAllByStatusdahoathanh();


//@Query(value = "")

}
