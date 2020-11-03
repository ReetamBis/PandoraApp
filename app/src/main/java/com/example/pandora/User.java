package com.example.pandora;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String name;
    private String roll;
    private String mail;
    private String pass;
    private String mob;
    private String isUser;
    private String isAdmin;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    public User(String name, String roll, String mail, String pass, String mob) {
        this.name = name;
        this.roll = roll;
        this.mail = mail;
        this.pass = pass;
        this.mob = mob;
        this.isAdmin = "0";
        this.isUser = "1";
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    public User(String mail, String pass) {
        this.mail = mail;
        this.pass = pass;
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    public String getName() {
        return name;
    }

    public String getRoll() {
        return roll;
    }

    public String getMail() {
        return mail;
    }

    public String getPass() {
        return pass;
    }

    public String getMob() {
        return mob;
    }

    public String getIsUser() {
        return isUser;
    }

    public String getIsAdmin() {
        return isAdmin;
    }


}
