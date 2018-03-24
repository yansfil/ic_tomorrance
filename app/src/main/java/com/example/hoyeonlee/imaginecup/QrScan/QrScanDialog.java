package com.example.hoyeonlee.imaginecup.QrScan;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hoyeonlee.imaginecup.MainActivity;
import com.example.hoyeonlee.imaginecup.QrScan.barcode.BarcodeCaptureActivity;
import com.example.hoyeonlee.imaginecup.R;

/**
 * Created by hoyeonlee on 2018. 3. 23..
 */

public class QrScanDialog extends Dialog{
    MainActivity context;
    public QrScanDialog(@NonNull Context context) {
        super(context);
        this.context = (MainActivity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_qr);
        Button button = findViewById(R.id.btn_scan);
        final EditText weightInput = findViewById(R.id.input_weight);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(weightInput.getText().toString().equals("") || Integer.valueOf(weightInput.getText().toString()) < 10) {
                    Toast.makeText(context, "Please Type your weight(kg) correctly", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(context, BarcodeCaptureActivity.class);
                    context.setWeight(Integer.valueOf(weightInput.getText().toString()));
                    context.startActivityForResult(intent, context.BARCODE_READER_REQUEST_CODE);
                    cancel();
                }
            }
        });
    }
}
