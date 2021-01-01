package com.handy.test2.Model.entity;


import android.content.Context;
import com.handy.test2.Model.CurrencyApiInterface;
import java.util.HashMap;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    private static NetworkUtils instance;
    private static final String BASE_URL = " https://api.exchangeratesapi.io/";
    private static CurrencyApiInterface currencyApiInterface;

    private final Retrofit retrofit;
    private final Context context;
    private static final String BASE_PARAM = "base";
    public static NetworkUtils getInstance(Context application) {
        if (instance == null){
            instance =new NetworkUtils(application);
        }
        return instance;
    }

    private NetworkUtils(Context application) {
        context = application.getApplicationContext();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        currencyApiInterface = retrofit.create(CurrencyApiInterface.class);

    }

    public static CurrencyApiInterface getCurrencyApiInterface() {
        return currencyApiInterface;
    }

    public HashMap<String, String> getQueryParams(String base){
        HashMap<String, String> map = new HashMap<>();
        map.put(BASE_PARAM, base);
        return map;
    }
}
