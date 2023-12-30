package com.example.datnsd56.controller;

import com.example.datnsd56.entity.Color;
import com.example.datnsd56.entity.OrderItem;
import com.example.datnsd56.entity.Orders;
import com.example.datnsd56.service.AccountService;
import com.example.datnsd56.service.AddressService;
import com.example.datnsd56.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/hoa-don")
public class OrderController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private AddressService service;

    @GetMapping("/hien-thi")
    public String viewHoaDon(@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
        Page<Orders> page = ordersService.getAllOrders(pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("list", page);
        model.addAttribute("currentPage", pageNo);
        return "/dashboard/thongke-hoadon/hoa-don";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id,
                         Model model) {
        List<OrderItem> lstBillDetails = ordersService.getLstDetailByOrderId(id);
        model.addAttribute("page", lstBillDetails);
        return "/admin/thongke-hoadon/update-hoa-don";
    }


    @GetMapping("/cancel-bill/{id}")
    public String cancelBill(@PathVariable Integer id, RedirectAttributes attributes) {
        ordersService.cancelOrder(id);
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
    public String completeBill(@PathVariable Integer id, RedirectAttributes attributes) {
        ordersService.completeOrder(id);
        return "redirect:/admin/hoa-don/hien-thi";
    }
}
