package com.twittzel.Hassan.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.github.pierry.simpletoast.SimpleToast;
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

import java.util.Objects;

public class DownloadActivity extends AppCompatActivity {
    public static final String KEY_TWT_ID = "KEY_TWT_ID";
    public static final String KEY_SIZE = "KEY_SIZE";

    private String mUserUrl;
    private long mId;

    private TwitterVideoResult twitterVideoResult = new TwitterVideoResult();


    private DatabaseForAdapter databaseForAdapter;

    private Dialog settingsDialogMid;
    private Dialog settingsDialogEnd;

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
                addUrlToDataBase(filePath);
                showFinishedDownload();
            } else {
                if (settingsDialogMid.isShowing()) {
                    SimpleToast.error(DownloadActivity.this, getString(R.string.error_in_downloading));
                    settingsDialogMid.setCanceledOnTouchOutside(true);
                    settingsDialogMid.cancel();
                }

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
        if (twitterVideoResult == null) finish();
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

        setSettingsDialog();

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(onDownloadCompleteReceiver);
        super.onDestroy();
    }

    private void setSettingsDialog() {
        settingsDialogMid = new Dialog(this);
        settingsDialogEnd = new Dialog(this);
        Objects.requireNonNull(settingsDialogMid.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(settingsDialogEnd.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialogMid.setContentView(getLayoutInflater().inflate(R.layout.layout_download_mid, null));
        settingsDialogEnd.setContentView(getLayoutInflater().inflate(R.layout.layout_download_end, null));
        settingsDialogMid.setCanceledOnTouchOutside(false);
        settingsDialogEnd.setCanceledOnTouchOutside(true);
    }


    private void showFinishedDownload() {
        if (settingsDialogMid.isShowing())
            settingsDialogMid.cancel();
        settingsDialogEnd.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (settingsDialogEnd.isShowing())
                    settingsDialogEnd.cancel();
                startActivity(new Intent(DownloadActivity.this, LastDownUrlActivity.class));
                finish();
            }
        }, 500);
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
        String fileName = System.currentTimeMillis() + "t.mp4";
        DownloadManager.Request downLoadRequest = new DownloadManager.Request(Uri.parse(datum.getUrl()));
        downLoadRequest.setTitle(fileName);
        downLoadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, fileName);
        downLoadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        final DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            mId = downloadManager.enqueue(downLoadRequest);
            if (mId != 0) settingsDialogMid.show();
        } else {
            if (settingsDialogMid.isShowing())
                settingsDialogMid.cancel();
        }
        //اوامر اظهار الاشعار
        Data data = new Data.Builder()
                .putString(KEY_TWT_ID, fileName)
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

    private void addUrlToDataBase(final String filePath) {
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
