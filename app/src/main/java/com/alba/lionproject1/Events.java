package com.alba.lionproject1;

public class Events {
    String EVENT, TIME, DATE, MONTH, YEAR;

    public String getDATE() {
        return DATE;
    }

    public Events(String EVENT, String TIME, String DATE, String MONTH, String YEAR) {
        this.EVENT = EVENT;
        this.TIME = TIME;
        this.DATE = DATE;
        this.MONTH = MONTH;
        this.YEAR = YEAR;
    }
}
