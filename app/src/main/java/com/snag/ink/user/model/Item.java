package com.snag.ink.user.model;

import androidx.annotation.Keep;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
@Keep

public class Item {
    private String itemname;
    private String itemurl;
    private String itemdescription;
    private Boolean itemstatus;
    private int itemprice;
    private int itemquantity;
    private int ordersmade;


    public Item() {
    }

    public Item(String itemname, String itemurl, String itemdescription, Boolean itemstatus, int itemprice, int itemquantity, int ordersmade) {
        this.itemname = itemname;
        this.itemurl = itemurl;
        this.itemdescription = itemdescription;
        this.itemstatus = itemstatus;
        this.itemprice = itemprice;
        this.itemquantity = itemquantity;
        this.ordersmade = ordersmade;
    }

    public String getItemdescription() {
        return itemdescription;
    }

    public Boolean getItemstatus() {
        return itemstatus;
    }

    public String getItemname() {
        return itemname;
    }

    public String getItemurl() {
        return itemurl;
    }

    public int getItemprice() {
        return itemprice;
    }

    public int getItemquantity() {
        return itemquantity;
    }

    public int getOrdersmade() { return ordersmade;  }
}



