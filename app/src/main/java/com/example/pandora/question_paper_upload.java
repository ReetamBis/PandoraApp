package com.example.pandora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class question_paper_upload extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner sub;
    Spinner year;
    Spinner type;
    PrevPaper p;
    String subject,sem,yy,uid;
    Button upload,select;
    FirebaseUser currentFirebaseUser;
    Uri pdfUri;
    TextView notify;
    FirebaseFirestore fStore;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    public void onUploadpaper(View view){

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

            if(pdfUri!=null)
                uploadFile(pdfUri,p);
            else
               Toast.makeText(this,"Select a file ..",Toast.LENGTH_SHORT).show();
        }
    }


    public void uploadFile(Uri pdfUri, PrevPaper p){

        progressDialog = new ProgressDialog(question_paper_upload.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File.......");
        progressDialog.setProgress(0);
        progressDialog.show();

        StorageReference storageReference = storage.getReference();
        storageReference.child("PaperUpload").child(p.getSubject()).child(p.getType()).child(p.getName()).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Map<String,Object> f=new HashMap<>();
                                f.put("F",1);
                                fStore.collection("PrevPaper").document(p.getSubject()).set(f);
                                DocumentReference df = fStore.collection("PrevPaper").document(p.getSubject()).collection(p.getType()).document(p.getName());
                                Map<String,Object> paperInfo = new HashMap<>();
                                paperInfo.put("UserID",p.getUid());
                                paperInfo.put("Date/Time",p.getDateTime());
                                paperInfo.put("Year",p.getYear());
                                paperInfo.put("Visible",p.getCheckBit());
                                paperInfo.put("URL",uri.toString());
                                df.set(paperInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful())
                                            Toast.makeText(question_paper_upload.this,"File Successfully Uploaded",Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(question_paper_upload.this,"Error!! File not uploaded"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(question_paper_upload.this,"Error!! File not uploaded",Toast.LENGTH_SHORT).show();
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

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==86 && resultCode == RESULT_OK && data!=null) {
            pdfUri = data.getData();
            notify.setText("The file Selected : "+getFileName(pdfUri));
        }
        else
            Toast.makeText(this,"Please Select a File",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_paper_upload);

        sub = findViewById(R.id.sub);
        year = findViewById(R.id.year);
        type = findViewById(R.id.type);
        upload = findViewById(R.id.upload);
        select = findViewById(R.id.select);
        notify = findViewById(R.id.notify);

        sub.setOnItemSelectedListener(this);
        year.setOnItemSelectedListener(this);
        type.setOnItemSelectedListener(this);

        fStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

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