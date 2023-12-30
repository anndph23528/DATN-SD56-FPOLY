package com.example.datnsd56.ViewModel;

import com.example.datnsd56.entity.Color;
import com.example.datnsd56.entity.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Blob;

public interface ViewCart {
     String getName();
     BigDecimal getPrice();
     Color getColorId();
     Size getSizeId();
     Blob getAnh();



}
