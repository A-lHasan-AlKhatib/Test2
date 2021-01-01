package com.handy.test2.Model;

import com.handy.test2.Model.entity.CurrencyInfo;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface CurrencyApiInterface {

    @GET("latest")
    Call<CurrencyInfo> getCurrencyInfo(@QueryMap HashMap<String, String> queryParams);
}
