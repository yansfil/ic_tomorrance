package com.example.hoyeonlee.imaginecup.History;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;

import com.example.hoyeonlee.imaginecup.BackActionBarActivity;
import com.example.hoyeonlee.imaginecup.R;
import com.example.hoyeonlee.imaginecup.databinding.ActivityHistoryBinding;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

//데이터 바인딩 간단 사용
public class HistoryActivity extends BackActionBarActivity {

    ActivityHistoryBinding layout;
    public String date = "날짜 : 2018-02-21";
    public String shape = "타입 : 하체발달형";
    public String tall = "키 : 172cm";
    public String weight = "몸무게 : 78kg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = DataBindingUtil.setContentView(this,R.layout.activity_history);
        setToolbar();
        setTitle(getResources().getString(R.string.text_history));
        layout.setActivity(this);
        final List<String> dataset = new LinkedList<>(Arrays.asList("2018-02-21", "2018-01-21", "2017-12-21", "2017-11-21", "2017-10-21"));
        layout.spinnerDate.attachDataSource(dataset);
        layout.spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                date = "날짜 : "+dataset.get(position);
                if(position == 1){
                    weight = "몸무게 : 83kg";
                }else if(position == 2){
                    weight = "몸무게 : 87kg";
                }
                layout.setActivity(HistoryActivity.this);
                layout.webView.loadUrl("http://34.212.159.48/view/45");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        settingWebView("http://34.212.159.48/view/45");
    }

    void settingWebView(String url){
        WebSettings webSettings = layout.webView.getSettings();
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        layout.webView.setVerticalScrollBarEnabled(true);
        layout.webView.setHorizontalScrollBarEnabled(true);
        if(Build.VERSION.SDK_INT >= 21){
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        layout.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        layout.webView.loadUrl(url);
    }
}
