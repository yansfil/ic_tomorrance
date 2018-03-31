package com.example.hoyeonlee.imaginecup.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hoyeonlee on 2018. 3. 26..
 */

public class ExerciseData {

    public ExerciseData(int image, String title, int number,int set,String videoUrl) {
        this.image = image;
        this.title = title;
        this.number = number;
        this.videoUrl = videoUrl;
        this.set = set;
    }

    @SerializedName("image")
    @Expose
    private int image;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("number")
    @Expose
    private int number;
    @SerializedName("videoUrl")
    @Expose
    private String videoUrl;
    @SerializedName("set")
    @Expose
    private int set;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }
}
