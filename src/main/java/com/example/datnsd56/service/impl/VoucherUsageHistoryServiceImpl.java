package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.VoucherUsageHistory;
import com.example.datnsd56.repository.VoucherUsageHistoryRepository;
import com.example.datnsd56.service.VoucherUsageHistoryService;
import com.example.datnsd56.service.VoucherUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoucherUsageHistoryServiceImpl implements VoucherUsageHistoryService {
    @Autowired
    private VoucherUsageHistoryRepository voucherUsageHistoryRepository;

    @Override
    public List<VoucherUsageHistory> findAllOrderByUsedDateDesc() {
        return voucherUsageHistoryRepository.findAllOrderByUsedDateDesc();
    }

    @Override
    public Page<VoucherUsageHistory> getall(Pageable pageable) {
        return voucherUsageHistoryRepository.getall(pageable);
    }

    @Override
    public Page<VoucherUsageHistory> filterAndSearch(LocalDate startDate, LocalDate endDate, String searchInput, Pageable pageable) {
        Page<VoucherUsageHistory> historyList = voucherUsageHistoryRepository.findAll(pageable);

        // Lọc theo ngày
        if (startDate != null && endDate != null) {
            historyList = historyList
                .stream()
                .filter(history -> !history.getUsedDate().isBefore(startDate.atStartOfDay())
                    && !history.getUsedDate().isAfter(endDate.atTime(23, 59, 59)))
                .collect(Collectors.collectingAndThen(Collectors.toList(), PageImpl::new));
        }

        // Tìm kiếm theo mã voucher hoặc tên người dùng
        if (searchInput != null && !searchInput.isEmpty()) {
            String lowerCaseSearchInput = searchInput.toLowerCase();
            historyList = historyList
                .stream()
                .filter(history ->
                    history.getVoucher().getCode().toLowerCase().contains(lowerCaseSearchInput)
                        || history.getAccount().getName().toLowerCase().contains(lowerCaseSearchInput))
                .collect(Collectors.collectingAndThen(Collectors.toList(), PageImpl::new));
        }

        return historyList;
    }

    public Page<VoucherUsageHistory> getAll(Pageable pageable) {
        return voucherUsageHistoryRepository.findAll(pageable);
    }


}

