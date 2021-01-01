package com.handy.test2.ViewModel;

import android.content.Context;
import com.handy.test2.Model.entity.CurrencyInfo;
import com.handy.test2.Model.entity.NetworkUtils;
import com.handy.test2.Model.entity.ObservableResult;
import com.handy.test2.Model.entity.Result;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel {

    private final NetworkUtils networkUtils;
    private final ObservableResult observableResult;

    public MainViewModel(Context application){
        networkUtils = NetworkUtils.getInstance(application);
        observableResult = new ObservableResult();
    }

    public ObservableResult getCurrencyInfo() {
        return observableResult;
    }

    public void requestCurrencyInfo(String base){
        HashMap<String, String> params = networkUtils.getQueryParams(base);
        networkUtils.getCurrencyApiInterface().getCurrencyInfo(params).enqueue(new Callback<CurrencyInfo>() {
            @Override
            public void onResponse(Call<CurrencyInfo> call, Response<CurrencyInfo> response) {
                if (response.isSuccessful()){
                    observableResult.setResult(Result.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<CurrencyInfo> call, Throwable t) {

            }
        });
    }
}