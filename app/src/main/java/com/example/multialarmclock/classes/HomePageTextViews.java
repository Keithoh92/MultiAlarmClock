package com.example.multialarmclock.classes;

public class HomePageTextViews {

    private String last_used_tv;
    private String days_of_the_week_tv;
    private String time_range_tv;
    private String set_every_tv;
    private String quick_alarm_builder_tv;
    private String my_saved_alarms_tv;
    private String build_new_alarm_tv;

    public HomePageTextViews(String last_used_tv, String days_of_the_week_tv, String time_range_tv, String set_every_tv, String quick_alarm_builder_tv, String my_saved_alarms_tv, String build_new_alarm_tv) {
        this.last_used_tv = last_used_tv;
        this.days_of_the_week_tv = days_of_the_week_tv;
        this.time_range_tv = time_range_tv;
        this.set_every_tv = set_every_tv;
        this.quick_alarm_builder_tv = quick_alarm_builder_tv;
        this.my_saved_alarms_tv = my_saved_alarms_tv;
        this.build_new_alarm_tv = build_new_alarm_tv;
    }

    public String getLast_used_tv() {
        return last_used_tv;
    }

    public void setLast_used_tv(String last_used_tv) {
        this.last_used_tv = last_used_tv;
    }

    public String getDays_of_the_week_tv() {
        return days_of_the_week_tv;
    }

    public void setDays_of_the_week_tv(String days_of_the_week_tv) {
        this.days_of_the_week_tv = days_of_the_week_tv;
    }

    public String getTime_range_tv() {
        return time_range_tv;
    }

    public void setTime_range_tv(String time_range_tv) {
        this.time_range_tv = time_range_tv;
    }

    public String getSet_every_tv() {
        return set_every_tv;
    }

    public void setSet_every_tv(String set_every_tv) {
        this.set_every_tv = set_every_tv;
    }

    public String getQuick_alarm_builder_tv() {
        return quick_alarm_builder_tv;
    }

    public void setQuick_alarm_builder_tv(String quick_alarm_builder_tv) {
        this.quick_alarm_builder_tv = quick_alarm_builder_tv;
    }

    public String getMy_saved_alarms_tv() {
        return my_saved_alarms_tv;
    }

    public void setMy_saved_alarms_tv(String my_saved_alarms_tv) {
        this.my_saved_alarms_tv = my_saved_alarms_tv;
    }

    public String getBuild_new_alarm_tv() {
        return build_new_alarm_tv;
    }

    public void setBuild_new_alarm_tv(String build_new_alarm_tv) {
        this.build_new_alarm_tv = build_new_alarm_tv;
    }

}
