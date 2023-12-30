package com.example.datnsd56.controller;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Roles;
import com.example.datnsd56.repository.AccountRepository;
import com.example.datnsd56.service.AccountService;
import com.example.datnsd56.service.RolesService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private RolesService rolesService;
    @Autowired
    private AccountRepository accountRepository;

    //    @GetMapping("/hien-thi")
//    public String get(Model model){
//        model.addAttribute("account",new Account());
//        Page<Account> page = accountService.getAll(0);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("list", page);
//        model.addAttribute("currentPage", 0);
//        return "/dashboard/account/account";
//    }
    @GetMapping("/hien-thi")
//    @PreAuthorize("hasAuthority('admin')")
    public String getAllBypage( Model model,@RequestParam(defaultValue = "0") Integer page){
        model.addAttribute("account",new Account());
        Page<Account> page1 = accountService.getAll(PageRequest.of(page,5));
//        model.addAttribute("totalPages", page1.getTotalPages());
        model.addAttribute("list", page1);
        List<Roles> listr=rolesService.getAll();
        model.addAttribute("rolelist",listr);
        model.addAttribute("roles",new Roles());
//        model.addAttribute("currentPage", pageNo);
        return "dashboard/account/account";

    }
    @GetMapping("/view-update/{id}")
//    @PreAuthorize("hasAuthority('admin')")
    public String detail(@PathVariable("id") Integer id,Model model){
//        model.addAttribute("account",new Account());
        Account account= accountService.detail(id);
        model.addAttribute("account",account);
        List<Roles> listr=rolesService.getAll();
        model.addAttribute("rolelist",listr);
        model.addAttribute("roles",new Roles());
        return "dashboard/account/update-account";
    }
//
//    @PostMapping("/add")
//
//    public String add(@Valid @ModelAttribute("account") Account account, BindingResult result, Model model, HttpSession session,@RequestParam(defaultValue = "0") Integer page){
//        if(result.hasErrors()){
//            model.addAttribute("list",accountService.getAll(Pageable.unpaged()));
//            Page<Account> page1 = accountService.getAll(PageRequest.of(page,5));
////        model.addAttribute("totalPages", page1.getTotalPages());
//            model.addAttribute("list", page1);
//            List<Roles> listr=rolesService.getAll();
//            model.addAttribute("rolelist",listr);
//            model.addAttribute("roles",new Roles());
//            return "/dashboard/account/account";
//
//        }
////        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
////        Account account1 = new Account();
////        account1.setPassword(encoder.encode(account.getPassword()));
//        accountService.add(account);
//
//        session.setAttribute("successMessage", "Thêm thành công");
//        return "redirect:/admin/account/hien-thi";
//
//    }
@PostMapping("/add")
//@PreAuthorize("hasAuthority('admin') || hasAuthority('user')")
public String add(@Valid @ModelAttribute("account") Account account, BindingResult result, Model model, HttpSession session, @RequestParam(defaultValue = "0") Integer page,RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
        // Handle validation errors
        model.addAttribute("list", accountService.getAll(Pageable.unpaged()));
        Page<Account> page1 = accountService.getAll(PageRequest.of(page, 5));
        model.addAttribute("list", page1);
        List<Roles> listr = rolesService.getAll();
        model.addAttribute("rolelist", listr);
        model.addAttribute("roles", new Roles());
        return "/dashboard/account/account";
    }
  //   Kiểm tra xem email đã tồn tại hay chưa
    if (accountService.findByEmail(account.getEmail()) != null) {
        // Email đã tồn tại, xử lý lỗi và trả về trang tạo tài khoản
        redirectAttributes.addFlashAttribute("errorMessage", "Email đã tồn tại");
        return "redirect:/admin/account/hien-thi";
    }

    // Hash the password using BCryptPasswordEncoder
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String hashedPassword = encoder.encode(account.getPassword());
    account.setPassword(hashedPassword);

    accountService.add(account);

    redirectAttributes.addFlashAttribute("Message", "Thêm thành công");
    return "redirect:/admin/account/hien-thi";
}
//@PostMapping("/add")
//public String add(@Valid @ModelAttribute("account") Account account, RedirectAttributes redirectAttributes,BindingResult result, Model model, HttpSession session, @RequestParam(defaultValue = "0") Integer page) {
//    // Kiểm tra lỗi validation
//    if (result.hasErrors()) {
//        // Handle validation errors
//        model.addAttribute("list", accountService.getAll(Pageable.unpaged()));
//        Page<Account> page1 = accountService.getAll(PageRequest.of(page, 5));
//        model.addAttribute("list", page1);
//        List<Roles> listr = rolesService.getAll();
//        model.addAttribute("rolelist", listr);
//        model.addAttribute("roles", new Roles());
//        return "/dashboard/account/account";
//    }
//
//    // Kiểm tra xem email đã tồn tại hay chưa
//    if (accountService.findByEmail(account.getEmail()) != null) {
//        // Email đã tồn tại, xử lý lỗi và trả về trang tạo tài khoản
//        redirectAttributes.addFlashAttribute("emailError", "Email đã tồn tại");
//        return "redirect:/admin/account/hien-thi";
//    }
//
//    // Hash mật khẩu bằng BCryptPasswordEncoder
//    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//    String hashedPassword = encoder.encode(account.getPassword());
//    account.setPassword(hashedPassword);
//
//    // Thêm tài khoản vào cơ sở dữ liệu
//    accountService.add(account);
//
//    redirectAttributes.addFlashAttribute("Message", "Thêm thành công");
//    return "redirect:/admin/account/hien-thi";
//}

    @PostMapping("/add1")
//    @PreAuthorize("hasAuthority('admin')")
    public String add1(@Valid @ModelAttribute("account") Account account, BindingResult result, Model model, HttpSession session,RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            model.addAttribute("list",accountService.getAll(Pageable.unpaged()));
            List<Roles> listr=rolesService.getAll();
            model.addAttribute("rolelist",listr);
            model.addAttribute("roles",new Roles());
            return "/dashboard/account/account";

        }
        accountService.add(account);
       redirectAttributes.addFlashAttribute("successMessage", "Thêm thành công");
        return "redirect:/admin/address/hien-thi";


    }
    @PostMapping("/update/{id}")
//    @PreAuthorize("hasAuthority('admin')")
    public String update( @Valid @ModelAttribute("account") Account account, BindingResult result,@PathVariable("id") Integer id , Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("account",account);
            List<Roles> listr=rolesService.getAll();
            model.addAttribute("rolelist",listr);
            model.addAttribute("roles",new Roles());
            return "dashboard/account/update-account";

        }
        // Hash the password using BCryptPasswordEncoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(account.getPassword());
        account.setPassword(hashedPassword);
        accountService.update(account);
        session.setAttribute("successMessage", "sửa thành công");
        return "redirect:/admin/account/hien-thi";
    }
    @GetMapping("delete/{id}")
//    @PreAuthorize("hasAuthority('admin')")
    public String delete(@PathVariable("id") Integer id){
        accountService.delete(id);
        return "redirect:/admin/account/hien-thi";
    }
    @GetMapping("search")
//    @PreAuthorize("hasAuthority('admin')")
    public String search(@RequestParam("phone") String phone, Model model, RedirectAttributes redirectAttributes) {
        Page<Account> accounts = accountService.findByPhone(phone);
        model.addAttribute("list", accounts);
//        model.addAttribute("list",accountService.getAll(Pageable.unpaged()));
        model.addAttribute("account",new Account() );
        model.addAttribute("roles",new Roles());
        List<Roles> listr=rolesService.getAll();
        model.addAttribute("rolelist",listr);
//        redirectAttributes.addAttribute("phone", phone);
        return "/dashboard/account/account";
    }
}
