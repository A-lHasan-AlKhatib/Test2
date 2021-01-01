package com.handy.test2.Model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.handy.test2.R;
import com.handy.test2.Model.entity.CurrencyInfo;
import com.handy.test2.Model.entity.Result;
import com.handy.test2.ViewModel.MainViewModel;

import java.util.HashMap;

public class MyReceiver extends BroadcastReceiver {
    CurrencyInfo currencyInfo;
    String currencyTo;
    @Override
    public void onReceive(Context context, Intent intent) {
        String[] currencyNames = context.getResources().getStringArray(R.array.Currency);
        MainViewModel mainViewModel = new MainViewModel(context);
        NotificationUtils.createMainNotificationChannel(context);
        HashMap<String, Object> hashMap = SharedPrefs.getExchangeRate(context.getApplicationContext());

        if (!hashMap.isEmpty()){
            float from = (float) hashMap.get(SharedPrefs.FROM_KEY);
            float to = (float) hashMap.get(SharedPrefs.TO_KEY);
            int positionFrom = (int) hashMap.get(SharedPrefs.FROM_POSITION_KEY);
            int positionTo = (int) hashMap.get(SharedPrefs.TO_POSITION_KEY);
            currencyTo = currencyNames[positionTo];
            if (from != 0 && to != 0){
                String base = currencyNames[positionFrom];
                mainViewModel.getCurrencyInfo().addObserver((observable, o) -> {
                    Result result = (Result) o;
                    switch (result.status) {
                        case SUCCESS:
                            currencyInfo = (CurrencyInfo) result.data;
                            if (currencyInfo != null) {
                                double newTo = calculateCurrency();
                                if (newTo > to){
                                    NotificationUtils.showBasicNotification(context,
                                            context.getString(R.string.notification_title),
                                            context.getString(R.string.notification_body_1));
                                }else if (newTo < to){
                                    NotificationUtils.showBasicNotification(context,
                                            context.getString(R.string.notification_title),
                                            context.getString(R.string.notification_body_2));
                                }else {
                                    NotificationUtils.showBasicNotification(context,
                                            context.getString(R.string.notification_title),
                                            context.getString(R.string.notification_body_3));
                                }
                            }
                            break;
                        case ERROR:

                            break;
                    }
                });
                mainViewModel.requestCurrencyInfo(base);
            }
        }
    }

    public double calculateCurrency() {
        double currencyToDouble = 0;
        switch (currencyTo) {
            case "CAD":
                currencyToDouble = currencyInfo.getRates().getCAD();
                break;
            case "HKD":
                currencyToDouble = currencyInfo.getRates().getHKD();
                break;
            case "ISK":
                currencyToDouble = currencyInfo.getRates().getISK();
                break;
            case "PHP":
                currencyToDouble = currencyInfo.getRates().getPHP();
                break;
            case "DKK":
                currencyToDouble = currencyInfo.getRates().getDKK();
                break;
            case "HUF":
                currencyToDouble = currencyInfo.getRates().getHUF();
                break;
            case "CZK":
                currencyToDouble = currencyInfo.getRates().getCZK();
                break;
            case "RON":
                currencyToDouble = currencyInfo.getRates().getRON();
                break;
            case "SEK":
                currencyToDouble = currencyInfo.getRates().getSEK();
                break;
            case "IDR":
                currencyToDouble = currencyInfo.getRates().getIDR();
                break;
            case "INR":
                currencyToDouble = currencyInfo.getRates().getINR();
                break;
            case "BRL":
                currencyToDouble = currencyInfo.getRates().getBRL();
                break;
            case "RUB":
                currencyToDouble = currencyInfo.getRates().getRUB();
                break;
            case "HRK":
                currencyToDouble = currencyInfo.getRates().getHRK();
                break;
            case "JPY":
                currencyToDouble = currencyInfo.getRates().getJPY();
                break;
            case "THB":
                currencyToDouble = currencyInfo.getRates().getTHB();
                break;
            case "CHF":
                currencyToDouble = currencyInfo.getRates().getCHF();
                break;
            case "EUR":
                currencyToDouble = currencyInfo.getRates().getEUR();
                break;
            case "MYR":
                currencyToDouble = currencyInfo.getRates().getMYR();
                break;
            case "BGN":
                currencyToDouble = currencyInfo.getRates().getBGN();
                break;
            case "TRY":
                currencyToDouble = currencyInfo.getRates().getTRY();
                break;
            case "CNY":
                currencyToDouble = currencyInfo.getRates().getCNY();
                break;
            case "NOK":
                currencyToDouble = currencyInfo.getRates().getNOK();
                break;
            case "NZD":
                currencyToDouble = currencyInfo.getRates().getNZD();
                break;
            case "ZAR":
                currencyToDouble = currencyInfo.getRates().getZAR();
                break;
            case "USD":
                currencyToDouble = currencyInfo.getRates().getUSD();
                break;
            case "MXN":
                currencyToDouble = currencyInfo.getRates().getMXN();
                break;
            case "SGD":
                currencyToDouble = currencyInfo.getRates().getSGD();
                break;
            case "AUD":
                currencyToDouble = currencyInfo.getRates().getAUD();
                break;
            case "ILS":
                currencyToDouble = currencyInfo.getRates().getILS();
                break;
            case "KRW":
                currencyToDouble = currencyInfo.getRates().getKRW();
                break;
            case "PLN":
                currencyToDouble = currencyInfo.getRates().getPLN();
                break;
        }
        return currencyToDouble;
    }


}