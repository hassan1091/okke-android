package com.example.okke;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    String  mJSONObject_twtId;
    Button one, tow, three;
    DownloadManager downloadManager;
    JSONObject jsonArray2,jsonObject0,jsonObject1,jsonObject2;
    JSONArray data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayDownloadingPe();
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        EditText editText = findViewById(R.id.E1);

        if (getUrlFromTwt() != "") {
            editText.setText(getUrlFromTwt());
        }


    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && ((NetworkInfo) activeNetworkInfo).isConnected();

    }
    public void displayDownloadingPe() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(this
                , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

    }


    public void displayDownloadVido(String finalUrl, String finalQuality, String finalSize,String TWTid) {
        //اوامر التحميل one
        DownloadManager.Request downLoadRequest = new DownloadManager.Request(Uri.parse(finalUrl));
        downLoadRequest.setTitle("is downloading");
        downLoadRequest.setDescription(finalQuality + " / " + finalSize);
        downLoadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, "TwtVideo: " + TWTid);
        downloadManager.enqueue(downLoadRequest);
    }

    public void checkNet (View view){
        if (isNetworkAvailable()) {
B1();
        }else {
Toast.makeText(this ,"ليس لديك انترنت ارجو المحاولة لاحقا ",Toast.LENGTH_SHORT).show();
        }

    }
    public void B1() {


        //تعريف النص المدخل من المستخدم
       final EditText editText =findViewById(R.id.E1);
        //رابط الاتصال ب API **ملاحظة يوجد رابط تجربيبي الان*https://twitter.com/cybersec2030/status/1185280330086965248*
        final String test = editText.getText().toString();

        final String url = "http://api.96.lt/twitter/?url=" + test;

        final Request request = new Request.Builder().url(url).build();
        //تعريف النص التجريبي لاخراج البيانات
      //  final TextView textView = findViewById(R.id.T1);

        OkHttpClient Client = new OkHttpClient();
        Client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("Onfailure" , "norespons");
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String R1 = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
/*
النتيجة الافتراضية لل api
{
statusCode: 200,
data: [
{
url: "https://video.twimg.com/ext_tw_video/1185280178781642760/pu/vid/480x270/WkuUYweY4mAaZAZX.mp4",
szie: "316.84 KiB",
Quality: "480x270"
},
{
url: "https://video.twimg.com/ext_tw_video/1185280178781642760/pu/vid/640x360/Lql1VWcyPWr8sBEI.mp4",
szie: "795.2 KiB",
Quality: "640x360"
},
{
url: "https://video.twimg.com/ext_tw_video/1185280178781642760/pu/vid/1280x720/TX5WCv-FxFRLoK4V.mp4",
szie: "2.06 MiB",
Quality: "1280x720"
}
]
}
*/

// مهم للتحميل


//متغيرات للمخرجات
                            String imageUrl = null;
                            String size = null;
                            String quality = null;
                            //فصل مواد نتائج `api`

                            try {

                                 jsonArray2 = new JSONObject(R1);



                                mJSONObject_twtId  = jsonArray2.getString("id");



                               String id  = mJSONObject_twtId;
                                     data = jsonArray2.getJSONArray("data");


                                    //the one button
                                     jsonObject0 = data.getJSONObject(0);
                                    imageUrl = jsonObject0.getString("url");
                                    size = jsonObject0.getString("szie");
                                    quality = jsonObject0.getString("Quality");
                                    one = (Button) findViewById(R.id.one);
                                    one.setText(quality);
                                    //تشغيل الزر one
                                    final String finalImageUrl = imageUrl;
                                    final String finalQuality = quality;
                                    final String finalSize = size;
                                    one.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            displayDownloadVido(finalImageUrl, finalQuality, finalSize,mJSONObject_twtId);

                                        }
                                    });
                                    //end the one buttom

                                    //the tow button
                                     jsonObject1 = data.getJSONObject(1);
                                    imageUrl = jsonObject1.getString("url");
                                    size = jsonObject1.getString("szie");
                                    quality = jsonObject1.getString("Quality");

                                    tow = (Button) findViewById(R.id.tow);
                                    tow.setText(quality);
                                    final String finalImageUrl1 = imageUrl;
                                    final String finalQuality1 = quality;
                                    final String finalSize1 = size;
                                    tow.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //اوامر التحميل tow
                                            displayDownloadVido(finalImageUrl1, finalQuality1, finalSize1,mJSONObject_twtId);
                                        }
                                    });
                                    //end the tow buttom

                                    //the three button
                                     jsonObject2 = data.getJSONObject(2);
                                    imageUrl = jsonObject2.getString("url");
                                    size = jsonObject2.getString("szie");
                                    quality = jsonObject2.getString("Quality");

                                    three = (Button) findViewById(R.id.three);
                                    three.setText(quality);
                                    final String finalImageUrl2 = imageUrl;
                                    final String finalQuality2 = quality;
                                    final String finalSize2 = size;
                                    three.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //اوامر التحميل three
                                            displayDownloadVido(finalImageUrl2, finalQuality2, finalSize2,mJSONObject_twtId);

                                        }
                                    });
                                    //end the tow buttom

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });

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


}
