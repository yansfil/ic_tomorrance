package com.tomorrance.yonsei.tomo.ViewModel;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.tomorrance.yonsei.tomo.BackActionBarActivity;
import com.tomorrance.yonsei.tomo.Network.ApiService;
import com.tomorrance.yonsei.tomo.Network.SharedPreferenceBase;
import com.tomorrance.yonsei.tomo.R;
import com.tomorrance.yonsei.tomo.Utils.DownloadFilesTask;
import com.tomorrance.yonsei.tomo.Utils.ModelLoadTask;
import com.tomorrance.yonsei.tomo.Utils.StaticFunctions;
import com.tomorrance.yonsei.tomo._Application;
import com.tomorrance.yonsei.tomo.data.BodyInfo;
import com.tomorrance.yonsei.tomo.data.Item;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewerActivity extends BackActionBarActivity {
    private _Application app;
    private ViewGroup containerView;
    private ModelLoadTask modelLoadTask;
    private TextView heightView;
    private TextView weightView;
    private TextView bmiView;
    private TextView shapeView;
    ApiService apiService;
    private String modelUrl = null;
    private boolean isFirstCall = true;
    private static final String TAG = "VIEWER_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        setToolbar();
        setTitle(getResources().getString(R.string.text_model));
        //Views initialization
        heightView = findViewById(R.id.tv_height);
        weightView = findViewById(R.id.tv_weight);
        bmiView = findViewById(R.id.tv_bmi);
        shapeView = findViewById(R.id.tv_shape);
        app = _Application.getInstance();
        containerView = findViewById(R.id.container_view);

        //Loading Dialog
        Dialog dialogTransparent = new Dialog(this, android.R.style.Theme_Black);
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_loading, null);
        dialogTransparent.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogTransparent.getWindow().setBackgroundDrawableResource(
                R.color.transparent_dialog);
        dialogTransparent.setContentView(view);
        dialogTransparent.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ViewerActivity.this.finish();
            }
        });
        dialogTransparent.show();
        modelLoadTask = new ModelLoadTask(this,dialogTransparent,containerView);
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
                    Item item = response.body().getItem();
                    modelUrl = item.getModel();
                    Uri uri = Uri.parse(modelUrl);
                    //Check 3d model already exists
                    File path= getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                    File outputFile= new File(path, modelLoadTask.getFileName(uri)); //파일명까지 포함함 경로의 File 객체 생성

                    if(outputFile.exists()){
                        Log.v(TAG,"File already Exists");
                        modelLoadTask.loadCurrentModel(outputFile,0);
                    }else {
                        Log.v(TAG, "File Download");
                        //a model in network --> directory --> View
                        DownloadFilesTask downloadTask = new DownloadFilesTask(ViewerActivity.this, modelLoadTask, 0);
                        downloadTask.execute(modelUrl);
                    }
                    dataBind(item.getWeight()+"",item.getBmi()+"",item.getShape());

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
    private void dataBind(String weight,String bmi,String shape){
        heightView.setText(SharedPreferenceBase.getSharedPreference("height","0")+"cm");
        weightView.setText(weight+"kg");
        bmiView.setText(bmi);
        shapeView.setText(shape);
    }
}
