package com.example.hoyeonlee.imaginecup.Login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hoyeonlee.imaginecup.MainActivity;
import com.example.hoyeonlee.imaginecup.Network.ApiService;
import com.example.hoyeonlee.imaginecup.Notification.MyHandler;
import com.example.hoyeonlee.imaginecup.Notification.NotificationSettings;
import com.example.hoyeonlee.imaginecup.Notification.RegistrationIntentService;
import com.example.hoyeonlee.imaginecup.R;
import com.example.hoyeonlee.imaginecup._Application;
import com.example.hoyeonlee.imaginecup.data.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by hoyeonlee on 2018. 2. 23..
 */

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginButton;
    Button signupButton;
    EditText emailInput;
    EditText passwordInput;
    ApiService apiService;
    Dialog dialogTransparent;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "LOGIN_ACTIVITY";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        loginButton = (Button) findViewById(R.id.btn_login);
        signupButton = findViewById(R.id.btn_join);
        emailInput = findViewById(R.id.input_email);
        passwordInput = findViewById(R.id.input_password);

        apiService = _Application.getInstance().getApiService();
        loginButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
        NotificationsManager.handleNotifications(this, NotificationSettings.SenderId, MyHandler.class);
        registerWithNotificationHubs();

        //Loading Dialog
        dialogTransparent = new Dialog(this, android.R.style.Theme_Black);
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_loading, null);
        dialogTransparent.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogTransparent.getWindow().setBackgroundDrawableResource(
                R.color.transparent_dialog);
        dialogTransparent.setContentView(view);
        dialogTransparent.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login){
            dialogTransparent.show();
            Call<LoginResult> login = apiService.login(emailInput.getText().toString(),passwordInput.getText().toString(),_Application.getDeviceId());
            login.enqueue(new Callback<LoginResult>() {
                @Override
                public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if(response.body().getCode().equals("1")) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else Toast.makeText(SigninActivity.this, "Login Error. Try again!!", Toast.LENGTH_SHORT).show();
                        dialogTransparent.dismiss();
                }

                @Override
                public void onFailure(Call<LoginResult> call, Throwable t) {
                    Toast.makeText(SigninActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    dialogTransparent.dismiss();
                }
            });

        }else{
            Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
            startActivity(intent);
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported by Google Play Services.");
                Toast.makeText(this, "This device is not supported by Google Play Services.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }

    public void registerWithNotificationHubs()
    {
        if (checkPlayServices()) {
            // Start IntentService to register this application with FCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }
}
