package com.example.datnsd56.controller;

import com.example.datnsd56.entity.Color;
import com.example.datnsd56.entity.Image;
import com.example.datnsd56.entity.ProductDetails;
import com.example.datnsd56.entity.Products;
import com.example.datnsd56.entity.Size;
import com.example.datnsd56.service.ColorService;
import com.example.datnsd56.service.ImageService;
import com.example.datnsd56.service.ProductDetailsService;
import com.example.datnsd56.service.ProductsService;
import com.example.datnsd56.service.SizeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
//import java.math.Double;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/admin/chi-tiet-san-pham/")
@PreAuthorize("hasAuthority('admin') || hasAuthority('staff')")

public class ProductDetailsController {
    @Autowired
    private ProductDetailsService productDetailsService;

    @Autowired
    private ColorService colorService;

    @Autowired
    private SizeService sizeService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ImageService imageService;

    @GetMapping("hien-thi")
//    @PreAuthorize("hasAuthority('admin') || hasAuthority('staff')")


    public String getAllBypage(Model model, @RequestParam(value = "page", defaultValue = "0") Integer pageNo) {
        Page<ProductDetails> page = productDetailsService.getAll(pageNo);
        List<Products> products = productsService.getAllPro();
        List<Color> colors = colorService.getAllColor();
        List<Size> sizes = sizeService.getAllSZ();
        Products products1=new Products();
        model.addAttribute("images", products1.getImages());

        model.addAttribute("list", page);
        model.addAttribute("products", products);
        model.addAttribute("colors", colors);
        model.addAttribute("sizes", sizes);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("ctsp", new ProductDetails());
        model.addAttribute("color", new Color());
        model.addAttribute("size", new Size());

//        model.addAttribute("currentPage", pageNo);
        return "dashboard/chi-tiet-san-pham/chi-tiet-san-pham";

    }

    @PostMapping("add")
    public String add(@Valid @ModelAttribute("ctsp") ProductDetails productDetails, BindingResult result, Model model, @RequestParam("image") MultipartFile[] files, HttpSession session) throws SQLException, IOException {
        if (result.hasErrors()) {
            Page<ProductDetails> page = productDetailsService.getAll(0);
            List<Products> products = productsService.getAllPro();
            List<Color> colors = colorService.getAllColor();
            List<Size> sizes = sizeService.getAllSZ();
            model.addAttribute("list", page);
            model.addAttribute("products", products);
            model.addAttribute("colors", colors);
            model.addAttribute("sizes", sizes);
            model.addAttribute("color",new Color());
            model.addAttribute("size", new Size());
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("currentPage", 0);
            return "/dashboard/chi-tiet-san-pham/chi-tiet-san-pham";

        }
        productDetailsService.add(productDetails, files);
        session.setAttribute("successMessage", "Thêm thành công");
        return "redirect:/admin/chi-tiet-san-pham/hien-thi";

    }

    @GetMapping("/display")
//    @PreAuthorize("hasAuthority('admin') || hasAuthority('staff')")


    public ResponseEntity<byte[]> getImage(@RequestParam("id") Integer productId,@RequestParam("imageId") Integer imageId,Model model) throws SQLException {
        List<Image> imageList = imageService.getImagesForProducts(productId,imageId);

        if (imageList != null && !imageList.isEmpty()) {
            byte[] imageBytes = imageList.get(0).getUrl().getBytes(1, (int) imageList.get(0).getUrl().length());
//            model.addAttribute("image", imageList);

            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
        } else {
            // Trả về ResponseEntity 404 nếu không tìm thấy ảnh
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("view-update/{id}")
//    @PreAuthorize("hasAuthority('admin') || hasAuthority('staff')")


    public String detail(@PathVariable("id") Integer id, Model model) {
        ProductDetails productDetails = productDetailsService.getById(id);
        model.addAttribute("ctsp", productDetails);
        List<Products> products = productsService.getAllPro();
        List<Color> colors = colorService.getAllColor();
        List<Size> sizes = sizeService.getAllSZ();
        model.addAttribute("products", products);
        model.addAttribute("colors", colors);
        model.addAttribute("sizes", sizes);
        return "dashboard/chi-tiet-san-pham/update-chi-tiet-san-pham";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        productDetailsService.delete(id);
        return "redirect:/admin/chi-tiet-san-pham/hien-thi";
    }

    @PostMapping("update/{id}")
    public String update(@Valid @ModelAttribute("ctsp") ProductDetails productDetails, BindingResult result, @PathVariable("id") Integer id, Model model, HttpSession session) throws SQLException, IOException {
        if (result.hasErrors()) {
            Page<ProductDetails> productDetail = productDetailsService.getAll(0);
            List<Products> products = productsService.getAllPro();
            List<Color> colors = colorService.getAllColor();
            List<Size> sizes = sizeService.getAllSZ();
//            model.addAttribute("images", productDetails.getProductId().getImages());

            model.addAttribute("productDetails", productDetail);
            model.addAttribute("products", products);
            model.addAttribute("colors", colors);
            model.addAttribute("sizes", sizes);
            return "dashboard/chi-tiet-san-pham/update-chi-tiet-san-pham";

        }
        productDetailsService.update(productDetails);
        session.setAttribute("successMessage", "sửa thành công");
        return "redirect:/admin/chi-tiet-san-pham/hien-thi";
    }

    @GetMapping("search")
    public String search(
        @RequestParam(value = "minPrice", required = false) Double minPrice,
        @RequestParam(value = "maxPrice", required = false) Double maxPrice,
        @RequestParam(value = "productName", required = false) String productName,
        Model model, HttpSession session) {

        if (session.getAttribute("successMessage") != null) {
            String successMessage = (String) session.getAttribute("successMessage");
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }

        Page<ProductDetails> ketQuaTimKiem;

        if ((minPrice != null && maxPrice != null) || (productName != null && !productName.isEmpty())) {
            // Nếu có giá trị minPrice và maxPrice hoặc có giá trị productName, thực hiện tìm kiếm
            if (minPrice != null && maxPrice != null) {
                ketQuaTimKiem = productDetailsService.searchByPriceRange(minPrice, maxPrice, PageRequest.of(0, 10));
                model.addAttribute("list", ketQuaTimKiem);
                model.addAttribute("products", new Products());
                model.addAttribute("colors", new Color());
                model.addAttribute("sizes", new Size());
                model.addAttribute("totalPages", ketQuaTimKiem.getTotalPages());
                model.addAttribute("currentPage", 0);
                model.addAttribute("ctsp", new ProductDetails());
                model.addAttribute("color", new Color());
                model.addAttribute("size", new Size());
            } else {

                ketQuaTimKiem = productDetailsService.searchByName(productName, PageRequest.of(0, 10));
                model.addAttribute("list", ketQuaTimKiem);
                model.addAttribute("products", new Products());
                model.addAttribute("colors", new Color());
                model.addAttribute("sizes", new Size());
                model.addAttribute("totalPages", ketQuaTimKiem.getTotalPages());
                model.addAttribute("currentPage", 0);
                model.addAttribute("ctsp", new ProductDetails());
                model.addAttribute("color", new Color());
                model.addAttribute("size", new Size());
            }

        } else {
            // Nếu không có giá trị nào được cung cấp, hiển thị tất cả sản phẩm
            ketQuaTimKiem = productDetailsService.getAllProductDetails(PageRequest.of(0, 10));
            model.addAttribute("list", ketQuaTimKiem);
            model.addAttribute("products", new Products());
            model.addAttribute("colors", new Color());
            model.addAttribute("sizes", new Size());
            model.addAttribute("totalPages", ketQuaTimKiem.getTotalPages());
            model.addAttribute("currentPage", 0);
            model.addAttribute("ctsp", new ProductDetails());
            model.addAttribute("color", new Color());
            model.addAttribute("size", new Size());
        }

        // Các dòng code khác để lấy dữ liệu và thiết lập model

        return "/dashboard/chi-tiet-san-pham/chi-tiet-san-pham";
    }


}
