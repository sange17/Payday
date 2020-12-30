package com.alba.lionproject1;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class customcalendarview extends LinearLayout {
    ImageButton NextButton, PreviousButton;
    TextView CurrentDate;
    GridView gridView;

    private static final int MAX_CALENDAR_DAYS = 42;
    public static Calendar calendar = Calendar.getInstance(Locale.KOREA);
    public static Context context;
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMMM", Locale.KOREA);
    public static SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.KOREA);
    public static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
    public static SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

    MyGridAdapter myGridAdapter;
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();

    DBOpenHelper dbOpenHelper;

    public customcalendarview(Context context) {
        super(context);
    }

    public customcalendarview(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        customcalendarview.context = context;
        InitializeLayout();
        SetUpCalendar();


        String currentMonthAndYear = (String) CurrentDate.getText();
        PreferenceManager.setString(context, "time", currentMonthAndYear);

        PreviousButton.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            SetUpCalendar();
        });

        NextButton.setOnClickListener(view -> {
            calendar.add(Calendar.MONTH, 1);
            SetUpCalendar();
        });

        // 그리드 즉 날짜칸 클릭했을 때 발생하는 이벤트
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Button btn_parttimeselect = ((MainActivity) MainActivity.context_main).findViewById(R.id.btn_ChooseParttime);
            if (!btn_parttimeselect.getText().toString().equals("알바 선택해")) {
                final String date = eventDateFormat.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year = yearFormat.format(dates.get(position));
                //SaveEvent("event","time",date,month,year);
                //SetUpCalendar();
                Intent intent = new Intent(getContext(), registerWork.class);
                intent.putExtra("date", date);
                intent.putExtra("month", month);
                intent.putExtra("year", year);
                context.startActivity(intent);
            } else {
                Toast.makeText(getContext(), "알바 기본정보를 설정하고 그 알바를 선택해야 들어갈 수 있어", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public customcalendarview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }


    public void InitializeLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layour, this);
        NextButton = view.findViewById(R.id.nextBtn);
        PreviousButton = view.findViewById(R.id.previousBtn);
        CurrentDate = view.findViewById(R.id.current_Date);
        gridView = view.findViewById(R.id.gridview);
    }

    public void SetUpCalendar() {
        String currentDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayOfMonth);
        CollectEventsPerMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));

        while (dates.size() < MAX_CALENDAR_DAYS) {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);


        }

        myGridAdapter = new MyGridAdapter(context, dates, calendar, eventsList);
        gridView.setAdapter(myGridAdapter);

    }

    private void CollectEventsPerMonth(String Month, String year) {
        eventsList.clear();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsperMonth(Month, year, database);
        while (cursor.moveToNext()) {
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            Events events = new Events(event, time, date, month, Year);
            eventsList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();
    }

}
