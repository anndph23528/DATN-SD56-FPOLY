package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Voucher;
import com.example.datnsd56.entity.VoucherUsage;
import com.example.datnsd56.entity.VoucherUsageHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface VoucherUsageRepository extends JpaRepository<VoucherUsage,Integer> {
    Optional<VoucherUsage> findTopByAccountIdAndIsUsedFalseAndVoucher_ActiveTrueOrderByUsedDateDesc(Integer accountId);
    Optional<VoucherUsage> findByAccountAndVoucherAndIsUsedTrue(Account account, Voucher voucher);
//    VoucherUsage findByAccountAndVoucher(Account account, Voucher voucher);
    List<VoucherUsage> findByAccountAndVoucher(Account account, Voucher voucher);
    boolean existsByAccountAndVoucherAndIsUsed(Account account, Voucher voucher, boolean isUsed);
//    List<VoucherUsage> findByAccountAndVouchers(Account account, String selectedVoucherCode);
//    List<VoucherUsage> findByAccountAndIsUsed(Account account, Boolean isUsed);
    List<VoucherUsage> findByVoucherAndIsUsed(Voucher voucher, Boolean isUsed);
    @Query(value= " SELECT * FROM VoucherUsage  WHERE isVisible = true",nativeQuery = true)
    List<VoucherUsage> findByIsVisibleTrue();
    @Query("SELECT vu FROM VoucherUsage vu " +
        "JOIN vu.voucher v " +
        "WHERE vu.account.id = ?1 " +
        "AND vu.isVisible = true " +
        "AND v.expiryDateTime > CURRENT_TIMESTAMP")
    List<VoucherUsage> findVisibleVoucherUsagesByAccount( Integer accountId);
    @Query(value = "SELECT * FROM VoucherUsage WHERE account_id = ?1", nativeQuery = true)
    List<VoucherUsage> findVoucherUsagesByAccount(Integer accountId);

}
