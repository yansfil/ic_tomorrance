package com.tomorrance.yonsei.tomo;

import android.app.Application;
import android.provider.Settings;

import com.tomorrance.yonsei.tomo.Network.AddCookiesInterceptor;
import com.tomorrance.yonsei.tomo.Network.ApiService;
import com.tomorrance.yonsei.tomo.Network.ReceivedCookiesInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by hoyeonlee on 2018. 2. 23..
 */

public class _Application extends Application{
    private static _Application appInstance;
    Retrofit retrofit;
    ApiService apiService = null;
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/NotoSansCJKjp-DemiLight.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        appInstance = this;
        OkHttpClient.Builder oktHttpClient = new OkHttpClient.Builder();
        oktHttpClient.interceptors().add(new AddCookiesInterceptor());
        oktHttpClient.interceptors().add(new ReceivedCookiesInterceptor());
        retrofit = new Retrofit.Builder().
                baseUrl(ApiService.URL).
                client(oktHttpClient.build()).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        apiService = retrofit.create(ApiService.class);
    }
    public static _Application getInstance(){
        if(appInstance == null){
            appInstance = new _Application();
        }
        return appInstance;
    }

    public static String getDeviceId(){
        return Settings.Secure.getString(appInstance.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public ApiService getApiService(){
        if(apiService == null){
            OkHttpClient.Builder oktHttpClient = new OkHttpClient.Builder();
            oktHttpClient.interceptors().add(new AddCookiesInterceptor());
            oktHttpClient.interceptors().add(new ReceivedCookiesInterceptor());
            retrofit = new Retrofit.Builder().
                    baseUrl(ApiService.URL).
                    client(oktHttpClient.build()).
                    addConverterFactory(GsonConverterFactory.create()).
                    build();
            apiService = retrofit.create(ApiService.class);
        }
      return apiService;
    }

}
