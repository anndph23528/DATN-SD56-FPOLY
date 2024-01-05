package com.example.datnsd56.controller;

import com.example.datnsd56.entity.OrderItem;
import com.example.datnsd56.entity.Orders;
import com.example.datnsd56.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/thong-ke")
@PreAuthorize("hasAuthority('admin')")
public class ThongKeController {
    @Qualifier("thongKeServiceImpl")
    @Autowired
    private ThongKeService service;

    @GetMapping("hien-thi")
    public String viewHoaDon(@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
        BigDecimal total1 = service.getToTal1();
        model.addAttribute("total1",total1);
        BigDecimal totalManey1 = service.getToTalManey1();
        model.addAttribute("totalManey1", totalManey1);
        BigDecimal totalHuy = service.getToTalHuy();
        model.addAttribute("totalHuy", totalHuy);
        BigDecimal totalAll = service.getToTalAll();
        model.addAttribute("totalAll", totalAll);
        BigDecimal totalAllManey = service.getToTalAllManey();
        model.addAttribute("totalAllManey", totalAllManey);

        List<OrderItem> list = service.getAll();
        model.addAttribute("list",list);
        return "/dashboard/thongke-hoadon/thong-ke";
    }  @GetMapping("hien-thi/dang-giao")
    public String viewHoaDonDangGiao(@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
        BigDecimal total1 = service.getToTal1();
        model.addAttribute("total1",total1);
        BigDecimal totalManey1 = service.getToTalManey1();
        model.addAttribute("totalManey1", totalManey1);
        BigDecimal totalHuy = service.getToTalHuy();
        model.addAttribute("totalHuy", totalHuy);
        BigDecimal totalAll = service.getToTalAll();
        model.addAttribute("totalAll", totalAll);
        BigDecimal totalAllManey = service.getToTalAllManey();
        model.addAttribute("totalAllManey", totalAllManey);

        List<OrderItem> list = service.getAlldanggiao();
        model.addAttribute("list",list);
        return "/dashboard/thongke-hoadon/thong-ke";
    }  @GetMapping("hien-thi/hoan-thanh")
    public String viewHoaDonHoanThanh(@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
        BigDecimal total1 = service.getToTal1();
        model.addAttribute("total1",total1);
        BigDecimal totalManey1 = service.getToTalManey1();
        model.addAttribute("totalManey1", totalManey1);
        BigDecimal totalHuy = service.getToTalHuy();
        model.addAttribute("totalHuy", totalHuy);
        BigDecimal totalAll = service.getToTalAll();
        model.addAttribute("totalAll", totalAll);
        BigDecimal totalAllManey = service.getToTalAllManey();
        model.addAttribute("totalAllManey", totalAllManey);

        List<OrderItem> list = service.getAllhoanthanh();
        model.addAttribute("list",list);
        return "/dashboard/thongke-hoadon/thong-ke";
    }  @GetMapping("hien-thi/huy")
    public String viewHoaDonHuy(@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
        BigDecimal total1 = service.getToTal1();
        model.addAttribute("total1",total1);
        BigDecimal totalManey1 = service.getToTalManey1();
        model.addAttribute("totalManey1", totalManey1);
        BigDecimal totalHuy = service.getToTalHuy();
        model.addAttribute("totalHuy", totalHuy);
        BigDecimal totalAll = service.getToTalAll();
        model.addAttribute("totalAll", totalAll);
        BigDecimal totalAllManey = service.getToTalAllManey();
        model.addAttribute("totalAllManey", totalAllManey);

        List<OrderItem> list = service.getAllhuy();
        model.addAttribute("list",list);
        return "/dashboard/thongke-hoadon/thong-ke";
    }  @GetMapping("hien-thi/top5")
    public String viewHoaTop5(@RequestParam(value = "page", defaultValue = "0") Integer pageNo, Model model) {
        BigDecimal total1 = service.getToTal1();
        model.addAttribute("total1",total1);
        BigDecimal totalManey1 = service.getToTalManey1();
        model.addAttribute("totalManey1", totalManey1);
        BigDecimal totalHuy = service.getToTalHuy();
        model.addAttribute("totalHuy", totalHuy);
        BigDecimal totalAll = service.getToTalAll();
        model.addAttribute("totalAll", totalAll);
        BigDecimal totalAllManey = service.getToTalAllManey();
        model.addAttribute("totalAllManey", totalAllManey);

        List<OrderItem> list = service.getAllTop5();
        model.addAttribute("list",list);
        return "/dashboard/thongke-hoadon/thong-ke";
    }

}
