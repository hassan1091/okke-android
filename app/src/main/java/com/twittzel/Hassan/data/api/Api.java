package com.twittzel.Hassan.data.api;

import com.twittzel.Hassan.data.api.result.TwitterVideoResult;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static final String BASE_URL = "http://api.nahn.tech";

    private ApiInterface retrofitBuild() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface.class);
    }

    public Call<TwitterVideoResult> getTwitterVideoResult(String url) {
        return retrofitBuild().getTwitterVideoResult(url);
    }
}
