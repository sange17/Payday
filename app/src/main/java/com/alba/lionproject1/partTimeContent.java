package com.alba.lionproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import static com.alba.lionproject1.registerWorkContract.registerWorkEntry.TABLE_NAME;
import java.util.Objects;

public class partTimeContent extends AppCompatActivity {
    String numberofpeople = "";
    public static Context mContext;
    String[] cbResult = new String[8];
    String parttimename = "";
    String hourlywage = "";
    String dayofweek = "";
    String startingwork = "";
    String finishingwork = "";
    int mealtime = 0;
    RadioGroup rb_group;
    int id;
    String[] totalDBArray = new String[6];
    String[] splitarray = new String[7];

    public static Context getmContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_time_content);
        mContext = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_part_time_content);
        //배열 초기화
        for (int i = 0; i < 8; i++) {
            cbResult[i] = "null";
        }


        //화면의 모든 정보 아이템들 캐스팅
        final TextView txt_parttimename = findViewById(R.id.txt_ParttimeName);
        final TextView txt_hourlywage = findViewById(R.id.txt_hourlypay);
        final TextView txt_ptcfrom = findViewById(R.id.txt_pFrom);
        final TextView txt_ptcTo = findViewById(R.id.txt_pTo);
        final CheckBox cb_mon = findViewById(R.id.cb_mon);
        final CheckBox cb_tues = findViewById(R.id.cb_tues);
        final CheckBox cb_wed = findViewById(R.id.cb_wed);
        final CheckBox cb_thu = findViewById(R.id.cb_thurs);
        final CheckBox cb_fri = findViewById(R.id.cb_fri);
        final CheckBox cb_sat = findViewById(R.id.cb_sat);
        final CheckBox cb_sun = findViewById(R.id.cb_sun);
        final RadioButton rb_yes = findViewById(R.id.rb_yes);
        final RadioButton rb_no = findViewById(R.id.rb_no);
        final EditText txt_mealtime = findViewById(R.id.txt_mealtime);
        final ImageButton helpworkplace = findViewById(R.id.btn_helpWorkplace);
        final ImageButton helpbreaktime = findViewById(R.id.btn_helpBreaktime);


        // 전화면으로 부터의 ID(데이터베이스 수정시 버튼 구분을 위한 번호) 받기
        Intent intent = getIntent();
        id = Objects.requireNonNull(intent.getExtras()).getInt("id");
        id = Integer.parseInt(PreferenceManager.getString(getApplicationContext(), "spid"));
        totalDBArray = intent.getExtras().getStringArray("totaldbarray");
        //DB로 받아온 정보들을 화면에 표시(기존저장정보)
        assert totalDBArray != null;
        txt_parttimename.setText(totalDBArray[0]);
        txt_hourlywage.setText(totalDBArray[1]);
        txt_ptcfrom.setText(totalDBArray[3]);
        txt_ptcTo.setText(totalDBArray[4]);
        txt_mealtime.setText(totalDBArray[6]);

        splitarray = totalDBArray[2].split(",");
        for (int i = 0; i < 7; i++) {
            if (splitarray[i].equals("mon")) {
                cbResult[0] = "mon";
                cb_mon.setChecked(true);
            }
            if (splitarray[i].equals("tue")) {
                cbResult[1] = "tue";
                cb_tues.setChecked(true);
            }
            if (splitarray[i].equals("wed")) {
                cbResult[2] = "wed";
                cb_wed.setChecked(true);
            }
            if (splitarray[i].equals("thu")) {
                cbResult[3] = "thu";
                cb_thu.setChecked(true);
            }
            if (splitarray[i].equals("fri")) {
                cbResult[4] = "fri";
                cb_fri.setChecked(true);
            }
            if (splitarray[i].equals("sat")) {
                cbResult[5] = "sat";
                cb_sat.setChecked(true);
            }
            if (splitarray[i].equals("sun")) {
                cbResult[6] = "sun";
                cb_sun.setChecked(true);
            }

        }
        if (totalDBArray[5].equals("true")) {
            rb_yes.setChecked(true);
            numberofpeople = "true";
        } else if (totalDBArray[5].equals("false")) {
            rb_no.setChecked(true);
            numberofpeople = "false";
        }


        //체크박스 클릭 및 체크박스로부터 요일 데이터 얻기
        cb_mon.setOnClickListener(view -> {
            if (cb_mon.isChecked()) {
                cbResult[0] = "mon";
            } else {
                cbResult[0] = "null";
            }
        });

        cb_tues.setOnClickListener(view -> {
            if (cb_tues.isChecked()) {
                cbResult[1] = "tue";
            } else {
                cbResult[1] = "null";
            }
        });

        cb_wed.setOnClickListener(view -> {
            if (cb_wed.isChecked()) {
                cbResult[2] = "wed";
            } else {
                cbResult[2] = "null";
            }
        });

        cb_thu.setOnClickListener(view -> {
            if (cb_thu.isChecked()) {
                cbResult[3] = "thu";
            } else {
                cbResult[3] = "null";
            }
        });

        cb_fri.setOnClickListener(view -> {
            if (cb_fri.isChecked()) {
                cbResult[4] = "fri";
            } else {
                cbResult[4] = "null";
            }
        });

        cb_sat.setOnClickListener(view -> {
            if (cb_sat.isChecked()) {
                cbResult[5] = "sat";
            } else {
                cbResult[5] = "null";
            }
        });

        cb_sun.setOnClickListener(view -> {
            if (cb_sun.isChecked()) {
                cbResult[6] = "sun";
            } else {
                cbResult[6] = "null";
            }
        });


        helpworkplace.setOnClickListener(view -> {
            Intent help_workplace_intent = new Intent(partTimeContent.this, partTimeContent_help_Workplace.class);
            startActivity(help_workplace_intent);
        });

        helpbreaktime.setOnClickListener(view -> {
            Intent help_breaktime_intent = new Intent(partTimeContent.this, partTimeContent_help_Breaktime.class);
            startActivity(help_breaktime_intent);
        });


        //저장 버튼 캐스팅과 클릭시 이벤트
        Button btn_Save = (Button) findViewById(R.id.btn_pSave);
        btn_Save.setOnClickListener(view -> {
            //알바이름,시간당 임금
            dayofweek = ShowResultOfDaysOfWeek();
            parttimename = txt_parttimename.getText().toString();
            hourlywage = txt_hourlywage.getText().toString();
            //근무시작과 끝시간텍스트
            startingwork = txt_ptcfrom.getText().toString();
            finishingwork = txt_ptcTo.getText().toString();



            try {
                mealtime = Integer.parseInt(txt_mealtime.getText().toString());
            } catch (Exception ex) {
                mealtime = 0;
            }

            if (dayofweek.equals("null,null,null,null,null,null,null") || parttimename.equals("null") || hourlywage.equals("null") || startingwork.equals("null") || finishingwork.equals("null") || numberofpeople.equals("null")) {
                Toast.makeText(getApplicationContext(), "잘봐봐 안쓴거 있어", Toast.LENGTH_LONG).show();
            } else if (txt_mealtime.getText().toString().isEmpty() || !isNumber(txt_mealtime.getText().toString())) {
                Toast.makeText(getApplicationContext(), "시간은 숫자로 써야되", Toast.LENGTH_LONG).show();
            } else {

                ContentValues myContentValues = new ContentValues();
                myContentValues.put(DataBases.CreatDB.PARTTIMENAME, parttimename);
                myContentValues.put(DataBases.CreatDB.HOURLYWAGE, hourlywage);
                myContentValues.put(DataBases.CreatDB.DAYOFWEEK, dayofweek);
                myContentValues.put(DataBases.CreatDB.WORKINGSTART, startingwork);
                myContentValues.put(DataBases.CreatDB.WORKINGFINISH, finishingwork);
                myContentValues.put(DataBases.CreatDB.NUMBER_OF_PEOPLE, numberofpeople);
                myContentValues.put(DataBases.CreatDB.MEAL_TIME, mealtime);


                SQLiteDatabase db = totalDB.getInstance(getmContext()).getWritableDatabase();
                ///long newRowId = db.insert(DataBases.CreatDB._TABLENAME0,null,myContentValues);
                long newRowId = db.update(DataBases.CreatDB._TABLENAME0, myContentValues,
                        DataBases.CreatDB._ID + " = " + id, null);
                if (newRowId == 0) {
                    Toast.makeText(getmContext(), "저장에 문제 발생", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getmContext(), "저장 성공", Toast.LENGTH_SHORT).show();
                }
                long rowid = 0;
                if(!totalDBArray[0].equals(parttimename))
                {
                    SQLiteDatabase rwDB = registerWorkDBHelper.getInstance(getmContext()).getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(registerWorkContract.registerWorkEntry.COLUMN_NAME_TITLE,parttimename);
                    rowid = rwDB.update(TABLE_NAME,values,registerWorkContract.registerWorkEntry.COLUMN_NAME_TITLE + " =  ?",new String[]{totalDBArray[0]});
                }
                System.out.println("rwrowid = "+ rowid);

                Intent intent1 = new Intent(partTimeContent.this, SelectParttime.class);
                getmContext().startActivity(intent1);
                finish();
            }
        });
        //라디오버튼,라디오그룹 (5인이상사업장)에 대한 처리
        rb_group = (RadioGroup) findViewById(R.id.rb_group);

        rb_group.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_yes) {
                numberofpeople = "true";
            } else if (i == R.id.rb_no) {
                numberofpeople = "false";
            }
        });


        //취소 버튼 캐스팅과 클릭시 이벤트
        Button btn_Cancel = (Button) findViewById(R.id.btn_pCancel);
        btn_Cancel.setOnClickListener(view -> {
            Intent intent12 = new Intent(partTimeContent.this, SelectParttime.class);
            getmContext().startActivity(intent12);
            finish();
        });

        //몇시부터 몇시까지 고정적으로 하는지 설정할수 있게 해주는 버튼 btn_tpFrom 그리고 클릭시 이벤트
        Button btn_From = (Button) findViewById(R.id.btn_tpFrom);
        btn_From.setOnClickListener(view -> {
            Intent intent13 = new Intent(partTimeContent.this, Timepicker.class);
            intent13.putExtra("view", "ptcfrom");
            startActivity(intent13);
        });
        //몇시부터 몇시까지 일을 고정적으로 하는지 설정할수 있게 해주는 버튼 btn_tpTo 그리고 클릭시 이벤트
        Button btn_To = (Button) findViewById(R.id.btn_tpTo);
        btn_To.setOnClickListener(view -> {
            Intent intent14 = new Intent(partTimeContent.this, Timepicker.class);
            intent14.putExtra("view", "ptcto");
            startActivity(intent14);
        });
    }

    private String ShowResultOfDaysOfWeek() {
        StringBuilder Results = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            if (i == 0) {
                Results.append(cbResult[i]);
            } else {
                Results.append(",").append(cbResult[i]);
            }
        }
        return Results.toString();
    }

    private boolean isNumber(String toCheck)
    {
        char tmp;
        boolean output = true;
        for(int i =0;i<toCheck.length();i++)
        {
            tmp = toCheck.charAt(i);
            if(!Character.isDigit(tmp))
            {
                output = false;
            }
        }
        return output;
    }



}