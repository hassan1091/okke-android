package com.twittzel.Hassan.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.twittzel.Hassan.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AboutUsActivity extends AppCompatActivity {
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        mAdView = findViewById(R.id.adView);
        //مهم للاعلانات
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //اعلان قوقل
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void displayOpenTwitter(View view) {
        String twitter = "https://twitter.com/NahnNawjhak";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter));
        startActivity(intent);
    }

    public void displayOpenOurWebsite(View view) {
        String website = "https://nahn.tech";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
        startActivity(intent);
    }

    public void displayOpenLinkIn(View view) {
        String linkIn = "https://www.linkedin.com/company/%D9%86%D8%AD%D9%86-%D9%86%D9%88%D8%AC%D9%87%D9%83";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkIn));
        startActivity(intent);
    }

    public void displayOpenOurEmail(View view) {
        String email= "admin@nahn.tech";
        Intent it = new Intent(Intent.ACTION_SEND);
        it.putExtra(Intent.EXTRA_EMAIL,email );
        it.setType("message/rfc822");
        startActivity(Intent.createChooser(it,"Choose Mail App"));
    }
}
