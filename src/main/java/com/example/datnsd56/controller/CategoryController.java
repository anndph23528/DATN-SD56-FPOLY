package com.example.datnsd56.controller;

import com.example.datnsd56.entity.Category;
import com.example.datnsd56.entity.Color;
import com.example.datnsd56.service.CategoryService;
import com.example.datnsd56.service.ColorService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
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
@RequestMapping("/admin/loai-giay")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @GetMapping("/hien-thi")
    public String viewChatLieu(@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
        model.addAttribute("category", new Category());
        Page<Category> page = service.getAll(pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("list", page);
        model.addAttribute("currentPage", pageNo);
        return "/dashboard/loai-giay/loai-giay";
    }

    @GetMapping("/view-update/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        Category category = service.getById(id);
        model.addAttribute("category", category);
        return "/dashboard/loai-giay/update-loai-giay";
    }

    @PostMapping("/update/{id}")
    public String update(@Valid @ModelAttribute("category") Category category, BindingResult result, @PathVariable("id") Integer id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("category", category);
            return "/dashboard/loai-giay/update-loai-giay";

        }  if (service.existsByName(category.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category with the same name already exists");
            return "redirect:/admin/loai-giay/hien-thi";

        }
        service.update(category);
        redirectAttributes.addFlashAttribute("Message", "sửa thành công");
        return "redirect:/admin/loai-giay/hien-thi";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return "redirect:/admin/loai-giay/hien-thi";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("category") Category category, BindingResult result, Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Page<Category> page = service.getAll(0);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("list", page);
            model.addAttribute("currentPage", 0);
            return "/dashboard/loai-giay/loai-giay";
        }
        // Check if color with the same name already exists
        if (service.existsByName(category.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category with the same name already exists");
            return "redirect:/admin/loai-giay/hien-thi";

        }
        String code = "CL" + new Random().nextInt(100000);
        category.setCode(code);
        category.setStatus(true);
        service.add(category);
        redirectAttributes.addFlashAttribute("Message", "Thêm thành công");
        return "redirect:/admin/loai-giay/hien-thi";

    }
    @PostMapping("/add1")
    public String addd(@Valid @ModelAttribute("category") Category category, BindingResult result, Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Page<Category> page = service.getAll(0);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("list", page);
            model.addAttribute("currentPage", 0);
            return "/dashboard/loai-giay/loai-giay";
        }  if (service.existsByName(category.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category with the same name already exists");
            return "redirect:/admin/san-pham-test/create";
        }
        String code = "CL" + new Random().nextInt(100000);
        category.setCode(code);
        category.setStatus(true);
        service.add(category);
        redirectAttributes.addFlashAttribute("Message", "Thêm thành công");
        return "redirect:/admin/san-pham-test/create";

    }
    @GetMapping("search")
//    @PreAuthorize("hasAuthority('admin')")
    public String search(@RequestParam("name") String name,@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
        model.addAttribute("category", new Category());
        Page<Category> page = service.findByName(name);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("list", page);
        model.addAttribute("currentPage", pageNo);
        return "/dashboard/loai-giay/loai-giay";
    }
}
