package com.example.hoyeonlee.imaginecup.Exercise;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.example.hoyeonlee.imaginecup.BackActionBarActivity;
import com.example.hoyeonlee.imaginecup.R;

import java.util.ArrayList;

public class ExerciseActivity extends BackActionBarActivity {
    ExpandableListView recyclerView;
    ExerciseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        setToolbar();
        setTitle(getResources().getString(R.string.text_exercise));
        recyclerView = (ExpandableListView) findViewById(R.id.recyclerView);

        ArrayList<ExerciseData> datas = new ArrayList<>();
        datas.add(new ExerciseData(new ParentData("https://media.giphy.com/media/ytEKiyP1Ib2AU/giphy.gif","점핑잭","30"),"https://media.giphy.com/media/ytEKiyP1Ib2AU/giphy.gif"));
        datas.add(new ExerciseData(new ParentData("https://s-media-cache-ak0.pinimg.com/originals/81/cb/2e/81cb2eaf41bc1e6392e0296e4c767ca4.gif","스쿼트","30"),"https://s-media-cache-ak0.pinimg.com/originals/81/cb/2e/81cb2eaf41bc1e6392e0296e4c767ca4.gif"));
        datas.add(new ExerciseData(new ParentData("https://img.aws.livestrongcdn.com/ls-slideshow-main-image/cme/photography.prod.demandstudios.com/9c6525d1-5808-47bb-b72a-ff30d5661913.gif","사이드 런지","30"),"https://img.aws.livestrongcdn.com/ls-slideshow-main-image/cme/photography.prod.demandstudios.com/9c6525d1-5808-47bb-b72a-ff30d5661913.gif"));
        datas.add(new ExerciseData(new ParentData("https://img.aws.livestrongcdn.com/ls-slideshow-main-image/cme/photography.prod.demandstudios.com/be734cef-b8b1-415d-8f97-7ff3c62bde0d.gif","푸시업","25"),"https://img.aws.livestrongcdn.com/ls-slideshow-main-image/cme/photography.prod.demandstudios.com/be734cef-b8b1-415d-8f97-7ff3c62bde0d.gif"));
        datas.add(new ExerciseData(new ParentData("https://img.aws.livestrongcdn.com/ls-slideshow-main-image/cme/photography.prod.demandstudios.com/9b20a77f-2058-4fd2-9189-67ef02295d6f.gif","버피","20"),"https://img.aws.livestrongcdn.com/ls-slideshow-main-image/cme/photography.prod.demandstudios.com/9b20a77f-2058-4fd2-9189-67ef02295d6f.gif"));

        adapter = new ExerciseAdapter(this,datas);
        View v = getLayoutInflater().inflate(R.layout.item_exercise_header,null,false);
        ExpandableListView.LayoutParams params = new ExpandableListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 900);
        v.setLayoutParams(params);
        recyclerView.addHeaderView(v);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.v("EXERCISE_ADAPTER","CLICK!");
                return false;
            }
        });
        recyclerView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.v("EXERCISE_ADAPTER","UnCLICK!");
            }
        });
    }
}
