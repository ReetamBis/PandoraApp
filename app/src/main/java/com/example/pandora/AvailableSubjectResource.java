package com.example.pandora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AvailableSubjectResource extends AppCompatActivity {
    TextView subname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_subject_resource);
        Intent intent=getIntent();
        String Category=intent.getStringExtra("Category");
        String Subject=intent.getStringExtra("Subject");
        subname=findViewById(R.id.subname);
        subname.setText(Category+" :: "+Subject);
    }
}