package com.example.hoyeonlee.imaginecup.Utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContentResolverCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hoyeonlee.imaginecup.R;
import com.example.hoyeonlee.imaginecup.ViewModel.Model;
import com.example.hoyeonlee.imaginecup.ViewModel.ModelSurfaceView;
import com.example.hoyeonlee.imaginecup.ViewModel.obj.ObjModel;
import com.example.hoyeonlee.imaginecup.ViewModel.ply.PlyModel;
import com.example.hoyeonlee.imaginecup.ViewModel.stl.StlModel;
import com.example.hoyeonlee.imaginecup.ViewModel.util.Util;
import com.example.hoyeonlee.imaginecup._Application;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by hoyeonlee on 2018. 3. 24..
 */

public class ModelLoadTask extends AsyncTask<Uri, Integer, Model> {
    Context context;
    ProgressBar progressBar;
    public ModelSurfaceView modelView;
    private ViewGroup containerView;

    public ModelLoadTask(Context context, ProgressBar progressBar, ViewGroup containerView){
        this.context = context;
        this.progressBar = progressBar;
        this.containerView = containerView;
    }

    protected Model doInBackground(Uri... file) {
        InputStream stream = null;
        try {
            Uri uri = file[0];
            ContentResolver cr = context.getContentResolver();
            String fileName = getFileName(uri);

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
        if (((Activity)context).isDestroyed()) {
            return;
        }
        if (model != null) {
            setCurrentModel(model);
        } else {
            Toast.makeText(context, R.string.open_model_error, Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }
    public void createNewModelView(@Nullable Model model) {
        containerView.removeAllViews();
        _Application.getInstance().setCurrentModel(model);
        modelView = new ModelSurfaceView(context, model);
        containerView.addView(modelView, 0);
    }
    public void setCurrentModel(@NonNull Model model) {
        createNewModelView(model);
        Toast.makeText(context, R.string.open_model_success, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }

    public void loadCurrentModel(@NonNull File modelFile) {
        try {
            InputStream stream = new FileInputStream(modelFile);
            setCurrentModel(new ObjModel(stream));
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, R.string.open_model_success, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }
    @Nullable
    public String getFileName(@NonNull Uri uri) {
        ContentResolver cr = context.getContentResolver();
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
}
