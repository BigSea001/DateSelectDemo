package com.dahai.dateselectdemo;

/**
 * 描述：
 * 时间：2018/1/27/027
 * 作者：xjh
 */
public class DayBean {
    private int year;           //属于的年份
    private int month;           //属于的月份
    private int day ;           //日期，几号
    private String jr;
    private boolean isSelect;
    private boolean isMiddle;


    public DayBean(int year, int month, int day, String jr, boolean isSelect, boolean isMiddle) {

        this.year = year;
        this.month = month;
        this.day = day;
        this.jr = jr;
        this.isSelect = isSelect;
        this.isMiddle = isMiddle;
    }

    public String getJr() {
        return jr;
    }

    public void setJr(String jr) {
        this.jr = jr;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isMiddle() {
        return isMiddle;
    }

    public void setMiddle(boolean middle) {
        isMiddle = middle;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {

        this.year = year;
    }

}
