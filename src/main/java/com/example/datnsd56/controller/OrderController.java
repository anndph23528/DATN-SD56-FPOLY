package com.example.datnsd56.controller;

import com.example.datnsd56.entity.*;
import com.example.datnsd56.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/hoa-don")
@PreAuthorize("hasAuthority('admin') || hasAuthority('staff')")

public class OrderController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private AddressService service;
    @Autowired
    TransactionService transactionService;
    @Autowired
    ThongKeService thongKeService;

    @GetMapping("/hien-thi")
    public String viewHoaDon(@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
        Page<Orders> page = ordersService.getAllOrders(pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("list", page);
        model.addAttribute("currentPage", pageNo);
        return "/dashboard/thongke-hoadon/hoa-don";
    }

//    @GetMapping("/lich-su-hoa-don")
//    public String lSVoucher1(Model model){
//        List<Orders> listorder = ordersService.getAllOrders();
//
//        model.addAttribute("list",listorder);
//        return "dashboard/thongke-hoadon/hoa-don";
//
//    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id,
                         Model model,RedirectAttributes attributes) {
        Orders bill = ordersService.getOneBill(id);
        if (bill != null) {
            List<OrderItem> lstBillDetails = ordersService.getLstDetailByOrderId(id);
            List<Transactions> listTransection = transactionService.findAllByOrderId(id);
            model.addAttribute("bill", bill);
            model.addAttribute("lstBillDetails", lstBillDetails);
            model.addAttribute("listTransection", listTransection);
            return "/dashboard/thongke-hoadon/update-hoa-don";
        } else {
            attributes.addFlashAttribute("message", "Không tìm thấy hoá đơn");
            return "redirect:/admin/hoa-don/hien-thi";
        }
    }


    @GetMapping("/cancel-bill/{id}")
    public String cancelBill(@PathVariable Integer id, RedirectAttributes attributes,Principal principal) {
        Orders bill = ordersService.getOneBill(id);
        String name = principal.getName();
        Optional<Account> account = accountService.finByName(name);
        if (bill != null){
            ordersService.cancelOrder(id, account.get());
        }
        return "redirect:/admin/hoa-don/hien-thi";
    }
    @PostMapping("/shipping-bill")
    public String shippingBill(@RequestParam("id") Integer id,
                               @RequestParam("shippingFee") BigDecimal shippingFee){
        ordersService.shippingOrder(id, shippingFee);
        return "redirect:/admin/hoa-don/hien-thi";
    }

    @GetMapping("/confirm-bill/{id}")
    public String confirmBill(@PathVariable Integer id, RedirectAttributes attributes) {
        ordersService.acceptBill(id);
        return "redirect:/admin/hoa-don/hien-thi";
    }

    @GetMapping("/complete-bill/{id}")
    public String completeBill(@PathVariable Integer id, RedirectAttributes attributes, Principal principal) {
        Orders bill = ordersService.getOneBill(id);
        String name = principal.getName();
        Optional<Account> account = accountService.finByName(name);
        if (bill != null){
             ordersService.completeOrder(id, account.get());
            }
        return "redirect:/admin/hoa-don/hien-thi";
    }

    @GetMapping("/thoi-gian")
    public String getVoucherHistory(
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate,
//            @PageableDefault(size = 25, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable,
            Model model,@RequestParam(value = "page",defaultValue = "0") Integer page) {
        Page<Orders> filteredHistory = ordersService.filterAndSearch(startDate, endDate, page);
        model.addAttribute("totalPages", filteredHistory.getTotalPages());
        model.addAttribute("currentPage", 0);
        model.addAttribute("list", filteredHistory);

        return "/dashboard/thongke-hoadon/hoa-don";
    }

    @GetMapping("search")
//    @PreAuthorize("hasAuthority('admin')")
    public String search(@RequestParam("phone") String phone, Model model, RedirectAttributes redirectAttributes) {
        Page<Orders> orders = ordersService.findByPhone(phone);
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("currentPage", 0);
        model.addAttribute("list", orders);

//        model.addAttribute("list",accountService.getAll(Pageable.unpaged()));
//        redirectAttributes.addAttribute("phone", phone);
        return "/dashboard/thongke-hoadon/hoa-don";
    }
}
