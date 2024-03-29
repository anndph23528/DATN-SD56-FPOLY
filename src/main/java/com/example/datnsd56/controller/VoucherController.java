package com.example.datnsd56.controller;

import com.example.datnsd56.dto.MostUsedVoucherDTO;
import com.example.datnsd56.entity.*;
import com.example.datnsd56.service.VoucherService;
import com.example.datnsd56.service.VoucherUsageHistoryService;
import com.example.datnsd56.service.impl.VoucherSeviceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/voucher")
@PreAuthorize("hasAuthority('admin')")
public class VoucherController {
    @Autowired
    private VoucherSeviceImpl voucherService;
    @Autowired
    private VoucherUsageHistoryService voucherUsageHistoryService;

    @GetMapping("/hien-thi")
    public String getAllByPage(Model model,@RequestParam(defaultValue = "0") Integer page){
        voucherService.checkAndDeactivateExpiredVouchers();
//        model.addAttribute("voucher", new Voucher());
        Page<Voucher> page1 = voucherService.getAll(PageRequest.of(page,10));
        model.addAttribute("vouchers", page1);
        return "dashboard/voucher/voucher";

    }
    @GetMapping
    public String getAllVouchers(Model model) {

        voucherService.checkAndDeactivateExpiredVouchers();
        model.addAttribute("voucher", new Voucher());

        // Kiểm tra và cập nhật trạng thái trước khi hiển thị danh sách
        List<Voucher> vouchers = voucherService.getAllVouchers();
        model.addAttribute("vouchers", vouchers);


        return "dashboard/voucher/voucher";

    }
    @PostMapping("/new")
    public String newVoucherSubmit(@Valid @ModelAttribute("voucher") Voucher voucher, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            // Nếu có lỗi validation, điều hướng trở lại form với thông báo lỗi
            Page<Voucher> page1 = voucherService.getAll(Pageable.ofSize(5));
            model.addAttribute("vouchers", page1);
            return "dashboard/voucher/add";
        }

        // Kiểm tra xem mã voucher đã tồn tại hay không
        if (voucherService.existsByCode(voucher.getCode())) {
            model.addAttribute("errorMessage", "Mã voucher đã tồn tại");
            return "dashboard/voucher/add";
        }

        // Email không tồn tại, tiếp tục xử lý
        voucher.setStartDate(LocalDateTime.now());
        voucher.setActive(true);
        voucherService.saveVoucher(voucher);
        redirectAttributes.addFlashAttribute("successMessage", "Voucher created successfully!");

        return "redirect:/admin/voucher/hien-thi";
    }





    @PostMapping("/update/{id}")
    public String update(@Valid @ModelAttribute("voucher") Voucher voucher, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model,@PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            // Nếu có lỗi validation, điều hướng trở lại form với thông báo lỗi

            return "/dashboard/voucher/update-voucher";
        }

        voucher.setActive(true);
        voucherService.updateVoucher(voucher);
        redirectAttributes.addFlashAttribute("successMessage", "Voucher created successfully!");

        return "redirect:/admin/voucher/hien-thi";

    }

    @GetMapping("/{id}")
    public String getVoucherById(@PathVariable Integer id, Model model) {
        Voucher voucher = voucherService.getVoucherById(id);
        model.addAttribute("voucher", voucher);
        return "dashboard/voucher/update-voucher";
    }

    @GetMapping("/view-add")
    public String newVoucherForm(Model model) {
        model.addAttribute("voucher", new Voucher());
        return "dashboard/voucher/add";
    }

//    @PostMapping("/new")
//    public String newVoucherSubmit(@ModelAttribute Voucher voucher, RedirectAttributes redirectAttributes) {
//        voucher.setActive(true);
//        voucherService.saveVoucher(voucher);
//        redirectAttributes.addFlashAttribute("successMessage", "Voucher created successfully!");
//        return "redirect:/admin/voucher";
//    }

    @GetMapping("/delete/{id}")
    public String deleteVoucher(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        voucherService.deletess(id);
        redirectAttributes.addFlashAttribute("successMessage", "Voucher deleted successfully!");
        return "redirect:/admin/voucher/hien-thi";
    }
//    @GetMapping("/lich-su-dung-voucher")
//    public String lSVoucher(Model model,@RequestParam(defaultValue = "0") Integer page){
//        Page<VoucherUsageHistory> voucherUsageHistories = voucherUsageHistoryService.getall(PageRequest.of(page,5));
//
//model.addAttribute("history",voucherUsageHistories);
//        return "dashboard/voucher/lich-su-dung-voucher";
//
//    }

    @GetMapping("/lich-su-dung-voucher")
    public String lSVoucher1(Model model){
        List<VoucherUsage> voucherUsageHistories = voucherUsageHistoryService.getALLhistory();
        MostUsedVoucherDTO mostUsedVoucher = voucherService.getMostUsedVoucher();

        // Thêm thông tin vào model để hiển thị trên trang HTML
        model.addAttribute("mostUsedVoucher", mostUsedVoucher);

        model.addAttribute("history",voucherUsageHistories);
        return "dashboard/voucher/lich-su-dung-voucher";

    }
    @GetMapping("/user-voucher")
    public String userVoucher(Model model,@RequestParam(defaultValue = "0") Integer page){

        return "/website/index/user-voucher.html";

    }
    @GetMapping("/voucher-history")
    public String getVoucherHistory(
        @RequestParam(name = "startDate", required = false) LocalDate startDate,
        @RequestParam(name = "endDate", required = false) LocalDate endDate,
        @RequestParam(name = "searchInput", required = false) String searchInput,
        @PageableDefault(size = 10, sort = "usedDate", direction = Sort.Direction.DESC) Pageable pageable,
        Model model) {

        List<VoucherUsage> filteredHistory = voucherUsageHistoryService.filterAndSearch(startDate, endDate, searchInput);
        model.addAttribute("history", filteredHistory);
        return "dashboard/voucher/lich-su-dung-voucher";
    }
//    @GetMapping("/most-used-voucher")
//    public String getMostUsedVoucher(Model model) {
//        Object[] mostUsedVoucher = voucherService.findMostUsedVoucher();
//        // Truyền dữ liệu tới view thông qua model
//        model.addAttribute("mostUsedVoucher", mostUsedVoucher);
//
//        // Trả về tên view (HTML template) để hiển thị kết quả
//        return "mostUsedVoucher";
//    }

    @GetMapping("/search")
    public String searchVouchers(
        @RequestParam(name = "searchText", required = false) String searchText,
        @RequestParam(name = "status", required = false) String status,
        Model model) {
        // Xử lý lọc và trả về kết quả
        Page<Voucher> filteredVouchers = voucherService.searchVouchers(searchText, status);
        model.addAttribute("vouchers", filteredVouchers);
        return "/dashboard/voucher/voucher";
    }

}
