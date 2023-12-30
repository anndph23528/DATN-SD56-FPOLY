package com.example.datnsd56.service;

import com.example.datnsd56.entity.Account;
import com.example.datnsd56.entity.Address;
import com.example.datnsd56.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    Page<Address> getAll(Pageable pageable);
    Address detail(Integer id);
    Address add(Address address);
    void update(Address address);
    void delete(Integer id);
    Page<Address> findByEmail(String phone);
    List<Address> get();
    Address findAccountDefaultAddress(Integer AccountId);
//    Address addNewAddress(Account account, Address newAddress);
    List<Address> findAccountAddresses(@Param("accountId") Integer accountId);
    Address setDefaultAddress(Account account, Integer addressId);
    Address addNewAddress(Account account, Address newAddress, boolean setAsDefault);
    Address  findDefaultAddress(@Param("accountId") Integer accountId);

}
