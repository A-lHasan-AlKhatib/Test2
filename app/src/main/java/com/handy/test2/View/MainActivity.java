package com.handy.test2.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import com.google.android.material.snackbar.Snackbar;
import com.handy.test2.R;
import com.handy.test2.Model.CustomAdapter;
import com.handy.test2.Model.MyReceiver;
import com.handy.test2.Model.SharedPrefs;
import com.handy.test2.Model.entity.ApiError;
import com.handy.test2.Model.entity.CurrencyInfo;
import com.handy.test2.Model.entity.Result;
import com.handy.test2.ViewModel.MainViewModel;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;
    private Spinner spinnerFrom, spinnerTo;
    private String baseCurrency;
    private Button convertBtn;
    private EditText amountEt, resultEt;
    private String currencyTo;
    private CurrencyInfo currencyInfo;
    private ProgressBar progressBar;
    private ImageButton imageBtn;
    private int positionTo;
    private int positionFrom;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        String[] currencyNames = getResources().getStringArray(R.array.Currency);
        int[] flags = getFlags();

        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),flags,currencyNames);

        spinnerFrom.setAdapter(customAdapter);
        spinnerTo.setAdapter(customAdapter);

        HashMap<String, Object> hashMap = SharedPrefs.getExchangeRate(getApplicationContext());
        if (!hashMap.isEmpty()){
            positionTo = (int) hashMap.get(SharedPrefs.TO_POSITION_KEY);
            positionFrom = (int) hashMap.get(SharedPrefs.FROM_POSITION_KEY);
            spinnerFrom.setSelection(positionFrom);
            spinnerTo.setSelection(positionTo);
            float from = (float) hashMap.get(SharedPrefs.FROM_KEY);
            float to = (float) hashMap.get(SharedPrefs.TO_KEY);
            if (from != 0 && to != 0){
                amountEt.setText(from+"");
                resultEt.setText(to+"");
            }
        }else {
            spinnerTo.setSelection(2);
        }

        imageBtn.setOnClickListener(view -> {
            spinnerFrom.setSelection(positionTo);
            spinnerTo.setSelection(positionFrom);
            String temp = resultEt.getText().toString();
            resultEt.setText(amountEt.getText().toString());
            amountEt.setText(temp);
        });

        convertBtn.setOnClickListener(view -> convertCurrency());
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                baseCurrency = currencyNames[i];
                positionFrom = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //ignored
            }
        });

        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currencyTo = currencyNames[i];
                positionTo = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if (SharedPrefs.getState(getApplicationContext())){
            registerAlarm();
        }else {
            cancelAlarm();
        }
    }

    private void registerAlarm() {
        int hour = 60 * 60 * 1000;
        Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + hour, hour,pendingIntent);
    }

    private void cancelAlarm(){
        if (pendingIntent != null && alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (SharedPrefs.getState(getApplicationContext())){
            menu.getItem(0).setIcon(R.drawable.ic_view);
        }else {
            menu.getItem(0).setIcon(R.drawable.ic_visibility);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (SharedPrefs.getState(getApplicationContext())){
            SharedPrefs.setState(getApplicationContext(), false);
            item.setIcon(R.drawable.ic_visibility);
        }else {
            SharedPrefs.setState(getApplicationContext(), true);
            item.setIcon(R.drawable.ic_view);
        }
        return super.onOptionsItemSelected(item);
    }

    private void convertCurrency() {
        // check the edit text if is empty or not
        if (!TextUtils.isEmpty(amountEt.getText().toString())) {
            progressBar.setVisibility(View.VISIBLE);
            convertBtn.setVisibility(View.GONE);
            mainViewModel = new MainViewModel(getApplication());
            mainViewModel.getCurrencyInfo().addObserver((observable, o) -> {
                Result result = (Result) o;
                switch (result.status) {
                    case SUCCESS:
                        currencyInfo = (CurrencyInfo) result.data;
                        if (currencyInfo != null) {
                            float amount = Float.parseFloat(amountEt.getText().toString());
                            resultEt.setText(String.valueOf(calculateCurrency(amount)));
                            float to = Float.parseFloat(resultEt.getText().toString());
                            SharedPrefs.setExchangeRate(getApplicationContext(), positionFrom, positionTo,amount, to );
                            progressBar.setVisibility(View.GONE);
                            convertBtn.setVisibility(View.VISIBLE);
                        } else {
                            ApiError apiError = (ApiError) result.data;
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),apiError.getError(), Snackbar.LENGTH_INDEFINITE);
                            if (apiError.isRecoverable()){
                                snackbar.setAction(getString(R.string.retry), view1 -> mainViewModel.requestCurrencyInfo(baseCurrency));
                            }
                            snackbar.show();
                        }
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        convertBtn.setVisibility(View.VISIBLE);
                        ApiError apiError = (ApiError) result.data;
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),apiError.getError(), Snackbar.LENGTH_INDEFINITE);
                        if (apiError.isRecoverable()){
                            snackbar.setAction(getString(R.string.retry), view -> mainViewModel.requestCurrencyInfo(baseCurrency));
                        }
                        snackbar.show();
                        break;
                }
            });
            mainViewModel.requestCurrencyInfo(baseCurrency);
        } else {
            amountEt.setError(getString(R.string.empty_et));
        }
    }

    public double calculateCurrency(double amount) {
        double result;
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
        result = amount * currencyToDouble;
        return result;
    }

    public void initViews(){
        spinnerFrom = findViewById(R.id.spinner);
        spinnerTo = findViewById(R.id.spinner2);
        convertBtn = findViewById(R.id.convert);
        amountEt = findViewById(R.id.et_amount);
        resultEt = findViewById(R.id.et_result);
        progressBar = findViewById(R.id.progressBar);
        imageBtn = findViewById(R.id.image_btn_switch);
    }

    public int [] getFlags(){
        return new int[]{
                R.drawable.ic_cad,
                R.drawable.ic_hkd,
                R.drawable.ic_isk,
                R.drawable.ic_php,
                R.drawable.ic_dkk,
                R.drawable.ic_huf,
                R.drawable.ic_czk,
                R.drawable.ic_rom,
                R.drawable.ic_sek,
                R.drawable.ic_idr,
                R.drawable.ic_inr,
                R.drawable.ic_brl,
                R.drawable.ic_rub,
                R.drawable.ic_hrk,
                R.drawable.ic_jpy,
                R.drawable.ic_thb,
                R.drawable.ic_chf,
                R.drawable.ic_eur,
                R.drawable.ic_myr,
                R.drawable.ic_bgn,
                R.drawable.ic_tru,
                R.drawable.ic_cnu,
                R.drawable.ic_nok,
                R.drawable.ic_nzd,
                R.drawable.ic_zar,
                R.drawable.ic_usd,
                R.drawable.ic_mxn,
                R.drawable.ic_sgd,
                R.drawable.ic_aud,
                R.drawable.ic_ils,
                R.drawable.ic_krw,
                R.drawable.ic_pln
        };
    }
}