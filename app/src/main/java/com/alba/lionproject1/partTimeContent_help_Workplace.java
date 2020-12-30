package com.alba.lionproject1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class partTimeContent_help_Workplace extends AppCompatActivity {
    Button helpOk;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.part_time_content_help_workplace);

        String help_Workplace =
                "위의 표에서 해당하는 부분은 근로기준법에 의해 근로자가 보호를 받을 수 있습니다.\n\n" +
                        "1. 5인 이상 사업장은 상시근로자 수가 5인 이상인 사업장이고 5인 미만 사업장은 상시근로자 수가 5인 미만인 사업장입니다.\n\n" +
                        "2. 상시 근로자 수 계산방법은 1개월 동안 동원된 총 근로자 ÷ 1개월 동안 사업일 수 입니다.\n\n" +
                        "※ 1일 근로자 수가 5명 이상인 날이 50%를 넘으면 계산상 상시근로자가 5인 미만이더라도 5인 이상 사업장으로 간주됩니다.";

        TextView HelpWorkPlace = findViewById(R.id.txt_help_workplace);
        HelpWorkPlace.setText(help_Workplace);

        helpOk = findViewById(R.id.btn_help_workplace_ok);
        helpOk.setOnClickListener(view -> finish());
    }
}
