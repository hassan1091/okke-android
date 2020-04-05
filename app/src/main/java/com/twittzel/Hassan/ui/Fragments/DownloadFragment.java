package com.twittzel.Hassan.ui.Fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LifecycleOwner;
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

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.view.View.INVISIBLE;

public class DownloadFragment extends DialogFragment {
    public static final String KEY_TWT_ID = "KEY_TWT_ID";
    public static final String KEY_QUALITY = "KEY_QUALITY";
    public static final String KEY_SIZE = "KEY_SIZE";

    public DownloadFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DownloadFragment newInstance(String RBody) {
        DownloadFragment fragment = new DownloadFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ExtraContext.REQ_BODY, RBody);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blank_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //متغيرات للمخرجات
        String imageUrl;
        String size;
        String quality;
        final String mTwtId;
        try {
            //فصل مواد نتائج `api`
            JSONObject jsonArray2, jsonObject0, jsonObject1, jsonObject2;
            JSONArray data;
            jsonArray2 = new JSONObject(getArguments().getString(ExtraContext.REQ_BODY));
            mTwtId = jsonArray2.getString("id");
            data = jsonArray2.getJSONArray("data");
            //بيانات العنصر الاول
            Button oneButton = view.findViewById(R.id.one);
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
                oneButton.setText(getResources().getString(R.string.Quality) + ": " + finalQuality + "\n " + getResources().getString(R.string.Size) + " = " + finalSize);
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
            Button towButton = view.findViewById(R.id.tow);
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

                towButton.setText(getResources().getString(R.string.Quality) + ": " + finalQuality1 + "\n " + getResources().getString(R.string.Size) + " = " + finalSize1);
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
            Button threeButton = view.findViewById(R.id.three);
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
                threeButton.setText(getResources().getString(R.string.Quality) + ": " + finalQuality2 + "\n " + getResources().getString(R.string.Size) + " = " + finalSize2);
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
    public void displayDownloadVideoAndNotify(String url, String twtId, String quality, String size, final View view) {
        //اوامر التحميل
        final Context context = getContext();
        DownloadManager.Request downLoadRequest = new DownloadManager.Request(Uri.parse(url));
        downLoadRequest.setTitle("download: " + "TwtVideo: " + twtId);
        downLoadRequest.setDescription(quality + " / " + size);
        downLoadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, "TwtVideo: " + twtId);
        downLoadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(downLoadRequest);

        /* PeriodicWorkRequest.Builder builder = new PeriodicWorkRequest.Builder();
        Constraints constraints = new Constraints.Builder()
                .setTriggerContentUpdateDelay(5 , TimeUnit.MINUTES)
                .build();
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(WorkForNotify.class,1, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();*/
        //اوامر اظهار الاشعار
        Data data = new Data.Builder()
                .putString(KEY_TWT_ID , twtId)
                .putString(KEY_QUALITY , quality)
                .putString(KEY_SIZE , size)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(WorkForNotify.class)
                .setInputData(data)
                .build();
        WorkManager.getInstance(context).enqueue(request);

        WorkManager.getInstance(context).getWorkInfoByIdLiveData(request.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                int a = workInfo.getOutputData().getInt("asd",0);
                Log.d("SDFGHJK", String.valueOf(a));
                Log.d("SDFGHJK",workInfo.getId().toString());
                Log.d("SDFGHJK",workInfo.getState().name());
            }
        });


    }
}
