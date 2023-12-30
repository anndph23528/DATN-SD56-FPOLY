package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Address;
import com.example.datnsd56.repository.AddressRepository;
import com.example.datnsd56.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressSeviceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Page<Address> getAll(Pageable pageable) {
//        Pageable pageable = PageRequest.of(page, 5);
        return addressRepository.findAll(pageable);
    }

    @Override
    public Address detail(Integer id) {
        Address address = addressRepository.findById(id).orElse(null);
        return address;
    }
   @Override
    public Address addNewAddress(Account account, Address newAddress, boolean setAsDefault) {
        newAddress.setAccount(account);

        if (setAsDefault) {
            // Nếu muốn đặt địa chỉ mới làm địa chỉ mặc định, thì đặt mọi địa chỉ khác thành không phải mặc định
            account.getAddress().forEach(address -> address.setDefaultAddress(false));
            newAddress.setDefaultAddress(true);
        }

        // Lưu newAddress vào cơ sở dữ liệu
        Address savedAddress = addressRepository.save(newAddress);

        // Nếu cần lưu lại thay đổi trong Account, hãy cập nhật và lưu lại Account
        // accountRepository.save(account);

        return savedAddress;
    }

    @Override
    public Address findDefaultAddress(Integer accountId) {
        return addressRepository.findDefaultAddress(accountId);
    }


    @Override
    public List<Address> findAccountAddresses(Integer accountId) {
        return addressRepository.findAccountAddresses(accountId);
    }
//    public Address setDefaultAddress(Account account, Long addressId) {
//        // Xóa địa chỉ mặc định trước đó
//        Optional<Address> currentDefaultAddressOptional = addressRepository.findByAccountAndDefaultAddress(account, true);
//        if (currentDefaultAddressOptional.isPresent()) {
//            currentDefaultAddressOptional.get().setDefaultAddress(false);
//            addressRepository.save(currentDefaultAddressOptional.get());
//        }
//
//        // Cập nhật địa chỉ mới thành mặc định
//        Optional<Address> newDefaultAddressOptional = addressRepository.findById(addressId);
//        if (newDefaultAddressOptional.isPresent()) {
//            newDefaultAddressOptional.get().setDefaultAddress(true);
//            return addressRepository.save(newDefaultAddressOptional.get());
//        }
//        return null;
//    }
    public Optional<Address> findByAccountAndDefaultAddress(Account account, boolean defaultAddress) {
        return addressRepository.findOne(
            Example.of(
                Address.builder()
                    .account(account)
                    .defaultAddress(defaultAddress)
                    .build()
            )
        );
    }


    public Address setDefaultAddress(Account account, Integer addressId) {
        List<Address> addresses = account.getAddress();

        // Đặt mọi địa chỉ khác thành không phải mặc định
        addresses.forEach(address -> address.setDefaultAddress(false));

        // Tìm địa chỉ cần đặt làm mặc định và đặt mặc định
        Address defaultAddress = addresses.stream()
            .filter(address -> address.getId().equals(addressId))
            .findFirst()
            .orElse(null);

        if (defaultAddress != null) {
            defaultAddress.setDefaultAddress(true);
            // Lưu thay đổi vào cơ sở dữ liệu (nếu cần)
            addressRepository.save(defaultAddress);
        }

        // Nếu cần lưu lại thay đổi trong Account, hãy cập nhật và lưu lại Account
        // accountRepository.save(account);
        return  null;
    }


    @Override
    public Address add(Address address) {

            // Lấy thông tin Account từ đối tượng Address
            Account account = address.getAccount();
            // Gán Acount cho đối tượng Address nếu chưa có
            if (account != null) {
                address.setAccount(account);
            }

            // Thực hiện thêm địa chỉ vào cơ sở dữ liệu
            // ...



        return  addressRepository.save(address);
    }

    @Override
    public void update(Address address) {
        addressRepository.save(address);

    }

    @Override
    public void delete(Integer id) {
        Address address = detail(id);
        addressRepository.delete(address);
    }
    public Page<Address> findByEmail(String phone) {
        Pageable page=PageRequest.of(0,5);
        Page<Address> list=addressRepository.findAddressesByPhone(phone,page);
        return list;
    }

    @Override
    public List<Address> get() {
        return addressRepository.findAll();
    }

    @Override
    public Address findAccountDefaultAddress(Integer AccountId) {
        return addressRepository.findAccountDefaultAddress(AccountId);
    }




}
