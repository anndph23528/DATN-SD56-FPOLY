package com.example.datnsd56.controller;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Voucher;
import com.example.datnsd56.entity.VoucherUsage;
import com.example.datnsd56.service.AccountService;
import com.example.datnsd56.service.VoucherService;
import com.example.datnsd56.service.VoucherUsageService;
import com.example.datnsd56.service.impl.VoucherSeviceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product/")
public class VoucherContrller2 {

    @Autowired
    private VoucherService voucherService;
    @Autowired
    private VoucherSeviceImpl voucherSeviceImpl;
    @Autowired
    private VoucherUsageService voucherUsageService;
@Autowired
private AccountService accountService;
@Autowired
private VoucherSeviceImpl voucherSevice;
    @GetMapping("/saveVoucher/{voucherId}")
    public String saveVoucher(@PathVariable Integer voucherId, Principal principal) {
        // Lấy thông tin tài khoản đang đăng nhập
        Optional<Account> account = accountService.finByName(principal.getName());


        // Lấy thông tin voucher
        Optional<Voucher> voucher = voucherSeviceImpl.findByid(voucherId);

        // Lưu voucher cho tài khoản
        voucherUsageService.saveVoucherForAccount(voucher.get(), account.get());

        return "redirect:/product/vouchers";
    }

    @GetMapping("/vouchers")
    public String getVouchers(Model model, Principal principal) {
        // Lấy thông tin tài khoản đang đăng nhập
        Optional<Account> account = accountService.finByName(principal.getName());


        // Lấy danh sách tất cả voucher
//        List<Voucher> allVouchers = voucherSeviceImpl.getAllVouchers();

        // Lấy danh sách voucher đã lưu cho tài khoản
        List<VoucherUsage> voucherUsages = voucherUsageService.findVisibleVoucherUsagesByAccount(account.get().getId());

        // Loại bỏ những voucher đã lưu khỏi danh sách tất cả voucher
//        allVouchers.removeAll(voucherUsages.stream().map(VoucherUsage::getVoucher).collect(Collectors.toList()));

//        model.addAttribute("allVouchers", allVouchers);
        model.addAttribute("voucherUsagess", voucherUsages);
        return "website/index/user-voucher";
    }

//    @GetMapping("/vouchers1")
//    public String getVouchers1(Model model, Principal principal) {
//        // Lấy thông tin tài khoản đang đăng nhập
//        Optional<Account> account = accountService.finByName(principal.getName());
//
//
//        // Lấy danh sách tất cả voucher
//        List<Voucher> allVouchers = voucherSeviceImpl.getAllVouchers();
//
//        // Lấy danh sách voucher đã lưu cho tài khoản
//        List<VoucherUsage> voucherUsages = voucherUsageService.findVoucherUsagesByAccount(account.get().getId());
//
//        // Loại bỏ những voucher đã lưu khỏi danh sách tất cả voucher
//        allVouchers.removeAll(voucherUsages.stream().map(VoucherUsage::getVoucher).collect(Collectors.toList()));
//
//        model.addAttribute("allVouchers", allVouchers);
////        model.addAttribute("voucherUsages", voucherUsages);
//        return "website/index/vc1";
//    }
}
