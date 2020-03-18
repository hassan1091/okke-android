package com.example.okke.Activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.example.okke.Fragments.DownloadFragment;
import com.example.okke.R;
import com.example.okke.data.ExtraContext;
import com.example.okke.data.RequestData;
import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity  {
    //العنصر الذي تم اختيااره من قائمة القوائم الاسبقة
    public static final int CODE_LDU = 114;
    private static final String DOWNLOAD_FRAGMENT_TAG ="DOWNLOAD_FRAGMENT_TAG";
    private String TAG= "faceBookAD";
    String mTwtId, mUserUrl;
    EditText editText;
    //قائمة اخر ما حمل
    ArrayList mLRDownloadList;
    DownloadManager downloadManager;
    JSONObject jsonArray2, jsonObject0, jsonObject1, jsonObject2;
    JSONArray data;
    ProgressBar progressBar;
    //ads
    private AdView mAdView; Ad adfacebook;
  //  private InterstitialAd mInterstitialAd;
    private com.facebook.ads.InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        mLRDownloadList = new ArrayList<String>();
        editText = findViewById(R.id.E1);
        progressBar = findViewById(R.id.progressBar);
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        displayCheckSelfPermission();
        //جلب البيانات اذا تم مشاركة الفيديو من تويتر
        if (getUrlFromTwt() != "") {
            editText.setText(getUrlFromTwt());
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    //  displayInterstitialAd();
        AdSettings.addTestDevice("88888888-aaaa-bbbb-cccc-222222222222");
        String placeMent = "215067366274176_215069979607248";
        AudienceNetworkAds.initialize(MainActivity.this);
        interstitialAd = new com.facebook.ads.InterstitialAd(MainActivity.this, placeMent);
        interstitialAd.setAdListener(new AbstractAdListener() {
            public void onAdLoaded(Ad ad) {
                adfacebook = ad;


            }
        });
        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

interstitialAd.loadAd();

    }
    @Override
    public void onBackPressed() {
        if (adfacebook == interstitialAd) {
            interstitialAd.show();
        }
        super.onBackPressed();
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

    public void bTForDownload(View view) {
        if (isNetworkAvailable()) {
            progressBar.setVisibility(View.VISIBLE);
            displayApiTwitte();
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, getResources().getString(R.string.You_do_not_have_internet_please_try_again_later), LENGTH_SHORT).show();
        }

    }

    public void displayApiTwitte() {
        //تعريف النص المدخل من المستخدم
        //رابط الاتصال ب API **ملاحظة يوجد رابط تجربيبي الان*https://twitter.com/cybersec2030/status/1185280330086965248*
        mUserUrl = editText.getText().toString();
        final String url = "http://api.nahn.tech/twitter/?url=" + mUserUrl;
        Request request = new Request.Builder().url(url).build();
        OkHttpClient Client = new OkHttpClient();
        Client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Onfailure", "norespons");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.v("onResponse", "respons");

                if (response.isSuccessful()) {
                    final String R1 = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //متغيرات للمخرجات
                            String imageUrl;
                            String size;
                            String quality;
                            //فصل مواد نتائج `api`
                            try {
                                mLRDownloadList.add(mUserUrl);
                                jsonArray2 = new JSONObject(R1);
                                mTwtId = jsonArray2.getString("id");
                                data = jsonArray2.getJSONArray("data");
                                progressBar.setVisibility(View.GONE);
                                //the oneBt button
                                RequestData requestData1 = null;
                                if (data.getJSONObject(0) != null) {
                                    jsonObject0 = data.getJSONObject(0);
                                    imageUrl = jsonObject0.getString("url");
                                    size = jsonObject0.getString("szie");
                                    quality = jsonObject0.getString("Quality");
                                    final String finalImageUrl = imageUrl;
                                    final String finalQuality = quality;
                                    final String finalSize = size;
                                    requestData1 = new RequestData(finalImageUrl, finalQuality, finalSize, mTwtId);
                                }
                                //the towBt button
                                RequestData requestData2 = null;
                                if (data.getJSONObject(1) != null) {
                                    jsonObject1 = data.getJSONObject(1);
                                    imageUrl = jsonObject1.getString("url");
                                    size = jsonObject1.getString("szie");
                                    quality = jsonObject1.getString("Quality");
                                    final String finalImageUrl1 = imageUrl;
                                    final String finalQuality1 = quality;
                                    final String finalSize1 = size;
                                    requestData2 = new RequestData(finalImageUrl1, finalQuality1, finalSize1, mTwtId);
                                }
                                //the threeBt button
                                RequestData requestData3 = null;
                                if (data.getJSONObject(2) != null) {
                                    jsonObject2 = data.getJSONObject(2);
                                    imageUrl = jsonObject2.getString("url");
                                    size = jsonObject2.getString("szie");
                                    quality = jsonObject2.getString("Quality");
                                    final String finalImageUrl2 = imageUrl;
                                    final String finalQuality2 = quality;
                                    final String finalSize2 = size;
                                    requestData3 = new RequestData(finalImageUrl2, finalQuality2, finalSize2, mTwtId);
                                }
                                //end the threeBt buttom

                                FragmentManager fragmentManager = getSupportFragmentManager();
                                DownloadFragment downloadFragment = DownloadFragment.newInstance(requestData1, requestData2, requestData3);
                                downloadFragment.show(fragmentManager, DOWNLOAD_FRAGMENT_TAG);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    Log.e("the respons ", "is not work ");
                }
            }
        });
    }


    public String getUrlFromTwt() {
        String theTwtUrl = "";
        String receivedText = null;
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
    //وارسال الرابط اليها
    public void displayToLR(View view) {
        Intent intLR_UserUrl = new Intent(MainActivity.this, LastDownUrlActivity.class);
        intLR_UserUrl.putExtra(ExtraContext.LRList, mUserUrl);
        startActivityForResult(intLR_UserUrl, CODE_LDU);
    }

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

    public void Open_menu(View view) {
      startActivity(new Intent(MainActivity.this, AboutUsActivity.class));  ;
    }

    public void displayInterstitialAd(View view) {
     /*   mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4200825572816870/1251621628");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();*/

        if (interstitialAd.isAdLoaded()){
            interstitialAd.show();
        }

        }

    }

