package com.example.pandora;
import java.util.Calendar;

public class PrevPaper {

    String filename;
    String dateTime;
    String subject;
    String year;
    String type;
    String uid;
    String url;
    Calendar cal;
    String checkBit;

    public PrevPaper(String subject, String year, String type, String uid) {

        this.cal = Calendar.getInstance();
        this.subject = subject;
        this.year = year;
        this.type = type;
        this.uid = uid;
        this.dateTime = cal.getTime().toString();
        this.filename = this.subject+"/"+this.year+"/"+this.type+"/"+this.dateTime;
        this.checkBit = "0";

    }



}
