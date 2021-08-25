package com.snag.ink.user.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.snag.ink.user.R;
import com.snag.ink.user.adapter.myorderadapter;
import com.snag.ink.user.model.myorders;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    TextView share, rate,usermail;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mauth = FirebaseAuth.getInstance();
    private final FirebaseUser muser = mauth.getCurrentUser();
    public CollectionReference orderref;
    private myorderadapter orderadapter;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        share = findViewById(R.id.share);
        rate = findViewById(R.id.rate);
        usermail=findViewById(R.id.customer_mail);
        share.setOnClickListener(this);
        rate.setOnClickListener(this);

        usermail.setText(muser.getEmail());
        orderref = db.collection("orders");
        query = orderref.whereEqualTo("customermail",muser.getEmail()).orderBy("ordertime",Query.Direction.DESCENDING);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(6)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<myorders> options = new FirestorePagingOptions.Builder<myorders>()
                .setQuery(query, config, myorders.class)
                .build();
        orderadapter = new myorderadapter(options);

        RecyclerView recyclerView = findViewById(R.id.userorders_rcview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(orderadapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        orderadapter.startListening();
    }


    @Override
    protected void onStop() {
       orderadapter.stopListening();
        super.onStop();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Snackbox");
                    String shareMessage = "\nSnackbox - App \nOrder Snacks& sweets from your locality\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Share Snackbox"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;

            case R.id.rate:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;

        }

    }
}