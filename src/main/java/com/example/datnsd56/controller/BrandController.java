package com.example.datnsd56.controller;

import com.example.datnsd56.entity.Brand;
import com.example.datnsd56.service.BrandService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Random;

@Controller
@RequestMapping("/admin/thuong-hieu")
public class BrandController {
    @Qualifier("brandServiceImpl")
    @Autowired
    private BrandService service;

    @GetMapping("/hien-thi")
    @PreAuthorize("hasAuthority('admin')")
    public String viewChatLieu(@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
        model.addAttribute("brand", new Brand());
        Page<Brand> page = service.getAll(pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("list", page);
        model.addAttribute("currentPage", pageNo);
        return "/dashboard/thuong-hieu/thuong-hieu";
    }

    @GetMapping("/view-update/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        Brand brand = service.getById(id);
        model.addAttribute("brand", brand);
        return "/dashboard/thuong-hieu/update-thuong-hieu";
    }

    @PostMapping("/update/{id}")
    public String update(@Valid @ModelAttribute("brand") Brand brand, BindingResult result, @PathVariable("id") Integer id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("brand", brand);
            return "/dashboard/thuong-hieu/update-thuong-hieu";

        } // Check if color with the same name already exists
        if (service.existsByName(brand.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Brand with the same name already exists");
            return "redirect:/admin/loai-giay/hien-thi";
        }
        service.update(brand);
        session.setAttribute("Message", "sửa thành công");
        return "redirect:/admin/thuong-hieu/hien-thi";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return "redirect:/admin/thuong-hieu/hien-thi";
    }


    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("brand") Brand brand, BindingResult result, Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Page<Brand> page = service.getAll(0);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("list", page);
            model.addAttribute("currentPage", 0);
            return "/dashboard/thuong-hieu/thuong-hieu";
        }
        // Check if color with the same name already exists
        if (service.existsByName(brand.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Brand with the same name already exists");
            return "redirect:/admin/thuong-hieu/hien-thi";
        }
        String code = "Brand" + new Random().nextInt(100000);
        brand.setCode(code);
        brand.setStatus(true);
        service.add(brand);
        redirectAttributes.addFlashAttribute("Message", "Thêm thành công");
        return "redirect:/admin/thuong-hieu/hien-thi";

    }

    @PostMapping("/add1")
    public String add1(@Valid @ModelAttribute("brand") Brand brand, BindingResult result, Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Page<Brand> page = service.getAll(0);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("list", page);
            model.addAttribute("currentPage", 0);
            return "/dashboard/thuong-hieu/thuong-hieu";
        }   if (service.existsByName(brand.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Brand with the same name already exists");
            return "redirect:/admin/san-pham-test/create";
        }
        String code = "TH" + new Random().nextInt(100000);
        brand.setCode(code);
        brand.setStatus(true);
        service.add(brand);
        redirectAttributes.addFlashAttribute("Message",  "Thêm thành công");
        return "redirect:/admin/san-pham-test/create";

    }
    @GetMapping("search")
//    @PreAuthorize("hasAuthority('admin')")
    public String search(@RequestParam("name") String name,@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
        model.addAttribute("brand", new Brand());
        Page<Brand> page = service.findByName(name);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("list", page);
        model.addAttribute("currentPage", pageNo);
        return "/dashboard/thuong-hieu/thuong-hieu";
    }
}
