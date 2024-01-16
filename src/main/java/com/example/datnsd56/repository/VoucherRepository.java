package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Voucher;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher,Integer> {
    List<Voucher> findByExpiryDateTimeBeforeAndActiveIsTrue(LocalDateTime dateTime);
    Optional<Voucher> findByCode(String code);
@Query(value = "SELECT * FROM Voucher  WHERE Active = 1 AND quantity > 0 AND expiryDate > CURRENT_TIMESTAMP",nativeQuery = true)
    List<Voucher> getAllls();
    @Query("SELECT v FROM Voucher v " +
        "WHERE (:searchText IS NULL OR LOWER(v.code) LIKE LOWER(CONCAT('%', :searchText, '%')) OR LOWER(v.description) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
        "AND (:status IS NULL OR (:status = 'true' AND v.active = true) OR (:status = 'false' AND v.active = false))")
    Page<Voucher> searchVouchers(@Param("searchText") String searchText, @Param("status") String status,Pageable pageable);
//    Page<Voucher> findAll(Pageable pageable, Sort createDate);
@Transactional
@Modifying
@Query(value = "update [Voucher] set Active = 0 where id = ?", nativeQuery = true)
void deletects(@Param("id") Integer id);
@Query(value = "SELECT TOP 1 v.id, v.code, COUNT(vu.id) AS usageCount\n" +
    "FROM Voucher v\n" +
    "JOIN VoucherUsage vu ON v.id = vu.voucher_id\n" +
    "WHERE vu.is_used = 1\n" +
    "GROUP BY v.id,v.code\n" +
    "ORDER BY usageCount DESC   ",nativeQuery = true)
List<Object[]> findMostUsedVoucher();
@Query(value = "select *from Voucher where  Code=?1",nativeQuery = true)
List<Voucher> findByCode1(String code);
    boolean existsByCode(String code);

}
