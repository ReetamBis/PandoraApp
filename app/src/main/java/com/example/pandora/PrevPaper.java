package com.example.pandora;
import android.net.Uri;

import java.net.URL;
import java.util.Calendar;

public class PrevPaper {

    private String filename;
    private String dateTime;
    private String subject;
    private String year;
    private String type;
    private String uid;
    private Calendar cal;
    private String checkBit;
    private Uri url;
    public PrevPaper(String filename, String dateTime, String subject, String year, String type, Uri url) {
        this.filename = filename;
        this.dateTime = dateTime;
        this.subject = subject;
        this.year = year;
        this.type = type;
        this.url=url;
    }

    public Uri getUrl() {
        return url;
    }

    public PrevPaper(String subject, String year, String type, String uid) {

        this.cal = Calendar.getInstance();
        this.subject = subject;
        this.year = year;
        this.type = type;
        this.uid = uid;
        this.dateTime = cal.getTime().toString();
        this.filename = this.subject+" "+this.year+" "+this.type+" "+this.dateTime;
        this.checkBit = "0";
    }

    public String getFilename() {
        return filename;
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
