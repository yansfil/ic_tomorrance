package com.example.hoyeonlee.imaginecup.Exercise;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoyeonlee.imaginecup.BackActionBarActivity;
import com.example.hoyeonlee.imaginecup.Network.ApiService;
import com.example.hoyeonlee.imaginecup.Network.SharedPreferenceBase;
import com.example.hoyeonlee.imaginecup.R;
import com.example.hoyeonlee.imaginecup.Utils.StaticFunctions;
import com.example.hoyeonlee.imaginecup._Application;
import com.example.hoyeonlee.imaginecup.data.BodyInfos;
import com.example.hoyeonlee.imaginecup.data.ExerciseData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseActivity extends BackActionBarActivity {
    ListView recyclerView;
    ExerciseAdapter adapter;
    ArrayList<ExerciseData> datas;
    ApiService apiService;
    Dialog dialogTransparent;

    private int time = 0;
    private String intensity = "";
    private String level = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        setToolbar();
        setTitle(getResources().getString(R.string.text_exercise));
        //Loading Dialog
        dialogTransparent = new Dialog(this, android.R.style.Theme_Black);
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_loading, null);
        dialogTransparent.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogTransparent.getWindow().setBackgroundDrawableResource(
                R.color.transparent_dialog);
        dialogTransparent.setContentView(view);
        dialogTransparent.show();
        apiService = _Application.getInstance().getApiService();
        apiService.getStatus().enqueue(new Callback<BodyInfos>() {
            @Override
            public void onResponse(Call<BodyInfos> call, Response<BodyInfos> response) {
                if(response.code() == 401){
                    StaticFunctions.getInstance(ExerciseActivity.this).goFirstPage();
                    return;
                }
                BodyInfos infos = response.body();
                //Set Exercises
                if(infos.getCode() == 1){
                    float bmi = infos.getItems().get(0).getBmi();
                    if(bmi < 20){
                        setSkinny();
                    }else if(bmi >= 30){
                        setObesity();
                    }else{
                        String bodyShape = infos.getItems().get(0).getShape();
                        if(bodyShape.equals("Advanced Lower Body") || bodyShape.equals("Slightly Advanced Lower Body")){
                            setLowerBodyOverWeight();
                        }else{
                            setUpperBodyOverWeight();
                        }
                    }

                    //set listView
                    recyclerView = (ListView) findViewById(R.id.recyclerView);
                    adapter = new ExerciseAdapter(getApplicationContext(),datas);
                    //set listview's header
                    View v = getLayoutInflater().inflate(R.layout.item_exercise_header,null,false);
                    ListView.LayoutParams params = new ListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 900);
                    v.setLayoutParams(params);
                    ((TextView)v.findViewById(R.id.tv_name)).setText(String.format("%s's", SharedPreferenceBase.getSharedPreference("name","")));
                    ((TextView)v.findViewById(R.id.tv_shape)).setText(infos.getItems().get(0).getShape());
                    ((TextView)v.findViewById(R.id.tv_time)).setText(time + " min");
                    ((TextView)v.findViewById(R.id.tv_intensity)).setText(intensity);
                    ((TextView)v.findViewById(R.id.tv_level)).setText(level);

                    recyclerView.addHeaderView(v);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if(position == 0){
                                return;
                            }
                            Intent intent = new Intent(ExerciseActivity.this,VideoDialog.class);
                            intent.putExtra("url",datas.get(position-1).getVideoUrl());
                            startActivity(intent);
                        }
                    });
                    dialogTransparent.dismiss();
                    return;
                }
                Toast.makeText(ExerciseActivity.this, "SERVER_ERROR", Toast.LENGTH_SHORT).show();
                dialogTransparent.dismiss();
            }

            @Override
            public void onFailure(Call<BodyInfos> call, Throwable t) {
                Toast.makeText(ExerciseActivity.this, "SERVER_ERROR", Toast.LENGTH_SHORT).show();
                dialogTransparent.dismiss();
            }
        });
    }

    public void setLowerBodyOverWeight(){
        time = 40;
        level = "Beginner";
        intensity = "Weak";
        datas = new ArrayList<>();
        datas.add(new ExerciseData(R.drawable.g_vsitup,"All Day",20,3,""));
        datas.add(new ExerciseData(R.drawable.g_squat,"Squat",30,1,"aPYCiuiB4PA"));
        datas.add(new ExerciseData(R.drawable.g_pushup,"Push up",25,1,"IODxDxX7oi4"));
        datas.add(new ExerciseData(R.drawable.g_lunge,"Lunge",25,1,"QF0BQS2W80k"));
        datas.add(new ExerciseData(R.drawable.g_pullup,"Pull up",10,1,"eGo4IYlbE5g"));
        datas.add(new ExerciseData(R.drawable.g_vsitup,"V situp",20,1,"LPsepk-C-d4"));
        datas.add(new ExerciseData(R.drawable.g_climbing,"Climbing",30,1,"nmwgirgXLYM"));
        datas.add(new ExerciseData(R.drawable.g_burpee,"Burpee",30,1,"Pf7wZvraWV0"));
    }
    public void setSkinny(){
        time = 50;
        level = "Intermediate";
        intensity = "Medium";
        datas = new ArrayList<>();
        datas.add(new ExerciseData(R.drawable.g_vsitup,"Day 1",20,3,""));
        datas.add(new ExerciseData(R.drawable.g_running,"Stand running",5,1,"1VKryR2yjv0"));
        datas.add(new ExerciseData(R.drawable.g_dumbbellrow,"Dumbbell Row",15,3,"-koP10y1qZI"));
        datas.add(new ExerciseData(R.drawable.g_cablepullover,"Cable pullover",15,3,"NXaw3O2EUps"));
        datas.add(new ExerciseData(R.drawable.g_dumbbellcurl,"Dumbbell curl",15,3,"0AUGkch3tzc"));
        datas.add(new ExerciseData(R.drawable.g_vsitup,"V situp",20,3,"LPsepk-C-d4"));
        datas.add(new ExerciseData(R.drawable.g_vsitup,"Day 2",20,3,""));
        datas.add(new ExerciseData(R.drawable.g_running,"Stand running",5,1,"1VKryR2yjv0"));
        datas.add(new ExerciseData(R.drawable.g_dumbbellshoulderpress,"Dumbbell shoulder press",15,3,"0JfYxMRsUCQ"));
        datas.add(new ExerciseData(R.drawable.g_sidelateralraise,"Lateral raise",15,3,"3VcKaXpzqRo"));
        datas.add(new ExerciseData(R.drawable.g_lunge,"Lunge",20,3,"QF0BQS2W80k"));
        datas.add(new ExerciseData(R.drawable.g_squat,"Squat",15,3,"aPYCiuiB4PA"));
        datas.add(new ExerciseData(R.drawable.g_vsitup,"Day 3",20,3,""));
        datas.add(new ExerciseData(R.drawable.g_running,"Stand running",5,1,"1VKryR2yjv0"));
        datas.add(new ExerciseData(R.drawable.g_benchpress,"Bench Press",15,3,"uVp27_BdCJM"));
        datas.add(new ExerciseData(R.drawable.g_vsitup,"V situp",20,3,"LPsepk-C-d4"));
        datas.add(new ExerciseData(R.drawable.g_plank,"Plank",0,3,"IODxDxX7oi4"));
        datas.add(new ExerciseData(R.drawable.g_running,"Running",10,1,"LPsepk-C-d4"));
    }
    public void setObesity(){
        time = 60;
        level = "Intermediate";
        intensity = "Strong";
        datas = new ArrayList<>();
        datas.add(new ExerciseData(R.drawable.g_vsitup,"Day 1",20,3,""));
        datas.add(new ExerciseData(R.drawable.g_running,"Stand running",5,1,"1VKryR2yjv0"));
        datas.add(new ExerciseData(R.drawable.g_pullup,"Pull up",10,3,"eGo4IYlbE5g"));
        datas.add(new ExerciseData(R.drawable.g_vsitup,"V situp",0,3,"LPsepk-C-d4"));
        datas.add(new ExerciseData(R.drawable.g_plank,"Plank",0,3,"IODxDxX7oi4"));
        datas.add(new ExerciseData(R.drawable.g_running,"Running",30,1,"LPsepk-C-d4"));
        datas.add(new ExerciseData(R.drawable.g_vsitup,"Day 2",20,3,""));
        datas.add(new ExerciseData(R.drawable.g_running,"Stand running",5,1,"1VKryR2yjv0"));
        datas.add(new ExerciseData(R.drawable.g_dumbbellrow,"Dumbbell Row",15,3,"-koP10y1qZI"));
        datas.add(new ExerciseData(R.drawable.g_dumbbellcurl,"Dumbbell curl",15,3,"0AUGkch3tzc"));
        datas.add(new ExerciseData(R.drawable.g_lunge,"Lunge",20,4,"QF0BQS2W80k"));
        datas.add(new ExerciseData(R.drawable.g_squat,"Squat",15,4,"aPYCiuiB4PA"));
        datas.add(new ExerciseData(R.drawable.g_running,"Running",30,1,"LPsepk-C-d4"));
        datas.add(new ExerciseData(R.drawable.g_vsitup,"Day 3",20,3,""));
        datas.add(new ExerciseData(R.drawable.g_pushup,"Push up",10,5,"IODxDxX7oi4"));
        datas.add(new ExerciseData(R.drawable.g_dips,"Dips",10,5,"2z8JmcrW-As"));
        datas.add(new ExerciseData(R.drawable.g_vsitup,"V situp",0,3,"LPsepk-C-d4"));
        datas.add(new ExerciseData(R.drawable.g_plank,"Plank",0,3,"IODxDxX7oi4"));
        datas.add(new ExerciseData(R.drawable.g_running,"Running",30,1,"LPsepk-C-d4"));
    }

    private void setUpperBodyOverWeight(){
        time = 50;
        level = "Beginner";
        intensity = "Medium";
        datas = new ArrayList<>();
        datas.add(new ExerciseData(R.drawable.g_vsitup,"All Day",20,3,""));
        datas.add(new ExerciseData(R.drawable.g_squat,"Squat",20,1,"aPYCiuiB4PA"));
        datas.add(new ExerciseData(R.drawable.g_burpee,"Burpee",20,1,"Pf7wZvraWV0"));
        datas.add(new ExerciseData(R.drawable.g_vsitup,"V situp",20,1,"LPsepk-C-d4"));
        datas.add(new ExerciseData(R.drawable.g_pushup,"Push up",20,1,"IODxDxX7oi4"));
        datas.add(new ExerciseData(R.drawable.g_climbing,"Climbing",30,1,"nmwgirgXLYM"));
    }
}
