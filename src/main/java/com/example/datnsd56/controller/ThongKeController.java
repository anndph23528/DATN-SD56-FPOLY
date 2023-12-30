package com.example.datnsd56.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/thong-ke")
public class ThongKeController {

    @GetMapping("hien-thi")
    public String viewhoadon(Model model){

        return "/dashboard/thongke-hoadon/thong-ke";
    }
}
