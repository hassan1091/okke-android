package com.twittzel.Hassan.ui.Activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.twittzel.Hassan.R;
import com.twittzel.Hassan.WorkForNotify;
import com.twittzel.Hassan.data.ExtraContext;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.view.View.INVISIBLE;

public class DownloadActivity extends AppCompatActivity {
    private MediaController mediaController;
    public static final String KEY_TWT_ID = "KEY_TWT_ID";
    public static final String KEY_QUALITY = "KEY_QUALITY";
    public static final String KEY_SIZE = "KEY_SIZE";
    // فصل مواد نتائج `api`
    private JSONObject jsonArray2, jsonObject0, jsonObject1, jsonObject2;
    private JSONArray data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        Intent intent = getIntent();
        String R1 = intent.getStringExtra(ExtraContext.REQ_BODY);


        String test = "http://api.nahn.tech/video/?url=https://video.twimg.com/ext_tw_video/1185280178781642760/pu/vid/1280x720/TX5WCv-FxFRLoK4V.mp4";
        String t = "http://api.nahn.tech/video/?url=" + test;
        displayUrl(test);

        //متغيرات للمخرجات
        String imageUrl;
        String size;
        String quality;
        final String mTwtId;
        try {

            jsonArray2 = new JSONObject(R1);
            mTwtId = jsonArray2.getString("id");
            data = jsonArray2.getJSONArray("data");
            //بيانات العنصر الاول
            Button oneButton = findViewById(R.id.one);
            if (data.getJSONObject(0) != null) {
                if (oneButton.getVisibility() == INVISIBLE || oneButton.getVisibility() == View.GONE) {
                    oneButton.setVisibility(View.VISIBLE);
                }
                jsonObject0 = data.getJSONObject(0);
                imageUrl = jsonObject0.getString("url");
                size = jsonObject0.getString("szie");
                quality = jsonObject0.getString("Quality");
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
                imageUrl = jsonObject1.getString("url");
                size = jsonObject1.getString("szie");
                quality = jsonObject1.getString("Quality");
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
                imageUrl = jsonObject2.getString("url");
                size = jsonObject2.getString("szie");
                quality = jsonObject2.getString("Quality");
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


    public void displayUrl(final String t) {
        WebView webView = findViewById(R.id.videoView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(t);
                return true;
            }
        });
        webView.loadUrl(t);
        webView.reload();
    }

    public void displayDownloadVideoAndNotify(String url, String twtId, String quality, String size, final View view) {
        //اوامر التحميل
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
        WorkManager.getInstance(context).getWorkInfoByIdLiveData(request.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
            }
        });

    }
}

