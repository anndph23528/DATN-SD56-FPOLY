
//package com.example.datnsd56.controller;
//
//import com.example.datnsd56.entity.Account;
//import com.example.datnsd56.entity.CartItem;
//import com.example.datnsd56.entity.Image;
//import com.example.datnsd56.entity.OrderItem;
//import com.example.datnsd56.entity.Orders;
//import com.example.datnsd56.entity.ProductDetails;
//import com.example.datnsd56.entity.Products;
//import com.example.datnsd56.service.*;
//import com.fasterxml.jackson.databind.JsonNode;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.math.BigDecimal;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//import java.util.Random;
//
//@Controller
//@RequestMapping("/cart")
//public class CartController {
//    @Autowired
//    private CartService cartService;
//    @Autowired
//    private ImageService imageService;
//    @Autowired
//    private ColorService colorService;
//    @Autowired
//    private ProductDetailsService productDetailsService;
//    @Autowired
//    private SizeService sizeService;
//    @Autowired
//    private OrdersService ordersService;
//    @Autowired
//    private OrderItemService orderItemService;
//
//    @GetMapping("/view-cart")
//    @PreAuthorize("hasAuthority('user') || hasAuthority('admin')")
//    public String viewCart(Model model) {
//
//        model.addAttribute("cartItems", cartService.getAllItem());
//        model.addAttribute("total", cartService.getAmount());
//        // Xử lý yêu cầu và thêm sản phẩm vào giỏ hàng
////        model.addAttribute("sell",productDetailsService.getOneProdcut(id));
//        return "website/index/giohang";
//
//
//    }
//
//    @GetMapping("/getadd")
//    @PreAuthorize("hasAuthority('user') || hasAuthority('admin')")
//
//    public String getPrice(@RequestParam("productId") Integer id, @RequestParam("size") Integer size, @RequestParam("color") Integer color, RedirectAttributes redirectAttributes,  HttpSession session) {
//        ProductDetails productDetails = productDetailsService.getCart(id, color, size);
//        if (productDetails != null) {
//            CartItem item = new CartItem();
//            item.setId(productDetails.getId());
//            item.setProductDetails(productDetails);
//            item.setQuantity(1);
//            cartService.add(item);
//        }
////        redirectAttributes.addAttribute("productId", id);
//        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cart");
//
//        // Tính tổng số sản phẩm trong giỏ hàng
//        int totalItems = (cartItems != null) ? cartItems.size() : 0;
//
//        // Cập nhật số sản phẩm trong giỏ hàng vào session
//        session.setAttribute("cartItemCount", totalItems);
//        return "redirect:/product/detail/chi-tiet/" + id;
//
//    }
//
//    @GetMapping("/clear")
//    @PreAuthorize("hasAuthority('user') || hasAuthority('admin')")
//
//    public String clear() {
//        cartService.clear();
//        return "redirect:/cart/view-cart";
//    }
//
//    @GetMapping("/remove/{id}")
//    @PreAuthorize("hasAuthority('user') || hasAuthority('admin')")
//
//    public String removeclear(@PathVariable("id") Integer id) {
//        cartService.remove(id);
//        return "redirect:/cart/view-cart";
//    }
//
//    @PostMapping ("/update")
//    @PreAuthorize("hasAuthority('user') || hasAuthority('admin')")
//    public String update(@RequestParam("id") Integer id,@RequestParam("quantity") Integer quantity) {
//        System.out.println(id + " " + quantity);
//        cartService.update(id,quantity);
//        return "redirect:/cart/view-cart";
//
//    }
//
//    @GetMapping("add/{id}")
//    @PreAuthorize("hasAuthority('user') || hasAuthority('admin')")
//    public String addCart(@PathVariable("id") Integer id, Model model) {
//        ProductDetails productDetails = productDetailsService.getByIds(id);
//        model.addAttribute("listSize", sizeService.getColorId(id));
//        model.addAttribute("listColor", colorService.getColorId(id));
////    ProductDetails productDetails1=new ProductDetails();
//        if (productDetails != null) {
//            CartItem item = new CartItem();
//            item.setId(productDetails.getId());
//            item.setProductDetails(productDetails);
//            item.setQuantity(1);
//            cartService.add(item);
//        }
//        return "redirect:/cart/view-cart";
//    }
//@GetMapping("/dsdonhang")
//@PreAuthorize("hasAuthority('user') || hasAuthority('admin')")
//public String viewdsdonhang(){
//
//         return "website/index/danhsachdonhang";
//}
//    @GetMapping("/display")
//    @PreAuthorize("hasAuthority('user') || hasAuthority('admin')")
//
//    public ResponseEntity<byte[]> getImage(@RequestParam("id") Integer productId) throws SQLException {
//        List<Image> imageList = imageService.getImagesForProducts(productId);
//        byte[] imageBytes = null;
//        imageBytes = imageList.get(0).getUrl().getBytes(1, (int) imageList.get(0).getUrl().length());
//        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
//
//    }
//}

