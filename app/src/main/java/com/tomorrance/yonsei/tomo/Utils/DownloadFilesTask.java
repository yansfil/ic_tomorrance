package com.tomorrance.yonsei.tomo.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContentResolverCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by hoyeonlee on 2018. 3. 22..
 */

public class DownloadFilesTask extends AsyncTask<String,String,Long>{
    private Context context;
    private PowerManager.WakeLock mWakeLock;
    private File path;
    private File outputFile;
    private ModelLoadTask modelLoadTask;
    private int position = 0;
    public DownloadFilesTask(Context context, ModelLoadTask modelLoadTask, int position) {
        this.position = position;
        this.context = context;
        this.modelLoadTask = modelLoadTask;
    }

    //파일 다운로드를 시작하기 전에 프로그레스바를 화면에 보여줍니다.
    @Override
    protected void onPreExecute() { //2
        super.onPreExecute();

        //사용자가 다운로드 중 파워 버튼을 누르더라도 CPU가 잠들지 않도록 해서
        //다시 파워버튼 누르면 그동안 다운로드가 진행되고 있게 됩니다.
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        mWakeLock.acquire();
    }


    //파일 다운로드를 진행합니다.
    @Override
    protected Long doInBackground(String... string_url) { //3
        int count;
        long FileSize = -1;
        InputStream input = null;
        OutputStream output = null;
        URLConnection connection = null;

        try {
            URL url = new URL(string_url[0]);
            connection = url.openConnection();
            connection.connect();


            //파일 크기를 가져옴
            FileSize = connection.getContentLength() / 1024;

            //URL 주소로부터 파일다운로드하기 위한 input stream
            input = new BufferedInputStream(url.openStream());

            //TODO 저장소 상황별로 어떻게 모델을 다운로드할지 정의할 것.
            path= context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            ContentResolver cr = context.getContentResolver();
            Uri uri = Uri.parse(string_url[0]);
            outputFile= new File(path, getFileName(cr,uri)); //파일명까지 포함함 경로의 File 객체 생성

            // 내부 저장소에 저장하기 위한 Output stream
            output = new FileOutputStream(outputFile);


            byte data[] = new byte[1024];
            long downloadedSize = 0;
            while ((count = input.read(data)) != -1) {
                //사용자가 BACK 버튼 누르면 취소가능
                if (isCancelled()) {
                    input.close();
                    return Long.valueOf(-1);
                }

                downloadedSize += count/1024;

                if (FileSize > 0) {
                    float per = ((float)downloadedSize/FileSize) * 100;
                    String str = "Downloaded " + downloadedSize + "KB / " + FileSize + "KB (" + (int)per + "%)";
                    publishProgress("" + (int) ((downloadedSize * 100) / FileSize), str);
                }

                //파일에 데이터를 기록합니다.
                output.write(data, 0, count);
            }
            // Flush output
            output.flush();

            // Close streams
            output.close();
            input.close();


        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            mWakeLock.release();

        }
        return FileSize;
    }


    //다운로드 중 프로그레스바 업데이트
    @Override
    protected void onProgressUpdate(String... progress) { //4
        super.onProgressUpdate(progress);
    }

    //파일 다운로드 완료 후
    @Override
    protected void onPostExecute(Long size) { //5
        super.onPostExecute(size);
        if ( size > 0) {
            modelLoadTask.loadCurrentModel(outputFile,position);
        }
        else
            Toast.makeText(context, "DOWNLOAD ERROR", Toast.LENGTH_LONG).show();
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
}
