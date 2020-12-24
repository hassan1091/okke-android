package com.twittzel.Hassan.Activity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.twittzel.Hassan.AppExecutor;
import com.twittzel.Hassan.R;
import com.twittzel.Hassan.WorkForNotify;
import com.twittzel.Hassan.adapters.DownLoadTwitterAdapter;
import com.twittzel.Hassan.data.ExtraContext;
import com.twittzel.Hassan.data.api.result.Datum;
import com.twittzel.Hassan.data.api.result.TwitterVideoResult;
import com.twittzel.Hassan.data.database.DatabaseForAdapter;
import com.twittzel.Hassan.data.database.LastUrlList;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DownloadActivity extends AppCompatActivity {
    public static final String KEY_TWT_ID = "KEY_TWT_ID";
    public static final String KEY_SIZE = "KEY_SIZE";

    private String mUserUrl;

    private TwitterVideoResult twitterVideoResult = new TwitterVideoResult();

    private DatabaseForAdapter databaseForAdapter;

    private long mId;
    private Datum mDatum;

    final BroadcastReceiver onDownloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("DownloadActivity On Receive");
            final DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            Uri uri = downloadManager.getUriForDownloadedFile(mId);
            if (uri != null) {
                System.out.println("DownloadActivity FILE URI " + uri.getPath());
                String filePath = getRealPathFromURI(DownloadActivity.this, uri);
                System.out.println("DownloadActivity FILE PATH " + filePath);
                addUrlToDataBase(mDatum, filePath);
            }
        }
    };

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        databaseForAdapter = DatabaseForAdapter.getsInstance(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        //جعل الشاشة بشكل عمودي
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        //منع دوران الشاشة
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        //عرض الاعلان
        displayGoogleAds();
        //جلب response body
        twitterVideoResult = getIntent().getParcelableExtra(ExtraContext.TWITTER_DATA);
        mUserUrl = getIntent().getStringExtra(ExtraContext.THIS_URL);
        // تشغيل الرابط على الواجهة
        if (twitterVideoResult == null) return;
        displayUrl(Objects.requireNonNull(twitterVideoResult).getData().get(0).getUrl());
        // وضع البيانات في adapter
        DownLoadTwitterAdapter downLoadTwitterAdapter = new DownLoadTwitterAdapter(twitterVideoResult.getData(), new DownLoadTwitterAdapter.OnDownLoadTwitterItemClickListener() {
            @Override
            public void onItemDownLoadTwitterClicked(Datum datum) {
                //ارسال الرابط الى قائمة اخر ما حمل
                displayDownloadVideoAndNotify(datum);
            }
        });
        //تحديد وضع recycler view الى خطي
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // ربط adapter مع recycler view
        recyclerView.setAdapter(downLoadTwitterAdapter);

        registerReceiver(onDownloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(onDownloadCompleteReceiver);
        super.onDestroy();
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
        //String test = "http://api.nahn.tech/video/?url=https://video.twimg.com/ext_tw_video/1185280178781642760/pu/vid/1280x720/TX5WCv-FxFRLoK4V.mp4";
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
    public void displayDownloadVideoAndNotify(final Datum datum) {
        mDatum = datum;
        String fileName = System.currentTimeMillis() + "t.mp4";
        DownloadManager.Request downLoadRequest = new DownloadManager.Request(Uri.parse(datum.getUrl()));
        downLoadRequest.setTitle(fileName);
        downLoadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, fileName);
        downLoadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        final DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            mId = downloadManager.enqueue(downLoadRequest);
        }


        //اوامر اظهار الاشعار
        Data data = new Data.Builder()
                .putString(KEY_TWT_ID, twitterVideoResult.getId())
                .putString(KEY_SIZE, datum.getSzie())
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(WorkForNotify.class)
                .setInputData(data)
                .build();
        WorkManager.getInstance(this).enqueue(request);
    }

    private String getRealPathFromURI(Context mContext, Uri contentURI) {
        String result = null;
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Video.Media.DATA};
            ContentResolver mContentResolver = mContext.getContentResolver();
            String mime = mContentResolver.getType(contentURI);
            cursor = mContentResolver.query(contentURI, proj, null, null, null);
            if (cursor == null) {
                return null;
            } else {
                cursor.moveToFirst();
                int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                if (column_index > -1)
                    result = cursor.getString(column_index);
                cursor.close();
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return result;
    }

    private void addUrlToDataBase(@NotNull Datum datum, final String filePath) {
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                LastUrlList urlList = new LastUrlList();
                urlList.setLastDownLoadUrl(mUserUrl);
                urlList.setFilePath(filePath);
                databaseForAdapter.lastUrlDaw().InsertUrl(urlList);
            }
        });


    }


}
