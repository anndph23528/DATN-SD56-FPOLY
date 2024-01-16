package com.example.datnsd56.controller;

import com.example.datnsd56.entity.*;
import com.example.datnsd56.service.*;
import com.example.datnsd56.service.impl.VoucherSeviceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ViewProductController {

    @Autowired
    private ProductDetailsService productDetailsService;
    @Autowired
    ProductsService productsService;
    @Autowired
    ImageService imageService;
    @Autowired
    private VoucherSeviceImpl voucherSeviceImpl;
    @Autowired
    private VoucherUsageService voucherUsageService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
//    @Autowired
//    private VoucherSeviceImpl voucherService;
    @GetMapping("/trang-chu")
    public String hienthi(Model model, Principal principal, HttpSession session){
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
        return "/website/index/index";
    }

    @GetMapping("/list")
    public String productList(Model model) {
        List<Products> allProducts = productsService.getAllPro();
        List<Category> allCategories = categoryService.getAllCate();
        List<Brand> allBrands = brandService.getAllBrand();

        model.addAttribute("products", allProducts);
        model.addAttribute("categories", allCategories);
        model.addAttribute("brands", allBrands);

        return "website/index/product";
    }
    @Transactional
    @GetMapping("/hien-thi")
    public String productView(Model model, Principal principal, HttpSession session) {
        List<Products> lists = productsService.getAllPros();
        List<Products> allProducts = productsService.getAllPro();
        List<Category> allCategories = categoryService.getAllCate();
        List<Brand> allBrands = brandService.getAllBrand();

        model.addAttribute("products", allProducts);
        model.addAttribute("categories", allCategories);
        model.addAttribute("brands", allBrands);
        // Lấy danh sách tất cả voucher
        List<Voucher> allVouchers = voucherSeviceImpl.getAllls();

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (principal != null) {
            // Nếu đã đăng nhập
            Optional<Account> account = accountService.finByName(principal.getName());

            // Lấy danh sách voucher đã lưu cho tài khoản
            List<VoucherUsage> voucherUsages = voucherUsageService.findVoucherUsagesByAccount(account.get().getId());

            // Loại bỏ những voucher đã lưu khỏi danh sách tất cả voucher
            allVouchers.removeAll(voucherUsages.stream().map(VoucherUsage::getVoucher).collect(Collectors.toList()));

            model.addAttribute("allVouchers", allVouchers);
        } else {
            // Nếu chưa đăng nhập, hiển thị tất cả voucher
            model.addAttribute("allVouchers", allVouchers);
        }

        // Sắp xếp sản phẩm theo brand
        Collections.sort(lists, Comparator.comparing(product -> product.getBrandId().getName()));
        model.addAttribute("views", lists);

        // Tạo một Map để nhóm sản phẩm theo brand
        Map<String, List<Products>> productsByBrand = new HashMap<>();
        List<Products> productList;

        for (Products product : lists) {
            String brandName = product.getBrandId().getName();
            productList = productsByBrand.getOrDefault(brandName, new ArrayList<>());
            productList.add(product);
            productsByBrand.put(brandName, productList);
        }

        if (productsByBrand == null) {
            productsByBrand = new HashMap<>();
        }

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
            Optional<Account> accounts = accountService.finByName(name);
            if (accounts.isPresent()) {
                Cart cart = accounts.get().getCart();
                if (cart != null) {
                    totalCartItems = cart.getCartItems().size();
                }
            }

            // Lưu và xóa voucher cho người dùng đã đăng nhập
            // Xử lý logic lưu/xóa voucher tại đây
            // ...
        }

        model.addAttribute("totalItems", totalCartItems);
        model.addAttribute("productsByBrand", productsByBrand);

        return "website/index/product";
    }


    @GetMapping("/search")
    public String searchProducts(
        @RequestParam(name = "minPrice", required = false) Double minPrice,
        @RequestParam(name = "maxPrice", required = false) Double maxPrice,
        @RequestParam(name = "category", required = false) Integer categoryId,
        @RequestParam(name = "brand", required = false) Integer brandId,
        Model model
    ) {
        List<Products> filteredProducts = productsService.searchProducts(minPrice, maxPrice, categoryId, brandId);
        List<Category> allCategories = categoryService.getAllCate();
        List<Brand> allBrands = brandService.getAllBrand();

        model.addAttribute("products", filteredProducts);
        model.addAttribute("categories", allCategories);
        model.addAttribute("brands", allBrands);

        // Tạo một Map để nhóm sản phẩm theo brand
        Map<String, List<Products>> productsByBrand = createProductsByBrandMap(filteredProducts);

        model.addAttribute("productsByBrand", productsByBrand);

        return "website/index/product";
    }

    // Hàm để tạo Map nhóm sản phẩm theo brand
    @Transactional
    private Map<String, List<Products>> createProductsByBrandMap(List<Products> products) {
        Map<String, List<Products>> productsByBrand = new HashMap<>();
        List<Products> productList;

        for (Products product : products) {
            String brandName = product.getBrandId().getName();
            productList = productsByBrand.getOrDefault(brandName, new ArrayList<>());
            productList.add(product);
            productsByBrand.put(brandName, productList);
        }

        return productsByBrand;
    }
    @GetMapping("/display")
    public ResponseEntity<byte[]> getImage(@RequestParam("id") Integer productId,@RequestParam("imageId") Integer imageId) throws SQLException {
        List<Image> imageList= imageService.getImagesForProducts(productId,imageId);
        byte[] imageBytes = null;
        imageBytes = imageList.get(0).getUrl().getBytes(1, (int) imageList.get(0).getUrl().length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
//        if (image != null && image.get(0) != null) {
//            return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_JPEG)
//                .body(image.get(0).getUrl());
//        } else {
//            return ResponseEntity.notFound().build();
//        }

    }

    @GetMapping("nike/hien-thi")
    public String viewNike(){
        return "website/index/nike";
    }
}
