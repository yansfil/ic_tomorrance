package com.example.hoyeonlee.imaginecup.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoyeonlee.imaginecup.MainActivity;
import com.example.hoyeonlee.imaginecup.Network.AddCookiesInterceptor;
import com.example.hoyeonlee.imaginecup.Network.ApiService;
import com.example.hoyeonlee.imaginecup.Network.ReceivedCookiesInterceptor;
import com.example.hoyeonlee.imaginecup.R;
import com.example.hoyeonlee.imaginecup._Application;
import com.example.hoyeonlee.imaginecup.data.LoginResult;

import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {
    Context mContext;
    EditText height;
    EditText name;
    EditText email;
    EditText pw;
    EditText pwRe;
    Button joinButton;
    TextView login;
    private final int CHECKBOX_ERROR = 0;
    private final int EMAIL_ERROR = 1;
    private final int PW_ERROR = 2;
    private final int NAME_ERROR = 3;
    private final int EQUAL_PW_ERROR = 4;
    private final int SUCCESS = 5;
    Retrofit retrofit;
    ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mContext = SignUpActivity.this; // 타 메서드 호출에서 사용할 컨텍스트 객체
        name = findViewById(R.id.input_name);
        height = findViewById(R.id.input_height);
        email = findViewById(R.id.input_email);
        pw = findViewById(R.id.input_password);
        pwRe = findViewById(R.id.input_password_re);
        joinButton = findViewById(R.id.btn_join);
        login = findViewById(R.id.tv_login);
        OkHttpClient.Builder oktHttpClient = new OkHttpClient.Builder();
        oktHttpClient.interceptors().add(new AddCookiesInterceptor());
        oktHttpClient.interceptors().add(new ReceivedCookiesInterceptor());
        retrofit = new Retrofit.Builder().
                baseUrl(ApiService.URL).
                client(oktHttpClient.build()).
                build();
        apiService = retrofit.create(ApiService.class);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int result = check();
                login();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private int check() {
        if (email.getText().toString().trim().length() == 0 || !EMAIL_ADDRESS_PATTERN.matcher(email.getText()).matches()) {
            return EMAIL_ERROR;
        } else if (!(pw.getText().toString().trim().length() >= 8 && pw.getText().toString().trim().length() <= 16)) {
            return PW_ERROR;
        } else if (name.getText().toString().trim().length() == 0){
            return NAME_ERROR;
        } else if(!pw.getText().toString().equals(pwRe.getText().toString())){
            return EQUAL_PW_ERROR;
        }
        return SUCCESS;
    }

    private void login() {
//        if(result == CHECKBOX_ERROR){
//            Toast.makeText(SignUpActivity.this, "이용약관에 동의해주세요", Toast.LENGTH_SHORT).show();
//        }else if (result == EMAIL_ERROR) {
//            Toast.makeText(SignUpActivity.this, "이메일을 정확히 입력해주세요", Toast.LENGTH_SHORT).show();
//        } else if (result == PW_ERROR) {
//            Toast.makeText(SignUpActivity.this, "비밀번호를 8-16자로 입력해주세요", Toast.LENGTH_SHORT).show();
//        } else if (result == NAME_ERROR) {
//            Toast.makeText(SignUpActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
//        } else if (result == EQUAL_PW_ERROR) {
//            Toast.makeText(SignUpActivity.this, "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
//        }else{
            String emailText = email.getText().toString();
            String passwordText = pw.getText().toString();
            String nameText = name.getText().toString();
            String heightText = height.getText().toString();
            Call<LoginResult> join = apiService.join(nameText,heightText,emailText,passwordText, _Application.getDeviceId());
            join.enqueue(new Callback<LoginResult>() {
                @Override
                public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                    if(response.body().getCode().equals("1")) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<LoginResult> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
//        }

    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}