package com.tomorrance.yonsei.tomo.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.tomorrance.yonsei.tomo.Login.SigninActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hoyeonlee on 2018. 3. 24..
 */

public class StaticFunctions {
    private Context context;
    private static StaticFunctions instance;

    public StaticFunctions(Context context){
        this.context = context;
    }
    public static StaticFunctions getInstance(Context context){
        if(instance == null){
            instance = new StaticFunctions(context);
        }
        return instance;
    }

    public void goFirstPage(){
        SharedPreferences sp = context.getSharedPreferences("login",MODE_PRIVATE);
        sp.edit().putBoolean("login",false).commit();
        Toast.makeText(context, "SESSION OUTDATED. PLEASE LOGIN AGAIN", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, SigninActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
