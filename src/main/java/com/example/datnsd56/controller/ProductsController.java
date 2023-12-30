package com.example.datnsd56.controller;

import com.example.datnsd56.entity.*;
import com.example.datnsd56.service.BrandService;
import com.example.datnsd56.service.CategoryService;
import com.example.datnsd56.service.ColorService;
import com.example.datnsd56.service.ImageService;
import com.example.datnsd56.service.MaterialService;
import com.example.datnsd56.service.ProductDetailsService;
import com.example.datnsd56.service.ProductsService;
import com.example.datnsd56.service.ShoeSoleService;
import com.example.datnsd56.service.SizeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
//import java.math.Double;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/admin/san-pham-test")
public class ProductsController {
    @Qualifier("productsServiceImpl")
    @Autowired
    private ProductsService productService;
    @Autowired
    private BrandService brand;
    @Autowired
    private CategoryService category;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private ShoeSoleService shoeSole;
    @Autowired
    private ProductDetailsService productDetailsService;
    @Autowired
    private ColorService colorService;
    @Autowired
    private SizeService sizeService;
    @Autowired
    private ImageService imageService;

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('admin')")

    public String createProduct(Model model) {
        model.addAttribute("product", new Products());
        List<Brand> brands = brand.getAllBrand();
        model.addAttribute("brand", brands);
        model.addAttribute("brands", new Brand());
        List<Category> categories = category.getAllCate();
        model.addAttribute("category", categories);
        model.addAttribute("categoris", new Category());
        List<Material> materials = materialService.getAllMater();
        model.addAttribute("material", materials);
        model.addAttribute("materials", new Material());
        List<ShoeSole> shoeSoles = shoeSole.getAllSole();
        model.addAttribute("shoeSole", shoeSoles);
        model.addAttribute("shoeSoles", new ShoeSole());
        model.addAttribute("color", new Color());
        model.addAttribute("size", new Size());
        model.addAttribute("listPending", productDetailsService.listPending());
        model.addAttribute("listColor", colorService.getAllColor());
        model.addAttribute("listSize", sizeService.getAllSZ());
        return "dashboard/san-pham/add-san-pham";
    }

    @GetMapping("/display")
    @PreAuthorize("hasAuthority('admin')")

    public ResponseEntity<byte[]> getImage(@RequestParam("id") Integer productId,@RequestParam("imageId") Integer imageId, Model model) throws SQLException {
        List<Image> imageList = imageService.getImagesForProducts(productId,imageId);

        if (imageList != null && !imageList.isEmpty()) {
            byte[] imageBytes = imageList.get(0).getUrl().getBytes(1, (int) imageList.get(0).getUrl().length());
            model.addAttribute("images", imageList);

            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
        } else {
            // Trả về ResponseEntity 404 nếu không tìm thấy ảnh
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add-product")
    @PreAuthorize("hasAuthority('admin')")

    public String addProduct(@Valid @ModelAttribute("sanPham") Products products, BindingResult result, @RequestParam("kichThuocs") List<Size> kichThuocList, @RequestParam("colors") List<Color> colorList, @RequestParam("image") MultipartFile[] files, Model model) throws SQLException, IOException {
        if (result.hasErrors()) {
            model.addAttribute("product", new Products());
            List<Brand> brands = brand.getAllBrand();
            model.addAttribute("brand", brands);
            model.addAttribute("brands", new Brand());
            List<Category> categories = category.getAllCate();
            model.addAttribute("category", categories);
            model.addAttribute("categoris", new Category());
            List<Material> materials = materialService.getAllMater();
            model.addAttribute("material", materials);
            model.addAttribute("materials", new Material());
            List<ShoeSole> shoeSoles = shoeSole.getAllSole();
            model.addAttribute("shoeSole", shoeSoles);
            model.addAttribute("shoeSoles", new ShoeSole());
            model.addAttribute("color", new Color());
            model.addAttribute("size", new Size());
            model.addAttribute("listPending", productDetailsService.listPending());
            model.addAttribute("listColor", colorService.getAllColor());
            model.addAttribute("listSize", sizeService.getAllSZ());
        }

        products.setStatus(1);
        productService.addProduct(products, colorList, kichThuocList, files);
        return "redirect:/admin/san-pham-test/create";
    }

    @PostMapping("/update-pending")
    public String addProductDetail(@RequestParam("ids") List<Integer> id, @RequestParam("soLuongs") List<Integer> soLuong, @RequestParam("donGias") List<BigDecimal> donGia) {
        productService.addProductDetail(id, soLuong, donGia);
        return "redirect:/admin/chi-tiet-san-pham/hien-thi";
    }

    @GetMapping("/view-update/{id}")
    @PreAuthorize("hasAuthority('admin')")

    public String viewUpdateProduct(@PathVariable("id") Integer id,@RequestParam("imageId") Integer imageId, Model model) {
        Products products = productService.getById(id);
        model.addAttribute("product", products);
        List<Integer> selectedSizes = productService.findSelectedSizeIds(id);
        model.addAttribute("selectedSizes", selectedSizes);
        List<Integer> selectedColors = productService.findSelectedColorIds(id);
        model.addAttribute("selectedColors", selectedColors);
        List<Image> productImages = imageService.getImagesForProducts(id,imageId);
        model.addAttribute("productImages", productImages);
        List<Brand> brands = brand.getAllBrand();
        model.addAttribute("brand", brands);
        model.addAttribute("brands", new Brand());
        List<Category> categories = category.getAllCate();
        model.addAttribute("category", categories);
        model.addAttribute("categoris", new Category());
        List<Material> materials = materialService.getAllMater();
        model.addAttribute("material", materials);
        model.addAttribute("materials", new Material());
        List<ShoeSole> shoeSoles = shoeSole.getAllSole();
        model.addAttribute("shoeSole", shoeSoles);
        model.addAttribute("shoeSoles", new ShoeSole());
        model.addAttribute("color", new Color());
        model.addAttribute("size", new Size());
        model.addAttribute("listDetail", productService.getAllDetail(products.getId()));
        model.addAttribute("listColor", colorService.getAllColor());
        model.addAttribute("listSize", sizeService.getAllSZ());
        return "dashboard/san-pham/update-san-pham";
    }

    @PostMapping("/update-san-pham/{id}")
    @PreAuthorize("hasAuthority('admin')")

    public String updateSanPham(@Valid @ModelAttribute("product") Products products, BindingResult result, @RequestParam("images") MultipartFile[] files, Model model) throws SQLException, IOException {
        if (result.hasErrors()) {
            model.addAttribute("product", new Products());
            List<Brand> brands = brand.getAllBrand();
            model.addAttribute("brand", brands);
            model.addAttribute("brands", new Brand());
            model.addAttribute("images", products.getImages());

            List<Category> categories = category.getAllCate();
            model.addAttribute("category", categories);
            model.addAttribute("categoris", new Category());
            List<Material> materials = materialService.getAllMater();
            model.addAttribute("material", materials);
            model.addAttribute("materials", new Material());
            List<ShoeSole> shoeSoles = shoeSole.getAllSole();
            model.addAttribute("shoeSole", shoeSoles);
            model.addAttribute("shoeSoles", new ShoeSole());
            model.addAttribute("color", new Color());
            model.addAttribute("size", new Size());
            model.addAttribute("listPending", productDetailsService.listPending());
            model.addAttribute("listColor", colorService.getAllColor());
            model.addAttribute("listSize", sizeService.getAllSZ());
        }

        productService.updateProduct(products, files);
        return "redirect:/admin/chi-tiet-san-pham/hien-thi";
    }

    @PostMapping("/update-chi-tiet-san-pham")
    @PreAuthorize("hasAuthority('admin')")

    public String updateProductDetail(@RequestParam("ids") List<Integer> id, @RequestParam("soLuongs") List<Integer> soLuong, @RequestParam("donGias") List<BigDecimal> donGia) {
        productService.updateProductDetail(id, soLuong, donGia);
        return "redirect:/admin/san-pham-test/create";
    }
//    @GetMapping("/search")
////    @PreAuthorize("hasAuthority('admin')")
//    public String search(@RequestParam("name") String name,@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
//        model.addAttribute("product", new Products());
//        Page<Products> page1 = productService.findByName(name);
////        model.addAttribute("totalPages", page1.getTotalPages());
////        model.addAttribute("list", page1);
//        return "/dashboard/roles/roles";
//    }

}
