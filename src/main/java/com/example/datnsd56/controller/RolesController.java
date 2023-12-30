package com.example.datnsd56.controller;

import com.example.datnsd56.entity.Roles;
import com.example.datnsd56.entity.Size;
import com.example.datnsd56.service.RolesService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/roles")
public class RolesController {
    @Autowired
    private RolesService rolesService;

        @GetMapping("/hien-thi1")
        @PreAuthorize("hasAuthority('user')")
    public String get(Model model){
//        model.addAttribute("roles",new Roles());
        List<Roles> page = rolesService.getAll();
        model.addAttribute("list", page);
        return "/dashboard/roles/roles";
    }
    @GetMapping("/hien-thi")
    public String getAll( Model model,@RequestParam(defaultValue = "0") Integer page){
        model.addAttribute("roles",new Roles());
        Page<Roles> page1 = rolesService.getAllbypage(PageRequest.of(page,5));
//        model.addAttribute("totalPages", page1.getTotalPages());
        model.addAttribute("list", page1);
//        model.addAttribute("currentPage", pageNo);
        return "/dashboard/roles/roles";

    }
    @GetMapping("/view-update/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public String detail(@PathVariable("id") Integer id,Model model){

//        model.addAttribute("roles",new Roles());
        Roles roles= rolesService.detail(id);
        model.addAttribute("roles",roles);

        return "dashboard/roles/update-roles";
    }
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("roles") Roles roles,  BindingResult result, Model model, HttpSession session,RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            model.addAttribute("list",rolesService.getAllbypage(Pageable.unpaged()));
            return "/dashboard/roles/roles";

        }  // Check if color with the same name already exists
        if (rolesService.existsByName(roles.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Role with the same name already exists");
            return "redirect:/admin/roles/hien-thi";
        }
        roles.setStatus(true);
        rolesService.add(roles);
        redirectAttributes.addFlashAttribute("Message", "Thêm thành công");
        return "redirect:/admin/roles/hien-thi";

    }
    @PostMapping("/add1")
    public String add1(@Valid @ModelAttribute("roles") Roles roles, BindingResult result, Model model, HttpSession session,RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            model.addAttribute("list",rolesService.getAllbypage(Pageable.unpaged()));
            return "/dashboard/roles/roles";

        } if (rolesService.existsByName(roles.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Role with the same name already exists");
            return "redirect:/admin/roles/hien-thi";
        }
        roles.setStatus(true);
        rolesService.add(roles);
        redirectAttributes.addFlashAttribute("Message", "Thêm thành công");
        return "redirect:/admin/account/hien-thi";

    }
    @PostMapping("/update/{id}")
    public String update( @Valid @ModelAttribute("roles") Roles roles, BindingResult result,@PathVariable("id") Integer id , Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("roles",roles);
            return "dashboard/roles/update-roles";

        }   if (rolesService.existsByName(roles.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Role with the same name already exists");
            return "redirect:/admin/roles/hien-thi";
        }
        rolesService.update(roles);
        redirectAttributes.addFlashAttribute("Message", "sửa thành công");
        return "redirect:/admin/roles/hien-thi";
    }
    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") Integer id){
        rolesService.delete(id);
        return "redirect:/admin/roles/hien-thi";
    }
    @GetMapping("/search")
//    @PreAuthorize("hasAuthority('admin')")
    public String search(@RequestParam("name") String name,@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
        model.addAttribute("roles",new Roles());
        Page<Roles> page1 = rolesService.findByName(name);
//        model.addAttribute("totalPages", page1.getTotalPages());
        model.addAttribute("list", page1);
        return "/dashboard/roles/roles";
    }
}
