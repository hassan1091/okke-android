package com.twittzel.Hassan.ui.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.twittzel.Hassan.R;
import com.twittzel.Hassan.data.ExtraContext;
import com.twittzel.Hassan.data.database.DatabaseForAdapter;
import com.twittzel.Hassan.data.database.LastUrlList;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    //العنصر الذي تم اختياره من قائمة القوائم الاسبقة
    public static final int CODE_LDU = 114;
    private DatabaseForAdapter databaseForAdapter;
    private String mUserUrl;
    private EditText editText;
    private ProgressBar mProgressBar;
    public InterstitialAd mInterstitialAd;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseForAdapter = DatabaseForAdapter.getsInstance(this);
        editText = findViewById(R.id.E1);
        mProgressBar = findViewById(R.id.progressBar);
        //جعل الشاشة بشكل عمودي
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        //منع دوران الشاشة
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        //استقبال البيانات من تويتر
        displayGetFromTWT();
        // طلب صلاحية التحميل
        displayCheckSelfPermission();
        //تشغيل اعلا قوقل
        displayGoogleAds();
    }

    //جلب البيانات اذا تم مشاركة الفيديو من تويتر
    private void displayGetFromTWT() {
        if (getUrlFromTwt() != "") {
            editText.setText(getUrlFromTwt());
        }
    }

    //اعلان قوقل
    private void displayGoogleAds() {
        AdView mAdView = findViewById(R.id.adView);
        //مهم للاعلانات
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        //اعلان البانر
        mAdView.loadAd(adRequest);
        //اعلان الخيلالي
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4200825572816870/1251621628");
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //اظهار الاعلان الخلالي
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    //التأكد من الاتضال بالانترنت
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //طلب صلاحية التحميل
    public void displayCheckSelfPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(this
                , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    //موقع زر حمل
    public void bTForDownload(View view) {
        //التأكد من وجود الانترنت
        if (!editText.getText().toString().isEmpty()) {
            if (isNetworkAvailable()) {
                mProgressBar.setVisibility(View.VISIBLE);
                //تشغيل API
                displayApiTwitte();
            } else {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(this, getResources().getString(R.string.You_do_not_have_internet_please_try_again_later), LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.Add_the_link), Toast.LENGTH_LONG).show();
        }
    }

    // تفعيل api
    public void displayApiTwitte() {
        //رابط الاتصال ب API **ملاحظة يوجد رابط تجربيبي الان*https://twitter.com/cybersec2030/status/1185280330086965248*
        //تعريف النص المدخل من المستخدم
        mUserUrl = editText.getText().toString();
        //اضافة الرابط الى api
        final String url = "http://api.nahn.tech/twitter/?url=" + mUserUrl;
        Request request = new Request.Builder().url(url).build();
        OkHttpClient Client = new OkHttpClient();
        Client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mProgressBar.setVisibility(View.GONE);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String R1 = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //ارسال الرابط الى قائمة اخر ما حمل
                            new AsynM().execute();
                            //فتح صفحة التحميل
                            startActivity(new Intent(MainActivity.this, com.twittzel.Hassan.ui.Activity.DownloadActivity.class)
                                    .putExtra(ExtraContext.REQ_BODY, R1));
                            mProgressBar.setVisibility(View.GONE);
                        }

                    });

                } else {
                    mProgressBar.setVisibility(View.GONE);
                    Log.e("the respons ", "is not work ");
                }
            }
        });
    }

    // استقبال الرابط من تويتر
    public String getUrlFromTwt() {
        String theTwtUrl = "";
        String receivedText;
        //get the received intent
        Intent receivedIntent = getIntent();
        String receivedAction = receivedIntent.getAction();
        //find out what we are dealing with
        String receivedType = receivedIntent.getType();
        //make sure it's an action and type we can handle
        if (receivedAction.equals(Intent.ACTION_SEND)) {
            //content is being shared
            if (receivedType.startsWith("text/")) {
                //handle sent text
                receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
                if (receivedText != null) {
                    //set the text
                    theTwtUrl = receivedText;
                }

            }
        } else if (receivedAction.equals(Intent.ACTION_MAIN)) {
            //app has been launched directly, not from share list
        }

        return theTwtUrl;
    }

    // الانتقال الى صفحة LastDownUrlActivity
    public void displayToLR(View view) {
        startActivityForResult(new Intent(MainActivity.this, com.twittzel.Hassan.ui.Activity.LastDownUrlActivity.class), CODE_LDU);
    }

    //استقبال الرابط الذي تم ارساله من واجهة قائمة الروابط السابقة
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //تعين قيمة editTest بالرابط المختار
        if (requestCode == CODE_LDU) {
            if (resultCode == RESULT_OK && data != null) {
                editText.setText(data.getStringExtra(ExtraContext.THIS_URL));
            }
        }
    }

    // حاليا فقط الذهاب الى AboutActivity
    public void Open_menu(View view) {
        startActivity(new Intent(MainActivity.this, com.twittzel.Hassan.ui.Activity.AboutUsActivity.class));
    }

    //تغيير من الوضع الداكن الى الوضع البيعي والعكس
    public void openDownloadAc(View view) {

        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
    }

    //اضافة الرابط عن طريق Thread
    public class AsynM extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            LastUrlList urlList = new LastUrlList();
            urlList.setmLRDownList(mUserUrl);
            databaseForAdapter.lastUrlDaw().InsertUrl(urlList);
            return null;
        }
    }

}