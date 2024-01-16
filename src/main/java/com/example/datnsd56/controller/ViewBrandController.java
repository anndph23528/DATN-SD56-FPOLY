package com.example.datnsd56.controller;

import com.example.datnsd56.entity.*;
import com.example.datnsd56.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product/")
public class ViewBrandController {
    @Autowired
    private ProductsService productsService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ProductDetailsService productDetailsService;
@Autowired
private AccountService accountService;

    @GetMapping("/nike")
    public String productView(Model model, Principal principal, HttpSession session) {
//List<Products>lists=productsService.getAllPro();
        List<ProductDetails> list = productDetailsService.getAllCTSP();
        List<Products> lists = productsService.getAllPros();
        // Sắp xếp sản phẩm theo brand
        List<Products> nikeProducts = lists.stream()
            .filter(product -> product.getBrandId().getName().equalsIgnoreCase("nike"))
            .collect(Collectors.toList());
        int totalCartItems = 0;

        if (principal == null) {
            // Nếu người dùng chưa đăng nhập, kiểm tra giỏ hàng trong session
            SessionCart sessionCart = (SessionCart) session.getAttribute("sessionCart");
            if (sessionCart != null) {
                totalCartItems = sessionCart.getCartItems().size();
            }
        } else {
            // Nếu người dùng đã đăng nhập, kiểm tra giỏ hàng của tài khoản
            String name = principal.getName();
            Optional<Account> account = accountService.finByName(name);
            if (account.isPresent()) {
                Cart cart = account.get().getCart();
                if (cart != null) {
                    totalCartItems = cart.getCartItems().size();
                }
            }
        }

        model.addAttribute("totalItems", totalCartItems);
        model.addAttribute("views", nikeProducts);
        return "website/index/nike";
    }

    @GetMapping("/mlb")
    public String viewMlb(Model model, Principal principal, HttpSession session) {
//List<Products>lists=productsService.getAllPro();
        List<ProductDetails> list = productDetailsService.getAllCTSP();
         List<Products> lists = productsService.getAllPros();
        // Sắp xếp sản phẩm theo brand
        List<Products> nikeProducts = lists.stream()
            .filter(product -> product.getBrandId().getName().equalsIgnoreCase("mlb"))
            .collect(Collectors.toList());
        int totalCartItems = 0;

        if (principal == null) {
            // Nếu người dùng chưa đăng nhập, kiểm tra giỏ hàng trong session
            SessionCart sessionCart = (SessionCart) session.getAttribute("sessionCart");
            if (sessionCart != null) {
                totalCartItems = sessionCart.getCartItems().size();
            }
        } else {
            // Nếu người dùng đã đăng nhập, kiểm tra giỏ hàng của tài khoản
            String name = principal.getName();
            Optional<Account> account = accountService.finByName(name);
            if (account.isPresent()) {
                Cart cart = account.get().getCart();
                if (cart != null) {
                    totalCartItems = cart.getCartItems().size();
                }
            }
        }

        model.addAttribute("totalItems", totalCartItems);
        model.addAttribute("views", nikeProducts);
        return "website/index/mlb";
    }

    @GetMapping("/adidas")
    public String viewadidas(Model model, Principal principal, HttpSession session) {
//List<Products>lists=productsService.getAllPro();
        List<ProductDetails> list = productDetailsService.getAllCTSP();
         List<Products> lists = productsService.getAllPros();
        // Sắp xếp sản phẩm theo brand
        List<Products> nikeProducts = lists.stream()
            .filter(product -> product.getBrandId().getName().equalsIgnoreCase("adidas"))
            .collect(Collectors.toList());
        int totalCartItems = 0;

        if (principal == null) {
            // Nếu người dùng chưa đăng nhập, kiểm tra giỏ hàng trong session
            SessionCart sessionCart = (SessionCart) session.getAttribute("sessionCart");
            if (sessionCart != null) {
                totalCartItems = sessionCart.getCartItems().size();
            }
        } else {
            // Nếu người dùng đã đăng nhập, kiểm tra giỏ hàng của tài khoản
            String name = principal.getName();
            Optional<Account> account = accountService.finByName(name);
            if (account.isPresent()) {
                Cart cart = account.get().getCart();
                if (cart != null) {
                    totalCartItems = cart.getCartItems().size();
                }
            }
        }

        model.addAttribute("totalItems", totalCartItems);
        model.addAttribute("views", nikeProducts);
        return "website/index/adidas";
    }
    @GetMapping("/lv")
    public String viewlv(Model model, Principal principal, HttpSession session) {
//List<Products>lists=productsService.getAllPro();
        List<ProductDetails> list = productDetailsService.getAllCTSP();
         List<Products> lists = productsService.getAllPros();
        // Sắp xếp sản phẩm theo brand
        List<Products> nikeProducts = lists.stream()
            .filter(product -> product.getBrandId().getName().equalsIgnoreCase("lv"))
            .collect(Collectors.toList());
        int totalCartItems = 0;

        if (principal == null) {
            // Nếu người dùng chưa đăng nhập, kiểm tra giỏ hàng trong session
            SessionCart sessionCart = (SessionCart) session.getAttribute("sessionCart");
            if (sessionCart != null) {
                totalCartItems = sessionCart.getCartItems().size();
            }
        } else {
            // Nếu người dùng đã đăng nhập, kiểm tra giỏ hàng của tài khoản
            String name = principal.getName();
            Optional<Account> account = accountService.finByName(name);
            if (account.isPresent()) {
                Cart cart = account.get().getCart();
                if (cart != null) {
                    totalCartItems = cart.getCartItems().size();
                }
            }
        }

        model.addAttribute("totalItems", totalCartItems);
        model.addAttribute("views", nikeProducts);
        return "website/index/lv";
    }

    @GetMapping("/converse")
    public String viewconvers(Model model, Principal principal, HttpSession session) {
//List<Products>lists=productsService.getAllPro();
        List<ProductDetails> list = productDetailsService.getAllCTSP();
         List<Products> lists = productsService.getAllPros();
        // Sắp xếp sản phẩm theo brand
        List<Products> nikeProducts = lists.stream()
            .filter(product -> product.getBrandId().getName().equalsIgnoreCase("converse"))
            .collect(Collectors.toList());
        int totalCartItems = 0;

        if (principal == null) {
            // Nếu người dùng chưa đăng nhập, kiểm tra giỏ hàng trong session
            SessionCart sessionCart = (SessionCart) session.getAttribute("sessionCart");
            if (sessionCart != null) {
                totalCartItems = sessionCart.getCartItems().size();
            }
        } else {
            // Nếu người dùng đã đăng nhập, kiểm tra giỏ hàng của tài khoản
            String name = principal.getName();
            Optional<Account> account = accountService.finByName(name);
            if (account.isPresent()) {
                Cart cart = account.get().getCart();
                if (cart != null) {
                    totalCartItems = cart.getCartItems().size();
                }
            }
        }

        model.addAttribute("totalItems", totalCartItems);
        model.addAttribute("views", nikeProducts);
        return "website/index/converse";
    }
    @GetMapping("/vans")
    public String viewvans(Model model, Principal principal, HttpSession session) {
//List<Products>lists=productsService.getAllPro();
        List<ProductDetails> list = productDetailsService.getAllCTSP();
         List<Products> lists = productsService.getAllPros();
        // Sắp xếp sản phẩm theo brand
        List<Products> nikeProducts = lists.stream()
            .filter(product -> product.getBrandId().getName().equalsIgnoreCase("vans"))
            .collect(Collectors.toList());
        int totalCartItems = 0;

        if (principal == null) {
            // Nếu người dùng chưa đăng nhập, kiểm tra giỏ hàng trong session
            SessionCart sessionCart = (SessionCart) session.getAttribute("sessionCart");
            if (sessionCart != null) {
                totalCartItems = sessionCart.getCartItems().size();
            }
        } else {
            // Nếu người dùng đã đăng nhập, kiểm tra giỏ hàng của tài khoản
            String name = principal.getName();
            Optional<Account> account = accountService.finByName(name);
            if (account.isPresent()) {
                Cart cart = account.get().getCart();
                if (cart != null) {
                    totalCartItems = cart.getCartItems().size();
                }
            }
        }

        model.addAttribute("totalItems", totalCartItems);
        model.addAttribute("views", nikeProducts);
        return "website/index/vans";
    }

    @GetMapping("nike/display")
    public ResponseEntity<byte[]> getImage(@RequestParam("id") Integer productId,@RequestParam("imageId") Integer imageId) throws SQLException {
        List<Image> imageList= imageService.getImagesForProducts(productId,imageId);
        byte[] imageBytes = null;
        imageBytes = imageList.get(0).getUrl().getBytes(1, (int) imageList.get(0).getUrl().length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);

    }
    @GetMapping("/contact")
    public String viewcontact(Model model, Principal principal, HttpSession session) {
        int totalCartItems = 0;

        if (principal == null) {
            // Nếu người dùng chưa đăng nhập, kiểm tra giỏ hàng trong session
            SessionCart sessionCart = (SessionCart) session.getAttribute("sessionCart");
            if (sessionCart != null) {
                totalCartItems = sessionCart.getCartItems().size();
            }
        } else {
            // Nếu người dùng đã đăng nhập, kiểm tra giỏ hàng của tài khoản
            String name = principal.getName();
            Optional<Account> account = accountService.finByName(name);
            if (account.isPresent()) {
                Cart cart = account.get().getCart();
                if (cart != null) {
                    totalCartItems = cart.getCartItems().size();
                }
            }
        }

        model.addAttribute("totalItems", totalCartItems);
        return "website/index/contact";
    }

}
