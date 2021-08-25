package com.snag.ink.user.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.snag.ink.user.R;
import com.snag.ink.user.adapter.merchantadapter;
import com.snag.ink.user.model.Item;
import com.snag.ink.user.roomdb.MainData;
import com.snag.ink.user.roomdb.RoomDB;

import java.util.ArrayList;
import java.util.List;

public class MerchantActivity extends AppCompatActivity implements merchantadapter.onvaluechanged {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference itemsref;
    private merchantadapter adapter;
    Query query;
    RoomDB databasee;

    private TextView btn_viewcart;
    private final String merchant_id = "ink";

    ImageView account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);

        btn_viewcart = findViewById(R.id.viewcart_btn);
        account = findViewById(R.id.account);

        //Intialize database
        databasee = RoomDB.getInstance(this);

        itemsref = db.collection("items");
        query = itemsref.whereEqualTo("itemstatus", true);
        setUpRecyclerView();

        btn_viewcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartintent = new Intent(MerchantActivity.this, CartActivity.class);
                cartintent.putExtra("mID", merchant_id);
                startActivity(cartintent);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accountintent = new Intent(MerchantActivity.this, AccountActivity.class);
                startActivity(accountintent);
            }
        });
    }

    private void setUpRecyclerView() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(7)
                .setPageSize(3)
                .build();

        FirestorePagingOptions<Item> options = new FirestorePagingOptions.Builder<Item>()
                .setQuery(query, config, Item.class)
                .build();
        adapter = new merchantadapter(options, this::onvaluechanged);

        RecyclerView recyclerView = findViewById(R.id.merchant_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    protected void onStop() {
        adapter.stopListening();
        super.onStop();
    }


    @Override
    public void onvaluechanged(DocumentSnapshot snapshot, int position, int newvalue) {

        int value = newvalue;
        String itemname = snapshot.getString("itemname");
        String itemid = snapshot.getId();
        int itemquantity = Integer.parseInt(String.valueOf(snapshot.getLong("itemquantity")));
        int itemprice = Integer.parseInt(String.valueOf(snapshot.getLong("itemprice")));

        MainData data = new MainData();
        data.setItemname(itemname);
        data.setItemprice(itemprice);
        data.setId(itemid);
        data.setItemcount(value);
        data.setItemquantity(itemquantity);
        data.setMerchantid(merchant_id);

        if (value == 0) {
            databasee.mainDao().delete(data);
        } else {
            databasee.mainDao().insert(data);
        }
    }

}
