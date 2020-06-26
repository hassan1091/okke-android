package com.twittzel.Hassan.ui.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.twittzel.Hassan.R;
import com.twittzel.Hassan.adapters.LR_Adapter;
import com.twittzel.Hassan.data.ExtraContext;
import com.twittzel.Hassan.data.database.DatabaseForAdapter;
import com.twittzel.Hassan.data.database.LastUrlList;
import com.twittzel.Hassan.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class LastDownUrlActivity extends AppCompatActivity {
    private DatabaseForAdapter databaseForAdapter;
    private LR_Adapter lr_adapter;
    private List<LastUrlList> urlLists = new ArrayList<>();


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re2s);
        //جعل الشاشة بشكل عمودي
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        //منع دوران الشاشة
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        //تشغيل اعلان قوقل
        displayGoogleAds();
        //استخدام قاعدة البيانات
        databaseForAdapter = DatabaseForAdapter.getsInstance(this);
        //جلب البيانات من قاعدة البيانات
        new Asyn().execute();
        //تشغيل RecycleView
        displayRecycleView();
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
        mAdView.loadAd(adRequest);
    }

    //تعيين recyclerView
    private void displayRecycleView() {

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //بدء حدث حيما  يتم النقر على الزر
        lr_adapter = new LR_Adapter(urlLists, new OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, List<LastUrlList> lastUserUrlArray) {
                //اخذ الرابط المختار وارساله الى MainActivity
                Intent intent = new Intent();
                intent.putExtra(ExtraContext.THIS_URL, lastUserUrlArray.get(position).getmLRDownList());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        //تعين adapter الى recyclerView
        recyclerView.setAdapter(lr_adapter);
    }

    // بدء Thread جديد
    public class Asyn extends AsyncTask<Void, Void, List<LastUrlList>> {

        @Override
        protected List<LastUrlList> doInBackground(Void... voids) {
            //اخذ البيانات قاعدة البيانات
            return databaseForAdapter.lastUrlDaw().getLastUrlList();
        }

        @Override
        protected void onPostExecute(List<LastUrlList> lastUrlLists) {
            super.onPostExecute(lastUrlLists);
            //وضع جميع الروابط داخل متغير
            urlLists.addAll(lastUrlLists);
            //فتح Thread للتعامل مع الواجهة
            LastDownUrlActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //تحديث بيانات Adapter
                    lr_adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
