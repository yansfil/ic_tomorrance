package com.example.hoyeonlee.imaginecup.Exercise;

/**
 * Created by hoyeonlee on 2018. 2. 25..
 */

public class ParentData {
    private String image;
    private String name;
    private String number;

    public ParentData(String image, String name, String number) {
        this.image = image;
        this.name = name;
        this.number = number;
    }

    public String getImage() {
        return image;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
