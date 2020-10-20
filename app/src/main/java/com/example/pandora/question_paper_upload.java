package com.example.pandora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class question_paper_upload extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner sub;
    Spinner year;
    Spinner type;
    PrevPaper p;
    String subject,sem,yy,uid;
    Button upload,select;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseStorage storage;
    FirebaseDatabase database;
    FirebaseUser currentFirebaseUser;
    Uri pdfUri;

    void onUploadpaper(View view){

        if(view.getId() == R.id.select){

            if(ContextCompat.checkSelfPermission(question_paper_upload.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                selectPdf();
            }
            else
                ActivityCompat.requestPermissions(question_paper_upload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
        }

        if(view.getId() == R.id.upload){

            currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
            uid = currentFirebaseUser.getUid().toString();
            p = new PrevPaper(subject,yy,sem,uid);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==9 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            selectPdf();
        else
            Toast.makeText(this, "Please Provide Permission", Toast.LENGTH_SHORT).show();
    }

    void selectPdf(){
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_paper_upload);

        sub = findViewById(R.id.sub);
        year = findViewById(R.id.year);
        type = findViewById(R.id.type);
        upload = findViewById(R.id.upload);
        select = findViewById(R.id.upload);

        sub.setOnItemSelectedListener(this);
        year.setOnItemSelectedListener(this);
        type.setOnItemSelectedListener(this);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        ArrayAdapter<String> subAdapter = new ArrayAdapter<String>(question_paper_upload.this,
                R.layout.custom_spinner,getResources().getStringArray(R.array.paper));
        subAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        sub.setAdapter(subAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(question_paper_upload.this,
                R.layout.custom_spinner,getResources().getStringArray(R.array.year));
        yearAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        year.setAdapter(yearAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(question_paper_upload.this,
                R.layout.custom_spinner,getResources().getStringArray(R.array.type));
        typeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        type.setAdapter(typeAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId()==R.id.sub){
            subject = parent.getItemAtPosition(position).toString();
        }

        if(parent.getId()==R.id.year){
            yy = parent.getItemAtPosition(position).toString();
        }

        if(parent.getId()==R.id.type){
            sem = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}