package com.example.pandora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class AvailableSubjectResource extends AppCompatActivity {
    TextView subname;
    Spinner typewise;
    Spinner yearwise;
    RecyclerView listitem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_subject_resource);
        Intent intent=getIntent();
        String Category=intent.getStringExtra("Category");
        String Subject=intent.getStringExtra("Subject");
        subname=findViewById(R.id.subname);
        typewise=findViewById(R.id.typewise);
        yearwise=findViewById(R.id.yearwise);
        listitem=findViewById(R.id.listItems);
        subname.setText(Category+" :: "+Subject);
    }
    public void searchitem(View view)
    {

    }
}