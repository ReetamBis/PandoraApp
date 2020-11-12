package com.example.pandora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class AvailableSubjectNotes extends AppCompatActivity {

    TextView subname;
    Spinner teacherwise;
    Spinner ratingswise;
    RecyclerView listitem;
    String Category,Subject;
    ArrayList<Notes> notes=new ArrayList<>();
    ArrayList<Item> actnotes=new ArrayList<>();
    ArrayList<String> teacher=new ArrayList<>();
    ArrayList<String> ratings=new ArrayList<>();
    ListItemAdapter notesAdap;
    ArrayAdapter<String> teacherwiseAdap,ratingwiseAdap;
    FirebaseFirestore db;
    String gname;
    Uri gurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_subject_notes);

        Intent intent=getIntent();
        Category=intent.getStringExtra("Category");
        Subject=intent.getStringExtra("Subject");
        ratings.add("Ratings:All");
        ratings.add("<=2");
        ratings.add("3");
        ratings.add(">=4");

        notesAdap=new ListItemAdapter(this, actnotes, new ListItemAdapter.onitemclicklistener() {
            @Override
            public void onClick(Uri url, String name) {
                if(ContextCompat.checkSelfPermission(AvailableSubjectNotes.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    gname=name;
                    gurl=url;
                    download();
                }
                else {
                    gname=name;
                    gurl=url;
                    ActivityCompat.requestPermissions(AvailableSubjectNotes.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 9);
                }
            }
        });
        listitem=findViewById(R.id.listItems);
        listitem.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        listitem.setAdapter(notesAdap);

        db=FirebaseFirestore.getInstance();

        subname=findViewById(R.id.subname);
        subname.setText(Category+" :: "+Subject);

        teacherwise=findViewById(R.id.teacherwise);
        teacherwiseAdap = new ArrayAdapter<String>(this,
                R.layout.custom_spinner,teacher);
        teacherwiseAdap.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        teacherwise.setAdapter(teacherwiseAdap);

        ratingswise=findViewById(R.id.ratingwise);
        ratingwiseAdap = new ArrayAdapter<String>(this,R.layout.custom_spinner,ratings);
        ratingwiseAdap.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        ratingswise.setAdapter(ratingwiseAdap);

        load();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==9 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            download();
        else
            Toast.makeText(this, "Please Provide Permission", Toast.LENGTH_SHORT).show();
    }
    public void download()
    {
        Log.i("My Log:","Clicked");
        Toast.makeText(getBaseContext(),"Starting Download",Toast.LENGTH_SHORT).show();
        DownloadManager downloadManager=(DownloadManager)AvailableSubjectNotes.this.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request req=new DownloadManager.Request(gurl);
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"Pandora/Notes/"+Subject+"/"+gname);
        downloadManager.enqueue(req);
    }
    public void searchitem(View view)
    {
        int s=teacherwise.getSelectedItemPosition();
        if(s>0)
        {
            loadTeacher(teacherwise.getSelectedItem().toString());
        }
    }
    public void load() {
        notes.clear();
        notesAdap.notifyDataSetChanged();
        teacher.clear();
        teacher.add("All");
        db.collection("Notes").document(Subject).collection(Subject).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                    actnotes.add(new Item(correct(d.getId()),Uri.parse(d.get("URL").toString())));
                    //notes.add(new Notes(correct(d.getId()), d.get(FieldPath.of("Date/Time")).toString(), Subject, d.get("Year").toString(), "END-SEM", Uri.parse(d.get("URL").toString())));
                    notesAdap.notifyDataSetChanged();
                    teacher.add(d.get("Teacher").toString());
                    teacherwiseAdap.notifyDataSetChanged();

                }
            }
        });
    }
    public void loadTeacher(String s)
    {
        actnotes.clear();
        notes.clear();
        notesAdap.notifyDataSetChanged();
        teacher.clear();
        teacher.add("All");
        db.collection("Notes").document(Subject).collection(Subject).whereEqualTo("Teacher",s).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                    actnotes.add(new Item(correct(d.getId()),Uri.parse(d.get("URL").toString())));
                    //notes.add(new Notes(correct(d.getId()), d.get(FieldPath.of("Date/Time")).toString(), Subject, d.get("Year").toString(), "END-SEM", Uri.parse(d.get("URL").toString())));
                    notesAdap.notifyDataSetChanged();
                    teacher.add(d.get("Teacher").toString());
                    teacherwiseAdap.notifyDataSetChanged();

                }
            }
        });
    }
    public String correct(String s)
    {
        StringTokenizer st =new StringTokenizer(s);
        String s1=st.nextToken();
        return s;
    }
}