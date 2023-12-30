package com.example.datnsd56.service.impl;

import com.example.datnsd56.entity.Rate;
import com.example.datnsd56.repository.RateRepository;
import com.example.datnsd56.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RateSeviceImpl implements RateService {
    @Autowired
    private RateRepository rateRepository;

    @Override
    public Page<Rate> getAll(Integer page) {
        Pageable pageable = PageRequest.of(page, 5);
        return rateRepository.findAll(pageable);
    }

    @Override
    public Rate detail(Integer id) {
        Rate rate = rateRepository.findById(id).get();
        return rate;
    }

    @Override
    public void add(Rate rate) {
        rateRepository.save(rate);
    }

    @Override
    public void update(Rate rate) {
        rateRepository.save(rate);

    }

    @Override
    public void delete(Integer id) {
        Rate rate = detail(id);
        rateRepository.delete(rate);
    }
}
