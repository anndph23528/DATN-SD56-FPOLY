package com.example.datnsd56.service;

import com.example.datnsd56.entity.Rate;
import org.springframework.data.domain.Page;

public interface RateService {
    Page<Rate> getAll(Integer page);
    Rate detail(Integer id);
    void add(Rate rate);
    void update(Rate rate);
    void delete(Integer id);

}
