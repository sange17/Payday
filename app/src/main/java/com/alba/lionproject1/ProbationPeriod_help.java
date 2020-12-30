package com.alba.lionproject1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProbationPeriod_help extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.probation_period_help);

        String ProbationPeriod_mean = "수습기간이란 법적으로 근로계약을 할 때 1년 이상의 근무를 약속하여 " +
                "일을 시작한 뒤로부터 3개월 이내가 법적수습기간으로 볼 수 있습니다.";

        String ProbationPeriod_calculate = "최저 금액에서 10%를 차감한 금액입니다. " +
                "예) 시급으로 8000원을 받는다면 시급 8000원에서 10%인 800원을 뺀 7200원을 시급으로 받게됩니다.\n\n" +
                "단, 단순 노무 업무로 고용노동부장관이 정하여 고시한 직종에 종사하는 근로자의 경우" +
                " 1년 이상 근무 계약을 하더라도 무조건 수습기간이 적용되지는 않습니다.\n\n" +
                "그러므로 단순 노무 종사자의 경우 수습기간의 상관없이 최저임금을 100%를 받을 수 있습니다.";

        TextView Probation_Period_Mean = findViewById(R.id.txt_probation_period_mean);
        Probation_Period_Mean.setText(ProbationPeriod_mean);

        TextView Probation_Period_Calculate = findViewById(R.id.txt_probation_period_calculate);
        Probation_Period_Calculate.setText(ProbationPeriod_calculate);

        Button Probation_Period = findViewById(R.id.btn_help_probationperiod_ok);
        Probation_Period.setOnClickListener(view -> finish());
    }
}
