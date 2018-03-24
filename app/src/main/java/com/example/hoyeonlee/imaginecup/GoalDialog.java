package com.example.hoyeonlee.imaginecup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

/**
 * Created by hoyeonlee on 2018. 3. 25..
 */

public class GoalDialog extends Dialog {
    MainActivity context;
    String goal = "";
    String intensity = "";
    public GoalDialog(@NonNull Context context) {
        super(context);
        this.context = (MainActivity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_goal);
        Button button = findViewById(R.id.btn_complete);
        final RadioGroup goalGroup = findViewById(R.id.group_goal);
        final RadioGroup intensityGroup = findViewById(R.id.group_intensity);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (goalGroup.getCheckedRadioButtonId()){
                    case R.id.goal1:
                        goal = "loseWeight";
                        break;
                    case R.id.goal2:
                        goal = "gainWeight";
                        break;
                    case R.id.goal3:
                        goal = "gainMuscle";
                        break;
                    default:
                        goal = "loseWeight";
                        break;
                }
                switch (intensityGroup.getCheckedRadioButtonId()){
                    case R.id.intensity1:
                        intensity = "weak";
                        break;
                    case R.id.intensity2:
                        intensity = "moderate";
                        break;
                    case R.id.intensity3:
                        intensity = "strong";
                        break;
                    default:
                        intensity = "weak";
                        break;
                }
                context.setGoal(goal,intensity);
                cancel();
            }
        });
    }
}
