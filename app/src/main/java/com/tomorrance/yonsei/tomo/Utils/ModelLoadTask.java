package com.tomorrance.yonsei.tomo.Utils;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.ViewGroup;
import android.widget.Toast;

import com.tomorrance.yonsei.tomo.R;
import com.tomorrance.yonsei.tomo.ViewModel.Model;
import com.tomorrance.yonsei.tomo.ViewModel.ModelSurfaceView;
import com.tomorrance.yonsei.tomo.ViewModel.obj.ObjModel;
import com.tomorrance.yonsei.tomo.ViewModel.ply.PlyModel;
import com.tomorrance.yonsei.tomo.ViewModel.stl.StlModel;
import com.tomorrance.yonsei.tomo.ViewModel.util.Util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by hoyeonlee on 2018. 3. 24..
 */

public class ModelLoadTask extends AsyncTask<Uri, Integer, Model> {
    Context context;
    Dialog progressBar;
    public ModelSurfaceView modelView;
    private ViewGroup containerView;
    int position = 0;
    private Model currentModel;
    public ModelLoadTask(Context context, Dialog progressBar, ViewGroup containerView){
        this.context = context;
        this.progressBar = progressBar;
        this.containerView = containerView;
    }

    protected Model doInBackground(Uri... parmas) {
        InputStream stream = null;
        try {
            Uri uri = (Uri)parmas[0];
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
            progressBar.dismiss();
        }
    }
    public void createNewModelView(@Nullable Model model) {
        containerView.removeAllViews();

//        if(containerView.getChildAt(position) == null){
        modelView = new ModelSurfaceView(context, model);
        containerView.addView(modelView);
//        }else
//            containerView.bringChildToFront(containerView.getChildAt(position));

    }
    public void setCurrentModel(@NonNull Model model) {
        currentModel = model;
        createNewModelView(model);
        progressBar.dismiss();
    }
    @Nullable
    public Model getCurrentModel() {
        return currentModel;
    }

    public void loadCurrentModel(@NonNull File modelFile,int position) {
        this.position = position;
        this.execute(Uri.fromFile(modelFile));
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
