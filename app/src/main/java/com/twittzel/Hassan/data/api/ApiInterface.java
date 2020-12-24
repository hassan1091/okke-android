package com.twittzel.Hassan.data.api;

import com.twittzel.Hassan.data.api.result.TwitterVideoResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    //  http://api.nahn.tech/video/?url=https://video.twimg.com/ext_tw_video/1185280178781642760/pu/vid/1280x720/TX5WCv-FxFRLoK4V.mp4
    @GET("/twitter/")
    Call<TwitterVideoResult> getTwitterVideoResult(@Query("url") String userUrl);
}
