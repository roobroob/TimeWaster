package com.example.roope.myapplication;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Locale;

public class ResultPage extends AppCompatActivity {

    public static float lukema;
    public static float lastScore;
    String tuloste = "0.00";
    double randomnumber;
    boolean pressed = false;
    Intent gameintent;
    boolean achievement = false;

    // public static InterstitialAd mInterstitialAd;
    InterstitialAd mInterstitialAd = MainActivity.mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);
        randomnumber = Math.random();

        /*
        if (mInterstitialAd == null || !mInterstitialAd.isLoaded()) {
            // mInterstitialAd = new InterstitialAd(this);
            Log.i("ad", "before ad handler");
            adhandler.postDelayed(new AdInit(), 200);
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    Log.i("ad_is_closed", "ad_is_closed");
                    RequestAd();
                    gameintent = new Intent(getBaseContext(), TheGameActivity.class);
                    startActivity(gameintent);
                }
            });

            // handler.postDelayed(new AdRunnable(), 200);
        }
        */

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                RequestAd();
                gameintent = new Intent(getBaseContext(), TheGameActivity.class);
                startActivity(gameintent);
            }
        });

        Intent intent = getIntent();
        lukema = intent.getFloatExtra(TheGameActivity.lukema, 0);
        lastScore = intent.getFloatExtra(TheGameActivity.lastScore, 0);

        TextView textView = (TextView) findViewById(R.id.textViewResult);
        TextView textComment = (TextView) findViewById(R.id.textViewComment);

        // test values!
        /*if (randomnumber < 0.01) {
            lukema = (float) 2010;
        }
        else {
            lukema = (float) 2000;
        }*/
        lukema = lukema / 1000;
        tuloste = String.format(Locale.ROOT, "%.2f", lukema);
        textView.setText("You just wasted\n" + tuloste + "\nseconds.\n");

        setBaseComment();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();


        float longest_time = sharedPref.getFloat(getString(R.string.longest_time), 0);
        if (lukema > longest_time) {
            editor.putFloat(getString(R.string.longest_time), lukema);
            editor.apply();
        }

        float total_time = sharedPref.getFloat("total_time", 0);
        float new_total_time = total_time + lukema;
        editor.putFloat("total_time", new_total_time);
        editor.apply();

        Log.i("random", "num " + randomnumber);

        if (SeeIfNice()) {
            Log.i("result", "nice");
        }

        if (SeeIfAGoodTry()) {
            Log.i("result", "A good try");
        }

        if (randomnumber < 0.15
                && Math.abs(lukema - 10.00) > 0.5
                && Math.abs(lukema - 5.00) > 0.3
                && Math.abs(lukema - 2.00) > 0.2
                && Math.abs(lukema - 1.00) > 0.15) {
            textComment.setText("Uhm... Well done?\n");
        }

        if (Math.abs(lukema - 4.20) < 0.005) {
            textComment.setText("Blaze it.\n");
        }

        if (Math.abs(lukema - 6.66) < 0.005) {
            textComment.setText("The Number of the Beast.\n");
        }
    }

    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            pressed = true;
        }
    };

    final Handler adhandler = new Handler();
    public class AdRunnable implements Runnable {
        @Override
        public void run() {
            Log.i("RunnableAd", "starts loading");
            RequestAd();
        }
    }

    public class AdInit implements Runnable {
        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            RequestAd();
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            handler.postDelayed(mLongPressed, 300);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            handler.removeCallbacks(mLongPressed);
            if (!pressed) {
                if (mInterstitialAd.isLoaded() && randomnumber < 0.2) {
                    Log.i("Result", "ad_is_shown");
                    mInterstitialAd.show();
                } else {
                    gameintent = new Intent(this, TheGameActivity.class);
                    startActivity(gameintent);
                }
            } else {
                pressed = false;
            }
        }
        return false;
    }

    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(this);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void RequestAd() {
        AdRequest adRequest = new AdRequest.Builder().
                addTestDevice("903A5866C702A00465B80F0796528DEA").build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void setBaseComment() {
        TextView textComment = (TextView) findViewById(R.id.textViewComment);
        if (randomnumber < 0.88) {
            textComment.setText("Great job.\n");
        } else if (randomnumber < 0.98) {
            textComment.setText("But why?\n");
        } else if (randomnumber < 0.99) {
            textComment.setText("Take a break.\nRate this app on Play Store.\n");
        } else {
            textComment.setText("Take a break.\nClick an ad.\n");
        }
    }

    public boolean SeeIfAGoodTry() {
        TextView textComment = (TextView) findViewById(R.id.textViewComment);
        if (!achievement && Math.abs(lukema - 2.00) <= 0.015) {
            textComment.setText("Oh, so close.\n");
            return true;
        } else if (!achievement && Math.abs(lukema - 1.00) <= 0.015) {
            textComment.setText("Oh, so close.\n");
            return true;
        } else if (!achievement && Math.abs(lukema - 5.00) <= 0.015) {
            textComment.setText("Oh, so close.\n");
            return true;
        } else if (!achievement && Math.abs(lukema - 10.00) <= 0.015) {
            textComment.setText("Oh, so close.\n");
            return true;
        }
        return false;
    }

    public boolean SeeIfNice() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        TextView textComment = (TextView) findViewById(R.id.textViewComment);

        if(Math.abs(lukema-1.00)<=0.005)
        {
            textComment.setText("Nice.\n");
            achievement = true;
            editor.putBoolean("one_sec", true);
            if (Math.abs(lastScore - 1.00) <= 0.005) {
                editor.putBoolean("two_x_one_sec", true);
            }
            editor.apply();
        }

        if(Math.abs(lukema-2.00)<=0.005)

        {
            editor.putBoolean("two_sec", true);
            achievement = true;
            textComment.setText("Nice.\n");
            int two_count = sharedPref.getInt("two_count", 0);
            editor.putInt("two_count", two_count + 1);
            if (Math.abs(lastScore - 2.00) <= 0.005) {
                editor.putBoolean("two_x_two_sec", true);
            }
            editor.apply();
        }

        if(Math.abs(lukema-5.00)<=0.005)

        {
            editor.putBoolean("five_sec", true);
            achievement = true;
            textComment.setText("Nice.\n");
            if (Math.abs(lastScore - 5.00) <= 0.005) {
                editor.putBoolean("two_x_five_sec", true);
            }
            editor.apply();
        }

        if(Math.abs(lukema-10.00)<=0.005)

        {
            editor.putBoolean("ten_sec", true);
            achievement = true;
            textComment.setText("Nice.\n");
            editor.apply();
        }
            return achievement;
    }


}