package com.example.hoyeonlee.imaginecup.Exercise;

/**
 * Created by hoyeonlee on 2018. 2. 25..
 */

public class ExerciseData {
    private ParentData parentData;
    private String childData;

    public ExerciseData(ParentData parentData, String childData) {
        this.parentData = parentData;
        this.childData = childData;
    }

    public String getChildData() {
        return childData;
    }

    public ParentData getParentData() {
        return parentData;
    }
}
