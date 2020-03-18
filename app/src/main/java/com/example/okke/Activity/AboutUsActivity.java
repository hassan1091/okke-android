package com.example.okke.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.okke.R;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
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
