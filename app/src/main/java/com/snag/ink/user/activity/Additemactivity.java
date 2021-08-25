package com.snag.ink.user.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.snag.ink.user.R;
import com.snag.ink.user.roomdb.MainData;
import com.snag.ink.user.roomdb.RoomDB;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;

public class Additemactivity extends AppCompatActivity {

    ImageView itemimage;
    TextView itemname,itemdescription,itemprice,itemquantity,viewcart;
    ElegantNumberButton elegantNumberButton;
    private final String merchant_id = "ink";
    RoomDB databasee;

    String iname,id;
    int iquantity,iprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additemactivity);

        itemimage = findViewById(R.id.itemimageview);
        itemname = findViewById(R.id.itemname);
        itemdescription = findViewById(R.id.itemdescription);
        itemprice = findViewById(R.id.itemprice);
        itemquantity = findViewById(R.id.itemquantity);
        elegantNumberButton = findViewById(R.id.item_counter);
        viewcart = findViewById(R.id.viewcart);

        //Intialize database
        databasee = RoomDB.getInstance(this);

        Intent intent = getIntent();
        iname = intent.getStringExtra("itemname");
        id = intent.getStringExtra("mID");
        iquantity = intent.getIntExtra("itemquantity",0);
        iprice = intent.getIntExtra("itemprice",0);

        itemname.setText(iname);
        itemdescription.setText(intent.getStringExtra("itemdescription"));
        itemprice.setText(MessageFormat.format("INR  {0}", iprice));
        itemquantity.setText(MessageFormat.format("{0}  gms", iquantity));
        Picasso.get()
                .load(intent.getStringExtra("itemimage"))
                .fit()
                .centerInside()
                .into(itemimage);


        elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                int value = newValue;

                MainData data = new MainData();
                data.setItemname(iname);
                data.setItemprice(iprice);
                data.setId(id);
                data.setItemcount(value);
                data.setItemquantity(iquantity);
                data.setMerchantid(merchant_id);

                if (value == 0) {
                    databasee.mainDao().delete(data);
                } else {
                    databasee.mainDao().insert(data);
                }
            }
        });

        viewcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartintent = new Intent(Additemactivity.this, CartActivity.class);
                cartintent.putExtra("mID", merchant_id);
                startActivity(cartintent);
                finish();
            }
        });

    }
}