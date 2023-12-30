package com.example.datnsd56.repository;

import com.example.datnsd56.ViewModel.ViewCart;
import com.example.datnsd56.entity.CartItem;
import com.example.datnsd56.entity.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
//    @Query(value = "select *from cart_detail where product_id=?1 and quantity=?2",nativeQuery = true)
//@Query(value = "SELECT        Products.name, Product_details.color_id, Product_details.size_id, Product_details.sell_price, Image.url\n" +
//    "FROM            Image INNER JOIN\n" +
//    "                         Product_details ON Image.id = Product_details.id INNER JOIN Products ON Image.product_id = Products.id AND Product_details.product_id = Products.id ",nativeQuery = true)

//    List<ViewCart> getAllCartItem();

    List<CartItem> findAllByCartId(Integer idCart);

    void deleteAllByCartId(Integer idCart);

    void deleteById(Integer id);


}
