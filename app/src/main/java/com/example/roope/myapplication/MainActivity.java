package com.example.roope.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity {

    public static InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest adRequest = new AdRequest.Builder().
                addTestDevice("903A5866C702A00465B80F0796528DEA").build();
        mInterstitialAd.loadAd(adRequest);

    }

    boolean pressed = false;

    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {
            Intent intent = new Intent(getBaseContext(), HighScoreActivity.class);
            startActivity(intent);
            pressed = true;
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event){

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            handler.postDelayed(mLongPressed, 300);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            handler.removeCallbacks(mLongPressed);
            if (!pressed) {
                Intent intent = new Intent(this, TheGameActivity.class);
                startActivity(intent);
            }
            else {
                pressed = false;
            }
        }
        return false;
    }

}
