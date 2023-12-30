package com.example.datnsd56.service;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Voucher;
import com.example.datnsd56.entity.VoucherUsage;

import java.util.List;

public interface VoucherUsageService {
    void save(VoucherUsage voucherUsage);
    void updateVoucherStatusUsage( VoucherUsage voucherUsage);
//    public boolean isVoucherUsed(String voucherCode) ;/

    boolean isVoucherUsed(String username, String voucherCode);

    List<VoucherUsage> findVisibleVoucherUsagesByAccount(Integer accountId);
//    List<VoucherUsage> findByAccountAndVouchers(Account account, String selectedVoucherCode);
    boolean existsByAccountAndVoucherAndIsUsed(Account account, Voucher voucher, boolean isUsed);


    void saveVoucherForAccount(Voucher voucher, Account account);
    List<VoucherUsage> findVoucherUsagesByAccount(Integer accountId);

}
