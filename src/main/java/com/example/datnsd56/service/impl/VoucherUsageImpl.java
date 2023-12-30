package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Voucher;
import com.example.datnsd56.entity.VoucherUsage;
import com.example.datnsd56.repository.AccountRepository;
import com.example.datnsd56.repository.VoucherRepository;
import com.example.datnsd56.repository.VoucherUsageRepository;
import com.example.datnsd56.service.AccountService;
import com.example.datnsd56.service.VoucherService;
import com.example.datnsd56.service.VoucherUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherUsageImpl implements VoucherUsageService {
    @Autowired
    private VoucherUsageRepository voucherUsageRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private VoucherService voucherService;
    @Override
    public void save(VoucherUsage voucherUsage) {
        voucherUsageRepository.save(voucherUsage);
    }

    @Override
    public void updateVoucherStatusUsage(VoucherUsage voucherUsage) {
        voucherUsage.setIsUsed(true);
        voucherUsageRepository.save(voucherUsage);
    }




    @Override
    public boolean isVoucherUsed(String username, String voucherCode) {
        Optional<Account> accountOptional = accountService.finByName(username);
        Optional<Voucher> voucherOptional = voucherService.findByCode(voucherCode);

        if (accountOptional.isPresent() && voucherOptional.isPresent()) {
            Account account = accountOptional.get();
            Voucher voucher = voucherOptional.get();

            return voucherUsageRepository.existsByAccountAndVoucherAndIsUsed(account, voucher, true);
        }

        return false;
    }


    @Override
    public List<VoucherUsage> findVisibleVoucherUsagesByAccount(Integer accountId) {
        return voucherUsageRepository.findVisibleVoucherUsagesByAccount(accountId);
    }

//    @Override
//    public List<VoucherUsage> findByAccountAndVouchers(Account account, String selectedVoucherCode) {
//        return voucherUsageRepository.findByAccountAndVouchers(account,selectedVoucherCode);
//    }

    @Override
    public boolean existsByAccountAndVoucherAndIsUsed(Account account, Voucher voucher, boolean isUsed) {
        return voucherUsageRepository.existsByAccountAndVoucherAndIsUsed(account,voucher,isUsed);
    }


    @Override
    @Transactional
    public void saveVoucherForAccount(Voucher voucher, Account account) {
        VoucherUsage voucherUsage = new VoucherUsage();
        voucherUsage.setAccount(account);
        voucherUsage.setVoucher(voucher);
        voucherUsage.setUsedDate(null);  // Chưa sử dụng
        voucherUsage.setIsUsed(false);   // Chưa sử dụng
        voucherUsage.setIsVisible(true);   // Chưa sử dụng

        voucherUsageRepository.save(voucherUsage);
    }

    @Override
    public    List<VoucherUsage> findVoucherUsagesByAccount(Integer accountId)
    {
        return voucherUsageRepository.findVoucherUsagesByAccount(accountId);

    }


    // Trong Controller hoặc Service khi người dùng ấn "Lưu"
// accountId là id của tài khoản đăng nhập
// voucherId là id của voucher được lưu
    public void saveVoucherForAccount(Integer accountId, Integer voucherId) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow();

        // Tạo một bản ghi mới trong bảng VoucherUsage
        VoucherUsage voucherUsage = new VoucherUsage();
        voucherUsage.setAccount(account);
        voucherUsage.setVoucher(voucher);
        voucherUsage.setUsedDate(LocalDateTime.now());
        voucherUsage.setIsUsed(false); // Trạng thái chưa sử dụng

        // Thêm voucher vào danh sách voucher của tài khoản
        account.getVoucherUsages().add(voucherUsage);

        // Cập nhật thông tin trong cơ sở dữ liệu
        accountRepository.save(account);
    }


}
