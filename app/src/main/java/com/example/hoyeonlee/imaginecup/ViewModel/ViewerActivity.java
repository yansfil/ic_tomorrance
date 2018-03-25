package com.example.hoyeonlee.imaginecup.ViewModel;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hoyeonlee.imaginecup.BackActionBarActivity;
import com.example.hoyeonlee.imaginecup.Network.ApiService;
import com.example.hoyeonlee.imaginecup.R;
import com.example.hoyeonlee.imaginecup.Utils.DownloadFilesTask;
import com.example.hoyeonlee.imaginecup.Utils.ModelLoadTask;
import com.example.hoyeonlee.imaginecup.Utils.StaticFunctions;
import com.example.hoyeonlee.imaginecup._Application;
import com.example.hoyeonlee.imaginecup.data.BodyInfo;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewerActivity extends BackActionBarActivity {
    private _Application app;
    private ViewGroup containerView;
    private ProgressBar progressBar;
    private ProgressDialog progressDiaglog;
    private ModelLoadTask modelLoadTask;
    ApiService apiService;
    private String modelUrl = null;
    private boolean isFirstCall = true;
    private static final String TAG = "VIEWER_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        setToolbar();
        setTitle(getResources().getString(R.string.text_history));

        app = _Application.getInstance();
        containerView = findViewById(R.id.container_view);
        progressBar = findViewById(R.id.model_progress_bar);

        modelLoadTask = new ModelLoadTask(this,progressBar,containerView);
        //Progress Dialog Create


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
                    progressDiaglog=new ProgressDialog(ViewerActivity.this);
                    progressDiaglog.setMessage("Downloading...");
                    progressDiaglog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDiaglog.setIndeterminate(true);
                    progressDiaglog.setCancelable(true);
                    modelUrl = response.body().getItem().getModel();
                    Uri uri = Uri.parse(modelUrl);
                    //Check 3d model already exists
                    File path= getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                    File outputFile= new File(path, modelLoadTask.getFileName(uri)); //파일명까지 포함함 경로의 File 객체 생성

                    if(outputFile.exists()){
                        Log.v(TAG,"File already Exists");
                        modelLoadTask.loadCurrentModel(outputFile);
                        return;
                    }
                    Log.v(TAG,"File Download");
                    //a model in network --> directory --> View
                    DownloadFilesTask downloadTask = new DownloadFilesTask(ViewerActivity.this,progressDiaglog,modelLoadTask);
                    downloadTask.execute(modelUrl);
                }else{
                    Toast.makeText(app, "SERVER ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BodyInfo> call, Throwable t) {
                Toast.makeText(app, "SERVER ERROR", Toast.LENGTH_SHORT).show();
                finish();
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
}
