package com.example.datnsd56.controller;

import com.example.datnsd56.entity.*;
import com.example.datnsd56.repository.ProductsRepository;
import com.example.datnsd56.service.BrandService;
import com.example.datnsd56.service.CategoryService;
import com.example.datnsd56.service.ColorService;
import com.example.datnsd56.service.ImageService;
import com.example.datnsd56.service.MaterialService;
import com.example.datnsd56.service.ProductDetailsService;
import com.example.datnsd56.service.ProductsService;
import com.example.datnsd56.service.ShoeSoleService;
import com.example.datnsd56.service.SizeService;
import com.example.datnsd56.service.impl.ProductDetailsServiceImpl;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
//import java.math.Double;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/admin/san-pham-test")
@PreAuthorize("hasAuthority('admin') || hasAuthority('staff')")

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
    private ProductDetailsServiceImpl productDetailsService1;
    @Autowired
    private ColorService colorService;
    @Autowired
    private SizeService sizeService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ProductsRepository productsRepository;
    @GetMapping("/create")
//    @PreAuthorize("hasAuthority('admin') || hasAuthority('staff')")
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
    @PreAuthorize("hasAuthority('admin') || hasAuthority('staff')")


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
    @PostMapping("/delete-all-products")
    @ResponseBody
    public ResponseEntity<String> deleteAllProducts() {
        productDetailsService1.deleteAllProductDetails();
        return ResponseEntity.ok("Xóa tất cả sản phẩm thành công!");
    }
    @PostMapping("/add-product")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('staff')")
    public String addProduct(@Valid @ModelAttribute("product") Products products, BindingResult result, Model model,@RequestParam("kichThuocs") List<Size> kichThuocList, @RequestParam("colors") List<Color> colorList, @RequestParam("image") MultipartFile[] files) throws SQLException, IOException {
        if (result.hasErrors()) {
//            model.addAttribute("product", new Products());
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
//            return "/dashboard/san-pham/add-san-pham";
//            return "redirect:/admin/san-pham-test/create";


        }
        if (productService.existsByName(products.getName())) {
            model.addAttribute("errorMessage", "tên sản phẩm đã tồn tại đã tồn tại");
            return "/dashboard/san-pham/add-san-pham";
        }


        String code = "SP0" + new Random().nextInt(100000);
        products.setCode(code);
        products.setStatus(0);
        productService.addProduct(products, colorList, kichThuocList, files);
//        model.addAttribute("listPending", productDetailsService.listPending());

                    return "redirect:/admin/san-pham-test/create";

    }

    @PostMapping("/update-pending")
    public String addProductDetail(@RequestParam("ids") List<Integer> id, @RequestParam("soLuongs") List<Integer> soLuong, @RequestParam("donGias") List<BigDecimal> donGia) {

        productService.addProductDetail(id, soLuong, donGia);
        return "redirect:/admin/chi-tiet-san-pham/hien-thi";
    }

    @GetMapping("/view-update/{id}")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('staff')")


    public String viewUpdateProduct(@PathVariable("id") Integer id, @RequestParam(name = "imageId", required = false) Integer imageId, Model model) {
        System.out.println("id: " + id);
        System.out.println("imageId: " + imageId);
        Products products = productService.getById(id);
        model.addAttribute("product", products);
        List<Integer> selectedSizes = productService.findSelectedSizeIds(id);
        model.addAttribute("selectedSizes", selectedSizes);
        List<Integer> selectedColors = productService.findSelectedColorIds(id);
        model.addAttribute("selectedColors", selectedColors);
        List<Image> productImages = imageService.getImagesForProducts(id,imageId);
        Products productss=new Products();
        model.addAttribute("images",productss.getImages());
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
    @PreAuthorize("hasAuthority('admin') || hasAuthority('staff')")


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

//        productService.updateProduct(products, files);
        return "redirect:/admin/chi-tiet-san-pham/hien-thi";
    }

    @PostMapping("/update-chi-tiet-san-pham")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('staff')")


    public String updateProductDetail(@RequestParam("ids") List<Integer> id, @RequestParam("soLuongs") List<Integer> soLuong, @RequestParam("donGias") List<BigDecimal> donGia) {
        productService.updateProductDetail(id, soLuong, donGia);
        return "redirect:/admin/san-pham-test/create";
    }
//    @GetMapping("/search")images
////    @PreAuthorize("hasAuthority('admin') || hasAuthority('staff')")

    //    public String search(@RequestParam("name") String name,@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
//        model.addAttribute("product", new Products());
//        Page<Products> page1 = productService.findByName(name);
////        model.addAttribute("totalPages", page1.getTotalPages());
////        model.addAttribute("list", page1);
//        return "/dashboard/roles/roles";
//    }
    @GetMapping("delete-chi-tiet-san-pham/{id}")
//    @PreAuthorize("hasAuthority('admin')")
    public String delete(@PathVariable("id") Integer id){
        productDetailsService.deletess(id);
        return "redirect:/admin/san-pham-test/create";
    }
}
