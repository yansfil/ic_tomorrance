package com.example.hoyeonlee.imaginecup.ViewModel;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.hoyeonlee.imaginecup.BackActionBarActivity;
import com.example.hoyeonlee.imaginecup.History.HistoryActivity;
import com.example.hoyeonlee.imaginecup.R;

public class ModelViewActivity extends BackActionBarActivity implements View.OnClickListener {

    WebView webView;
    Button historyButton;
    Button measureButton;
    ProgressDialog dialog;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        setToolbar();
        setTitle(getResources().getString(R.string.text_model));
        progressBar=new ProgressDialog(ModelViewActivity.this);
        progressBar.setMessage("다운로드중");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(true);

//        historyButton = (Button) findViewById(R.id.btn_history);
        measureButton = (Button) findViewById(R.id.btn_measure);
//        webView = (WebView) findViewById(R.id.webView);
//        dialog = ProgressDialog.show(this,"","",true);
//        //Event Listener 등록
//        historyButton.setOnClickListener(this);
        measureButton.setOnClickListener(this);
        //3d 모델 View Setting
//
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setSupportZoom(false);
//        webSettings.setDisplayZoomControls(false);
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.setVerticalScrollBarEnabled(true);
//        webView.setHorizontalScrollBarEnabled(true);
//        if(Build.VERSION.SDK_INT >= 21){
//            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                dialog.dismiss();
//            }
//        });
//        webView.loadUrl("http://34.212.159.48/view/45");
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if(v.getId() == R.id.btn_history){
            intent = new Intent(this,HistoryActivity.class);
        }else{
//            intent = new Intent(this,MeasureActivity.class);
            String url = "https://tomorrance.blob.core.windows.net/device1/1_2018-03-21%2014%3A27%3A07?st=2018-03-21T05%3A27%3A08Z&se=2118-03-21T05%3A27%3A08Z&sp=r&sv=2017-07-29&sr=b&sig=n1EIX04SEMEdPs6bFLe%2FhKdFUOk%2FFMsSCVMNxuWmZm0%3D";
            final DownloadFilesTask downloadTask = new DownloadFilesTask(ModelViewActivity.this,progressBar);
            downloadTask.execute(url);

            progressBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    downloadTask.cancel(true);
                }
            });

        }
//        startActivity(intent);
//        finish();
    }

}
