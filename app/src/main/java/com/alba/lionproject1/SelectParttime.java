package com.alba.lionproject1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SelectParttime extends AppCompatActivity {
    int newid = 0;
    long[] id = new long[10];
    String[] totalDBArray = new String[7];
    int NumberOfButton = 0;
    public String[] parttimenames = new String[10];
    Cursor cursor;
    LinearLayout layout_ButtonPlace;
    Button button1;
    ImageButton btn_Plus;
    RadioGroup radioGroup;
    RadioButton rb_edit;
    RadioButton rb_choose;
    RadioButton rb_delete;
    String chooseOrEditOrDelete = "";
    Myapplication myapplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_parttime);
        //팝업창 형식처럼 띄우게 해주는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_parttime);
        myapplication = (Myapplication) getApplication();
        for (int i = 0; i < 7; i++) {
            totalDBArray[i] = "";
        }


        rb_edit = (RadioButton) findViewById(R.id.rb_edit);
        rb_choose = (RadioButton) findViewById(R.id.rb_choose);
        rb_delete = (RadioButton) findViewById(R.id.rb_delete);

        PreferenceManager.setString(getApplicationContext(), "choice", "edit");
        rb_edit.setChecked(true);
        chooseOrEditOrDelete = "edit";
        PreferenceManager.setInt(getApplicationContext(), "count", 2);


        //데이터베이스 READ
        cursor = getTotalCursor();
        NumberOfButton = cursor.getCount();
        for (int i = 0; i < NumberOfButton; i++) {
            cursor.moveToPosition(i);
            parttimenames[i] = cursor.getString(1);
            id[i] = cursor.getLong(0);

        }
        newid = (int) id[id.length - 1];
        //만들때 1,2,3 이런식으로 인식됨.
        for (int i = 0; i < NumberOfButton; i++) {
            creatButton(parttimenames[i], id[i], i);
        }
        createPlusButtonn();


        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_choose) {
                chooseOrEditOrDelete = "choose";
                PreferenceManager.setString(getApplicationContext(), "choice", "choose");
            } else if (i == R.id.rb_edit) {
                chooseOrEditOrDelete = "edit";
                PreferenceManager.setString(getApplicationContext(), "choice", "edit");
            } else if (i == R.id.rb_delete) {
                chooseOrEditOrDelete = "delete";
                PreferenceManager.setString(getApplicationContext(), "choice", "delete");
            }
        });

    }

    private void creatButton(final String nameOfButton, final long ID, final int position) //알바이름 설정및 여러가지 설정 버튼 생성
    { //여기는 xml이 아닌 버튼을 추가시켜주기 위하여 java내에서 화면을 설정하였습니다.
        layout_ButtonPlace = (LinearLayout) findViewById(R.id.layout);
        button1 = new Button(this);
        button1.setText(nameOfButton); //버튼 텍스트
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.font);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(40, 40, 40, 0);
        params.width = 450; //버튼크기
        params.height = 120; //버튼크기
        button1.setLayoutParams(params);
        button1.setTypeface(typeface);
        button1.setTextSize(20);
        button1.setBackgroundResource(R.drawable.circle);
        layout_ButtonPlace.addView(button1); //버튼추가
        setContentView(layout_ButtonPlace); //버튼추가


        button1.setOnClickListener(view -> { // 이 버튼 클릭시 이벤트
            //데이터베이스 관련

            switch (chooseOrEditOrDelete) {
                case "edit": {
                    cursor = getTotalCursor();
                    cursor.moveToPosition(position);
                    for (int i = 1; i <= 7; i++) {
                        totalDBArray[i - 1] = cursor.getString(i);
                    }
                    if (totalDBArray[2].equals("")) //나중에 지워도 되는거
                    {
                        totalDBArray[2] = "null,null,null,null,null,null,null";
                    }
                    Intent intent = new Intent(SelectParttime.this, partTimeContent.class);
                    intent.putExtra("id", ID);
                    PreferenceManager.setString(getApplicationContext(), "spid", Integer.toString((int) ID));
                    intent.putExtra("totaldbarray", totalDBArray);
                    startActivity(intent);
                    finish();
                    break;
                }
                case "choose": {

                    Intent intent = new Intent(SelectParttime.this, MainActivity.class);
                    Activity activity = (MainActivity) MainActivity.main_activity;
                    myapplication.setNameOfAlba(nameOfButton);
                    PreferenceManager.setString(getApplicationContext(), "albaname", nameOfButton);
                    finish();
                    startActivity(intent);
                    activity.finish();
                    break;
                }
                case "delete": {
                    PreferenceManager.setString(getApplicationContext(),"albaname","");
                    totalDB totaldb = totalDB.getInstance(getApplicationContext());
                    registerWorkDBHelper registerDB = registerWorkDBHelper.getInstance(getApplicationContext());
                    totaldb.getWritableDatabase().delete(DataBases.CreatDB._TABLENAME0, DataBases.CreatDB._ID + " = " + ID, null);
                    registerDB.getWritableDatabase().delete(registerWorkContract.registerWorkEntry.TABLE_NAME,
                            registerWorkContract.registerWorkEntry.COLUMN_NAME_TITLE + " = ?",new String[]{button1.getText().toString()});
                    totaldb.close();
                    registerDB.close();
                    Intent intent1 = new Intent(SelectParttime.this,MainActivity.class);
                    Intent intent = new Intent(SelectParttime.this, SelectParttime.class);
                    startActivity(intent1);
                    startActivity(intent);
                    finish();
                    break;
                }
            }
        });
    }

    private Cursor getTotalCursor() {
        totalDB db = totalDB.getInstance(this);
        return db.getReadableDatabase().query(
                DataBases.CreatDB._TABLENAME0, null, null, null, null, null, DataBases.CreatDB._ID);

    }


    private void createPlusButtonn()// 알바 추가 버튼 생성
    { //여기는 xml이 아닌 버튼을 추가시켜주기 위하여 java내에서 화면을 설정하였습니다.
        layout_ButtonPlace = (LinearLayout) findViewById(R.id.layout);
        btn_Plus = new ImageButton(this);
        Drawable myDrawable = getResources().getDrawable(R.drawable.plusbutton, null);
        btn_Plus.setImageDrawable(myDrawable);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 20, 20, 20);
        params.width = 100;
        params.height = 100;
        btn_Plus.setLayoutParams(params);
        btn_Plus.setBackgroundColor(Color.parseColor("#fbf5df"));
        layout_ButtonPlace.addView(btn_Plus);
        setContentView(layout_ButtonPlace);


        btn_Plus.setOnClickListener(view -> {

            ContentValues myContentValues = new ContentValues();
            myContentValues.put(DataBases.CreatDB.PARTTIMENAME, "이름 수정해");
            myContentValues.put(DataBases.CreatDB.HOURLYWAGE, "8500");
            myContentValues.put(DataBases.CreatDB.DAYOFWEEK, "null,null,null,null,null,null,null");
            myContentValues.put(DataBases.CreatDB.WORKINGSTART, "null");
            myContentValues.put(DataBases.CreatDB.WORKINGFINISH, "null");
            myContentValues.put(DataBases.CreatDB.NUMBER_OF_PEOPLE, "false");
            myContentValues.put(DataBases.CreatDB.MEAL_TIME, "0");
            SQLiteDatabase db = totalDB.getInstance(getApplicationContext()).getWritableDatabase();
            db.insert(DataBases.CreatDB._TABLENAME0, null, myContentValues);

            // 이버튼 클릭시 재 클릭시 화면안의 모든 요소를 지우고 다시 그려준다. 실제로 코딩할 때는 데이터베이스에 저장된 값의 개수를 불러와서
            // 그 개수와 알바 이름들을 불러오면 될거같습니다..
            layout_ButtonPlace.removeAllViews();
            for (int i = 0; i < NumberOfButton; i++) {
                creatButton(parttimenames[i], id[i], NumberOfButton);
            }
            creatButton("이름 수정해", newid + 1, NumberOfButton + 1);
            createPlusButtonn();
            Intent intent = new Intent(SelectParttime.this, SelectParttime.class);
            startActivity(intent);
            finish();

        });

    }

}