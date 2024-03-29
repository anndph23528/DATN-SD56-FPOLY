package com.example.datnsd56.service.impl;

import com.example.datnsd56.dto.MostUsedVoucherDTO;
import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Voucher;
import com.example.datnsd56.entity.VoucherUsage;
import com.example.datnsd56.repository.VoucherRepository;
import com.example.datnsd56.service.VoucherService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class VoucherSeviceImpl implements VoucherService  {
    @Autowired
    private VoucherRepository voucherRepository;

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }
    public Page<Voucher> searchVouchers(String searchText, String status) {
        // Chuyển đổi giá trị String thành
        Pageable pageable=PageRequest.of(0,10);
        boolean parsedStatus = Boolean.parseBoolean(status);
        // Gọi repository để thực hiện truy vấn dựa trên điều kiện
        return voucherRepository.searchVouchers(searchText, String.valueOf(parsedStatus),pageable);
    }
    public void deletess(Integer id) {
        voucherRepository.deletects(id);
    }
    @Override
    public List<Voucher> findByCode1(String code) {
        return voucherRepository.findByCode1(code);
    }

    public Voucher getVoucherById(Integer id) {
        return voucherRepository.findById(id).orElse(null);
    }

    public void saveVoucher(Voucher voucher) {
        voucher.setDiscount(voucher.getDiscount().setScale(2, RoundingMode.HALF_UP));
        voucherRepository.save(voucher);
    }
    public MostUsedVoucherDTO getMostUsedVoucher() {
        List<Object[]> result = voucherRepository.findMostUsedVoucher();
        if (!result.isEmpty()) {
            Object[] data = result.get(0);
            return new MostUsedVoucherDTO((Integer) data[0], (String) data[1], (Integer) data[2]);
        }
        return null;
    }
    public void updateVoucher(Voucher voucher) {
        Optional<Voucher> existingVoucherOptional = voucherRepository.findById(voucher.getId());

        if (existingVoucherOptional.isPresent()) {
            Voucher existingVoucher = existingVoucherOptional.get();

            // Cập nhật chỉ những trường mong muốn
            existingVoucher.setCode(voucher.getCode());
            existingVoucher.setActive(true);
            existingVoucher.setDescription(voucher.getDescription());
            existingVoucher.setExpiryDateTime(voucher.getExpiryDateTime());
            existingVoucher.setDiscount(voucher.getDiscount());
            existingVoucher.setDiscountType(voucher.getDiscountType());
            existingVoucher.setQuantity(voucher.getQuantity());
            existingVoucher.setMinOrderAmount(voucher.getMinOrderAmount());

            voucherRepository.save(existingVoucher);
        }
    }
@Transactional
    public void deleteVoucher(Integer id) {
        voucherRepository.deleteById(id);
    }

    @Transactional
    public void checkAndDeactivateExpiredVouchers() {
        try {
            LocalDateTime currentDateTime = LocalDateTime.now();
            List<Voucher> expiredVouchers = voucherRepository.findByExpiryDateTimeBeforeAndActiveIsTrue(currentDateTime);

            for (Voucher voucher : expiredVouchers) {
                voucher.setActive(false);
            }
            voucherRepository.saveAll(expiredVouchers);
        } catch (Exception e) {
            // Log exception
            e.printStackTrace();
        }
        }


        @Override
    public void updateVoucherStatus( Voucher voucher) {
        // Cập nhật trạng thái của voucher sau khi sử dụng
        voucher.setActive(false); // Đặt trạng thái voucher là không hoạt động sau khi sử dụng

        // Cập nhật vào cơ sở dữ liệu (sử dụng repository hoặc entityManager tùy thuộc vào cách bạn thao tác với cơ sở dữ liệu)
        voucherRepository.save(voucher);
    }


    @Override
    public Page<Voucher> getAll(Integer page) {
        return null;
    }

    @Override
    public Page<Voucher> getAllbypad(Pageable pageable) {
        return null;
    }

    //    @Override
    public Page<Voucher> getAll(Pageable pageable) {
        // Sắp xếp theo trường ngày tạo (createdDate) giảm dần để hiển thị voucher mới nhất lên đầu
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
            Sort.by("startDate").descending());

        return voucherRepository.findAll(sortedPageable);
    }

    @Override
    public Optional<Voucher> findByCode(String code) {
        return voucherRepository.findByCode(code);
    }

    @Override
    public boolean existsByCode(String code) {
        return voucherRepository.existsByCode(code);
    }

    public boolean canUseVoucher(Account account, Voucher voucher) {
        // Kiểm tra xem voucher có được sử dụng bởi tài khoản hay không
        Set<VoucherUsage> voucherUsages = voucher.getVoucherUsages();
        for (VoucherUsage usage : voucherUsages) {
            if (usage.getAccount().equals(account)) {
                return false; // Tài khoản đã sử dụng voucher này
            }
        }
        return true; // Tài khoản chưa sử dụng voucher này
    }

    public Optional<Voucher> findByid(Integer id) {
        return voucherRepository.findById(id);
    }
    @Override
    public List<Voucher> get() {
        return null;
    }

    @Override
    public Voucher detail(Integer id) {
        return null;
    }

    @Override
    public Voucher add(Voucher voucher) {
        return null;
    }

    @Override
    public void update(Voucher voucher) {

    }

    @Override
    public void delete(Integer id) {

    }
@Transactional
    @Override
    public List<Voucher> getAllls() {
        return voucherRepository.getAllls();
    }
}
