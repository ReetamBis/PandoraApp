package com.example.pandora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Tutorial extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner subj;
    String subject,linkt,uid,titlet;
    Button upload;
    FirebaseUser currentFirebaseUser;
    FirebaseFirestore fStore;
    EditText title,link;
    TutLink t;

    public boolean allfilled(){

        if(title.getText().toString().equals("") || link.getText().toString().equals("")) {
            Toast.makeText(Tutorial.this, "Fill all the fields !!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void onUploadTut(View view){

        if(view.getId() == R.id.uploadtut){

            currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
            uid = currentFirebaseUser.getUid().toString();
            titlet = title.getText().toString();
            linkt = link.getText().toString();
            t = new TutLink(subject,uid,linkt,titlet);

            if(allfilled())
                uploadLink(t);
            else
                Toast.makeText(this,"Please fill all th fields....",Toast.LENGTH_SHORT).show();
            //uploadLink(t);
        }
    }

    public void uploadLink(TutLink t){

        DocumentReference df = fStore.collection("Tutorials").document(t.getSubject()).collection(t.getSubject()).document(t.getFilename());
        Map<String,Object> tutInfo = new HashMap<>();
        tutInfo.put("UserID",t.getUid());
        tutInfo.put("Date/Time",t.getDateTime());
        tutInfo.put("Visible",t.getCheckBit());
        tutInfo.put("Info",t.getTitle());
        tutInfo.put("Rating",t.getRating());
        tutInfo.put("Link",t.getLink());
        df.set(tutInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                    Toast.makeText(Tutorial.this,"Link Successfully Uploaded",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Tutorial.this,"Error!! Link not uploaded",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        subj = findViewById(R.id.subtut);
        upload = findViewById(R.id.uploadtut);
        title = findViewById(R.id.info);
        link = findViewById(R.id.link);

        subj.setOnItemSelectedListener(this);

        fStore = FirebaseFirestore.getInstance();

        ArrayAdapter<String> subAdapter = new ArrayAdapter<String>(Tutorial.this,
                R.layout.custom_spinner,getResources().getStringArray(R.array.paper));
        subAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        subj.setAdapter(subAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId()==R.id.subtut){
            subject = parent.getItemAtPosition(position).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}