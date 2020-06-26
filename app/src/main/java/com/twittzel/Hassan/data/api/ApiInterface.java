package com.twittzel.Hassan.data.api;

import com.twittzel.Hassan.data.api.result.TwitterVideoResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/twitter/")
    Call<TwitterVideoResult> getTwitterVideoResult(@Query("url") String userUrl);
}
