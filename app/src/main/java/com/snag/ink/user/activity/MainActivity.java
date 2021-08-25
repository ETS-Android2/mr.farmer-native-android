package com.snag.ink.user.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.firestore.BuildConfig;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.snag.ink.user.R;
import com.snag.ink.user.adapter.itemadapter;
import com.snag.ink.user.model.Item;
import com.snag.ink.user.model.merchants;
import com.snag.ink.user.utility.Networkchangelistner;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity implements itemadapter.onlistitemclick {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference merchantref;
    private itemadapter adapter;
    Query query;
    private final String merchant_id = "ink";

    Networkchangelistner networkchangelistner = new Networkchangelistner();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Rate app
        AppRate.with(this)
                .setInstallDays(1)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);


        merchantref = db.collection("items");
        query = merchantref.whereEqualTo("itemstatus", true);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(6)
                .setPageSize(6)
                .build();

        FirestorePagingOptions<Item> options = new FirestorePagingOptions.Builder<Item>()
                .setQuery(query, config, Item.class)
                .build();
        adapter = new itemadapter(options, this);
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onitemclick(DocumentSnapshot snapshot, int position) {
        Intent intent = new Intent(MainActivity.this, Additemactivity.class);
        Bundle extras = new Bundle();
        extras.putString("mID", snapshot.getId());
        extras.putString("itemimage", snapshot.getString("itemurl"));
        extras.putString("itemname", snapshot.getString("itemname"));
        extras.putString("itemdescription", snapshot.getString("itemdescription"));
        extras.putInt("itemquantity", Integer.parseInt(String.valueOf(snapshot.getLong("itemquantity"))));
        extras.putInt("itemprice",Integer.parseInt(String.valueOf(snapshot.getLong("itemprice"))));
        intent.putExtras(extras);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                Intent cartintent = new Intent(MainActivity.this, CartActivity.class);
                cartintent.putExtra("mID", merchant_id);
                startActivity(cartintent);
                return true;

            case R.id.account:
                Intent intent1 = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkchangelistner,intentFilter);
        super.onStart();
        adapter.startListening();
    }


    @Override
    protected void onStop() {
        adapter.stopListening();
        unregisterReceiver(networkchangelistner);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}