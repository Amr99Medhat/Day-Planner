package com.amr_medhat.dayplanner.models;

public class Time {

    private final int hour, minute;
    private final String am_pm_format;


    public Time(int hour, int minute, String am_pm_format) {
        this.hour = hour;
        this.minute = minute;
        this.am_pm_format = am_pm_format;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getAm_pm_format() {
        return am_pm_format;
    }
}
