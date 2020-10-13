package com.example.pandora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText roll;
    EditText pass;
    Button signin;
    TextView reg;

    public void onClick(View view){

        if(view.getId()==R.id.signin){

            LoginActivity.this.finish();
        }

        if(view.getId()==R.id.reg){
            Intent intent= new Intent(LoginActivity.this,com.example.pandora.Register.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        roll = findViewById(R.id.roll);
        pass = findViewById(R.id.pass);
        signin = findViewById(R.id.signin);
        reg = findViewById(R.id.reg);


    }
}