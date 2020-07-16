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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.twittzel.Hassan.adapters.DownLoadTwitterAdapter;
import com.twittzel.Hassan.data.ExtraContext;
import com.twittzel.Hassan.data.api.Api;
import com.twittzel.Hassan.data.api.result.Datum;
import com.twittzel.Hassan.data.api.result.TwitterVideoResult;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadActivity extends AppCompatActivity {
    public static final String KEY_TWT_ID = "KEY_TWT_ID";
    public static final String KEY_QUALITY = "KEY_QUALITY";
    public static final String KEY_SIZE = "KEY_SIZE";

    private TwitterVideoResult twitterVideoResult = new TwitterVideoResult();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        //جعل الشاشة بشكل عمودي
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        //منع دوران الشاشة
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        //عرض الاعلان
        displayGoogleAds();
        //جلب response body
        twitterVideoResult = getIntent().getParcelableExtra(ExtraContext.TWITTER_DATA);
        // تشغيل الرابط على الواجهة
        if (twitterVideoResult == null)return;
        displayUrl(Objects.requireNonNull(twitterVideoResult).getData().get(0).getUrl());
        // وضع البيانات في adapter
        DownLoadTwitterAdapter downLoadTwitterAdapter = new DownLoadTwitterAdapter(twitterVideoResult.getData(), new DownLoadTwitterAdapter.OnDownLoadTwitterItemClickListener() {
            @Override
            public void onItemDownLoadTwitterClicked(Datum datum) {
                displayDownloadVideoAndNotify(datum);
            }
        });
        //تحديد وضع recycler view الى خطي
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // ربط adapter مع recycler view
        recyclerView.setAdapter(downLoadTwitterAdapter);
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
        mAdView.loadAd(new AdRequest.Builder().build());
    }

    //تشغيل الرابط على الواجهة
    @SuppressLint("SetJavaScriptEnabled")
    public void displayUrl(final String vUrl) {
        //   String test = "http://api.nahn.tech/video/?url=https://video.twimg.com/ext_tw_video/1185280178781642760/pu/vid/1280x720/TX5WCv-FxFRLoK4V.mp4";
        WebView webView = findViewById(R.id.videoView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl("http://api.nahn.tech/video/?url=" + vUrl);
                return true;
            }
        });
        webView.loadUrl("http://api.nahn.tech/video/?url=" + vUrl);
    }

    //اوامر التحميل
    public void displayDownloadVideoAndNotify(Datum datum) {

        final Context context = this;
        DownloadManager.Request downLoadRequest = new DownloadManager.Request(Uri.parse(datum.getUrl()));
        downLoadRequest.setTitle(twitterVideoResult.getId());
        downLoadRequest.setDescription(datum.getQuality() + "_" + datum.getSzie());
        downLoadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, getString(R.string.app_name) + twitterVideoResult.getId());

        downLoadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        Objects.requireNonNull(downloadManager).enqueue(downLoadRequest);
        //اوامر اظهار الاشعار
        Data data = new Data.Builder()
                .putString(KEY_TWT_ID, twitterVideoResult.getId())
                .putString(KEY_QUALITY, datum.getQuality())
                .putString(KEY_SIZE, datum.getSzie())
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