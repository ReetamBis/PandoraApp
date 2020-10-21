package com.example.pandora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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

public class notes_upload extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner subj;
    String subject,teacher,uid;
    Button upload,select;
    FirebaseUser currentFirebaseUser;
    Uri pdfUri;
    TextView notifyn;
    FirebaseFirestore fStore;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    EditText teachern;
    Notes n;

    public void onUploadnotes(View view){

        if(view.getId() == R.id.selectnotes){

            if(ContextCompat.checkSelfPermission(notes_upload.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                selectPdf();
            }
            else
                ActivityCompat.requestPermissions(notes_upload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
        }

        if(view.getId() == R.id.uploadnotes){

            currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
            uid = currentFirebaseUser.getUid().toString();
            teacher = teachern.getText().toString();
            n = new Notes(subject,uid,teacher);

            if(pdfUri!=null)
                uploadFile(pdfUri,n);
            else
                Toast.makeText(this,"Select a file ..",Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadFile(Uri pdfUri, Notes n){

        progressDialog = new ProgressDialog(notes_upload .this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File.......");
        progressDialog.setProgress(0);
        progressDialog.show();

        StorageReference storageReference = storage.getReference();
        storageReference.child("NotesUpload").child(n.getSubject()).child(n.getFilename()).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                        DocumentReference df = fStore.collection("Notes").document(n.getSubject()).collection(n.getSubject()).document(n.getFilename());
                        Map<String,Object> notesInfo = new HashMap<>();
                        notesInfo.put("UserID",n.getUid());
                        notesInfo.put("Date/Time",n.getDateTime());
                        notesInfo.put("Visible",n.getCheckBit());
                        notesInfo.put("URL",url);
                        notesInfo.put("Teacher",n.getTeacher());
                        notesInfo.put("Rating",n.getRating());
                        df.set(notesInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                    Toast.makeText(notes_upload.this,"File Successfully Uploaded",Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(notes_upload.this,"Error!! File not uploaded",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(notes_upload.this,"Error!! File not uploaded",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                int currentProgress = (int) (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==9 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            selectPdf();
        else
            Toast.makeText(this, "Please Provide Permission", Toast.LENGTH_SHORT).show();
    }

    public void selectPdf(){
        Intent intent = new Intent();

        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf"};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==86 && resultCode == RESULT_OK && data!=null) {
            pdfUri = data.getData();
            notifyn.setText("The file Selected : "+data.getData().getLastPathSegment());
        }
        else
            Toast.makeText(this,"Please Select a File",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_upload);

        subj = findViewById(R.id.subnotes);
        upload = findViewById(R.id.uploadnotes);
        select = findViewById(R.id.selectnotes);
        notifyn = findViewById(R.id.notifynotes);
        teachern = findViewById(R.id.teacher);

        subj.setOnItemSelectedListener(this);

        fStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        ArrayAdapter<String> subAdapter = new ArrayAdapter<String>(notes_upload.this,
                R.layout.custom_spinner,getResources().getStringArray(R.array.paper));
        subAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        subj.setAdapter(subAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId()==R.id.subnotes){
            subject = parent.getItemAtPosition(position).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}