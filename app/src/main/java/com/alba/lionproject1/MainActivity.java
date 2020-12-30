package com.alba.lionproject1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ArrayList<String[]> hoursSubtractions = new ArrayList<>();
    ArrayList<String[]> workingHours = new ArrayList<>();
    double[] oneDayWage = new double[100000];
    String[][] totalArray = new String[100][8];
    String[][] registerArray = new String[100000][9];
    double totalWage = 0;
    double totalWorkingHours = 0;
    double[] extraMoney = new double[5];
    int firstWeek = 0;
    int secondWeek = 0;
    int thirdWeek = 0;
    int forthWeek = 0;
    int fifthWeek = 0;
    int count = 0;
    Myapplication myapplication;
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
    SimpleDateFormat timeDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
    Date txtFrom;
    Date txtTo;
    TextView txt_mfrom;
    TextView txt_mto;
    public static Activity main_activity;
    public static Context context_main; // context 변수 선언
    Button btn_ChooseParttime;
    Button btn_ImgFilePath;
    Button btn_EvidencePath;
    String suggestedSavedWage = "";
    EditText showWage;
    CheckBox cb_probationPeriod;
    ImageButton btn_ProbationPeriod;


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context_main = this;
        main_activity = MainActivity.this;
        myapplication = (Myapplication) getApplication();

        // 국가 설정(한국으로) - 처음에 커스텀 달력이 아니라 기본 달력 사용 했을 때 국가설정해야 언어 바뀌어서 그렇게 했어
        Locale.setDefault(Locale.KOREA);
        this.setContentView(R.layout.activity_main);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.KOREA);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);

        String year = yearFormat.format(currentTime);
        String month = monthFormat.format(currentTime);
        String day = dayFormat.format(currentTime);
        String date = year + "/" + expressMonth(Integer.parseInt(month)) + "/" + expressDate(Integer.parseInt(day));


        txt_mfrom = findViewById(R.id.txt_from);
        txt_mto = findViewById(R.id.txt_to);
        btn_ChooseParttime = findViewById(R.id.btn_ChooseParttime);
        btn_ImgFilePath = findViewById(R.id.btn_ImgFilePath);
        btn_EvidencePath = findViewById(R.id.btn_EvidenceCsv);
        showWage = findViewById(R.id.txt_monthlyPay);
        cb_probationPeriod = findViewById(R.id.cb_susoup);
        LinearLayout linearLayout = findViewById(R.id.sclinear);
        ScrollView scrollView = findViewById(R.id.scroll);
        btn_ProbationPeriod = findViewById(R.id.btn_helpProbationPeriod);

        ScrollView lineLayout = (ScrollView) linearLayout.getParent();
        int x = lineLayout.getLeft();
        int y = lineLayout.getTop();
        scrollView.smoothScrollTo(x, y);


        try {
            myapplication.setMainDateFrom(PreferenceManager.getString(getApplicationContext(), "maindatefrom"));
            myapplication.setMainDateTo(PreferenceManager.getString(getApplicationContext(), "maindateto"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        txt_mfrom.setText(myapplication.getMainDateFrom());
        txt_mto.setText(myapplication.getMainDateTo());
        if (txt_mfrom.getText().toString().equals("")) {
            txt_mfrom.setText(date);

        }
        if (txt_mto.getText().toString().equals("")) {
            txt_mto.setText(date);
        }


        try {
            myapplication.setNameOfAlba(PreferenceManager.getString(context_main, "albaname"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        btn_ChooseParttime.setText(myapplication.getNameOfAlba());
        if (btn_ChooseParttime.getText().toString().equals("")) {
            btn_ChooseParttime.setText("알바 선택해");
        }

        try {
            txtFrom = format.parse(txt_mfrom.getText().toString());
            txtTo = format.parse(txt_mto.getText().toString());
            CalculateWage();
        } catch (ParseException ex) {
            showWage.setText("0 원");
        }
        try{
            if(PreferenceManager.getString(getApplicationContext(),"probationPeriodChecked").equals("true")) {
                cb_probationPeriod.setChecked(true);
                String[] tmpTotalWage = showWage.getText().toString().split(" ");
                System.out.println("tmpTotalWage = " + tmpTotalWage[0]);
                double probationPeriod = Double.parseDouble(tmpTotalWage[0]) / 100 * 90;
                System.out.println("proBation = " + probationPeriod);
                tmpTotalWage[0] = (int)probationPeriod + " 원";
                showWage.setText(tmpTotalWage[0]);
            }
        } catch (Exception ex) {
            System.out.println("probation 실패");
        }


        cb_probationPeriod.setOnClickListener(view -> {
            if (cb_probationPeriod.isChecked())
            {
                PreferenceManager.setString(getApplicationContext(),"probationPeriodChecked","true");
                String[] tmpTotalWage = showWage.getText().toString().split(" ");
                System.out.println("tmpTotalWage = " + tmpTotalWage[0]);
                double probationPeriod = Double.parseDouble(tmpTotalWage[0]) / 100 * 90;
                System.out.println("proBation = " + probationPeriod);
                tmpTotalWage[0] = (int) probationPeriod + " 원";
                showWage.setText(tmpTotalWage[0]);
            }else{
                PreferenceManager.setString(getApplicationContext(),"probationPeriodChecked","false");
                String[] tmpTotalWage = showWage.getText().toString().split(" ");
                double probationPeriod = Double.parseDouble(tmpTotalWage[0]) / 90 * 100;
                String tmp = (int)probationPeriod + " 원";
                showWage.setText(tmp);
            }
        });

        //알바비를 표시해주기 위하여 자신이 원하는 언제부터 언제 까지 를 설정하기 위해 날짜 선택 화면으로 넘어가는 버튼 from 이벤트
        Button btn_From = findViewById(R.id.btn_spFrom);
        btn_From.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, getDatePopUp.class);
            intent.putExtra("view", "mainfrom");
            startActivity(intent);


        });


        // 수습기간 도움말
        btn_ProbationPeriod.setOnClickListener(view -> {
            Intent intent_probatinoperiod = new Intent(MainActivity.this,ProbationPeriod_help.class);
            startActivity(intent_probatinoperiod);
        });

        //알바비를 표시해주기 위하여 자신이 원하는 언제부터 언제 까지 를 설정하기 위해 날짜 선택 화면으로 넘어가는 버튼 to 이벤트
        Button btn_To = findViewById(R.id.btn_spTo);
        btn_To.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, getDatePopUp.class);
            intent.putExtra("view", "mainto");
            startActivity(intent);
        });

        //무슨 알바인지 추가하고 선택할수있는 버튼 (알바1) 클릭시 이벤트
        btn_ChooseParttime.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SelectParttime.class);
            startActivity(intent);
        });


        // 이미지 파일 경로를 볼 수 있는 버튼
        btn_ImgFilePath.setOnClickListener(view -> {
            Intent intent_imgfilepath = new Intent(MainActivity.this, AlertDialog_filepath.class);
            startActivity(intent_imgfilepath);
        });


        // 증거물 파일 경로를 볼 수 있는 버튼
        btn_EvidencePath.setOnClickListener(view -> {
            Intent intent_evidencepath = new Intent(MainActivity.this, AlertDialog_addxls.class);
            startActivity(intent_evidencepath);
        });
    }


    private String expressMonth(int month) {
        String smonth = Integer.toString(month);
        if (month < 10) {
            smonth = "0" + smonth;
        }
        return smonth;
    }

    private String expressDate(int date) {
        String sdate = Integer.toString(date);
        if (date < 10) {
            sdate = "0" + sdate;
        }
        return sdate;
    }

    private long subtractDate(String dateFrom, String dateTo) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);

            Date fromDate = format.parse(dateFrom);
            Date toDate = format.parse(dateTo);


            assert fromDate != null;
            assert toDate != null;
            long calDate = toDate.getTime() - fromDate.getTime();

            long calDateDays = calDate / (24 * 60 * 60 * 1000);

            calDateDays = Math.abs(calDateDays);

            System.out.println("두 날짜의 날짜 차이: " + calDateDays + "\n fromDate : " + fromDate.toString() + " toDate : " + toDate.toString() +
                    " calDate : " + calDate + "\n 언제부터 : " + dateFrom + "  언제까지 : " + dateTo);

            return calDateDays + 1;
        } catch (ParseException ignored) {
            return 0;
        }

    }

    private Cursor getregisterCursor() {
        registerWorkDBHelper db = registerWorkDBHelper.getInstance(this);
        return db.getReadableDatabase().query(
                registerWorkContract.registerWorkEntry.TABLE_NAME, null, null, null, null, null, registerWorkContract.registerWorkEntry._ID);
    }

    private Cursor getTotalCursor() {
        totalDB db = totalDB.getInstance(this);
        return db.getReadableDatabase().query(
                DataBases.CreatDB._TABLENAME0, null, null, null, null, null, DataBases.CreatDB._ID);

    }

    private double subHours(String dateFrom, String dateTo, String hoursFrom, String hoursTo) {
        try {
            String[] organizedHoursFrom = hoursFrom.split(" ");
            String[] organizedHoursTo = hoursTo.split(" ");

            if (organizedHoursFrom[0].equals("오후")) {
                organizedHoursFrom[1] = Integer.toString(Integer.parseInt(organizedHoursFrom[1]) + 12);
            }

            if (organizedHoursTo[0].equals("오후")) {
                organizedHoursTo[1] = Integer.toString(Integer.parseInt(organizedHoursTo[1]) + 12);
            }

            if (Integer.parseInt(organizedHoursFrom[1]) < 10) {
                organizedHoursFrom[1] = "0" + organizedHoursFrom[1];
            }

            if (Integer.parseInt(organizedHoursTo[1]) < 10) {
                organizedHoursTo[1] = "0" + organizedHoursTo[1];
            }

            hoursFrom = dateFrom + " " + organizedHoursFrom[1] + ":" + organizedHoursFrom[3];
            hoursTo = dateTo + " " + organizedHoursTo[1] + ":" + organizedHoursTo[3];


            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);

            Date fromDate = format.parse(hoursFrom);
            Date toDate = format.parse(hoursTo);


            assert fromDate != null;
            assert toDate != null;
            double calHours = toDate.getTime() - fromDate.getTime();

            double calDateDays = calHours / (60 * 60 * 1000);

            calDateDays = Math.abs(calDateDays);

            System.out.println("두 날짜의 날짜 차이: " + calDateDays + "\n fromDate : " + fromDate.toString() + " toDate : " + toDate.toString() +
                    " calDate : " + calHours + "\n 언제부터 : " + hoursFrom + "  언제까지 : " + hoursTo);

            return calDateDays + 1;
        } catch (ParseException ignored) {
            return 0;
        }

    }

    private Date getFromHours(String date, String hoursFrom) {
        try {
            String[] organizedHoursFrom = hoursFrom.split(" ");

            if (organizedHoursFrom[0].equals("오후")) {
                organizedHoursFrom[1] = Integer.toString(Integer.parseInt(organizedHoursFrom[1]) + 12);
            }


            if (Integer.parseInt(organizedHoursFrom[1]) < 10) {
                organizedHoursFrom[1] = "0" + organizedHoursFrom[1];
            }


            hoursFrom = date + " " + organizedHoursFrom[1] + ":" + organizedHoursFrom[3];


            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);

            return format.parse(hoursFrom);


        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Date getToHours(String date, String hoursTo) {
        try {
            String[] organizedHoursTo = hoursTo.split(" ");

            if (organizedHoursTo[0].equals("오후")) {
                organizedHoursTo[1] = Integer.toString(Integer.parseInt(organizedHoursTo[1]) + 12);
            }


            if (Integer.parseInt(organizedHoursTo[1]) < 10) {
                organizedHoursTo[1] = "0" + organizedHoursTo[1];
            }

            hoursTo = date + " " + organizedHoursTo[1] + ":" + organizedHoursTo[3];

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);

            return format.parse(hoursTo);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getWeekOfYear(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] dates = date.split("-");
        int year = Integer.parseInt(dates[0]);
        int month = Integer.parseInt(dates[1]);
        int day = Integer.parseInt(dates[2]);
        calendar.set(year, month - 1, day);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    public String getDateDay(String date, String dateType, Locale locale) throws Exception {


        String day = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType, locale);
        Date nDate = dateFormat.parse(date);

        Calendar cal = Calendar.getInstance();
        assert nDate != null;
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);


        switch (dayNum) {
            case 1:
                day = "sun";
                break;
            case 2:
                day = "mon";
                break;
            case 3:
                day = "tue";
                break;
            case 4:
                day = "wed";
                break;
            case 5:
                day = "thu";
                break;
            case 6:
                day = "fri";
                break;
            case 7:
                day = "sat";
                break;

        }


        return day;
    }

    public void CalculateWage() {

        try {

            // 날짜를 뺄셈하여 기간을 저장하고 전역변수에 저장
            long subtractDate = subtractDate(txt_mfrom.getText().toString(), txt_mto.getText().toString());

            //totalDB,registerDB의 모든 정보를 가리킬수 있는 Cursor를 만들어준다.(이름은 실제 DB이름과 동일)
            Cursor totalDBCursor = getTotalCursor();
            Cursor registerDBCursor = getregisterCursor();


            //메인 버튼의 알바이름으과 일치하는 totalDB의 알바이름이있는 Row의 모든 Column정보를 가져온다.
            System.out.println("값  :  " + subtractDate);

            for (int i = 0; i < totalDBCursor.getCount(); i++) // n^2
            {
                System.out.println("for문 입장");
                if (i > 0 && !(totalDBCursor.getString(1).equals(btn_ChooseParttime.getText().toString()))) {
                    i = i - 1;
                }
                totalDBCursor.moveToPosition(count);
                if (count < totalDBCursor.getCount()) {
                    count = count + 1;
                } else {
                    break;
                }
                System.out.println(totalDBCursor.getString(1) + "  ,  " + btn_ChooseParttime.getText().toString());
                if (totalDBCursor.getString(1).equals(btn_ChooseParttime.getText().toString())) {
                    for (int j = 0; j < 8; j++) {
                        totalArray[i][j] = totalDBCursor.getString(j);
                        System.out.println("total : " + totalArray[i][j]);
                    }
                }
            }

            //식사시간 소수점 단위로 바꾸기
            double mealtime = Double.parseDouble(totalArray[0][7]) / 60;
            System.out.println("식사시간 : " + mealtime);


            //메인 버튼의 알바이름으과 일치하는 registerWork의 알바이름이있는 Row의 모든 Column정보를 가져온다.
            //fromDate는 메인의 txt_from에서 가져온 String을 Date
            //count는 재사용 위해 다시한번 초기화
            count = 0;
            for (int i = 0; i < registerDBCursor.getCount(); i++) {


                //regsterDB의 날짜 데이터를 하나씩 체크한다.
                registerDBCursor.moveToPosition(i);
                Date registerDate = format1.parse(registerDBCursor.getString(1)); //1번은 날짜정보
                assert registerDate != null;
                System.out.println("registerDate = " + registerDate.getTime() + " txtFrom = " + txtFrom.getTime() + " txtTo = " + txtTo.getTime());
                //registerDB의 이름 정보와 메인의 btn_chooseparttime 버튼의 텍스트와 같고
                //registerDate에 그날 알바정보를 등록한 시간이 txtFrom시간(보고싶은 알바비 기간의 시작날짜)보다 크거나 같고 또 txtTo시간(보고싶은 알바비 기간의 끝날짜)보다 작거나 같다면
                //그 데이터를 registerArray에 집어 넣어준다.
                if (registerDBCursor.getString(2).equals(btn_ChooseParttime.getText().toString()) && (registerDate.getTime() >= txtFrom.getTime() && registerDate.getTime() <= txtTo.getTime())) {
                    for (int j = 0; j < 9; j++) {
                        registerArray[count][j] = registerDBCursor.getString(j);
                    }
                    count++;
                }
            }

            //registerArray내용 확인용(지우지 마시오:버그패치용 이기도함)
            for (int i = 0; i < count; i++) {
                for (int j = 0; j < 9; j++) {
                    System.out.println("registerArray = " + registerArray[i][j]);
                }
            }


            //registerArray의 뒤죽 박죽 일수 있는 필요한! registerArray정보를 날짜 순서대로 버블정렬한다. 1번 = 날짜,3번 = 시작시간,4번 = 퇴근시간,8번 = 퇴근날짜 끼리만 버블 정렬
            for (int i = 0; i < count; i++) {
                for (int j = i + 1; j < count; j++) {
                    System.out.println("registerArray Date = " + registerArray[i][1]);
                    Date date = format1.parse(registerArray[i][1]);
                    if (registerArray[j][1] != null) {
                        Date date1 = format1.parse(registerArray[j][1]);
                        assert date1 != null;
                        assert date != null;
                        if (date.getTime() > date1.getTime()) {
                            String tmp = registerArray[i][1];
                            registerArray[i][1] = registerArray[j][1];
                            registerArray[j][1] = tmp;
                            String tmp1 = registerArray[i][3];
                            registerArray[i][3] = registerArray[j][3];
                            registerArray[j][3] = tmp1;
                            String tmp2 = registerArray[i][4];
                            registerArray[i][4] = registerArray[j][4];
                            registerArray[j][4] = tmp2;
                            String tmp3 = registerArray[i][8];
                            registerArray[i][8] = registerArray[j][8];
                            registerArray[j][8] = tmp3;

                        }
                    } else {
                        break;
                    }


                }
            }


            //registerArray의 1 index = 날짜,3 index =  시작시간,4 index = 퇴근시간,8 index =  퇴근날짜 확인용 (디버그용 지우지 마세요!)
            for (int i = 0; i < count; i++) {
                System.out.println("registerArray = (" + registerArray[i][1] + " , " + registerArray[i][3] + " , " + registerArray[i][4] + " , " + registerArray[i][8] + ")");
            }


            //count = registerArray에서 위의 모든 조건에 합당한 데이터 row의 갯수(즉 원하는 날짜사이의 필요한 데이터들)
            //workinghours에 registerArray의 버블 정렬한것의 시간데이터만 순서대로 넣어주고 hoursSubtractions에  to와 from의 시간차를 subHours()로 구해서 넣어준다.
            for (int i = 0; i < count; i++) {
                workingHours.add(new String[]{registerArray[i][1], registerArray[i][3], registerArray[i][4]});
                System.out.println(workingHours.get(i)[0]);
                String from = workingHours.get(i)[1];
                String to = workingHours.get(i)[2];
                hoursSubtractions.add(new String[]{registerArray[i][1], String.valueOf(subHours(registerArray[i][1], registerArray[i][8], from, to) - 1 - mealtime)});
            }

            //위에서 구한 hoursSubtractions 이차원 배열을 Locat에 표시해준다.
            for (int i = 0; i < count; i++) {
                System.out.println("hoursSubtractionsArray = (" + hoursSubtractions.get(i)[0] + " , " + hoursSubtractions.get(i)[1]);
            }


            //기본급으로 계산한시간 (연장/야간 적용 없이) //휴일수당,주휴수당 포함
            //그 날 하루급여를 기준으로 계산
            // 1 기본급
            for (int i = 0; i < count; i++) {
                totalWorkingHours = Double.parseDouble(hoursSubtractions.get(i)[1]);
                System.out.println("하루 일한시간  : " + i + " = " + totalWorkingHours);
                oneDayWage[i] = totalWorkingHours * Double.parseDouble(totalArray[0][2]);

                //연장근무수당 하루 통상근무 시간인 8시간을 초과했을 때 또는 주급으로 따진다면 주 40시간 초과 했을 때 적용(1.5배)
                if (totalWorkingHours > 8) {
                    double extendedHours = totalWorkingHours - 8;
                    double extendedWage = extendedHours * (Double.parseDouble(totalArray[0][2]) * 0.5);
                    oneDayWage[i] = oneDayWage[i] + extendedWage;
                }

                //야간 근무수당 밤 10시부터 오전 6시까지 일을 했을경우 그 사이의 일한 시간에 대하여 1.5를 받는다. 연장수당에 가산하여 받는다.(연장+야간 겹치기 가능)
                Date tmpFromHours = timeDateFormat.parse(registerArray[i][1] + " 22:00"); //근무당일 밤 10시
                Date today = format1.parse(registerArray[i][1]); //오늘 날짜
                // 내일 날짜 구하기
                assert today != null;
                long tod = today.getTime() + 86400000; //근무당일 날짜 + 86400000(밀리초로 구한하루)
                Date tomorrow = new Date(tod); //근무일다음날 날짜
                String tom = format1.format(tomorrow); // 근무일다음날 0시 0분 0초를 format1을 String인 yyyy-MM-dd HH:mm:ss 날짜형식으로 만들어 tom(tomorrow의 약자)에 넣어줌
                Date tmpToHours = timeDateFormat.parse(tom + " 06:00"); // tmpTohours = 근무일다음날 6시
                Date tmpCurrentFromHours = timeDateFormat.parse(registerArray[i][1] + " 06:00"); // 근무당일 6시
                Date fromHours = getFromHours(registerArray[i][1], registerArray[i][3]); // registerwork의 근무시작시간
                Date ToHours = getToHours(registerArray[i][8], registerArray[i][4]);// registerwork의 근무종료시간
                assert fromHours != null;
                assert tmpToHours != null;
                assert tmpFromHours != null;
                assert ToHours != null;
                assert tmpCurrentFromHours != null;
                //야간수당 조건을 위한 시간의 기준들의 절대적 값을 Logcat 으로 확인(디버그용 지우지 마세요)
                System.out.println(fromHours.getTime() + ", " + tmpFromHours.getTime() + ", " + ToHours.getTime() + ", " + tmpToHours.getTime());
                System.out.println(fromHours.getTime() + ", " + tmpCurrentFromHours.getTime() + ", " + ToHours.getTime() + ", " + tmpCurrentFromHours.getTime());
                System.out.println(fromHours.getTime() + ", " + tmpCurrentFromHours.getTime() + ", " + ToHours.getTime() + ", " + tmpCurrentFromHours.getTime());

                //야간 근무 수당 적용을 위한 조건들
                //조건 1 : 근무시작시간 >= 근무당일 22시 && 근무시작시간 <= 근무일다음날 6시&& 근무종료시간 <= 근무일다음날 6시 && 근무종료시간 >= 근무당일 22시
                //조건 2 : 근무시작시간 >= 근무당일 6시 && 근무시작시간 <= 근무당일 22시 && 근무종료시간 <= 근무일다음날 6시 && 근무종료시간 >= 근무당일 22시
                //조건 3 : 근무시작시간 <= 근무당일 6시 && 근무종료시간 <= 근무당일 6시 && 근무종료시간 > 근무시작시간
                //조건 4 : 근무시작시간 <= 근무당일 6시 && 근무종료시간 >= 근무당일 6시 && 근무종료시간 <= 근무당일 22시
                if ((fromHours.getTime() >= tmpFromHours.getTime() && fromHours.getTime() <= tmpToHours.getTime() && ToHours.getTime() <= tmpToHours.getTime() && ToHours.getTime() >= tmpFromHours.getTime())) {
                    double nightWorkingWage = totalWorkingHours * (Double.parseDouble(totalArray[0][2]) * 0.5);
                    System.out.println("hours" + totalWorkingHours);
                    oneDayWage[i] = oneDayWage[i] + nightWorkingWage;
                } else if (fromHours.getTime() >= tmpCurrentFromHours.getTime() && fromHours.getTime() <= tmpFromHours.getTime() && ToHours.getTime() <= tmpToHours.getTime() && ToHours.getTime() >= tmpFromHours.getTime()) {
                    double hours = Double.parseDouble(String.valueOf((ToHours.getTime() - tmpFromHours.getTime()))) / (1000 * 60 * 60);
                    System.out.println("hours1  : " + i + " = " + hours);
                    double nightWorkingWage = hours * (Double.parseDouble(totalArray[0][2]) * 0.5);
                    oneDayWage[i] = oneDayWage[i] + nightWorkingWage;
                } else if (fromHours.getTime() <= tmpCurrentFromHours.getTime() && ToHours.getTime() <= tmpCurrentFromHours.getTime() && ToHours.getTime() > fromHours.getTime()) {
                    double hours = Double.parseDouble(String.valueOf((ToHours.getTime() - fromHours.getTime()))) / (1000 * 60 * 60);
                    double nightWorkingWage = hours * (Double.parseDouble(totalArray[0][2]) * 0.5);
                    System.out.println("hours2" + hours);
                    oneDayWage[i] = oneDayWage[i] + nightWorkingWage;
                } else if (fromHours.getTime() <= tmpCurrentFromHours.getTime() && ToHours.getTime() >= tmpCurrentFromHours.getTime() && ToHours.getTime() <= tmpFromHours.getTime()) {
                    double hours = Double.parseDouble(String.valueOf((tmpCurrentFromHours.getTime() - fromHours.getTime()))) / (1000 * 60 * 60);
                    double nightWorkingWage = hours * (Double.parseDouble(totalArray[0][2]) * 0.5);
                    System.out.println("hours3" + hours);
                    oneDayWage[i] = oneDayWage[i] + nightWorkingWage;
                }


                //주말수당 검사 및 주말수당 적용
                //배열날짜(hoursSubtractions.get(i)[0]) == sat or sun 이라면 (그날의 날짜 == 토요일 또는 일요일이라면)
                if (getDateDay(hoursSubtractions.get(i)[0], "yyyy-MM-dd", Locale.KOREA).equals("sat") ||
                        getDateDay(hoursSubtractions.get(i)[0], "yyyy-MM-dd", Locale.KOREA).equals("sun")) {
                    double tmpBreakWage = totalWorkingHours * (Double.parseDouble(totalArray[0][2]) * 0.5);
                    oneDayWage[i] = oneDayWage[i] + tmpBreakWage;
                }


                System.out.println("하루 기본급  : " + i + " = " + oneDayWage[i]); //기본시급과 연장,주휴,야간을 포함한 하루 기본급 Locat에 표시 디버그용 지우지마세요
            }

            //주휴 수당
            double[] weekWorkingHours = new double[5];
            try {
                for (int i = 0; i < count; i++) {
                    //저장된 날짜가 그달의 몇주째인지 계산해서 weekNum(week number)에 넣어준다.
                    int weekNum = getWeekOfYear(hoursSubtractions.get(i)[0]);
                    System.out.println("weekNum = " + weekNum + " , " + hoursSubtractions.get(i)[0]); //디버그용 지우지마세요
                    String DateDay = getDateDay(hoursSubtractions.get(i)[0], "yyyy-MM-dd", Locale.KOREA); //근무당일날짜의 요일 getDateDay가 날짜를 그 날짜의 요일로 바꿔줌
                    //받은 날짜가 1주차 날짜라면
                    if (weekNum == 1) {
                        String[] dayOfWeeks = totalArray[0][3].split(","); // totalArray에 기본적으로 근무를하기로 정해진 요일들을 dayOfWeeks에 배열로 나누어 넣음
                        System.out.println("dayOfWeeks1" + dayOfWeeks.length); //디버그용 지우지마세요
                        for (int j = 0; j < 7; j++) {
                            if (dayOfWeeks[j].equals(DateDay)) {  //근무하기로 정해진 요일 == 근무당일날짜의 요일
                                firstWeek += 1; // firstweek 첫째주의 근무나간 횟수를 의미하는 int변수 +1
                                weekWorkingHours[0] = weekWorkingHours[0] + Double.parseDouble(hoursSubtractions.get(i)[1]); // 그주의 일한시간 = 그주의 일한시간 + 근무당일일한시간
                                System.out.println("firstweek = " + weekWorkingHours[0]);//디버그용 지우지마세요
                            }
                        }
                    }
                    //받은 날짜가 2주차 날짜라면 (과정은 위와 동일)
                    else if (weekNum == 2) {
                        String[] dayOfWeeks = totalArray[0][3].split(",");
                        System.out.println("dayOfWeeks2" + dayOfWeeks.length);//디버그용 지우지마세요
                        for (int j = 0; j < 7; j++) {
                            if (dayOfWeeks[j].equals(DateDay)) {
                                secondWeek += 1;
                                weekWorkingHours[1] = weekWorkingHours[1] + Double.parseDouble(hoursSubtractions.get(i)[1]);
                                System.out.println("secondweek = " + weekWorkingHours[1]);
                            }
                        }
                    }//받은 날짜가 3주차 날짜라면(과정은 위와 동일)
                    else if (weekNum == 3) {
                        String[] dayOfWeeks = totalArray[0][3].split(",");
                        System.out.println("dayOfWeeks3" + dayOfWeeks.length);//디버그용 지우지마세요
                        for (int j = 0; j < 7; j++) {
                            if (dayOfWeeks[j].equals(DateDay)) {
                                thirdWeek += 1;
                                weekWorkingHours[2] = weekWorkingHours[2] + Double.parseDouble(hoursSubtractions.get(i)[1]);
                                System.out.println("thirdweek = " + weekWorkingHours[2]);//디버그용 지우지마세요
                            }
                        }
                    }//받은 날짜가 4주차 날짜라면(과정은 위와 동일)
                    else if (weekNum == 4) {
                        String[] dayOfWeeks = totalArray[0][3].split(",");
                        System.out.println("dayOfWeeks4" + dayOfWeeks.length);//디버그용 지우지마세요
                        for (int j = 0; j < 7; j++) {
                            if (dayOfWeeks[j].equals(DateDay)) {
                                forthWeek += 1;
                                weekWorkingHours[3] = weekWorkingHours[3] + Double.parseDouble(hoursSubtractions.get(i)[1]);
                                System.out.println("forthweek = " + weekWorkingHours[3]);//디버그용 지우지마세요
                            }
                        }
                    }//받은 날짜가 5주차 날짜라면(과정은 위와 동일)
                    else if (weekNum == 5) {
                        String[] dayOfWeeks = totalArray[0][3].split(",");
                        System.out.println("dayOfWeeks5" + dayOfWeeks.length);//디버그용 지우지마세요
                        for (int j = 0; j < 7; j++) {
                            if (dayOfWeeks[j].equals(DateDay)) {
                                fifthWeek += 1;
                                weekWorkingHours[4] = weekWorkingHours[4] + Double.parseDouble(hoursSubtractions.get(i)[1]);
                                System.out.println("fifthweek = " + weekWorkingHours[4]);//디버그용 지우지마세요
                            }
                        }
                    }
                }
            }
            //위의 try문이 실패할경우 즉 저장된 데이터가 없을경우
            catch (Exception e) {
                e.printStackTrace();
            }

            //
            String[] dayOfWeeks = totalArray[0][3].split(",");
            int dayofweekcount = 0;
            double value;
            for (String dayOfWeek : dayOfWeeks) {
                if (!dayOfWeek.equals("null")) {
                    dayofweekcount += 1;
                }
            }
            //만약 그 grid의 개수가 totalarray의 요일의 개수와 일치하고 그 주의 총 근무시간이 15시간  이상이라면 주휴수당을 받아야한다.
            // (1주일 총근로시간/40) * totalArray를 통해서 구한 근무시간 * 시급
            if (firstWeek == dayofweekcount && weekWorkingHours[0] >= 15) {
                if (weekWorkingHours[0] >= 40) {
                    weekWorkingHours[0] = 40;
                }
                value = (weekWorkingHours[0] / 40) * 8 * Double.parseDouble(totalArray[0][2]);
                extraMoney[0] = value;
                System.out.println("extraMoney 0 " + extraMoney[0]);
            }
            if (secondWeek == dayofweekcount && weekWorkingHours[1] >= 15) {
                if (weekWorkingHours[1] >= 40) {
                    weekWorkingHours[1] = 40;
                }
                value = (weekWorkingHours[1] / 40) * 8 * Integer.parseInt(totalArray[0][2]);
                extraMoney[1] = value;
                System.out.println("extraMoney 1 " + extraMoney[1]);
            }
            if (thirdWeek == dayofweekcount && weekWorkingHours[2] >= 15) {
                if (weekWorkingHours[2] >= 40) {
                    weekWorkingHours[2] = 40;
                }
                value = (weekWorkingHours[2] / 40) * 8 * Integer.parseInt(totalArray[0][2]);
                extraMoney[2] = value;
                System.out.println("extraMoney 2 " + extraMoney[2]);
            }
            if (forthWeek == dayofweekcount && weekWorkingHours[3] >= 15) {
                if (weekWorkingHours[3] >= 40) {
                    weekWorkingHours[3] = 40;
                }
                value = (weekWorkingHours[3] / 40) * 8 * Integer.parseInt(totalArray[0][2]);
                extraMoney[3] = value;
                System.out.println("extraMoney 3 " + extraMoney[3]);
            }
            if (fifthWeek == dayofweekcount && weekWorkingHours[4] >= 15) {
                if (weekWorkingHours[4] >= 40) {
                    weekWorkingHours[4] = 40;
                }
                value = (weekWorkingHours[4] / 40) * 8 * Integer.parseInt(totalArray[0][2]);
                extraMoney[4] = value;
                System.out.println("extraMoney 4 " + extraMoney[4]);
            }
            double extramoney = 0;
            try {
                for (int i = 0; i < 5; i++) {
                    try {
                        extramoney += extraMoney[i];
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    System.out.println("extramoney " + extramoney);
                }
            } catch (Exception e) {
                System.out.println("주휴수당은 없습니다.");
            }

            //extramoney = 총 주휴수당 여기다 모든 oneDayWage를 더하면 그 기간동안의 법정 최소한의 급여가 된다.
            for (int i = 0; i < count; i++) {
                totalWage += oneDayWage[i];
                System.out.println(i + " : " + oneDayWage[i]);
            }
            totalWage = totalWage + extramoney;

            showWage.setText(String.format(Locale.KOREA, "%d 원", (int) totalWage));
            PreferenceManager.setString(getApplicationContext(), "suggestedSavedWage", String.format(Locale.KOREA, "%d 원", (int) totalWage));
            System.out.println(String.format(Locale.KOREA, "%d 원", (int) totalWage));

        } catch (Exception ignored) {
            System.out.println("무시됨");
        }
    }


}