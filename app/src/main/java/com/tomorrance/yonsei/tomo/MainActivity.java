package com.tomorrance.yonsei.tomo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.tomorrance.yonsei.tomo.Exercise.ExerciseActivity;
import com.tomorrance.yonsei.tomo.History.HistoryActivity;
import com.tomorrance.yonsei.tomo.Login.SigninActivity;
import com.tomorrance.yonsei.tomo.Network.ApiService;
import com.tomorrance.yonsei.tomo.Network.SharedPreferenceBase;
import com.tomorrance.yonsei.tomo.QrScan.QrScanDialog;
import com.tomorrance.yonsei.tomo.QrScan.barcode.BarcodeCaptureActivity;
import com.tomorrance.yonsei.tomo.Status.StatusActivity;
import com.tomorrance.yonsei.tomo.Utils.StaticFunctions;
import com.tomorrance.yonsei.tomo.ViewModel.ViewerActivity;
import com.tomorrance.yonsei.tomo.data.Main;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    public static final int BARCODE_READER_REQUEST_CODE = 9001;
    LinearLayout modelLayout;
    LinearLayout historyLayout;
    LinearLayout measureLayout;
    LinearLayout exerciseLayout;
    LinearLayout scanLayout;
    TextView logoutButton;
    QrScanDialog dialog;
    private int weight = 0;
    private boolean isFirst = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!hasPermissions(PERMISSIONS)) { //퍼미션 허가를 했었는지 여부를 확인
            requestNecessaryPermissions(PERMISSIONS);//퍼미션 허가안되어 있다면 사용자에게 요청
        }
        //Set Navigation Drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        logoutButton = toolbar.findViewById(R.id.tv_logout);
//        setSupportActionBar(toolbar);

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);

//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);
        //Set Views in Layout
        modelLayout = findViewById(R.id.layout_model);
        historyLayout = findViewById(R.id.layout_history);
        measureLayout = findViewById(R.id.layout_measure);
        exerciseLayout = findViewById(R.id.layout_exercise);
        scanLayout = findViewById(R.id.layout_scan);

        //Set EventListener
        modelLayout.setOnClickListener(this);
        historyLayout.setOnClickListener(this);
        measureLayout.setOnClickListener(this);
        exerciseLayout.setOnClickListener(this);
        scanLayout.setOnClickListener(this);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
                sp.edit().putBoolean("login",false).commit();
                Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        //Get Main Info
        ApiService apiService = _Application.getInstance().getApiService();
        apiService.getMain().enqueue(new Callback<Main>() {
            @Override
            public void onResponse(Call<Main> call, Response<Main> response) {
                if(response.code() == 401){
                    StaticFunctions.getInstance(MainActivity.this).goFirstPage();
                    return;
                }
                Main mainData = response.body();
                SharedPreferenceBase.putSharedPreference("name",mainData.getUser().getName());
                SharedPreferenceBase.putSharedPreference("email",mainData.getUser().getEmail());
                SharedPreferenceBase.putSharedPreference("height",mainData.getUser().getHeight().toString());
                setUserInfo();
                //If user access first time, provide a QR SCAN dialog
                if(mainData.getIsfirst() == 1){
                    isFirst = true;
                    dialog = new QrScanDialog(MainActivity.this);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    dialog.show();
                }

            }

            @Override
            public void onFailure(Call<Main> call, Throwable t) {
                Toast.makeText(MainActivity.this, "SERVER CONNECTION FAIL", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void setWeight(int weight){
        this.weight = weight;
    }

    private void setUserInfo(){
        String name = SharedPreferenceBase.getSharedPreference("name","NONE");
        String email= SharedPreferenceBase.getSharedPreference("email","NONE");
        ((TextView)findViewById(R.id.tv_name)).setText(name);
        ((TextView)findViewById(R.id.tv_email)).setText(email);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    if(weight != 0){
                        ApiService apiService = _Application.getInstance().getApiService();
                        Call<ResponseBody> reserve = apiService.reserve(weight,barcode.displayValue);
                        reserve.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(getApplicationContext(), "SCAN REGISTRATION SUCCESS", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(this, "ERROR. PLEASE RETRY", Toast.LENGTH_SHORT).show();
                }
            } else
                Log.e("ERROR_BARCODE", String.format(getString(R.string.barcode_error_format),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_body) {
            goModelActivity();
        } else if (id == R.id.nav_exercise) {
            goExerciseActivity();
        } else if (id == R.id.nav_measure) {
            goStatusActivity();
        } else if (id == R.id.nav_history) {
            goHistoryActivity();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    static final int PERMISSION_REQUEST_CODE = 1;
    String[] PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"};
    private File outputFile; //파일명까지 포함한 경로
    private File path;//디렉토리경로


    @SuppressLint("WrongConstant")
    private boolean hasPermissions(String[] permissions) {
        int res = 0;
        //스트링 배열에 있는 퍼미션들의 허가 상태 여부 확인
        for (String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                //퍼미션 허가 안된 경우
                return false;
            }
        }
        //퍼미션이 허가된 경우
        return true;
    }
    private void requestNecessaryPermissions(String[] permissions) {
        //마시멜로( API 23 )이상에서 런타임 퍼미션(Runtime Permission) 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }



    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.layout_model:
                goModelActivity();
                break;
            case R.id.layout_history:
                goHistoryActivity();
                break;
            case R.id.layout_measure:
                goStatusActivity();
                break;
            case R.id.layout_exercise:
                goExerciseActivity();
                break;
            case R.id.layout_scan:
                DisplayScanDialog();
                break;
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void goModelActivity(){
        Intent intent = new Intent(MainActivity.this,ViewerActivity.class);
        startActivity(intent);
    }
    private void goHistoryActivity(){
        Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
        startActivity(intent);
    }
    private void goStatusActivity(){
        Intent intent = new Intent(MainActivity.this,StatusActivity.class);
        startActivity(intent);
    }
    private void goExerciseActivity(){
        Intent intent = new Intent(MainActivity.this,ExerciseActivity.class);
        startActivity(intent);
    }
    private void DisplayScanDialog(){
        dialog = new QrScanDialog(MainActivity.this);
        dialog.setNotFirst();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
    }
}

