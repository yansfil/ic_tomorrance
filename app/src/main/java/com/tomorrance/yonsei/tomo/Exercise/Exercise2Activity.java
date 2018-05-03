package com.tomorrance.yonsei.tomo.Exercise;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.tomorrance.yonsei.tomo.BackActionBarActivity;
import com.tomorrance.yonsei.tomo.R;
import com.tomorrance.yonsei.tomo.data.ExerciseData;

import java.util.ArrayList;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

/**
 * Created by hoyeonlee on 2018. 3. 26..
 */

public class Exercise2Activity extends BackActionBarActivity {
    GridViewWithHeaderAndFooter gridView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise2);
        setTitle(R.string.text_exercise);
        gridView = findViewById(R.id.recyclerView);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ArrayList<ExerciseData> data = new ArrayList<>();
//        ExerciseData item1 = new ExerciseData("http://juliandance.org/wp-content/uploads/2016/01/RedApple.jpg","스쿼트","10회");
//        ExerciseData item2 = new ExerciseData("https://www.extremetech.com/wp-content/uploads/2014/07/Apple-NOM-640x353.jpg","런지","15회");
//        ExerciseData item3 = new ExerciseData("https://i.guim.co.uk/img/media/4371417c1b5e8fab6bb9f05904d260d40dc18f82/133_0_4175_2505/master/4175.jpg?w=300&q=55&auto=format&usm=12&fit=max&s=f179c1f4c8676e0afb265acae4413bb4","데드리프트","8회");
//        data.add(item1);
//        data.add(item2);
//        data.add(item3);
        View v = getLayoutInflater().inflate(R.layout.item_exercise_header,null,false);
        ExpandableListView.LayoutParams params = new ExpandableListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 900);
        v.setLayoutParams(params);
        gridView.addHeaderView(v);
        gridView.setAdapter(new Exercise2Adapter(data,this));
//        _Application.getInstance().getApiService().getExercise().enqueue(new Callback<ExerciseInfo>() {
//            @Override
//            public void onResponse(Call<ExerciseInfo> call, Response<ExerciseInfo> response) {
//                if(response.code() == 401){
//                    StaticFunctions.getInstance(Exercise2Activity.this).goFirstPage();
//                    return;
//                }
//                if(response.body().getCode() == 1) {
//
//
//                }else{
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ExerciseInfo> call, Throwable t) {
//
//            }
//        });
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


    }
}
