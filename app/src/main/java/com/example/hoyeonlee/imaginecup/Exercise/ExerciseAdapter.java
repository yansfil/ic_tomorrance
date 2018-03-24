package com.example.hoyeonlee.imaginecup.Exercise;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.example.hoyeonlee.imaginecup.R;

import java.util.ArrayList;

/**
 * Created by hoyeonlee on 2018. 2. 25..
 */

public class ExerciseAdapter extends BaseExpandableListAdapter{
    private Context mContext;
    private ArrayList<ExerciseData> datas;

    public ExerciseAdapter(Context mContext, ArrayList<ExerciseData> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Log.v("EXERCISE_ADAPTER","getChildrenCount");
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return datas.get(groupPosition).getParentData();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return datas.get(groupPosition).getChildData() ;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    ParentHolder holder;
    ChildHolder childHolder;
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_exercise,parent,false);
            holder = new ParentHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ParentHolder) convertView.getTag();
        }
        holder.setData(datas.get(groupPosition).getParentData());
        Log.v("EXERCISE_ADAPTER",groupPosition+"parent");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_exercise_child,parent,false);
            childHolder = new ChildHolder(convertView);
            convertView.setTag(childHolder);
        }else{
            childHolder = (ChildHolder) convertView.getTag();
        }
        childHolder.setData(datas.get(groupPosition).getChildData());
        Log.v("EXERCISE_ADAPTER",childPosition+"child");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
