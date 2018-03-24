package com.example.hoyeonlee.imaginecup.ViewModel;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hoyeonlee.imaginecup.Network.ApiService;
import com.example.hoyeonlee.imaginecup.R;
import com.example.hoyeonlee.imaginecup.Utils.ModelLoadTask;
import com.example.hoyeonlee.imaginecup.Utils.StaticFunctions;
import com.example.hoyeonlee.imaginecup._Application;
import com.example.hoyeonlee.imaginecup.data.BodyInfo;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewerActivity extends AppCompatActivity {
    private _Application app;
    private ViewGroup containerView;
    private ProgressBar progressBar;
    private ProgressDialog progressDiaglog;
    private ModelLoadTask modelLoadTask;
    ApiService apiService;
    private String modelUrl = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        app = _Application.getInstance();
        containerView = findViewById(R.id.container_view);
        progressBar = findViewById(R.id.model_progress_bar);
        progressBar.setVisibility(View.GONE);
        modelLoadTask = new ModelLoadTask(this,progressBar,containerView);
        //Progress Dialog Create
        progressDiaglog=new ProgressDialog(ViewerActivity.this);
        progressDiaglog.setMessage("Downloading...");
        progressDiaglog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDiaglog.setIndeterminate(true);
        progressDiaglog.setCancelable(true);

        //Network Process
        apiService = _Application.getInstance().getApiService();
        apiService.getRecentInfo().enqueue(new Callback<BodyInfo>() {
            @Override
            public void onResponse(Call<BodyInfo> call, Response<BodyInfo> response) {
                if(response.code() == 401){
                    StaticFunctions.getInstance(ViewerActivity.this).goFirstPage();
                    return;
                }
                if(response.body().getCode() == 1){
                    modelUrl = response.body().getItem().getModel();
                    Uri uri = Uri.parse(modelUrl);
                    //Check 3d model already exists
                    File path= getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                    File outputFile= new File(path, modelLoadTask.getFileName(uri)); //파일명까지 포함함 경로의 File 객체 생성
                    if(outputFile.exists()){
                        modelLoadTask.loadCurrentModel(outputFile);
                        return;
                    }
                    //a model in network --> view
                    modelLoadTask.execute(uri);
                    //a model in network --> directory
                    DownloadFilesTask downloadTask = new DownloadFilesTask(ViewerActivity.this);
                    downloadTask.execute(modelUrl);
                }else{
                    Toast.makeText(app, "SERVER ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BodyInfo> call, Throwable t) {
                Toast.makeText(app, "SERVER ERROR", Toast.LENGTH_SHORT).show();
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
}
