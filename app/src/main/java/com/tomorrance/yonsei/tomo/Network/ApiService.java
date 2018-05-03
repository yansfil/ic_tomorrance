package com.tomorrance.yonsei.tomo.Network;

import com.tomorrance.yonsei.tomo.data.BodyInfo;
import com.tomorrance.yonsei.tomo.data.BodyInfos;
import com.tomorrance.yonsei.tomo.data.ExerciseInfo;
import com.tomorrance.yonsei.tomo.data.LoginResult;
import com.tomorrance.yonsei.tomo.data.Main;

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
    public static final String URL = "https://tomorrances.azurewebsites.net";
    @FormUrlEncoded
    @POST("/client/login")
    Call<LoginResult> login(@Field("email") String email, @Field("password") String password, @Field("pushid") String clientId);

    @FormUrlEncoded
    @POST("/client/join")
    Call<LoginResult> join(@Field("name") String name, @Field("height") String height, @Field("email") String email, @Field("password") String password, @Field("goal") String goal, @Field("gender") String gender, @Field("pushid") String clientId, @Field("intensity") String intensity);

    @FormUrlEncoded
    @POST("/client/reservation")
    Call<ResponseBody> reserve(@Field("weight") int weight, @Field("deviceid") String deviceId);

    @GET("/client/main")
    Call<Main> getMain();

    @GET("/client/profile")
    Call<ResponseBody> getProfile();


    @GET("/client/recent-info")
    Call<BodyInfo> getRecentInfo();

    @GET("/client/all-info")
    Call<BodyInfos> getAllInfo();

    @GET("/client/status")
    Call<BodyInfos> getStatus();

    @GET("/client/exercise")
    Call<ExerciseInfo> getExercise();

    @GET("/client/bodySize")
    Call<BodyInfo> getBodySize();
}
