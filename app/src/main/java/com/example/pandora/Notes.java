package com.example.pandora;
import android.net.Uri;

import java.util.Calendar;

public class Notes extends Item {


    private String dateTime;
    private  String subject;
    private String teacher;
    private String uid;
    private Calendar cal;
    private String checkBit;
    private String rating;



    public Notes(String subject,String uid,String teacher,String name) {
        super(name,null);
        this.cal = Calendar.getInstance();
        this.subject = subject;
        this.uid = uid;
        this.dateTime = cal.getTime().toString();
        this.checkBit = "0";
        this.rating = "0";
        if(!teacher.equals("")){
            this.teacher = teacher;
        }
        else
            this.teacher = "Unknown";
    }



    public String getDateTime() {
        return dateTime;
    }

    public String getSubject() {
        return subject;
    }

    public String getTeacher() {
        return teacher;
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
