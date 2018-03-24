package com.example.hoyeonlee.imaginecup.Network;

import com.example.hoyeonlee.imaginecup.data.Body;
import com.example.hoyeonlee.imaginecup.data.LoginResult;
import com.example.hoyeonlee.imaginecup.data.Main;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by hoyeonlee on 2018. 3. 17..
 */

public interface ApiService {
    public static final String URL = "http://192.168.20.231:3000";
    @FormUrlEncoded
    @POST("/client/login")
    Call<LoginResult> login(@Field("email") String email, @Field("password") String password, @Field("pushid") String clientId);

    @FormUrlEncoded
    @POST("/client/join")
    Call<LoginResult> join(@Field("name") String name,@Field("height") String height, @Field("email") String email, @Field("password") String password,@Field("pushid") String clientId);

    @FormUrlEncoded
    @POST("/client/reservation")
    Call<ResponseBody> reserve(@Field("weight") int weight, @Field("deviceid") String deviceId);

    @GET("/client/main")
    Call<Main> getMain();

    @GET("/client/profile")
    Call<ResponseBody> getProfile();


    @GET("/client/recent-info")
    Call<Body> getBodyInfo();

    @GET("/client/bodySize")
    Call<Body> getBodySize();
}