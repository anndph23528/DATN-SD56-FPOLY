package com.example.datnsd56.controller;

import com.example.datnsd56.entity.Color;
import com.example.datnsd56.entity.Material;
import com.example.datnsd56.entity.ShoeSole;
import com.example.datnsd56.service.ColorService;
import com.example.datnsd56.service.ShoeSoleService;
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
@RequestMapping("/admin/de-giay")
public class ShoeSoleController {
    @Qualifier("shoeSoleServiceImpl")
    @Autowired
    private ShoeSoleService service;

    @GetMapping("/hien-thi")
    public String viewChatLieu(@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
        model.addAttribute("shoeSole", new ShoeSole());
        Page<ShoeSole> page = service.getAll(pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("list", page);
        model.addAttribute("currentPage", pageNo);
        return "/dashboard/de-giay/de-giay";
    }

    @GetMapping("/view-update/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        ShoeSole shoeSole = service.getById(id);
        model.addAttribute("shoeSole", shoeSole);
        return "/dashboard/de-giay/update-de-giay";
    }

    @PostMapping("/update/{id}")
    public String update(@Valid @ModelAttribute("shoeSole") ShoeSole shoeSole, BindingResult result, @PathVariable("id") Integer id, Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("shoeSole", shoeSole);
            return "/dashboard/de-giay/update-de-giay";

        }
        // Check if color with the same name already exists
        if (service.existsByName(shoeSole.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "ShoeSole with the same name already exists");
            return "redirect:/admin/de-giay/hien-thi";
        }
        service.update(shoeSole);
        session.setAttribute("Message", "sửa thành công");
        return "redirect:/admin/de-giay/hien-thi";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return "redirect:/admin/de-giay/hien-thi";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("shoeSole") ShoeSole shoeSole, BindingResult result, Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Page<ShoeSole> page = service.getAll(0);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("list", page);
            model.addAttribute("currentPage", 0);
            return "/dashboard/de-giay/de-giay";
        } if (service.existsByName(shoeSole.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "ShoeSole with the same name already exists");
            return "redirect:/admin/de-giay/hien-thi";
        }
        String code = "DG" + new Random().nextInt(100000);
        shoeSole.setCode(code);
        shoeSole.setStatus(true);
        service.add(shoeSole);
        redirectAttributes.addFlashAttribute("Message", "Thêm thành công");
        return "redirect:/admin/de-giay/hien-thi";

    }
    @PostMapping("/add1")
    public String addd(@Valid @ModelAttribute("shoeSole") ShoeSole shoeSole, BindingResult result, Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Page<ShoeSole> page = service.getAll(0);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("list", page);
            model.addAttribute("currentPage", 0);
            return "/dashboard/de-giay/de-giay";
        }
        // Check if color with the same name already exists
        if (service.existsByName(shoeSole.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "ShoeSole with the same name already exists");
            return "redirect:/admin/de-giay/hien-thi";
        }
        String code = "DG" + new Random().nextInt(100000);
        shoeSole.setCode(code);
        shoeSole.setStatus(true);
        service.add(shoeSole);
        session.setAttribute("Message", "Thêm thành công");
        return "redirect:/admin/san-pham-test/create";

    }
    @GetMapping("/search")
//    @PreAuthorize("hasAuthority('admin')")
    public String search(@RequestParam("name") String name,@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
        model.addAttribute("shoeSole", new ShoeSole());
        Page<ShoeSole> page = service.findByName(name);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("list", page);
        model.addAttribute("currentPage", pageNo);
        return "/dashboard/de-giay/de-giay";
    }
}
