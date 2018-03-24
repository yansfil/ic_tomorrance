package com.example.hoyeonlee.imaginecup.ViewModel;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContentResolverCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hoyeonlee.imaginecup.Network.ApiService;
import com.example.hoyeonlee.imaginecup.R;
import com.example.hoyeonlee.imaginecup.ViewModel.obj.ObjModel;
import com.example.hoyeonlee.imaginecup.ViewModel.ply.PlyModel;
import com.example.hoyeonlee.imaginecup.ViewModel.stl.StlModel;
import com.example.hoyeonlee.imaginecup.ViewModel.util.Util;
import com.example.hoyeonlee.imaginecup._Application;
import com.example.hoyeonlee.imaginecup.data.Body;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ViewerActivity extends AppCompatActivity {
    private _Application app;
    @Nullable
    private ModelSurfaceView modelView;
    private ViewGroup containerView;
    private ProgressBar progressBar;
    private ProgressDialog progressDiaglog;

    Retrofit retrofit;
    ApiService apiService;
    private String modelUrl = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        app = _Application.getInstance();
        containerView = findViewById(R.id.container_view);
        progressBar = findViewById(R.id.model_progress_bar);
        progressBar.setVisibility(View.GONE);

        //Progress Dialog Create
        progressDiaglog=new ProgressDialog(ViewerActivity.this);
        progressDiaglog.setMessage("Downloading...");
        progressDiaglog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDiaglog.setIndeterminate(true);
        progressDiaglog.setCancelable(true);

        //Network Process
        ApiService apiService = _Application.getInstance().getApiService();
        Call<Body> bodyInfoCall = apiService.getBodyInfo();
        bodyInfoCall.enqueue(new Callback<Body>() {
            @Override
            public void onResponse(Call<Body> call, Response<Body> response) {
                if(response.isSuccessful()){
                    modelUrl = response.body().getItem().getModel();
                    Uri uri = Uri.parse(modelUrl);
                    ContentResolver cr = getApplicationContext().getContentResolver();

                    //Check 3d model already exists
                    File path= getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                    File outputFile= new File(path, getFileName(cr,uri)); //파일명까지 포함함 경로의 File 객체 생성
                    if(outputFile.exists()){
                        loadCurrentModel(outputFile);
                        return;
                    }
                    //a model in network --> view
                    new ModelLoadTask().execute(uri);
                    //a model in network --> directory
                    DownloadFilesTask downloadTask = new DownloadFilesTask(ViewerActivity.this);
                    downloadTask.execute(modelUrl);
                }else{
                    Toast.makeText(app, "Please Login again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Body> call, Throwable t) {
                Toast.makeText(app, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        findViewById(R.id.btn_measure).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadSampleModel();
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        createNewModelView(app.getCurrentModel());
        if (app.getCurrentModel() != null) {
            setTitle(app.getCurrentModel().getTitle());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (modelView != null) {
            modelView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (modelView != null) {
            modelView.onResume();
        }
    }



    private void createNewModelView(@Nullable Model model) {
        if (modelView != null) {
            containerView.removeView(modelView);
        }
        _Application.getInstance().setCurrentModel(model);
        modelView = new ModelSurfaceView(this, model);
        containerView.addView(modelView, 0);
    }
    private void setCurrentModel(@NonNull Model model) {
        createNewModelView(model);
        Toast.makeText(getApplicationContext(), R.string.open_model_success, Toast.LENGTH_SHORT).show();
        setTitle(model.getTitle());
        progressBar.setVisibility(View.GONE);
    }

    private void loadCurrentModel(@NonNull File modelFile) {
        try {
            InputStream stream = new FileInputStream(modelFile);
            setCurrentModel(new ObjModel(stream));
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), R.string.open_model_success, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }

    @Nullable
    private String getFileName(@NonNull ContentResolver cr, @NonNull Uri uri) {
        if ("content".equals(uri.getScheme())) {
            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
            Cursor metaCursor = ContentResolverCompat.query(cr, uri, projection, null, null, null, null);
            if (metaCursor != null) {
                try {
                    if (metaCursor.moveToFirst()) {
                        return metaCursor.getString(0);
                    }
                } finally {
                    metaCursor.close();
                }
            }
        }
        return uri.getLastPathSegment();
    }

    private class ModelLoadTask extends AsyncTask<Uri, Integer, Model> {
        protected Model doInBackground(Uri... file) {
            InputStream stream = null;
            try {
                Uri uri = file[0];
                ContentResolver cr = getApplicationContext().getContentResolver();
                String fileName = getFileName(cr, uri);

                if ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(uri.toString()).build();
                    okhttp3.Response response = client.newCall(request).execute();

                    // TODO: figure out how to NOT need to read the whole file at once.
                    stream = new ByteArrayInputStream(response.body().bytes());
                } else {
                    stream = cr.openInputStream(uri);
                }

                if (stream != null) {
                    Model model;
                    if (!TextUtils.isEmpty(fileName)) {
                        if (fileName.toLowerCase().endsWith(".stl")) {
                            model = new StlModel(stream);
                        } else if (fileName.toLowerCase().endsWith(".obj")) {
                            model = new ObjModel(stream);
                        } else if (fileName.toLowerCase().endsWith(".ply")) {
                            model = new PlyModel(stream);
                        } else {
                            // assume it's STL.
                            model = new ObjModel(stream);
                        }
                        model.setTitle(fileName);
                    } else {
                        // assume it's STL.
                        // TODO: autodetect file type by reading contents?
                        model = new StlModel(stream);
                    }
                    return model;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Util.closeSilently(stream);
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Model model) {
            if (isDestroyed()) {
                return;
            }
            if (model != null) {
                setCurrentModel(model);
            } else {
                Toast.makeText(getApplicationContext(), R.string.open_model_error, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }
//
    }

}
