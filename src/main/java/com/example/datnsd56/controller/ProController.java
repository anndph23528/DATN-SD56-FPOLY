//package com.example.datnsd56.controller;
//
//import com.example.datnsd56.entity.Brand;
//import com.example.datnsd56.entity.Category;
//import com.example.datnsd56.entity.Color;
//import com.example.datnsd56.entity.Material;
//import com.example.datnsd56.entity.Products;
//import com.example.datnsd56.entity.ShoeSole;
//import com.example.datnsd56.entity.Size;
//import com.example.datnsd56.service.BrandService;
//import com.example.datnsd56.service.CategoryService;
//import com.example.datnsd56.service.ColorService;
//import com.example.datnsd56.service.ImageService;
//import com.example.datnsd56.service.MaterialService;
//import com.example.datnsd56.service.ProductDetailsService;
//import com.example.datnsd56.service.ProductsService;
//import com.example.datnsd56.service.ShoeSoleService;
//import com.example.datnsd56.service.SizeService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.sql.SQLException;
//import java.util.List;
//
//@Controller
//@RequestMapping("/admin/san-pham-test")
//public class ProController {
//    @Qualifier("productsServiceImpl")
//    @Autowired
//    private ProductsService productService;
//    @Autowired
//    private BrandService brand;
//    @Autowired
//    private CategoryService category;
//    @Autowired
//    private MaterialService materialService;
//    @Autowired
//    private ShoeSoleService shoeSole;
//    @Autowired
//    private ProductDetailsService productDetailsService;
//    @Autowired
//    private ColorService colorService;
//    @Autowired
//    private SizeService sizeService;
//    @Autowired
//    private ImageService imageService;
//
//    @GetMapping("/create")
//    public String createProduct(Model model) {
//        model.addAttribute("product", new Products());
//        List<Brand> brands = brand.getAllBrand();
//        model.addAttribute("brand", brands);
//        model.addAttribute("brands", new Brand());
//        List<Category> categories = category.getAllCate();
//        model.addAttribute("category", categories);
//        model.addAttribute("categoris", new Category());
//        List<Material> materials = materialService.getAllMater();
//        model.addAttribute("material", materials);
//        model.addAttribute("materials", new Material());
//        List<ShoeSole> shoeSoles = shoeSole.getAllSole();
//        model.addAttribute("shoeSole", shoeSoles);
//        model.addAttribute("shoeSoles", new ShoeSole());
//        model.addAttribute("color", new Color());
//        model.addAttribute("size", new Size());
//        model.addAttribute("listPending", productDetailsService.listPending());
//        model.addAttribute("listColor", colorService.getAllColor());
//        model.addAttribute("listSize", sizeService.getAllSZ());
//        return "dashboard/san-pham/add-san-pham";
//    }
//
//    @PostMapping("/add-product")
//    public String addProduct(@Valid @ModelAttribute("sanPham") Products products, BindingResult result, @RequestParam("kichThuocs") List<Size> kichThuocList, @RequestParam("colors") List<Color> colorList, @RequestParam("image") MultipartFile[] files, Model model) throws SQLException, IOException {
//        if (result.hasErrors()) {
//            model.addAttribute("product", new Products());
//            List<Brand> brands = brand.getAllBrand();
//            model.addAttribute("brand", brands);
//            model.addAttribute("brands", new Brand());
//            List<Category> categories = category.getAllCate();
//            model.addAttribute("category", categories);
//            model.addAttribute("categoris", new Category());
//            List<Material> materials = materialService.getAllMater();
//            model.addAttribute("material", materials);
//            model.addAttribute("materials", new Material());
//            List<ShoeSole> shoeSoles = shoeSole.getAllSole();
//            model.addAttribute("shoeSole", shoeSoles);
//            model.addAttribute("shoeSoles", new ShoeSole());
//            model.addAttribute("color", new Color());
//            model.addAttribute("size", new Size());
//            model.addAttribute("listPending", productDetailsService.listPending());
//            model.addAttribute("listColor", colorService.getAllColor());
//            model.addAttribute("listSize", sizeService.getAllSZ());
//        }
//        productService.addProduct(products, colorList, kichThuocList, files);
//        return "redirect:/admin/san-pham-test/create";
//    }
//
//    @PostMapping("/update-pending")
//    public String addProductDetail(@RequestParam("ids") List<Integer> id, @RequestParam("soLuongs") List<Integer> soLuong, @RequestParam("donGias") List<BigDecimal> donGia) {
//        productService.addProductDetail(id, soLuong, donGia);
//        return "redirect:/admin/san-pham-test/create";
//    }
//
//    @GetMapping("/view-update/{id}")
//    public String viewUpdateProduct(@PathVariable("id") Integer id, Model model) {
//        Products products = productService.getById(id);
//        model.addAttribute("product", products);
//        List<Brand> brands = brand.getAllBrand();
//        model.addAttribute("brand", brands);
//        model.addAttribute("brands", new Brand());
//        List<Category> categories = category.getAllCate();
//        model.addAttribute("category", categories);
//        model.addAttribute("categoris", new Category());
//        List<Material> materials = materialService.getAllMater();
//        model.addAttribute("material", materials);
//        model.addAttribute("materials", new Material());
//        List<ShoeSole> shoeSoles = shoeSole.getAllSole();
//        model.addAttribute("shoeSole", shoeSoles);
//        model.addAttribute("shoeSoles", new ShoeSole());
//        model.addAttribute("color", new Color());
//        model.addAttribute("size", new Size());
//        model.addAttribute("listDetail", productService.getAllDetail(products.getId()));
//        model.addAttribute("listColor", colorService.getAllColor());
//        model.addAttribute("listSize", sizeService.getAllSZ());
//        return "dashboard/san-pham/view-update-san-pham";
//    }
//
//    @PostMapping("/update-san-pham/{id}")
//    public String updateSanPham(@Valid @ModelAttribute("sanPham") Products products, BindingResult result, @RequestParam("image") MultipartFile[] files, Model model) throws SQLException, IOException {
//        if (result.hasErrors()) {
//            model.addAttribute("product", new Products());
//            List<Brand> brands = brand.getAllBrand();
//            model.addAttribute("brand", brands);
//            model.addAttribute("brands", new Brand());
//            List<Category> categories = category.getAllCate();
//            model.addAttribute("category", categories);
//            model.addAttribute("categoris", new Category());
//            List<Material> materials = materialService.getAllMater();
//            model.addAttribute("material", materials);
//            model.addAttribute("materials", new Material());
//            List<ShoeSole> shoeSoles = shoeSole.getAllSole();
//            model.addAttribute("shoeSole", shoeSoles);
//            model.addAttribute("shoeSoles", new ShoeSole());
//            model.addAttribute("color", new Color());
//            model.addAttribute("size", new Size());
//            model.addAttribute("listPending", productDetailsService.listPending());
//            model.addAttribute("listColor", colorService.getAllColor());
//            model.addAttribute("listSize", sizeService.getAllSZ());
//        }
//        productService.updateProduct(products, files);
//        return "redirect:/admin/san-pham-test/create";
//    }
//
//    @PostMapping("/update-chi-tiet-san-pham")
//    public String updateProductDetail(@RequestParam("ids") List<Integer> id, @RequestParam("soLuongs") List<Integer> soLuong, @RequestParam("donGias") List<BigDecimal> donGia) {
//        productService.updateProductDetail(id, soLuong, donGia);
//        return "redirect:/admin/san-pham-test/create";
//    }
//
//}
