package com.example.hoyeonlee.imaginecup.Exercise;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.hoyeonlee.imaginecup.data.ExerciseData2;

import java.util.ArrayList;

/**
 * Created by hoyeonlee on 2018. 3. 26..
 */

public class Exercise2Adapter extends BaseAdapter{
    ArrayList<ExerciseData2> data;
    Context context;
    public Exercise2Adapter(ArrayList<ExerciseData2> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = new ExerciseGridItem(context);
        }
        ((ExerciseGridItem)convertView).setData(data.get(position));
        return convertView;
    }
}
