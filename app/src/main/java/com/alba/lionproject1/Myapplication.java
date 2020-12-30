package com.alba.lionproject1;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Myapplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private String nameOfAlba = "알바를 선택하세요";
    private String photo_atPath = "";
    private String photo_afterPath = "";
    private String mainDateFrom = "";
    private String mainDateTo = "";
    private String date_To = "";
    private String gridDates = "";

    public String getNameOfAlba() {
        return nameOfAlba;
    }

    public void setNameOfAlba(String nameOfAlba) {
        this.nameOfAlba = nameOfAlba;
    }

    public String getPhoto_atPath() {
        return photo_atPath;
    }

    public void setPhoto_atPath(String phoroPath) {
        this.photo_atPath = phoroPath;
    }

    public String getPhoto_afterPath() {
        return photo_afterPath;
    }

    public void setPhoto_afterPath(String photo_afterPath) {
        this.photo_afterPath = photo_afterPath;
    }

    public String getDate_To() {
        return date_To;
    }

    public void setDate_To(String date_To) {
        this.date_To = date_To;
    }

    public String getGridDates() {
        return gridDates;
    }

    public void setGridDates(String gridDates) {
        this.gridDates = gridDates;
    }

    public String getMainDateFrom() {
        return mainDateFrom;
    }

    public String getMainDateTo() {
        return mainDateTo;
    }

    public void setMainDateFrom(String mainDateFrom) {
        this.mainDateFrom = mainDateFrom;
    }

    public void setMainDateTo(String mainDateTo) {
        this.mainDateTo = mainDateTo;
    }
}
