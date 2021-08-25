package com.snag.ink.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler =  new Handler();
        Runnable myRunnable = new Runnable() {
            public void run() {
                Intent intent=new Intent(SplashActivity.this,Signin.class);
                startActivity(intent);
                finish();
            }
        };
        handler.postDelayed(myRunnable, 500);
    }

}
