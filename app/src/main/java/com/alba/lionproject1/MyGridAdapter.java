package com.alba.lionproject1;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyGridAdapter extends ArrayAdapter {
    List<Date> dates;
    Calendar currentDate;
    List<Events> events;
    LayoutInflater inflater;
    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.KOREA);
    int count = 0;


    public MyGridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<Events> events) {
        super(context, R.layout.single_cell_layout);

        this.dates = dates;
        this.currentDate = currentDate;
        this.events = events;
        inflater = LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Date currentTime = Calendar.getInstance().getTime();
        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);
        int DayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentDay = Integer.parseInt(dayFormat.format(currentTime));


        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.single_cell_layout, parent, false);


        }


        if (displayMonth == currentMonth && displayYear == currentYear) {
            //view.setBackgroundColor(getContext().getResources().getColor(R.color.green));
            view.setBackground(getContext().getResources().getDrawable(R.drawable.calendarborder, null));


        } else {
            view.setBackground(getContext().getResources().getDrawable(R.drawable.offcalendarborder, null));
            count += 1;

        }

        String writtenMonth = PreferenceManager.getString(getContext(), "time");
        String cMonth = writtenMonth.substring(5, 6);
        String cYear = writtenMonth.substring(0, 4);
        if (Integer.parseInt(cYear) == currentYear && Integer.parseInt(cMonth) == currentMonth && DayNo == currentDay && count == 0) {
            view.setBackground(getContext().getResources().getDrawable(R.drawable.chosencalendar, null));


        }
        count = 0;

        TextView Day_Number = view.findViewById(R.id.Calendar_day);
        TextView EventNumber = view.findViewById(R.id.events_id);

        Button btn_forAlbaName = ((MainActivity) MainActivity.context_main).findViewById(R.id.btn_ChooseParttime);
        String albaname = btn_forAlbaName.getText().toString();

        String[] daysOfWeek = new String[7];
        String test;
        String albaday = "";
        Cursor cursor = getTotalCursor();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            if (cursor.getString(1).equals(albaname)) {
                daysOfWeek = cursor.getString(3).split(",");
                break;
            }

        }

        for (int i = 0; i < 7; i++) {
            System.out.println(daysOfWeek[i]);
        }
        String date = currentYear + "-" + ChangeDateString(currentMonth) + "-" + ChangeDateString(DayNo);
        try {

            test = getDateDay(date, "yyyy-MM-dd", Locale.KOREA);
            for (int i = 0; i < 7; i++) {
                if (test.equals(daysOfWeek[i]) && (displayMonth == currentMonth && displayYear == currentYear)) {
                    EventNumber.setTextColor(Color.BLACK);
                    EventNumber.setText("알바날");
                    albaday = "알바날";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //registerWorkDBHelper
        String tmpMonth = "null";
        String tmpDay = "null";
        String tmpYear = "null";
        Cursor cursor1 = getregisterCursor(); // registerWork database
        for (int i = 0; i < cursor1.getCount(); i++) {
            cursor1.moveToPosition(i);
            if (albaname.equals(cursor1.getString(2))) {
                tmpYear = cursor1.getString(1).substring(0, 3 + 1);
                tmpMonth = cursor1.getString(1).substring(5, 6 + 1);
                tmpDay = cursor1.getString(1).substring(8, 9 + 1);
            }
            if (tmpDay.equals(ChangeDateString(DayNo)) && tmpMonth.equals(ChangeDateString(displayMonth)) && tmpYear.equals(Integer.toString(displayYear))) {
                EventNumber.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.peach));
                String saved = "저장됨\n" + albaday;
                EventNumber.setText(saved);
                System.out.println("임시입력달 = " + tmpMonth + "\n임시입력일 = " + tmpDay);
            }
        }


        Day_Number.setText(String.valueOf(DayNo));
        Calendar eventCalendar = Calendar.getInstance();
        for (int i = 0; i < events.size(); i++) {
            eventCalendar.setTime(ConvertStringToDate(events.get(i).getDATE()));

        }


        //지워도 되는지 판단중(지워도 잘 작동 될거같음)
        if (EventNumber.getText().toString().equals("저장됨\n알바날")) {
            Myapplication myapplication = (Myapplication) ((Activity) getContext()).getApplication();
            myapplication.setGridDates(myapplication.getGridDates() + " , " + displayYear + "-" + displayMonth + "-" + DayNo);
            System.out.println("setgridDates = " + myapplication.getGridDates());
            try {
                PreferenceManager.setString(getContext(), "gridDates", myapplication.getGridDates());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return view;
    }


    private Date ConvertStringToDate(String eventDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
        Date date = null;
        try {
            date = format.parse(eventDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }


    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    private Cursor getTotalCursor() {
        totalDB db = totalDB.getInstance(getContext());
        return db.getReadableDatabase().query(
                DataBases.CreatDB._TABLENAME0, null, null, null, null, null, DataBases.CreatDB._ID);

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

    public String ChangeDateString(int dayno) {
        String Dayno = Integer.toString(dayno);
        if (dayno < 10) {
            return "0" + Dayno;
        } else {
            return Dayno;
        }
    }

    private Cursor getregisterCursor() {
        registerWorkDBHelper db = registerWorkDBHelper.getInstance(getContext());
        return db.getReadableDatabase().query(
                registerWorkContract.registerWorkEntry.TABLE_NAME, null, null, null, null, null, registerWorkContract.registerWorkEntry._ID);
    }


}


