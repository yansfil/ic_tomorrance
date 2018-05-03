package com.tomorrance.yonsei.tomo.Login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tomorrance.yonsei.tomo.MainActivity;
import com.tomorrance.yonsei.tomo.Network.ApiService;
import com.tomorrance.yonsei.tomo.R;
import com.tomorrance.yonsei.tomo._Application;
import com.tomorrance.yonsei.tomo.data.LoginResult;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    Context mContext;
    EditText height;
    EditText name;
    EditText email;
    EditText pw;
    EditText pwRe;
    Button joinButton;
    RadioGroup goalGroup;
    RadioGroup genderGroup;
    TextView login;
    int result;
    private final int EMAIL_ERROR = 1;
    private final int PW_ERROR = 2;
    private final int NAME_ERROR = 3;
    private final int EQUAL_PW_ERROR = 4;
    private final int SUCCESS = 5;
    ApiService apiService;
    Dialog dialogTransparent;
    String goal = "";
    String intensity = "";
    String gender = "man";

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
        goalGroup = findViewById(R.id.group_goal);
        genderGroup = findViewById(R.id.group_gender);
        apiService = _Application.getInstance().getApiService();

        //Loading Dialog
        dialogTransparent = new Dialog(this, android.R.style.Theme_Black);
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_loading, null);
        dialogTransparent.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogTransparent.getWindow().setBackgroundDrawableResource(
                R.color.transparent_dialog);
        dialogTransparent.setContentView(view);
        dialogTransparent.setCancelable(false);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = check();
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
        } else if (!(pw.getText().toString().trim().length() >= 6)) {
            return PW_ERROR;
        } else if (name.getText().toString().trim().length() == 0){
            return NAME_ERROR;
        } else if(!pw.getText().toString().equals(pwRe.getText().toString())){
            return EQUAL_PW_ERROR;
        }
        return SUCCESS;
    }

    private void login() {
        dialogTransparent.show();
        if (result == EMAIL_ERROR) {
            Toast.makeText(SignUpActivity.this, "이메일을 정확히 입력해주세요", Toast.LENGTH_SHORT).show();
        } else if (result == PW_ERROR) {
            Toast.makeText(SignUpActivity.this, "비밀번호를 6자 이상 입력해주세요", Toast.LENGTH_SHORT).show();
        } else if (result == NAME_ERROR) {
            Toast.makeText(SignUpActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
        } else if (result == EQUAL_PW_ERROR) {
            Toast.makeText(SignUpActivity.this, "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
        }else {
            String emailText = email.getText().toString();
            String passwordText = pw.getText().toString();
            String nameText = name.getText().toString();
            String heightText = height.getText().toString();
            switch (goalGroup.getCheckedRadioButtonId()) {
                case R.id.goal1:
                    goal = "loseWeight";
                    break;
                case R.id.goal2:
                    goal = "gainWeight";
                    break;
                case R.id.goal3:
                    goal = "gainMuscle";
                    break;
                default:
                    goal = "loseWeight";
                    break;
            }
            switch (genderGroup.getCheckedRadioButtonId()) {
                case R.id.gender1:
                    intensity = "man";
                    break;
                case R.id.gender2:
                    intensity = "woman";
                    break;
                default:
                    intensity = "man";
                    break;
            }
            Call<LoginResult> join = apiService.join(nameText, heightText, emailText, passwordText, goal, gender, _Application.getDeviceId(), "");
            join.enqueue(new Callback<LoginResult>() {
                @Override
                public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                    if (response.body().getCode().equals("1")) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else
                        Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dialogTransparent.dismiss();
                }

                @Override
                public void onFailure(Call<LoginResult> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    dialogTransparent.dismiss();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}