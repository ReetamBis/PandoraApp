package com.example.pandora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText id;
    EditText name;
    EditText num;
    EditText email;
    EditText pass;
    EditText repass;
    TextView verifyt;
    Button register,verbut;
    FirebaseAuth fAuth;


     boolean checkmail(String mail,String roll){

        for(int i =0;i<7;i++){

            if(mail.charAt(i)!=roll.charAt(i)){
                Toast.makeText(Register.this, "Mail doesn't match with roll !!", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        if(!mail.substring(8).equals("kiit.ac.in")){
            Toast.makeText(Register.this, "Please enter your KIIT mail ID !!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public boolean checkpassword(String s)
    {

        int n=s.length();
        if(n<8) {
            Toast.makeText(Register.this, "Minimum password length must be 8 !!", Toast.LENGTH_LONG).show();
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
            Toast.makeText(Register.this, "Password must contain minimum one digit and one symbol !!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    public boolean allfilled(){

         if(id.getText().toString().equals("") || name.getText().toString().equals("") ||
                 num.getText().toString().equals("") || email.getText().toString().equals("") ||
                 pass.getText().toString().equals("") || repass.getText().toString().equals("")) {
             Toast.makeText(Register.this, "Fill all the fields !!", Toast.LENGTH_LONG).show();
             return false;
         }
         return true;
    }

    public boolean match(){

         if(!pass.getText().toString().equals(repass.getText().toString())){
             Toast.makeText(Register.this, "Password does not match !!", Toast.LENGTH_LONG).show();
             return false;
         }
         return true;
    }

    public void onReg(View view){

        if(view.getId()==R.id.register){

            if(allfilled() && checkmail(email.getText().toString(),id.getText().toString()) && checkpassword(pass.getText().toString())
                    && match()){
                User u = new User(name.getText().toString(),id.getText().toString(),email.getText().toString(),pass.getText().toString(),num.getText().toString());
                u.createUser(getApplicationContext());
                verifyt.setVisibility(View.VISIBLE);
                verbut.setVisibility(View.VISIBLE);
            }
        }

        if(view.getId()==R.id.verbut){

            FirebaseUser user = fAuth.getCurrentUser();

            if(!user.isEmailVerified()){
                Toast.makeText(Register.this, "Verify your E-mail ", Toast.LENGTH_SHORT).show();
            }
            else
                Register.this.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        id = findViewById(R.id.id);
        name = findViewById(R.id.name);
        num = findViewById(R.id.num);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        repass = findViewById(R.id.repass);
        register = findViewById(R.id.register);
        verifyt = findViewById(R.id.verifyt);
        verbut = findViewById(R.id.verbut);
        fAuth = FirebaseAuth.getInstance();

        verifyt.setVisibility(View.INVISIBLE);
        verbut.setVisibility(View.INVISIBLE);


    }
}