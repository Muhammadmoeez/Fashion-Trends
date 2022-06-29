package com.trendily.fashiontrends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;

public class Dashboard extends AppCompatActivity {



    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    private AdView mAdView;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //BANNER ADS

        MobileAds.initialize(this, "ca-app-pub-8888309555375101~3116543692");
        mAdView = findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //InterstitialAd

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-8888309555375101/9559690780");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener()

                                     {
                                         @Override
                                         public void onAdClosed() {
                                             Intent intent = new Intent(Dashboard.this,AddDataInStock.class);
                                             startActivity(intent);
                                             interstitialAd.loadAd(new AdRequest.Builder().build());
                                         }
                                     }
        );



        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        ViewPagerAdapter  adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new FragmentClothes(),"Clothes");
        adapter.AddFragment(new FragmentCurtain(),"Curtain");
        adapter.AddFragment(new FragmentBedSheet(),"BedSheet");



        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.addNewdata:

                if (interstitialAd.isLoaded()){
                    interstitialAd.show();
                }
                else {



                Intent intent = new Intent(this,AddDataInStock.class);
                startActivity(intent);
                break;

                }
        }
        return super.onOptionsItemSelected(item);
    }
}
