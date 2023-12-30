package com.example.datnsd56.controller;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Roles;
import com.example.datnsd56.repository.AccountRepository;
import com.example.datnsd56.service.AccountService;
import com.example.datnsd56.service.RolesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.AuthenticationException;
import java.util.List;

@Controller
@RequestMapping("/login")
public class LoginController  {
    @Autowired
    RolesService rolesService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @GetMapping("/custom-login")
    public String showCustomLoginPage(Model model) {
        model.addAttribute("account", new Account());

        return "auth_login/auth-login";
        // Remove the leading "/"
    }

@GetMapping("/register")
    public String showRegister(Model model){
    model.addAttribute("account", new Account());

    return "auth_login/auth-register";

}

//    @PostMapping("/add")
//    public String add(@Valid @ModelAttribute("account") Account account, BindingResult result, Model model, HttpSession session) {
//        if (result.hasErrors()) {// Handle validation errors
////            model.addAttribute("account", new Account());
//            return "auth_login/auth-register";
//        }
////        model.addAttribute("account", new Account());
//        // Hash the password using BCryptPasswordEncoder
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String hashedPassword = encoder.encode(account.getPassword());
//        account.setPassword(hashedPassword);
//        Roles user = new Roles();
//         account.setRole_id(user.setId(Integer.parseInt("4")));
////        account.setRole_id(Integer.parseInt(4));
//        account.setStatuss(true);
//        accountService.add(account);
//
//        session.setAttribute("successMessage", "Thêm thành công");
//        return "redirect:/product/hien-thi";
//    }
//@PostMapping("/add")
//public String add(@Valid @ModelAttribute("account") Account account, BindingResult result, Model model, HttpSession session) {
//    if (result.hasErrors()) {
//        return "auth_login/auth-register";
//    }
//
//    // Hash the password using BCryptPasswordEncoder
//    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//    String hashedPassword = encoder.encode(account.getPassword());
//    account.setPassword(hashedPassword);
//
//    // Set the role ID to 4 by default
//    Roles userRole = rolesService.findbyname("user");
////    userRole.setId(userRole);
//    account.setRole_id(userRole);
//
//    account.setStatuss(true);
//    accountService.add(account);
//
//
//    session.setAttribute("successMessage", "Thêm thành công");
//    return "redirect:/login/custom-login";
//}
@PostMapping("/add")
public String add(@Valid @ModelAttribute("account") Account account, BindingResult result, Model model, RedirectAttributes redirectAttributes, HttpSession session) {
    if (result.hasErrors()) {
        model.addAttribute("account",account);
        return "auth_login/auth-register";
    }

    //   Kiểm tra xem email đã tồn tại hay chưa
    if (accountService.findByEmail(account.getEmail()) != null) {
        // Email đã tồn tại, xử lý lỗi và trả về trang tạo tài khoản
        redirectAttributes.addFlashAttribute("errorMessage", "Email đã tồn tại");

        return "redirect:/login/register";
    }

    // Hash mật khẩu bằng BCryptPasswordEncoder
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String hashedPassword = encoder.encode(account.getPassword());
    account.setPassword(hashedPassword);

    // Set role ID mặc định là 4 (user)
    Roles userRole = rolesService.findbyname1("user");
    account.setRole_id(userRole);

    account.setStatuss(true);
    accountService.add(account);

    session.setAttribute("Message", "Thêm thành công");
    return "redirect:/login/custom-login";
}

}
