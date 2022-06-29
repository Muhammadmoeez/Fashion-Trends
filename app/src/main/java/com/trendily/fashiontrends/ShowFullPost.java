package com.trendily.fashiontrends;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ShowFullPost extends AppCompatActivity {

    TextView ShowFullShopName;
    TextView ShowFullPostCode;
    TextView ShowFullPrice;
    TextView ShowFullPhone;
    TextView ShowFullCity;
    TextView ShowFullDescription;
    ImageView ShowFullImageView;



    private static final int REQUEST_CALL =1;


    String TempShowFullShopName, TempShowFullPostCode, TempShowFullPostCity, TempShowFullPrice, TempShowFullPhone, TempShowFullDescription, TempShowFullImageView;

    ImageView backArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_full_post);

        Intent intent = getIntent();

        TempShowFullShopName = intent.getStringExtra("FullShopName");
        TempShowFullPostCode = intent.getStringExtra("FullPostCode");
        TempShowFullPrice = intent.getStringExtra("FullPostPrice");
        TempShowFullPhone = intent.getStringExtra("FullPostPhone");
        TempShowFullDescription = intent.getStringExtra("FullPostDescription");
        TempShowFullPostCity = intent.getStringExtra("FullPostCity");
        TempShowFullImageView = intent.getStringExtra("FullImageURL");




        ShowFullShopName = (TextView) findViewById(R.id.FullShopName);
        ShowFullPostCode = (TextView) findViewById(R.id.FullPostCode);
        ShowFullPrice = (TextView) findViewById(R.id.FullPrice);
        ShowFullPhone = (TextView) findViewById(R.id.FullPhone);
        ShowFullCity = (TextView) findViewById(R.id.FullPostCity);
        ShowFullDescription = (TextView) findViewById(R.id.FullDescription);
        ShowFullImageView = (ImageView) findViewById(R.id.FullImageView);

        backArrow = (ImageView) findViewById(R.id.arrowBack);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        if (!TempShowFullPostCode.isEmpty() &&
                !TempShowFullPrice.isEmpty() &&
                !TempShowFullPhone.isEmpty() &&
                !TempShowFullDescription.isEmpty() &&
                !TempShowFullImageView.isEmpty() &&
                !TempShowFullPostCity.isEmpty() &&
                !TempShowFullShopName.isEmpty()) {

            ShowFullShopName.setText(TempShowFullShopName);
            ShowFullPostCode.setText(TempShowFullPostCode);
            ShowFullPrice.setText(TempShowFullPrice);
            ShowFullPhone.setText(TempShowFullPhone);
            ShowFullDescription.setText(TempShowFullDescription);
            ShowFullCity.setText(TempShowFullPostCity);

            Picasso.get().load(TempShowFullImageView).into(ShowFullImageView);


        }


        ShowFullPhone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                makePhoneCall();
            }
        });


    }

    private void makePhoneCall() {

        String Number = ShowFullPhone.getText().toString();

        if (Number.trim().length()>0)
        {
            if (ContextCompat.checkSelfPermission(ShowFullPost.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)

            {
                ActivityCompat.requestPermissions(ShowFullPost.this,new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            }
            else
            {

                String dial = "tel:" + Number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        }
        else
        {
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL)
        {
            if (grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                makePhoneCall();
            }
            else
            {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
