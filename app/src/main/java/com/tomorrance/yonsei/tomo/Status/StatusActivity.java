package com.tomorrance.yonsei.tomo.Status;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.tomorrance.yonsei.tomo.BackActionBarActivity;
import com.tomorrance.yonsei.tomo.Network.ApiService;
import com.tomorrance.yonsei.tomo.R;
import com.tomorrance.yonsei.tomo.Utils.StaticFunctions;
import com.tomorrance.yonsei.tomo._Application;
import com.tomorrance.yonsei.tomo.data.BodyInfos;
import com.tomorrance.yonsei.tomo.data.Item;
import com.tomorrance.yonsei.tomo.data.Size;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusActivity extends BackActionBarActivity {
    private boolean isFirst = true;
//    private ActivityStatu binding;
    ApiService apiService;
    Dialog dialogTransparent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = DataBindingUtil.setContentView(this,R.layout.activity_status);
        setToolbar();
        setColor(R.color.text_color_soft);
        setTitle(getResources().getString(R.string.text_measure));


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
                    StaticFunctions.getInstance(StatusActivity.this).goFirstPage();
                    return;
                }


                BodyInfos infos = response.body();

                //Public Setting
//                binding.tvName.setText(SharedPreferenceBase.getSharedPreference("name","???"));
                isFirst = infos.getItems().size() <= 1 ? true : false;
                if(infos.getCode() == 1){
                    if(isFirst){
                        setFirstEnv();
                        putFirstData(infos.getItems().get(0));
                    }else{
                        putData(infos.getItems());
                    }
                    dialogTransparent.dismiss();
                    return;
                }
                Toast.makeText(StatusActivity.this, "SERVER_ERROR", Toast.LENGTH_SHORT).show();
                dialogTransparent.dismiss();
            }

            @Override
            public void onFailure(Call<BodyInfos> call, Throwable t) {
                Toast.makeText(StatusActivity.this, "SERVER_ERROR", Toast.LENGTH_SHORT).show();
                dialogTransparent.dismiss();
            }
        });

    }

    private void putData(ArrayList<Item> data){
        //Now data Setting
//        binding.tvBiacromion.setText(data.get(0).getSize().getBiacromion()+"cm");
//        binding.tvUpperarm.setText(data.get(0).getSize().getUpperArm()+"cm");
//        binding.tvChest.setText(data.get(0).getSize().getChest()+"cm");
//        binding.tvWaist.setText(data.get(0).getSize().getWaist()+"cm");
//        binding.tvHip.setText(data.get(0).getSize().getHip()+"cm");
//        binding.tvMidthigh.setText(data.get(0).getSize().getMidThigh()+"cm");
//        binding.tvCalf.setText(data.get(0).getSize().getCalf()+"cm");
//
//        //Prev data Setting
//        binding.tvPrevBiacromion.setText(data.get(1).getSize().getBiacromion()+"cm");
//        binding.tvPrevUpperarm.setText(data.get(1).getSize().getUpperArm()+"cm");
//        binding.tvPrevChest.setText(data.get(1).getSize().getChest()+"cm");
//        binding.tvPrevWaist.setText(data.get(1).getSize().getWaist()+"cm");
//        binding.tvPrevHip.setText(data.get(1).getSize().getHip()+"cm");
//        binding.tvPrevMidthigh.setText(data.get(1).getSize().getMidThigh()+"cm");
//        binding.tvPrevCalf.setText(data.get(1).getSize().getCalf()+"cm");
//
//        //Analys Setting
//        binding.tvShape.setText(data.get(0).getShape());
//        binding.tvObesity.setText(getDegreeOfObesity(data.get(0).getBmi())+"("+data.get(0).getBmi()+")");
//        binding.tvChanged.setText(getMostChangedPart(data));
//        int weightVariation = data.get(0).getWeight()-data.get(1).getWeight();
//        if(weightVariation<=0){
//            binding.tvVariation.setText(weightVariation+"kg");
//        }else{
//            binding.tvVariation.setText("+"+weightVariation+"kg");
//        }
    }

    private void putFirstData(Item data){
//        binding.tvBiacromion.setText(data.getSize().getBiacromion()+"cm");
//        binding.tvUpperarm.setText(data.getSize().getUpperArm()+"cm");
//        binding.tvChest.setText(data.getSize().getChest()+"cm");
//        binding.tvWaist.setText(data.getSize().getWaist()+"cm");
//        binding.tvHip.setText(data.getSize().getHip()+"cm");
//        binding.tvMidthigh.setText(data.getSize().getMidThigh()+"cm");
//        binding.tvCalf.setText(data.getSize().getCalf()+"cm");
//
//        //Analys Setting
//        binding.tvShape.setText(data.getShape());
//        binding.tvObesity.setText(getDegreeOfObesity(data.getBmi())+"("+data.getBmi()+")");
    }

    private void setFirstEnv(){
//        binding.layoutPrev.setVisibility(View.INVISIBLE);
//        binding.layoutNow.setVisibility(View.INVISIBLE);
//        binding.tvPrevBiacromion.setVisibility(View.GONE);
//        binding.tvPrevCalf.setVisibility(View.GONE);
//        binding.tvPrevChest.setVisibility(View.GONE);
//        binding.tvPrevHip.setVisibility(View.GONE);
//        binding.tvPrevMidthigh.setVisibility(View.GONE);
//        binding.tvPrevUpperarm.setVisibility(View.GONE);
//        binding.tvPrevWaist.setVisibility(View.GONE);
//        binding.layoutChanged.setVisibility(View.GONE);
//        binding.layoutVariation.setVisibility(View.GONE);
    }


    private String getMostChangedPart(ArrayList<Item> data){
        Size nowData =  data.get(0).getSize();
        Size prevData = data.get(1).getSize();
        try {
            HashMap<String, Float> map = new HashMap<>();
            ValueComparator bvc = new ValueComparator(map);
            TreeMap<String, Float> sorted_map = new TreeMap<>(bvc);
            map.put("biacromion", (nowData.getBiacromion() - prevData.getBiacromion()) / prevData.getBiacromion());
            map.put("chest", (nowData.getChest() - prevData.getChest()) / prevData.getChest());
            map.put("upperArm", (nowData.getUpperArm() - prevData.getUpperArm()) / prevData.getUpperArm());
            map.put("waist", (nowData.getWaist() - prevData.getWaist()) / prevData.getWaist());
            map.put("hip", (nowData.getHip() - prevData.getHip()) / prevData.getHip());
            map.put("midThigh", (nowData.getMidThigh() - prevData.getMidThigh()) / prevData.getMidThigh());
            map.put("calf", (nowData.getCalf() - prevData.getCalf()) / prevData.getCalf());
            sorted_map.putAll(map);
            return sorted_map.firstKey();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    private String getDegreeOfObesity(float bmi){
        String degree = "skinny";
        if(bmi < 20){
            degree = "Skinny";
        }else if(bmi < 25){
            degree = "Average";
        }else if(bmi < 30){
            degree = "Overweight";
        }else{
            degree = "Obese";
        }
        return degree;
    }
    class ValueComparator implements Comparator<String> {
        Map<String, Float> base;

        public ValueComparator(Map<String, Float> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        public int compare(String a, String b) {
            if (base.get(a) <= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
}
