package com.example.hoyeonlee.imaginecup.Exercise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hoyeonlee.imaginecup.R;
import com.example.hoyeonlee.imaginecup.data.ExerciseData2;

/**
 * Created by hoyeonlee on 2018. 3. 26..
 */

public class ExerciseGridItem extends LinearLayout {
    ImageView imageView;
    TextView titleView;
    TextView numberView;
    Context context;

    public ExerciseGridItem(Context context) {
        super(context);
        this.context = context;
        init(context);
    }
    public void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise2,this);
        titleView = view.findViewById(R.id.tv_title);
        numberView = view.findViewById(R.id.tv_number);
        imageView = view.findViewById(R.id.iv_exercise);
    }
    public void setData(ExerciseData2 data){
        titleView.setText(data.getTitle());
        numberView.setText(data.getNumber());
        Glide.with(context).load(data.getImage())
                .fitCenter()
                .into(imageView);
    }
}
