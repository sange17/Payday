package com.alba.lionproject1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AlertDialog_filepath extends AppCompatActivity {
    Button Close;
    String imgpFilePath = "1. file:/storage/emulated/0/Android/data/com.alba.lionproject1/files/Pictures\n\n" +
            "2. 내 파일 > 내장 메모리 > Android > data > com.alba.lionproject1 > files > Pictures";
    String xlsFilePath = "1. file:/strage/emulated/0/Andriod/data/com.alba.lionproject1/files\n\n" +
            "2. 내 파일 > 내장 메모리 > Android > data > com.alba.lionproject1 > files";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertdialog_filepath);

        TextView imgfilepath = findViewById(R.id.txt_imgFilepath);
        imgfilepath.setText(imgpFilePath);
        TextView xlsfilepath = findViewById(R.id.xlsFilepath);
        xlsfilepath.setText(xlsFilePath);

        Close = findViewById(R.id.btn_Close);
        Close.setOnClickListener(view -> finish());
    }
}
