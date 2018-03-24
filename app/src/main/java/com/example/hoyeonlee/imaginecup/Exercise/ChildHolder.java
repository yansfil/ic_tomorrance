package com.example.hoyeonlee.imaginecup.Exercise;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.hoyeonlee.imaginecup.R;

/**
 * Created by hoyeonlee on 2018. 2. 25..
 */

public class ChildHolder extends RecyclerView.ViewHolder{
    private ImageView exerciseImage;
    public ChildHolder(View itemView){
        super(itemView);
        exerciseImage = itemView.findViewById(R.id.iv_exercise);
    }
    public void setData(String url){
        Glide.with(itemView.getContext()).load(Uri.parse(url)).into(exerciseImage);
    }
}
