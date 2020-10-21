package com.example.pandora;

import java.util.Calendar;

public class TutLink {

    private String filename;
    private String dateTime;
    private  String subject;
    private String title;
    private String link;
    private String uid;
    private Calendar cal;
    private String checkBit;
    private String rating;

    public TutLink(String subject,String uid,String link,String title) {

        this.cal = Calendar.getInstance();
        this.subject = subject;
        this.uid = uid;
        this.title = title;
        this.dateTime = cal.getTime().toString();
        this.filename = this.subject+" "+this.title+" "+this.dateTime;
        this.checkBit = "0";
        this.rating = "0";
        this.link = link;
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

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getUid() {
        return uid;
    }

    public String getCheckBit() {
        return checkBit;
    }

    public String getRating() {
        return rating;
    }
}
