package com.example.hoyeonlee.imaginecup.Measure;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hoyeonlee.imaginecup.BackActionBarActivity;
import com.example.hoyeonlee.imaginecup.Exercise.ExerciseActivity;
import com.example.hoyeonlee.imaginecup.R;

public class MeasureActivity extends BackActionBarActivity {
    Button exerciseButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);
        setToolbar();
        setTitle(getResources().getString(R.string.text_measure));
        exerciseButton = (Button) findViewById(R.id.btn_exercise);
        exerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ExerciseActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
