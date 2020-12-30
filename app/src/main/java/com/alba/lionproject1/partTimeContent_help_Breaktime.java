package com.alba.lionproject1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class partTimeContent_help_Breaktime extends AppCompatActivity {
    Button helpOk;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.part_time_content_help_breaktime);

        String help_Breaktime = "1. 근로시간이 4시간 이상인 경우 30분이상, 8시간 이상인 경우 1시간 이상의 휴게시간을 제공해야합니다.\n\n" +
                "2. 휴게시간은 근로가 이루어지는 시간이 아니기 때문에, 별도 사업장 규정이 없다면 무급이 원칙입니다.\n\n" +
                "3. 휴게시간은 근로자가 자유롭게 이용할 수 있습니다.\n\n" +
                "※ 해당 근로기준법은 근로기준법 제 54조 <휴게> 에 관한 사항으로 5인 미만 사업장과 5인 이상의 사업장 모두 적용됩니다.";

        TextView HelpBreaktime = findViewById(R.id.txt_help_breaktime);
        HelpBreaktime.setText(help_Breaktime);

        helpOk = findViewById(R.id.btn_help_breaktime_ok);
        helpOk.setOnClickListener(view -> finish());
    }
}
