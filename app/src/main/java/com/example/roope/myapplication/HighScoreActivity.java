package com.example.roope.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class HighScoreActivity extends AppCompatActivity {

    String lastscore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        SharedPreferences sharedPref = getDefaultSharedPreferences(this);

        float highScore = sharedPref.getFloat(getString(R.string.longest_time), 0);
        DecimalFormat newFormat = new DecimalFormat("0.00");
        String score = String.valueOf(newFormat.format(highScore));
        //TextView textView = (TextView) findViewById(R.id.textViewHigh);
        //textView.setText("High score is: " + score);

        float TotalTime = sharedPref.getFloat("total_time", 0);
        String totaltime = String.format(Locale.ROOT, "%.2f", TotalTime);
        TextView total = (TextView) findViewById(R.id.textViewTotal);
        total.setText("Total time wasted:\n" + totaltime + " seconds");

        int TwoCount = sharedPref.getInt("two_count", 0);
        TextView twocountView = (TextView) findViewById(R.id.textViewTwoCount);
        if (TwoCount >= 100) {
            twocountView.setText(TwoCount + " x 2.00");
            twocountView.setTextColor(Color.GREEN);
        }
        else {
            twocountView.setText(TwoCount + "/100");
        }

        /*
        float LastScore = ResultPage.lukema;
        //lastscore = String.valueOf(newFormat.format(LastScore));
        lastscore = String.format(Locale.ROOT, "%.2f", LastScore);
        TextView textView2 = (TextView) findViewById(R.id.textViewLast);
        textView2.setText("Last score was: " + lastscore);
        */
        double ex = SeeIfExactly();
        TextView exactly = (TextView) findViewById(R.id.textViewExactly);
        // exactly.setTextColor(Color.RED);
        exactly.setText("Waste Exactly", TextView.BufferType.SPANNABLE);
        Spannable span = (Spannable) exactly.getText();
        span.setSpan(new ForegroundColorSpan(Color.GREEN), 0, (int) Math.floor((ex/4)*"Waste Exactly".length()),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        double row = SeeIfTwoInARow();
        TextView twoinarow = (TextView) findViewById(R.id.textViewInARow);
        //twoinarow.setTextColor(Color.RED);
        twoinarow.setText("Waste Consecutively", TextView.BufferType.SPANNABLE);
        Spannable span2 = (Spannable) twoinarow.getText();
        span2.setSpan(new ForegroundColorSpan(Color.GREEN), 0, (int) Math.floor((row/3)*"Waste Consecutively".length()),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        TextView twoheader = (TextView) findViewById(R.id.textViewTwoHeader);
        //twoheader.setTextColor(Color.RED);
        twoheader.setText("Waste 2.00 100 times", TextView.BufferType.SPANNABLE);
        if (TwoCount < 100) {
            Spannable span3 = (Spannable) twoheader.getText();
            span3.setSpan(new ForegroundColorSpan(Color.GREEN), 0, (int) Math.floor((((double) TwoCount) / 100)
                    * "Waste 2.00 100 times".length()), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        else {
            twoheader.setTextColor(Color.GREEN);
        }
    }

    public int SeeIfExactly() {
        SharedPreferences sharedPref = getDefaultSharedPreferences(this);
        int num = 0;

        boolean one_sec = sharedPref.getBoolean("one_sec", false);
        if (one_sec) {
            TextView textOne = (TextView) findViewById(R.id.textView1sec);
            textOne.setTextColor(Color.GREEN);
            num = num + 1;
        }
        boolean two_sec = sharedPref.getBoolean("two_sec", false);
        if (two_sec) {
            TextView textView3 = (TextView) findViewById(R.id.textView2sec);
            textView3.setTextColor(Color.GREEN);
            num = num + 1;
        }
        boolean five_sec = sharedPref.getBoolean("five_sec", false);
        if (five_sec) {
            TextView textView4 = (TextView) findViewById(R.id.textView5sec);
            textView4.setTextColor(Color.GREEN);
            num = num + 1;
        }
        boolean ten_sec = sharedPref.getBoolean("ten_sec", false);
        if (ten_sec) {
            TextView textView5 = (TextView) findViewById(R.id.textView10sec);
            textView5.setTextColor(Color.GREEN);
            num = num + 1;
        }
        return num;
    }

    public int SeeIfTwoInARow() {
        SharedPreferences sharedPref = getDefaultSharedPreferences(this);
        int num = 0;
        boolean two_x_one_sec = sharedPref.getBoolean("two_x_one_sec", false);
        if (two_x_one_sec) {
            TextView textViewOne = (TextView) findViewById(R.id.textView2x1sec);
            textViewOne.setTextColor(Color.GREEN);
            num = num + 1;
        }
        boolean two_x_two_sec = sharedPref.getBoolean("two_x_two_sec", false);
        if (two_x_two_sec) {
            TextView textView10 = (TextView) findViewById(R.id.textView2x2sec);
            textView10.setTextColor(Color.GREEN);
            num = num + 1;
        }
        boolean two_x_five_sec = sharedPref.getBoolean("two_x_five_sec", false);
        if (two_x_five_sec) {
            TextView textView11 = (TextView) findViewById(R.id.textView2x5sec);
            textView11.setTextColor(Color.GREEN);
            num = num + 1;
        }
        return num;
    }

    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        return false;
    }

    public void resetScores(View view) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
