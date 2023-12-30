package com.example.datnsd56.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class HoaDonController {
    @GetMapping("/hoa-don")
    public String viewHoaDon(Model model){

        return "/dashboard/thongke-hoadon/hoa-don";

    }
}
