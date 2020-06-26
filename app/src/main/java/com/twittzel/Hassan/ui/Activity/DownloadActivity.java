package com.twittzel.Hassan.ui.Activity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.twittzel.Hassan.R;
import com.twittzel.Hassan.WorkForNotify;
import com.twittzel.Hassan.data.ExtraContext;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.view.View.INVISIBLE;

public class DownloadActivity extends AppCompatActivity {
    public static final String KEY_TWT_ID = "KEY_TWT_ID";
    public static final String KEY_QUALITY = "KEY_QUALITY";
    public static final String KEY_SIZE = "KEY_SIZE";
    // فصل مواد نتائج `api`
    private JSONObject jsonObject00, jsonObject0, jsonObject1, jsonObject2;
    private JSONArray data;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        //جعل الشاشة بشكل عمودي
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        //منع دوران الشاشة
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        //عرض الاعلان
        displayGoogleAds();
        //جلب response body
        String R1 = getIntent().getStringExtra(ExtraContext.REQ_BODY);
        //متغيرات للمخرجات
        String imageUrl;
        String size;
        String quality;
        //جلب اسماء apis object
        String urlApi = getResources().getString(R.string.url_api);
        String szieApi = getResources().getString(R.string.szie_api);
        String QualityApi = getResources().getString(R.string.Quality_api);
        final String mTwtId;

        try {
            jsonObject00 = new JSONObject(R1);
            mTwtId = jsonObject00.getString(getResources().getString(R.string.id_api));
            data = jsonObject00.getJSONArray(getResources().getString(R.string.data_api));
            //بيانات العنصر الاول
            Button oneButton = findViewById(R.id.one);
            if (data.getJSONObject(0) != null) {
                if (oneButton.getVisibility() == INVISIBLE || oneButton.getVisibility() == View.GONE) {
                    oneButton.setVisibility(View.VISIBLE);
                }
                jsonObject0 = data.getJSONObject(0);
                imageUrl = jsonObject0.getString(urlApi);

                size = jsonObject0.getString(szieApi);
                quality = jsonObject0.getString(QualityApi);
                final String finalImageUrl = imageUrl;
                final String finalQuality = quality;
                final String finalSize = size;
                //ارسال بينات  العنصر الاول
                oneButton.setText("    " + finalQuality + "          " + finalSize);
                oneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        displayDownloadVideoAndNotify(finalImageUrl, finalQuality, finalSize, mTwtId, view);
                    }
                });
                //عرض الفيديو على web view
                displayUrl(imageUrl);
            } else {
                oneButton.setVisibility(View.GONE);
            }
            //بيانات العنصر الثاني
            Button towButton = findViewById(R.id.tow);
            if (data.getJSONObject(1) != null) {
                if (towButton.getVisibility() == INVISIBLE || towButton.getVisibility() == View.GONE) {
                    towButton.setVisibility(View.VISIBLE);
                }
                jsonObject1 = data.getJSONObject(1);
                imageUrl = jsonObject1.getString(urlApi);
                size = jsonObject1.getString(szieApi);
                quality = jsonObject1.getString(QualityApi);
                final String finalImageUrl1 = imageUrl;
                final String finalQuality1 = quality;
                final String finalSize1 = size;
                //ارسال بينات  العنصر الثاني
                towButton.setText("    " + finalQuality1 + "          " + finalSize1);
                towButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        displayDownloadVideoAndNotify(finalImageUrl1, finalQuality1, finalSize1, mTwtId, view);
                    }
                });
            } else {
                towButton.setVisibility(View.GONE);
            }
            //بيانات العنصر الثالث
            Button threeButton = findViewById(R.id.three);
            if (data.getJSONObject(2) != null) {
                if (towButton.getVisibility() == INVISIBLE || towButton.getVisibility() == View.GONE) {
                    towButton.setVisibility(View.VISIBLE);
                }
                jsonObject2 = data.getJSONObject(2);
                imageUrl = jsonObject2.getString(urlApi);
                size = jsonObject2.getString(szieApi);
                quality = jsonObject2.getString(QualityApi);
                final String finalImageUrl2 = imageUrl;
                final String finalQuality2 = quality;
                final String finalSize2 = size;
                //ارسال بينات  العنصر الثالث
                threeButton.setText("    " + finalQuality2 + "          " + finalSize2);
                threeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        displayDownloadVideoAndNotify(finalImageUrl2, finalQuality2, finalSize2, mTwtId, view);
                    }
                });
            } else {
                threeButton.setVisibility(View.GONE);
            }
        } catch (Exception x) {
        }
    }

    //تشغيل اعلانات قوقل بانر.
    private void displayGoogleAds() {
        AdView mAdView = findViewById(R.id.adView2);
        //مهم للاعلانات
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    //تشغيل الرابط على الواجهة
    public void displayUrl(final String vidoeUrl) {
        //   String test = "http://api.nahn.tech/video/?url=https://video.twimg.com/ext_tw_video/1185280178781642760/pu/vid/1280x720/TX5WCv-FxFRLoK4V.mp4";
        WebView webView = findViewById(R.id.videoView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl("http://api.nahn.tech/video/?url=" + vidoeUrl);
                return true;
            }
        });
        webView.loadUrl("http://api.nahn.tech/video/?url=" + vidoeUrl);
    }

    //اوامر التحميل
    public void displayDownloadVideoAndNotify(String url, String twtId, String quality, String size, final View view) {

        final Context context = this;
        DownloadManager.Request downLoadRequest = new DownloadManager.Request(Uri.parse(url));
        downLoadRequest.setTitle("download: " + "TwtVideo: " + twtId);
        downLoadRequest.setDescription(quality + " / " + size);
        downLoadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, "TwtVideo: " + twtId);
        downLoadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(downLoadRequest);
        //اوامر اظهار الاشعار
        Data data = new Data.Builder()
                .putString(KEY_TWT_ID, twtId)
                .putString(KEY_QUALITY, quality)
                .putString(KEY_SIZE, size)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(WorkForNotify.class)
                .setInputData(data)
                .build();
        WorkManager.getInstance(context).enqueue(request);

    }

    //تشغيل صفحة من نحن
    public void displayToAboutUs(View view) {
        startActivity(new Intent(this, com.twittzel.Hassan.ui.Activity.AboutUsActivity.class));
    }
}