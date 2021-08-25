package com.snag.ink.user.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.snag.ink.user.R;
import com.snag.ink.user.adapter.Mainadapter;
import com.snag.ink.user.model.myorders;
import com.snag.ink.user.roomdb.MainData;
import com.snag.ink.user.roomdb.RoomDB;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CartActivity extends AppCompatActivity implements PaymentResultListener {
    TextView deleteall;
    RecyclerView recyclerView;

    List<MainData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;
    Mainadapter mainadapter;
    TextView totalamount;
    TextView btn_totalorder;
    Button dlocation;

    int totalvalue;

    //DeliveryDetails
    private TextInputEditText contactname,contactno,deliveryaddress;
    TextView deliverycoo;


    //shared preference to save user data
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String CONTACTNAME = "contactname";
    public static final String CONTACTNO = "contactno";
    public static final String DELIVERYADDRESS = "deliveryaddress";
    public static final String DELIVERYLINK = "deliverylink";

    private String cname;
    private String cnumber;
    private String dlink;
    private String address;

    private static String places = "location_tags";
    private FirebaseRemoteConfig remote_tag_Config = FirebaseRemoteConfig.getInstance();
    public String[] location_tags;
    public int deliverycharges;
    private TextView dcharges;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference orderref;
    public DocumentReference merchantref;
    private final FirebaseAuth mauth = FirebaseAuth.getInstance();
    private final FirebaseUser muser = mauth.getCurrentUser();

    String merchantid, merchantname, merchantmail, merchantlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //To load payment gateway without hassle
        Checkout.preload(getApplicationContext());

        dcharges = findViewById(R.id.text_dxcharges);
        TextView locations = findViewById(R.id.locations_dialog);
        totalamount = findViewById(R.id.Totalamount);
        btn_totalorder = findViewById(R.id.orderbutton1);
        deleteall = findViewById(R.id.reset_btn);
        recyclerView = findViewById(R.id.cart_rcview);
        dlocation = findViewById(R.id.btn_deliverylocation);
        deliverycoo = findViewById(R.id.coordinates);

        totalamount.setText("0");

        contactname = findViewById(R.id.text_input_contact_name);
        contactno = findViewById(R.id.text_input_contact_no);
        deliveryaddress = findViewById(R.id.text_input_address);
        //deliverylink = findViewById(R.id.text_input_maplink);

        //To get verion from reality config
        remote_tag_Config.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder().build());
        HashMap<String, Object> values = new HashMap<>();
        values.put(places, "Srirangam,Thillainagar,Chatram BusStand,Woraiyur");
        values.put("delivery_charges", "5");
        remote_tag_Config.setDefaultsAsync(values);
        final Task<Void> fetch = remote_tag_Config.fetch(BuildConfig.DEBUG ? 0 : TimeUnit.HOURS.toHours(48));
        fetch.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                remote_tag_Config.fetchAndActivate();
                String temp_tags = remote_tag_Config.getString(places);
                deliverycharges = Integer.parseInt(String.valueOf(remote_tag_Config.getLong("delivery_charges")));
                location_tags = temp_tags.replace("[", "").replace("]", "").split(",");
            }
        });

        loadDeliveryDetails();
        updateDeliveryDetails();

        //Intialize database
        database = RoomDB.getInstance(this);
        //Store database value in datalist

        ExampleThread thread = new ExampleThread();
        thread.start();

        //Delete all data
        deleteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearroomdatabase();
                finish();
            }
        });

        btn_totalorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cname = String.valueOf(contactname.getText());
                cnumber = String.valueOf(contactno.getText());
                dlink = String.valueOf(deliverycoo.getText());
                address = String.valueOf(deliveryaddress.getText());

                if(cname.isEmpty() | cnumber.isEmpty() | dlink.isEmpty() | address.isEmpty()){
                    Toast.makeText(CartActivity.this, "Please enter delivery details", Toast.LENGTH_SHORT).show();
                }else{
                    saveDeliveryDetails();
                }
            }
        });

        locations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog2 = new AlertDialog.Builder(CartActivity.this)
                        .setTitle("Delivery Locations")
                        .setItems(location_tags, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }

                        }).create();
                alertDialog2.show();
            }
        });

        dlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, MapsActivity.class);
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                dlink=data.getStringExtra("coordinates");
                String locname = data.getStringExtra("locname");
                deliverycoo.setText(String.format("%s\n%s", locname, dlink));
            }
            if (resultCode==RESULT_CANCELED){
                Toast.makeText(this, "Please select delivery location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ExampleThread extends Thread {
        @Override
        public void run() {
            try {
                dataList = database.mainDao().getall();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makelist();
                    }
                });

            } catch (Exception e) {
                Log.w("e", e);
            }
        }
    }

    private void makelist() {
        if (dataList.size() == 0) {
            Toast.makeText(this, "cart is empty", Toast.LENGTH_SHORT).show();
        } else {
            getmerchantdetails();
            deliverycharges = Integer.parseInt(String.valueOf(remote_tag_Config.getLong("delivery_charges")));
            dcharges.setText(String.format("Delivery charges Rs%s  will be added", String.valueOf(deliverycharges)));
            btn_totalorder.setVisibility(View.VISIBLE);
            totalvalue = database.mainDao().gettotalcost() + deliverycharges;

            totalamount.setText(String.format("Rs %s", String.valueOf(totalvalue)));
            btn_totalorder.setText("Confirm Order");
        }

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mainadapter = new Mainadapter(CartActivity.this, dataList);
        recyclerView.setAdapter(mainadapter);
    }

    public void getmerchantdetails() {
        String merchantdetails = new Gson().toJson(dataList);
        try {
            JSONArray jsonArray = new JSONArray(merchantdetails);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            merchantid = jsonObject.getString("merchantid");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        merchantref = db.collection("merchants").document(merchantid);
        merchantref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        merchantname = document.getString("merchantname");
                        merchantmail = document.getString("merchantmail");
                        merchantlocation = document.getString("merchantlocation");
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });

    }

    public void clearroomdatabase() {
        database.mainDao().reset(dataList);
        dataList.clear();
        dataList.addAll(database.mainDao().getall());
        mainadapter.notifyDataSetChanged();
        totalamount.setText("0");
        btn_totalorder.setVisibility(View.INVISIBLE);
    }

    public void saveDeliveryDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CONTACTNAME, cname);
        editor.putString(CONTACTNO, cnumber);
        editor.putString(DELIVERYLINK, dlink);
        editor.putString(DELIVERYADDRESS,address);
        editor.apply();
        startPayment();
    }

    public void loadDeliveryDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        cname = sharedPreferences.getString(CONTACTNAME, "");
        cnumber = sharedPreferences.getString(CONTACTNO, "");
        dlink = sharedPreferences.getString(DELIVERYLINK, "");
        address = sharedPreferences.getString(DELIVERYADDRESS,"");
    }

    public void updateDeliveryDetails() {
        contactname.setText(cname);
        contactno.setText(cnumber);
        deliverycoo.setText(dlink);
        deliveryaddress.setText(address);
    }

    public void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_aFajm4o35EwpRk");
        checkout.setImage(R.drawable.snackbox);
        final Activity activity = this;
        int totalvalue = database.mainDao().gettotalcost() + deliverycharges;

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Snackbox");
            options.put("description", "Your Order");
            options.put("currency", "INR");
            options.put("amount", totalvalue * 100); // Amount always in paise ( 5000 = 50rs)
            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e("Razor_pay", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        final Date orderdate = new Date();
        int totalvalue = database.mainDao().gettotalcost() + deliverycharges;
        String orderdetails = new Gson().toJson(dataList);

        AlertDialog alertDialog2 = new AlertDialog.Builder(CartActivity.this)
                .setMessage("Hold on, We are placing your order.")
                .create();
        alertDialog2.show();

        alertDialog2.setCanceledOnTouchOutside(false);

        orderref = db.collection("orders");
        orderref.add(new myorders(merchantname, "Order placed", orderdetails, orderdate, String.valueOf(totalvalue), cnumber, muser.getEmail(),cname, merchantmail, "address", dlink, merchantlocation, s))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Handler handler = new Handler();
                        Runnable myRunnable = new Runnable() {
                            public void run() {
                                clearroomdatabase();
                                Intent intent = new Intent(CartActivity.this, AccountActivity.class);
                                startActivity(intent);
                                alertDialog2.dismiss();
                                finish();
                            }
                        };
                        handler.postDelayed(myRunnable, 3000);
                    }
                });
    }


    @Override
    public void onPaymentError(int i, String s) {
        final Date orderdate = new Date();
        int totalvalue = database.mainDao().gettotalcost() + deliverycharges;
        String orderdetails = new Gson().toJson(dataList);

        orderref = db.collection("failedorders");
        orderref.add(new myorders(merchantname, "Payment failed", orderdetails, orderdate, String.valueOf(totalvalue), cnumber, muser.getEmail(),cname, merchantmail, "address", dlink, merchantlocation, s));
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}