package com.twittzel.Hassan.ui.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.twittzel.Hassan.AboutUs;
import com.twittzel.Hassan.AppExecutor;
import com.twittzel.Hassan.R;
import com.twittzel.Hassan.data.ExtraContext;
import com.twittzel.Hassan.data.api.Api;
import com.twittzel.Hassan.data.api.result.TwitterVideoResult;
import com.twittzel.Hassan.data.database.DatabaseForAdapter;
import com.twittzel.Hassan.data.database.LastUrlList;

import java.util.Objects;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //العنصر الذي تم اختياره من قائمة القوائم الاسبقة
    public static final int CODE_LDU = 114;
    private String mUserUrl;

    private EditText editText;
    private ProgressBar mProgressBar;

    private DatabaseForAdapter databaseForAdapter;
    public InterstitialAd mInterstitialAd;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        databaseForAdapter = DatabaseForAdapter.getsInstance(this);
        editText = findViewById(R.id.E1);
        mProgressBar = findViewById(R.id.progressBar);

        toolbar = findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
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
        //
        displayNavigationView();
    }

    private void displayNavigationView() {
        //
        setSupportActionBar(toolbar);
        //
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    //جلب البيانات اذا تم مشاركة الفيديو من تويتر
    private void displayGetFromTWT() {
        if (!TextUtils.isEmpty(getUrlFromTwt())) editText.setText(getUrlFromTwt());
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
        if (mInterstitialAd.isLoaded()) mInterstitialAd.show();
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
        if (editText.getText().toString().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.Add_the_link), Toast.LENGTH_LONG).show();
            return;
        }
        if (isNetworkAvailable()) {
            mProgressBar.setVisibility(View.VISIBLE);
            //تشغيل API
            displayTwitterApi();
        } else {
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(this, getResources().getString(R.string.You_do_not_have_internet_please_try_again_later), LENGTH_SHORT).show();
        }

    }

    // تفعيل api
    private void displayTwitterApi() {
        mUserUrl = String.valueOf(editText.getText());
        new Api().getTwitterVideoResult(mUserUrl).enqueue(new retrofit2.Callback<TwitterVideoResult>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<TwitterVideoResult> call, @NonNull final retrofit2.Response<TwitterVideoResult> response) {
                if (response.isSuccessful()) {
                    //ارسال الرابط الى قائمة اخر ما حمل
                    addUrlToDataBase();
                    //فتح صفحة التحميل
                    if (Objects.requireNonNull(response.body()).getStatusCode() != 200) return;
                    Log.e("response ", "isSuccessful");
                    AppExecutor.getInstance().getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, DownloadActivity.class);
                            intent.putExtra(ExtraContext.TWITTER_DATA, response.body());
                            startActivity(intent);
                        }
                    });
                } else Log.e("response ", "!isSuccessful");
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<TwitterVideoResult> call, @NonNull Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                t.printStackTrace();
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
        assert receivedAction != null;
        if (receivedAction.equals(Intent.ACTION_SEND)) {
            //content is being shared
            assert receivedType != null;
            if (receivedType.startsWith("text/")) {
                //handle sent text
                receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
                //set the text
                if (receivedText != null) theTwtUrl = receivedText;
            }
        }
        return theTwtUrl;
    }

    // الانتقال الى صفحة LastDownUrlActivity
    public void displayToLR(View view) {
        startActivityForResult(new Intent(MainActivity.this, LastDownUrlActivity.class), CODE_LDU);
    }

    //استقبال الرابط الذي تم ارساله من واجهة قائمة الروابط السابقة
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //تعين قيمة editTest بالرابط المختار
        if (requestCode == CODE_LDU) if (resultCode == RESULT_OK && data != null)
            editText.setText(data.getStringExtra(ExtraContext.THIS_URL));
    }

    //تغيير من الوضع الداكن الى الوضع البيعي والعكس
    public void dayDark() {
        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    //فتح صفحة التطبيق على قوقل بلاي
    private void rateUs() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void addUrlToDataBase() {
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                LastUrlList urlList = new LastUrlList();
                urlList.setmLRDownList(mUserUrl);
                databaseForAdapter.lastUrlDaw().InsertUrl(urlList);
            }
        });
    }

    //<a target="_blank" href="https://icons8.com/icons/set/paste">Paste icon</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
    public void paste(View view) {
        if (!getPastedTextFromClipboard().equals(""))
            editText.setText(getPastedTextFromClipboard());
    }

    private CharSequence getPastedTextFromClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard == null) return "";
        ClipData clip = clipboard.getPrimaryClip();
        if (clip == null) return "";
        ClipData.Item item = clip.getItemAt(0);
        if (item == null) return "";
        CharSequence textToPaste = item.getText();
        if (textToPaste == null) return "";
        return textToPaste;
    }

    ///Icons made by <a href="https://www.flaticon.com/authors/srip" title="srip">srip</a> from <a href="https://www.flaticon.com/" title="Flaticon"> www.flaticon.com</a>
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case R.id.action_day:
                dayDark();
                break;
            case R.id.action_our_twitter:
                startActivity(AboutUs.openOurTwitter());
                break;
            case R.id.action_our_linked_in:
                startActivity(AboutUs.openOurLinkIn());
                break;
            case R.id.action_our_website:
                startActivity(AboutUs.openOurWebsite());
                break;
            case R.id.action_our_email:
                startActivity(AboutUs.openOurEmail());
                break;
            case R.id.action_share:
                startActivity(AboutUs.shareOurApp());
                break;
            case R.id.action_rate_us:
                rateUs();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        /*We will implement this method because we want to close the drawer
         when a back button is pressed on mobile and the drawer is Open.*/
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}