package com.alba.lionproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Intro extends AppCompatActivity {

    TextView txt_extra_p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        txt_extra_p = (TextView) findViewById(R.id.txt_extra_p);

        Animation anim = AnimationUtils.loadAnimation
                (getApplicationContext(), // 현재화면의 제어권자
                        R.anim.translate_anim);   // 에니메이션 설정 파일
        txt_extra_p.startAnimation(anim);

        //인트로 화면에서 화면 지나갈때 지나가는 시간을 지연시켜주기
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(Intro.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 4000); // 2초 지연
    }
}