package com.example.datnsd56.controller;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Address;
import com.example.datnsd56.entity.Customers;
import com.example.datnsd56.service.CustomersService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/customers")
public class CustomersController {
    @Autowired
    private CustomersService customersService;

    @GetMapping("/hien-thi1")
    public String get(Model model){
//        model.addAttribute("customers",new Customers());
        List<Customers> page = customersService.get();
        model.addAttribute("list", page);
        return "/dashboard/customers/customers";
    }
    @GetMapping("/hien-thi")
    public String getAll( Model model,@RequestParam(defaultValue = "0") Integer page){
        model.addAttribute("customers",new Customers());
        Page<Customers> page1 = customersService.getAll(PageRequest.of(page,5));
//        model.addAttribute("totalPages", page1.getTotalPages());
        model.addAttribute("list", page1);
//        model.addAttribute("currentPage", pageNo);
        return "/dashboard/customers/customers";

    }
    @GetMapping("/view-update/{id}")
    public String detail(@PathVariable("id") Integer id,Model model){
//        model.addAttribute("customers",new Customers());
        Customers customers= customersService.detail(id);
        model.addAttribute("customers",customers);

        return "dashboard/customers/update-customers";
    }
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("customers") Customers customers, BindingResult result, Model model, HttpSession session){
        if(result.hasErrors()){
            model.addAttribute("list",customersService.getAll(Pageable.unpaged()));
            return "/dashboard/customers/customers";

        }
        customersService.add(customers);
        session.setAttribute("successMessage", "Thêm thành công");
        return "redirect:/admin/customers/hien-thi";

    }
    @PostMapping("/add1")
    public String add1(@Valid @ModelAttribute("customers") Customers customers, BindingResult result, Model model, HttpSession session){
        if(result.hasErrors()){
            model.addAttribute("list",customersService.getAll(Pageable.unpaged()));
            return "/dashboard/customers/customers";

        }
        customersService.add(customers);
        session.setAttribute("successMessage", "Thêm thành công");
        return "redirect:/admin/account/hien-thi";

    }
    @PostMapping("/update/{id}")
    public String update( @Valid @ModelAttribute("customers") Customers customers, BindingResult result,@PathVariable("id") Integer id , Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("customers",customers);
            return "dashboard/customers/update-customers";

        }
        customersService.update(customers);
        session.setAttribute("successMessage", "sửa thành công");
        return "redirect:/admin/customers/hien-thi";
    }
    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") Integer id){
        customersService.delete(id);
        return "redirect:/admin/customers/hien-thi";
    }
    @GetMapping("search")
    public String search(@RequestParam("phone") String phone, Model model, RedirectAttributes redirectAttributes) {
        Page<Customers> addresss = customersService.findByEmail(phone);
        model.addAttribute("list", addresss);
//        model.addAttribute("list",addressService.getAll(Pageable.unpaged()));
        model.addAttribute("customers",new Customers() );
//        model.addAttribute("account",new Account());
//        List<Account> listr=accountService.get();
//        model.addAttribute("accountlist",listr);
//        redirectAttributes.addAttribute("phone", phone);
        return "/dashboard/customers/customers";
    }
}
