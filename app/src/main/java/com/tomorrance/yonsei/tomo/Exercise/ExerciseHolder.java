package com.tomorrance.yonsei.tomo.Exercise;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tomorrance.yonsei.tomo.R;
import com.tomorrance.yonsei.tomo.data.ExerciseData;

/**
 * Created by hoyeonlee on 2018. 2. 25..
 */

public class ExerciseHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView numberText;
    private TextView nameText;
    private TextView setText;
    private TextView dayText;
    public ExerciseHolder(View itemView){
        super(itemView);
        imageView = itemView.findViewById(R.id.iv_exercise);
        numberText = itemView.findViewById(R.id.tv_number);
        nameText = itemView.findViewById(R.id.tv_name);
        setText = itemView.findViewById(R.id.tv_set);
        dayText = itemView.findViewById(R.id.tv_day);
    }
    public void setData(ExerciseData data){
        if(data.getVideoUrl().equals("")){
            imageView.setVisibility(View.GONE);
            numberText.setVisibility(View.GONE);
            nameText.setVisibility(View.GONE);
            setText.setVisibility(View.GONE);
            dayText.setVisibility(View.VISIBLE);
            dayText.setText(data.getTitle());
        }else {
            imageView.setVisibility(View.VISIBLE);
            numberText.setVisibility(View.VISIBLE);
            nameText.setVisibility(View.VISIBLE);
            setText.setVisibility(View.VISIBLE);
            dayText.setVisibility(View.GONE);

            Glide.with(itemView.getContext())
                    .load(data.getImage())
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
            nameText.setText(data.getTitle());
            if (data.getNumber() == 0) {
                numberText.setText("until you can't");
            } else {
                numberText.setText(data.getNumber() + " times");
            }
            if (data.getSet() == 1) {
                setText.setVisibility(View.GONE);
            } else {
                setText.setText(data.getSet() + " sets");
            }
        }
    }
}
