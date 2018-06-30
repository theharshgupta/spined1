package com.assistx.hg.sp;

public class Contact {
    int id;
    String date;
    String bad;
    String good;

    public Contact() {

    }

    public Contact(String date, String bad, String good) {

        this.date = date;
        this.bad = bad;
        this.good = good;

    }

    public Contact(int id, String date, String bad, String good) {

        this.id = id;
        this.date = date;
        this.bad = bad;
        this.good = good;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;

    }

    public String getBad() {
        return bad;
    }

    public void setBad(String bad) {
        this.bad = bad;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }
}
