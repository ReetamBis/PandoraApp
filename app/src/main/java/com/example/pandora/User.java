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

    public void createUser(Context context){
        boolean flag = false;
        fAuth.createUserWithEmailAndPassword(mail,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = fAuth.getCurrentUser();
                DocumentReference df = fStore.collection("Users").document(user.getUid());
                Map<String,Object> userInfo = new HashMap<>();
                Toast.makeText(context, "Account Created", Toast.LENGTH_SHORT).show();
                userInfo.put("UserName",name);
                userInfo.put("Roll",roll);
                userInfo.put("E-Mail",mail);
                userInfo.put("Mobile",mob);
                userInfo.put("isAdmin","0");
                userInfo.put("isUser","1");
                df.set(userInfo);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error !!"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkUsersAccessLevel(String uid,Context context){

        DocumentReference df = fStore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG","onSuccess: "+ documentSnapshot.getData());

                /*if(documentSnapshot.getString("isAdmin").equals("1")){
                    //admin intent
                }*/

                if(documentSnapshot.getString("isUser").equals("1")){
                    Intent intent1= new Intent(context,com.example.pandora.DashBoard.class);
                    context.startActivity(intent1);
                }
            }
        });
    }

    public void loginuser(Context context){

        fAuth.signInWithEmailAndPassword(mail,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Toast.makeText(context, "Successfully Logged In !!", Toast.LENGTH_LONG).show();
                checkUsersAccessLevel(authResult.getUser().getUid(),context);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error !!"+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
