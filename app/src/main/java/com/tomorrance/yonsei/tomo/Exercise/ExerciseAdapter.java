package com.tomorrance.yonsei.tomo.Exercise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tomorrance.yonsei.tomo.R;
import com.tomorrance.yonsei.tomo.data.ExerciseData;

import java.util.ArrayList;

/**
 * Created by hoyeonlee on 2018. 2. 25..
 */

public class ExerciseAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ExerciseData> datas;

    public ExerciseAdapter(Context mContext, ArrayList<ExerciseData> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    ExerciseHolder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_exercise, parent, false);
            holder = new ExerciseHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ExerciseHolder) convertView.getTag();
        }
        holder.setData(datas.get(position));
        return convertView;
    }

}
