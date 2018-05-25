package com.tomorrance.yonsei.tomo.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.tomorrance.yonsei.tomo._Application;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hoyeonlee on 2018. 3. 17..
 */

public class SharedPreferenceBase{
    private static SharedPreferences sp;
    public static void putSharedPreference(String key, HashSet<String> value){
        sp = _Application.getInstance().getSharedPreferences("storage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(key,value);
        editor.commit();
    }
    public static void putSharedPreference(String key, String value){
        sp = _Application.getInstance().getSharedPreferences("storage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static Set<String> getSharedPreference(String key, @Nullable Set<String> defaultValue){
        sp = _Application.getInstance().getSharedPreferences("storage", Context.MODE_PRIVATE);
        return sp.getStringSet(key,defaultValue);
    }

    public static String getSharedPreference(String key, @Nullable String defaultValue){
        sp = _Application.getInstance().getSharedPreferences("storage", Context.MODE_PRIVATE);
        return sp.getString(key,defaultValue);
    }

}
