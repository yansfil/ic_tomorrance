package com.example.hoyeonlee.imaginecup.Status;

import android.os.Bundle;

import com.example.hoyeonlee.imaginecup.BackActionBarActivity;
import com.example.hoyeonlee.imaginecup.R;

public class StatusActivity extends BackActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        setToolbar();
        setTitle(R.color.text_color_soft);
        setTitle(getResources().getString(R.string.text_measure));
    }
}
