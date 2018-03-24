package com.example.hoyeonlee.imaginecup.History;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hoyeonlee.imaginecup.BackActionBarActivity;
import com.example.hoyeonlee.imaginecup.Network.ApiService;
import com.example.hoyeonlee.imaginecup.R;
import com.example.hoyeonlee.imaginecup.Utils.ModelLoadTask;
import com.example.hoyeonlee.imaginecup.Utils.StaticFunctions;
import com.example.hoyeonlee.imaginecup.ViewModel.DownloadFilesTask;
import com.example.hoyeonlee.imaginecup._Application;
import com.example.hoyeonlee.imaginecup.data.BodyInfos;
import com.example.hoyeonlee.imaginecup.data.Item;

import org.angmarch.views.NiceSpinner;

import java.io.File;
import java.util.ArrayList;
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
    private ViewGroup containerView;
    private ProgressBar progressBar;
    private ModelLoadTask modelLoadTask;
    private ArrayList<String> modelUrlList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setToolbar();
        setTitle(getResources().getString(R.string.text_history));

        containerView = findViewById(R.id.container_view);
        progressBar = findViewById(R.id.model_progress_bar);
        progressBar.setVisibility(View.GONE);
        modelLoadTask = new ModelLoadTask(this,progressBar,containerView);
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
                }else
                    Toast.makeText(HistoryActivity.this, "SERVER ERROR", Toast.LENGTH_SHORT).show();
                //put dates into Spinner
                dateSpinner.attachDataSource(dateSet);
                modelLoad(0);
            }

            @Override
            public void onFailure(Call<BodyInfos> call, Throwable t) {
                Toast.makeText(HistoryActivity.this, "SERVER ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        //set DateSpinner
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelLoad(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        modelLoadTask.createNewModelView(app.getCurrentModel());
        if (app.getCurrentModel() != null) {
            setTitle(app.getCurrentModel().getTitle());
        }
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

    private void modelLoad(int position){
        Uri modelUrl = Uri.parse(bodyInfos.getItems().get(position).getModel());
        File path= getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File outputFile= new File(path, modelLoadTask.getFileName(modelUrl)); //파일명까지 포함함 경로의 File 객체 생성
        if(outputFile.exists()){
            modelLoadTask.loadCurrentModel(outputFile);
            return;
        }
        modelLoadTask = new ModelLoadTask(this,progressBar,containerView);
        // a model in network --> view
        modelLoadTask.execute(modelUrl);
        //a model in network --> directory
        DownloadFilesTask downloadTask = new DownloadFilesTask(HistoryActivity.this);
        downloadTask.execute(bodyInfos.getItems().get(position).getModel());
    }
}
