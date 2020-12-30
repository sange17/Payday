package com.alba.lionproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Locale;
import java.util.Objects;

public class Timepicker extends AppCompatActivity {

    TimePicker timePicker;
    public static String mins, hours;
    public static Context timecontext;
    public static String screenview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timepicker);
        timecontext = this;

        //한국으로 설정
        Locale locale = Locale.KOREA; //Your specific locale. Locale.setDefault(locale);
        Locale.setDefault(locale);
        Configuration configuration = timecontext.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        timecontext.createConfigurationContext(configuration);
        //팝업창으로 설정
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_timepicker);

        try {
            Intent intent = getIntent();
            screenview = Objects.requireNonNull(intent.getExtras()).getString("view");
        } catch (Exception ignored) {

        }

        timePicker = (TimePicker) findViewById(R.id.timepicker);
        hours = timePicker.getHour() + "";
        mins = timePicker.getMinute() + "";

        timePicker.setOnTimeChangedListener((timePicker, hour, min) -> {
            hours = Integer.toString(hour);
            mins = Integer.toString(min);
        });

        //저장 버튼
        Button btn_Save = (Button) findViewById(R.id.btn_tpSave);
        btn_Save.setOnClickListener(view -> {
            String time = getAmPm(Integer.parseInt(hours)) + " " + getAmPmHour(Integer.parseInt(hours)) + " : " + getMins(mins);

            try {
                if (screenview.equals("ptcfrom")) {
                    final TextView txt_pFrom = ((partTimeContent) partTimeContent.getmContext()).findViewById(R.id.txt_pFrom);
                    txt_pFrom.setText(time);
                    finish();
                }

                if (screenview.equals("ptcto")) {
                    final TextView txt_pTo = ((partTimeContent) partTimeContent.getmContext()).findViewById(R.id.txt_pTo);
                    txt_pTo.setText(time);
                    finish();
                }
                if (screenview.equals("rwfrom")) {
                    final TextView txt_rwfrom = ((registerWork) registerWork.context).findViewById(R.id.txt_rwFrom);
                    txt_rwfrom.setText(time);
                    finish();
                }
                if (screenview.equals("rwto")) {
                    final TextView txt_rwTo = ((registerWork) registerWork.context).findViewById(R.id.txt_rwTo);
                    txt_rwTo.setText(time);
                    finish();
                }
            } catch (Exception e) {
                finish();
            }
        });

        //취소 버튼
        Button btn_Cancel = (Button) findViewById(R.id.btn_tpCancel);
        btn_Cancel.setOnClickListener(view -> finish());
    }

    private String getAmPm(int hour) {
        if (hour >= 12)
            return "오후";
        else
            return "오전";
    }

    private int getAmPmHour(int hour) {
        if (hour >= 12) {
            return hour - 12;
        } else {
            return hour;
        }
    }

    private String getMins(String min) {
        int numMin = Integer.parseInt(min);
        if (numMin < 10) {
            return "0" + min;
        } else {
            return min;
        }
    }


}