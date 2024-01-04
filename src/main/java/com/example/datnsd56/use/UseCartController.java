package com.example.datnsd56.use;

import com.example.datnsd56.entity.*;
import com.example.datnsd56.service.AccountService;
import com.example.datnsd56.service.CartService;
import com.example.datnsd56.service.ImageService;
import com.example.datnsd56.service.ProductDetailsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UseCartController {
    @Autowired
    private ProductDetailsService productDetailsService;
    @Autowired
    private CartService cartService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ImageService imageService;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal, HttpSession session) {
        if (principal == null) {
            SessionCart sessionCart = (SessionCart) session.getAttribute("sessionCart");
            if (sessionCart == null) {
                model.addAttribute("check", "Giỏ hàng của bạn đang trống");
            }
            if (sessionCart != null) {
                model.addAttribute("grandTotal", sessionCart.getTotalPrice());
                model.addAttribute("cart", sessionCart);
                session.setAttribute("totalItems", sessionCart.getTotalItems());
                if (sessionCart.getCartItems().isEmpty()) {
                    model.addAttribute("check", "Giỏ hàng của bạn đang trống");
                }
            }
        } else {
            Optional<Account> account = accountService.finByName(principal.getName());
            Cart cart = account.get().getCart();
            if (cart == null) {
                model.addAttribute("check", "Giỏ hàng của bạn đang trống");
            }
            if (cart != null) {
                model.addAttribute("grandTotal", cart.getTotalPrice());
                model.addAttribute("cart", cart);
                session.setAttribute("totalItems", cart.getTotalItems());
                if (cart.getCartItems().isEmpty()) {
                    model.addAttribute("check", "Giỏ hàng của bạn đang trống");
                }
            }
        }
        return "website/index/giohang";
    }

    @PostMapping("/add-to-cart")
    @ResponseBody
    public ResponseEntity<String> addToCart(
        @RequestParam("productId") Integer productId,
        @RequestParam("size") Integer sizeId,
        @RequestParam("color") Integer colorId,
        @RequestParam("quantity") Integer quantity,
        Principal principal,
        HttpSession session) {

        if (sizeId == null || colorId == null) {
            return new ResponseEntity<>("Vui lòng chọn size và màu!", HttpStatus.BAD_REQUEST);
        }
        boolean isQuantityAvailable = productDetailsService.isQuantityAvailable(productId, sizeId, colorId, quantity);

        if (!isQuantityAvailable) {
            return new ResponseEntity<>("Sản phẩm không có đủ số lượng!", HttpStatus.BAD_REQUEST);
        }

        ProductDetails productDetail = productDetailsService.getCart(productId, colorId, sizeId);

        if (productDetail == null) {
            return new ResponseEntity<>("Sản phẩm không tồn tại!", HttpStatus.BAD_REQUEST);
        }

        if (principal == null) {
            SessionCart oldSessionCart = (SessionCart) session.getAttribute("sessionCart");
            SessionCart sessionCart = cartService.addToCartSession(oldSessionCart, productDetail, quantity);
            session.setAttribute("sessionCart", sessionCart);
            session.setAttribute("totalItems", sessionCart.getTotalItems());
        } else {
            String name = principal.getName();
            Cart cart = cartService.addToCart(productDetail, quantity, name);
            session.setAttribute("totalItems", cart.getTotalItems());
        }

        return new ResponseEntity<>("Thêm giỏ hàng thành công!", HttpStatus.OK);
    }


    @PostMapping("/update-cart-quantity")
    @ResponseBody
    public ResponseEntity<String> updateCartQuantity(
        @RequestParam("productId") Integer productId,
        @RequestParam("newQuantity") Integer newQuantity,
        HttpSession session) {

        // Kiểm tra newQuantity nếu bạn muốn

        // Lấy giỏ hàng từ session
        SessionCart sessionCart = (SessionCart) session.getAttribute("sessionCart");
        if (sessionCart == null) {
            // Xử lý khi giỏ hàng không tồn tại
            return new ResponseEntity<>("Giỏ hàng không tồn tại!", HttpStatus.BAD_REQUEST);
        }

        // Thực hiện cập nhật số lượng trong giỏ hàng
        sessionCart.updateQuantity(productId, newQuantity);

        // Cập nhật sessionCart
        session.setAttribute("sessionCart", sessionCart);

        // Trả về phản hồi thành công
        return ResponseEntity.ok("Cập nhật số lượng thành công!");
    }
    @GetMapping("/display")
//    @PreAuthorize("hasAuthority('user') || hasAuthority('admin')")

    public ResponseEntity<byte[]> getImage(@RequestParam("id") Integer productId, @RequestParam("imageId") Integer imageId) throws SQLException {
        List<Image> imageList = imageService.getImagesForProducts(productId, imageId);
        byte[] imageBytes = null;
        imageBytes = imageList.get(0).getUrl().getBytes(1, (int) imageList.get(0).getUrl().length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);

    }

    @RequestMapping(value = "/user/update-cart", method = RequestMethod.POST, params = "action=update")
//    @PostMapping("/update-cart/{id}")
    public String updateCart(@RequestParam("id") Integer id,
                             @RequestParam("quantity") Integer quantity,
                             Principal principal,
                             HttpSession session) {
        ProductDetails productDetail = productDetailsService.getById(id);
        if (principal == null) {
            SessionCart oldSessionCart = (SessionCart) session.getAttribute("sessionCart");
            SessionCart sessionCart = cartService.updateCartSession(oldSessionCart, productDetail, quantity);
            session.setAttribute("sessionCart", sessionCart);
            session.setAttribute("totalItems", sessionCart.getTotalItems());
        } else {
            String name = principal.getName();
            Cart cart = cartService.updateCart(productDetail, quantity, name);
            session.setAttribute("totalItems", cart.getTotalItems());
        }
        return "redirect:/user/cart";
    }

//    @RequestMapping(value = "/user/delete", method = RequestMethod.GET, params = "action=delete")
    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable("id") Integer id,
                             Principal principal,
                             HttpSession session) {
        ProductDetails productDetail = productDetailsService.getById(id);
        if (principal == null) {
            SessionCart oldSessionCart = (SessionCart) session.getAttribute("sessionCart");
            SessionCart sessionCart = cartService.removeFromCartSession(oldSessionCart, productDetail);
            session.setAttribute("sessionCart", sessionCart);
            session.setAttribute("totalItems", sessionCart.getTotalItems());
        } else {
            String name = principal.getName();
            Cart cart = cartService.removeFromCart(productDetail, name);
            session.setAttribute("totalItems", cart.getTotalItems());
        }
        return "redirect:/user/cart";
    }
}
