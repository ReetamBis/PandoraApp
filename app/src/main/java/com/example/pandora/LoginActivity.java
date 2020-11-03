package com.example.pandora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText mail;
    EditText pass;
    Button signin;
    TextView reg,forget;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    public boolean checkpassword(String s)
    {

        int n=s.length();
        if(n<8) {
            Toast.makeText(LoginActivity.this, "Minimum password length must be 8 !!", Toast.LENGTH_LONG).show();
            return false;
        }
        int c=0,num=0;
        String specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^_`{|}";
        for(int i=0;i<n;i++)
        {
            char ch = s.charAt(i);
            if(specialCharactersString.contains(Character.toString(ch))) {
                c=c+1;

            }
            else if(Character.isDigit(s.charAt(i)))
            {
                num=num+1;
            }

        }
        if(num==0||c==0)
        {
            Toast.makeText(LoginActivity.this, "Password must contain minimum one digit and one symbol !!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    public void onClick(View view){

        if(view.getId()==R.id.signin){

            if(checkpassword(pass.getText().toString())){

                User user = new User(mail.getText().toString(),pass.getText().toString());
                user.loginuser(getApplicationContext());
                //final Handler handler = new Handler();

                /*handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LoginActivity.this.finish();
                    }
                }, 4000);*/

            }
            else
                Toast.makeText(LoginActivity.this, "Wrong UserId or Password !!", Toast.LENGTH_LONG).show();
        }

        if(view.getId()==R.id.reg){
            Intent intent= new Intent(LoginActivity.this,com.example.pandora.Register.class);
            startActivity(intent);
        }

        if(view.getId()==R.id.forget){

            final EditText resetMail = new EditText(view.getContext());
            final AlertDialog.Builder passwordResestDialog = new AlertDialog.Builder(view.getContext());
            passwordResestDialog.setTitle("Reset Password?");
            passwordResestDialog.setMessage("Enter your Email to receive the reset link..");
            passwordResestDialog.setView(resetMail);

            passwordResestDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String mail = resetMail.getText().toString();
                    fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(LoginActivity.this, "Reset Link has been sent !!!", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Error !!"+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            passwordResestDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            passwordResestDialog.create().show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mail = findViewById(R.id.mail);
        pass = findViewById(R.id.pass);
        signin = findViewById(R.id.signin);
        reg = findViewById(R.id.reg);
        forget = findViewById(R.id.forget);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = fAuth.getCurrentUser();
        if(user != null){
            if(user.isEmailVerified()) {
                Intent intent1 = new Intent(LoginActivity.this, com.example.pandora.DashBoard.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(intent1);

            }
            else
                Toast.makeText(LoginActivity.this, "Verify your E-mail first.", Toast.LENGTH_SHORT).show();
        }
    }
}