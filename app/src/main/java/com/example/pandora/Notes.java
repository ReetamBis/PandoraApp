package com.example.pandora;
import java.util.Calendar;

public class Notes {

    private String filename;
    private String dateTime;
    private  String subject;
    private String teacher;
    private String uid;
    private Calendar cal;
    private String checkBit;
    private String rating;

    public Notes(String subject,String uid,String teacher) {

        this.cal = Calendar.getInstance();
        this.subject = subject;
        this.uid = uid;
        this.dateTime = cal.getTime().toString();
        this.filename = this.subject+" "+this.dateTime;
        this.checkBit = "0";
        this.rating = "0";
        if(!teacher.equals("")){
            this.teacher = teacher;
        }
        else
            this.teacher = "Unknown";
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
