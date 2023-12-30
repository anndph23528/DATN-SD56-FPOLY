package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Voucher;
import com.example.datnsd56.entity.VoucherUsage;
import com.example.datnsd56.entity.VoucherUsageHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherUsageHistoryRepository extends JpaRepository<VoucherUsageHistory,Integer> {
//    Optional<VoucherUsage> findTopByAccountIdAndIsUsedFalseAndVoucher_ActiveTrueOrderByUsedDateDesc(Integer accountId);
//    Optional<VoucherUsage> findByAccountAndVoucherAndIsUsedTrue(Account account, Voucher voucher);
//
//    @Query(value = "SELECT * FROM VoucherUsage WHERE account_id = ?1", nativeQuery = true)
//    List<VoucherUsage> findVoucherUsagesByAccount(Integer accountId);
@Query(value = "SELECT * FROM VoucherUsageHistory  ORDER BY used_date DESC",nativeQuery = true)
List<VoucherUsageHistory> findAllOrderByUsedDateDesc();
    @Query(value = "SELECT * FROM VoucherUsageHistory  ORDER BY used_date DESC",nativeQuery = true)
Page<VoucherUsageHistory> getall(Pageable pageable);
Page<VoucherUsageHistory> findAll(Pageable pageable);
}
