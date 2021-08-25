package com.snag.ink.user.roomdb;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "table_name")
public class MainData implements Serializable {

    //Id column
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;

    //Itemname column
    @ColumnInfo(name = "itemname")
    private String itemname;

    //Itemprice
    @ColumnInfo(name = "itemprice")
    private int itemprice;

    //Itemcount
    @ColumnInfo(name = "itemcount")
    private int itemcount;

    //Itemquantity
    @ColumnInfo(name = "itemquantity")
    private int itemquantity;

    //Merchantid
    @ColumnInfo(name = "merchantid")
    private String merchantid;


    public String getMerchantid() {
        return merchantid;
    }

    public void setMerchantid(String merchantid) {
        this.merchantid = merchantid;
    }

    public int getItemquantity() {
        return itemquantity;
    }

    public void setItemquantity(int itemquantity) {
        this.itemquantity = itemquantity;
    }

    public int getItemcount() {
        return itemcount;
    }

    public void setItemcount(int itemcount) {
        this.itemcount = itemcount;
    }

    public int getItemprice() {
        return itemprice;
    }

    public void setItemprice(int itemprice) {
        this.itemprice = itemprice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

}
