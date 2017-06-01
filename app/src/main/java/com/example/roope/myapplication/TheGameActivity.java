package com.example.roope.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class TheGameActivity extends AppCompatActivity {

    public final static String lukema = "com.roope.myapplication.lukema";
    public final static String lastScore = "com.roope.myapplication.lastScore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_game);
        Log.i("game_start", "game_start");
    }

    public boolean pressed = false;
    long starttime = -1L;
    long endtime = -1L;
    long time = -1L;


    public boolean onTouchEvent(MotionEvent event){
        int action = MotionEventCompat.getActionMasked(event);
        time = System.currentTimeMillis();
        TextView textViewGame = (TextView) findViewById(R.id.textViewGame);
        textViewGame.setTextColor(Color.GREEN);
        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                starttime = time;
                break;
            case (MotionEvent.ACTION_UP) :
                endtime = time;
                Intent intent = new Intent(this, ResultPage.class);
                float numero = endtime - starttime;
                intent.putExtra(lukema, numero);
                intent.putExtra(lastScore, ResultPage.lukema);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
    }
        return pressed;
    }

    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(this);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

}
