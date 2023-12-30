package com.example.datnsd56.repository;

import com.example.datnsd56.entity.Voucher;
import org.springframework.beans.factory.annotation.Autowired;
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
@Query(value = "SELECT * FROM Voucher  WHERE Active = 1",nativeQuery = true)
    List<Voucher> getAllls();
}
