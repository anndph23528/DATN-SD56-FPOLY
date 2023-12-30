package com.example.datnsd56.controller;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Color;
import com.example.datnsd56.entity.Material;
import com.example.datnsd56.entity.Roles;
import com.example.datnsd56.service.MaterialService;
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

import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/admin/chat-lieu")
public class MaterialController {
    @Qualifier("materialServiceImpl")
    @Autowired
    private MaterialService service;

    @GetMapping("/hien-thi")
    public String viewChatLieu(@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
        model.addAttribute("chatlieu", new Material());
        Page<Material> page = service.pageMaterial(pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("list", page);
        model.addAttribute("currentPage", pageNo);
        return "/dashboard/chat-lieu/chat-lieu";
    }

    @GetMapping("/view-update/{id}")
    public String detail(@PathVariable("id") Integer id,Model model,RedirectAttributes redirectAttributes) {

        Material material= service.getById(id);
        redirectAttributes.addFlashAttribute("material",material);
        return "/dashboard/chat-lieu/update-chat-lieu";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        service.remove(id);
        return "redirect:/admin/chat-lieu/hien-thi";
    }

    @PostMapping("/add")
    public String addChatLieu(@Valid @ModelAttribute("chatlieu") Material material, BindingResult result, Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Page<Material> page = service.pageMaterial(0);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("list", page);
            model.addAttribute("currentPage", 0);
            return "/dashboard/chat-lieu/chat-lieu";
        }
        // Check if color with the same name already exists
        if (service.existsByName(material.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Material with the same name already exists");
            return "redirect:/admin/chat-lieu/hien-thi";
        }
        String code = "CL" + new Random().nextInt(100000);
        material.setCode(code);
        material.setStatus(true);
        service.add(material);
        redirectAttributes.addFlashAttribute("Message", "Thêm thành công");
        return "redirect:/admin/chat-lieu/hien-thi";
    }
    @PostMapping("/add1")
    public String add(@Valid @ModelAttribute("chatlieu") Material material, BindingResult result, Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Page<Material> page = service.pageMaterial(0);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("list", page);
            model.addAttribute("currentPage", 0);
            return "/dashboard/chat-lieu/chat-lieu";
        }  // Check if color with the same name already exists
        if (service.existsByName(material.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Material with the same name already exists");
            return "redirect:/admin/chat-lieu/hien-thi";
        }
        String code = "CL" + new Random().nextInt(100000);
        material.setCode(code);
        material.setStatus(true);
        service.add(material);
        session.setAttribute("Message", "Thêm thành công");
        return "redirect:/admin/san-pham-test/create";
    }

    @PostMapping("/update/{id}")
    public String update(@Valid @ModelAttribute("color") Material material, BindingResult result, @PathVariable("id") Integer id, Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("material", material);
            return "/dashboard/chat-lieu/update-chat-lieu";

        }   // Check if color with the same name already exists
        if (service.existsByName(material.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Material with the same name already exists");
            return "redirect:/admin/chat-lieu/hien-thi";
        }
        service.update(material);
        redirectAttributes.addFlashAttribute("Message", "sửa thành công");
        return "redirect:/admin/chat-lieu/hien-thi";
    }
    @GetMapping("search")
//    @PreAuthorize("hasAuthority('admin')")
    public String search(@RequestParam("name") String name,@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
        model.addAttribute("chatlieu", new Material());
        Page<Material> page = service.findByName(name);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("list", page);
        model.addAttribute("currentPage",pageNo );
        return "/dashboard/chat-lieu/chat-lieu";
    }
}
