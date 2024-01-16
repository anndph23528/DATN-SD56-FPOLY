package com.example.datnsd56.dto;

public class MostUsedVoucherDTO {
    private Integer id;
    private String code;
    private Integer usageCount;

    public MostUsedVoucherDTO(Integer id, String code, Integer usageCount) {
        this.id = id;
        this.code = code;
        this.usageCount = usageCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }
// Getters and setters
}
