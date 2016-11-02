package com.lionsquare.canisovismanager;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    private static final int REQUEST_RESULT = 0;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        StartAnimations();


    }


    private void StartAnimations() {
        // Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_login);
        //anim.reset();
        TextView iv = (TextView) findViewById(R.id.title);
        //iv.clearAnimation();
        //iv.startAnimation(anim);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            public void run() {
                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.zoom_entrada, R.anim.zoom_salida);
                finish();
            }
        }, 1000);
    }

}
