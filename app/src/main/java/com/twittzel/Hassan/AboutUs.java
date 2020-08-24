package com.twittzel.Hassan;

import android.content.Intent;
import android.net.Uri;

public class AboutUs {

    // تشغيل تويتر
    public static Intent openOurTwitter() {
        String twitter = "https://twitter.com/NahnNawjhak";
        return new Intent(Intent.ACTION_VIEW, Uri.parse(twitter));
    }

    // تشغيل الموقع
    public static Intent openOurWebsite() {
        String website = "https://nahn.tk/";
        return new Intent(Intent.ACTION_VIEW, Uri.parse(website));
    }

    //  تشغيل linkIn
    public static Intent openOurLinkIn() {
        String linkIn = "https://www.linkedin.com/company/%D9%86%D8%AD%D9%86-%D9%86%D9%88%D8%AC%D9%87%D9%83";
        return new Intent(Intent.ACTION_VIEW, Uri.parse(linkIn));

    }

    // فتح البريد الالكتروني
    public static Intent openOurEmail() {
        String email = "admin@nahn.tech";
        Intent it = new Intent(Intent.ACTION_SEND);
        it.putExtra(Intent.EXTRA_EMAIL, email);
        it.setType("message/rfc822");
        return Intent.createChooser(it, "Choose Mail App");
    }

    //نشر تطبيقنا
    public static Intent shareOurApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.twittzel.Hassan");
        sendIntent.setType("text/plain");
        Intent.createChooser(sendIntent, "Share via");
        return sendIntent;
    }
}
