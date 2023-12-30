package com.example.datnsd56.controller;

import com.example.datnsd56.entity.Rate;
import com.example.datnsd56.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rate")
public class RateController {
    @Autowired
    private RateService rateService;

    @GetMapping("getAll")
    public List<Rate> getAll(@RequestParam(value = "page",defaultValue = "0")Integer page){
        List<Rate> list=rateService.getAll(page).getContent();
        return list;
    }
    @GetMapping("detail/{id}")
    public Rate detail(@PathVariable("id") Integer id){
       Rate rate= rateService.detail(id);
        return rate;
    }
    @PostMapping("add")
    public void add(@RequestBody Rate rate){
        rateService.add(rate);
    }
    @PutMapping("update/{id}")
    public void update(@RequestBody Rate rate){
        rateService.update(rate);
    }
@DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") Integer id){
        rateService.delete(id);
}
}
