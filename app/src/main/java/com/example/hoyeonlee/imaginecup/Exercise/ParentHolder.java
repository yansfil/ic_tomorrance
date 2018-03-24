package com.example.hoyeonlee.imaginecup.Exercise;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hoyeonlee.imaginecup.R;

/**
 * Created by hoyeonlee on 2018. 2. 25..
 */

public class ParentHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView numberText;
    private TextView nameText;
    public ParentHolder(View itemView){
        super(itemView);
        imageView = itemView.findViewById(R.id.iv_exercise);
        numberText = itemView.findViewById(R.id.tv_number);
        nameText = itemView.findViewById(R.id.tv_name);
    }
    public void setData(ParentData data){
        Glide.with(itemView.getContext()).load(Uri.parse(data.getImage())).into(imageView);
        numberText.setText(data.getNumber());
        nameText.setText(data.getName());
    }
}
