package com.snag.ink.user.model;


import androidx.annotation.Keep;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
@Keep
public class myorders {
    private String merchantname;
    private String orderstatus;
    private String orderdetails;
    @ServerTimestamp
    Date ordertime;
    private String ordervalue;

    //extra properties
    private String customerno;
    private String customermail;
    private String customername;
    private String merchantmail;
    private String orderdeliverylocation;
    private String orderdeliverylocationlink;
    private String merchantlocation;
    private String razorpayid;

    public myorders() {
    }

    public myorders(String merchantname, String orderstatus, String orderdetails, Date ordertime, String ordervalue, String customerno, String customermail, String customername, String merchantmail, String orderdeliverylocation, String orderdeliverylocationlink, String merchantlocation, String razorpayid) {
        this.merchantname = merchantname;
        this.orderstatus = orderstatus;
        this.orderdetails = orderdetails;
        this.ordertime = ordertime;
        this.ordervalue = ordervalue;
        this.customerno = customerno;
        this.customermail = customermail;
        this.customername = customername;
        this.merchantmail = merchantmail;
        this.orderdeliverylocation = orderdeliverylocation;
        this.orderdeliverylocationlink = orderdeliverylocationlink;
        this.merchantlocation = merchantlocation;
        this.razorpayid = razorpayid;
    }

    public String getMerchantname() {
        return merchantname;
    }

    public void setMerchantname(String merchantname) {
        this.merchantname = merchantname;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getOrderdetails() {
        return orderdetails;
    }

    public void setOrderdetails(String orderdetails) {
        this.orderdetails = orderdetails;
    }

    public Date getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(Date ordertime) {
        this.ordertime = ordertime;
    }

    public String getOrdervalue() {
        return ordervalue;
    }

    public void setOrdervalue(String ordervalue) {
        this.ordervalue = ordervalue;
    }

    public String getCustomerno() {
        return customerno;
    }

    public void setCustomerno(String customerno) {
        this.customerno = customerno;
    }

    public String getCustomermail() {
        return customermail;
    }

    public void setCustomermail(String customermail) {
        this.customermail = customermail;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getMerchantmail() {
        return merchantmail;
    }

    public void setMerchantmail(String merchantmail) {
        this.merchantmail = merchantmail;
    }

    public String getOrderdeliverylocation() {
        return orderdeliverylocation;
    }

    public void setOrderdeliverylocation(String orderdeliverylocation) {
        this.orderdeliverylocation = orderdeliverylocation;
    }

    public String getOrderdeliverylocationlink() {
        return orderdeliverylocationlink;
    }

    public void setOrderdeliverylocationlink(String orderdeliverylocationlink) {
        this.orderdeliverylocationlink = orderdeliverylocationlink;
    }

    public String getMerchantlocation() {
        return merchantlocation;
    }

    public void setMerchantlocation(String merchantlocation) {
        this.merchantlocation = merchantlocation;
    }

    public String getRazorpayid() {
        return razorpayid;
    }

    public void setRazorpayid(String razorpayid) {
        this.razorpayid = razorpayid;
    }
}
