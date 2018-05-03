package com.tomorrance.yonsei.tomo.History;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.tomorrance.yonsei.tomo.BackActionBarActivity;
import com.tomorrance.yonsei.tomo.Network.ApiService;
import com.tomorrance.yonsei.tomo.R;
import com.tomorrance.yonsei.tomo.Utils.DownloadFilesTask;
import com.tomorrance.yonsei.tomo.Utils.ModelLoadTask;
import com.tomorrance.yonsei.tomo.Utils.StaticFunctions;
import com.tomorrance.yonsei.tomo._Application;
import com.tomorrance.yonsei.tomo.data.BodyInfos;
import com.tomorrance.yonsei.tomo.data.Item;

import org.angmarch.views.NiceSpinner;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends BackActionBarActivity {


    NiceSpinner dateSpinner;
    List<String> dateSet;
    BodyInfos bodyInfos;
    ApiService apiService;
    _Application app;

    private TextView weightView;
    private TextView bmiView;
    private TextView shapeView;
    private ViewGroup containerView;
    Dialog dialogTransparent;
    private ModelLoadTask modelLoadTask;
    boolean isFirstCall= true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setToolbar();
        setTitle(getResources().getString(R.string.text_history));
        weightView = findViewById(R.id.tv_weight);
        bmiView = findViewById(R.id.tv_bmi);
        shapeView = findViewById(R.id.tv_shape);
        containerView = findViewById(R.id.container_view);
        //Set Loading dialog
        dialogTransparent = new Dialog(this, android.R.style.Theme_Black);
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_loading, null);
        dialogTransparent.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogTransparent.getWindow().setBackgroundDrawableResource(
                R.color.transparent_dialog);
        dialogTransparent.setContentView(view);
        dialogTransparent.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                HistoryActivity.this.finish();
            }
        });
        dialogTransparent.show();
        modelLoadTask = new ModelLoadTask(this,dialogTransparent,containerView);

        app = _Application.getInstance();
        dateSet = new LinkedList<>();
        dateSpinner = findViewById(R.id.spinner_date);
        apiService = _Application.getInstance().getApiService();

        apiService.getAllInfo().enqueue(new Callback<BodyInfos>() {
            @Override
            public void onResponse(Call<BodyInfos> call, Response<BodyInfos> response) {
                if(response.code() == 401){
                    StaticFunctions.getInstance(HistoryActivity.this).goFirstPage();
                    return;
                }
                bodyInfos = response.body();
                if(bodyInfos.getCode() == 1){
                    for(Item item : bodyInfos.getItems()){
                        dateSet.add(item.getTimestamp());
                    }
                    Item info = bodyInfos.getItems().get(0);
                    dateSpinner.attachDataSource(dateSet);
                    modelLoad(0);
                    dataBind(String.valueOf(info.getWeight()),info.getBmi()+"",info.getShape());
                }else
                    Toast.makeText(HistoryActivity.this, "SERVER ERROR", Toast.LENGTH_SHORT).show();
                //put dates into Spinner
            }

            @Override
            public void onFailure(Call<BodyInfos> call, Throwable t) {
                Toast.makeText(HistoryActivity.this, "SERVER ERROR", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        //set DateSpinner
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelLoad(position);
                Item info = bodyInfos.getItems().get(position);
                dataBind(String.valueOf(info.getWeight()),info.getBmi()+"",info.getShape());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(!isFirstCall){
            modelLoadTask.createNewModelView(modelLoadTask.getCurrentModel());
        }
        isFirstCall = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (modelLoadTask.modelView != null) {
            modelLoadTask.modelView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (modelLoadTask.modelView!= null) {
            modelLoadTask.modelView.onResume();
        }
    }
    private void dataBind(String weight,String bmi,String shape){
        weightView.setText(weight+"kg");
        bmiView.setText(bmi);
        shapeView.setText(shape);
    }

    private void modelLoad(int position){
        dialogTransparent.show();
        modelLoadTask = new ModelLoadTask(this,dialogTransparent,containerView);
        Uri modelUrl = Uri.parse(bodyInfos.getItems().get(position).getModel());
        File path= getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File outputFile= new File(path, modelLoadTask.getFileName(modelUrl)); //파일명까지 포함함 경로의 File 객체 생성
        if(outputFile.exists()){
            modelLoadTask.loadCurrentModel(outputFile,position);
            return;
        }
        //a model in network --> directory --> View
        DownloadFilesTask downloadTask = new DownloadFilesTask(HistoryActivity.this,modelLoadTask,position);
        downloadTask.execute(bodyInfos.getItems().get(position).getModel());
    }
}
