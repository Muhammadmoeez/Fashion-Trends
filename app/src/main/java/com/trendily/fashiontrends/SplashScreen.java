package com.trendily.fashiontrends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    TextView FashionTrends;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FashionTrends = (TextView) findViewById(R.id.fashionTrends);

        Animation animation  = AnimationUtils.loadAnimation(this,R.anim.my_anim);

        FashionTrends.startAnimation(animation);

        Thread thread = new Thread(){
            @Override
            public void run(){
                try{
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                    startActivity(intent);
                    finish();
                }

                catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();
    }
}
