package com.example.datnsd56.responsi;

import java.util.Date;

public interface OrdersCustomer {
    Integer getid();
    String getcode();
    String getfullname();
    Date getcreate_date();
    Integer getquantity();
    Double gettotal();
    Integer getorder_status();
}
