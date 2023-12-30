
package com.example.datnsd56.repository;

import com.example.datnsd56.entity.OrderItem;
import com.example.datnsd56.entity.Orders;
import com.example.datnsd56.entity.Voucher;
import com.example.datnsd56.responsi.OrdersCustomer;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    @Query(value = "SELECT HD.id, HD.code, HD.fullname, HD.create_date, SUM(HDCT.quantity) AS tong_so_luong,\n" +
            "            SUM(HDCT.quantity * HDCT.price) as tong_tien, HD.order_status\n" +
            "            FROM Orderss HD\n" +
            "            JOIN order_items HDCT ON HD.id = HDCT.order_id\n" +
            "            GROUP BY HD.id, HD.code, HD.fullname, HD.create_date, HD.total, HD.order_status, HD.phone\n" +
            "            ORDER BY HD.create_date DESC",nativeQuery = true)

    public Page<OrdersCustomer> hienThiPageHD(Pageable pageable);

    List<Orders> findAllByOrderStatus(Integer orderStatus);



    Page<Orders> findAllByOrderStatus(Pageable pageable,Integer orderStatus);

    @Query(value = "select * from Orderss b where b.order_status = ?1 and b.account_id= ?2",nativeQuery = true)
    List<Orders> getOrdes(@Param("orderStatus") Integer orderStatus,
                          @Param("accountId") Integer accountId);
//    Orders applyVoucherToOrder(Orders order, Voucher voucher);


//    Page<Orders> findAllByOrderStatusPT(Pageable pageable);

    @Query(value = "SELECT * FROM Orderss b WHERE b.account_id = ?1 ORDER BY b.create_date DESC",nativeQuery = true)
    List<Orders> getAllOrders(@Param("accountId") Integer accountId);

//    List<Orders> findByAccountIdOrderByCreateDateDesc(Integer accountId);
}
