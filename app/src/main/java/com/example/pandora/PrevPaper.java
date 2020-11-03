package com.example.pandora;
import android.net.Uri;

import androidx.cardview.widget.CardView;

import java.net.URL;
import java.util.Calendar;

public class PrevPaper extends Item{


    private String dateTime;
    private String subject;
    private String year;
    private String type;
    private String uid;
    private Calendar cal;
    private String checkBit;

    public PrevPaper(String filename, String dateTime, String subject, String year, String type, Uri url) {
        super(filename,url);

        this.dateTime = dateTime;
        this.subject = subject;
        this.year = year;
        this.type = type;

    }



    public PrevPaper(String subject, String year, String type, String uid) {
        super(subject + " " + year + " " + type + " " + Calendar.getInstance().getTime().toString(), null);
        this.cal = Calendar.getInstance();
        this.subject = subject;
        this.year = year;
        this.type = type;
        this.uid = uid;
        this.dateTime = cal.getTime().toString();

        this.checkBit = "0";
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getSubject() {
        return subject;
    }

    public String getYear() {
        return year;
    }

    public String getType() {
        return type;
    }

    public String getUid() {
        return uid;
    }

    public String getCheckBit() {
        return checkBit;
    }
}
