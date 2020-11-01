package com.example.pandora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class AvailableSubjectResource extends AppCompatActivity {
    TextView subname;
    Spinner typewise;
    Spinner yearwise;
    RecyclerView listitem;
    String Category,Subject;
    ArrayList<PrevPaper> prevPaper=new ArrayList<>();
    ListItemAdapter prepaperAdap;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_subject_resource);
        Intent intent=getIntent();
        Category=intent.getStringExtra("Category");
        Subject=intent.getStringExtra("Subject");

        prepaperAdap=new ListItemAdapter(this, prevPaper, new ListItemAdapter.onitemclicklistener() {
            @Override
            public void onClick(Uri url,String name) {
                Toast.makeText(getBaseContext(),"Starting Download",Toast.LENGTH_SHORT).show();
                DownloadManager downloadManager=(DownloadManager)AvailableSubjectResource.this.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request req=new DownloadManager.Request(url);
                req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                req.setDestinationInExternalFilesDir(AvailableSubjectResource.this,DIRECTORY_DOWNLOADS,name+".pdf");
                downloadManager.enqueue(req);
            }
        });
        db=FirebaseFirestore.getInstance();
        subname=findViewById(R.id.subname);
        typewise=findViewById(R.id.typewise);
        yearwise=findViewById(R.id.yearwise);
        listitem=findViewById(R.id.listItems);
        listitem.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        listitem.setAdapter(prepaperAdap);
        subname.setText(Category+" :: "+Subject);
        load();
    }
    public void searchitem(View view)
    {

    }
    public void load()
    {
        Log.i("My LOG:","PrevPaper"+" "+Subject+" END-SEM");
        db.collection("PrevPaper").document(Subject).collection("END-SEM").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot d : queryDocumentSnapshots) {

                        prevPaper.add(new PrevPaper(d.getId(),d.get(FieldPath.of("Date/Time")).toString(),Subject,d.get("Year").toString(),"END-SEM", Uri.parse( d.get("URL").toString())));
                        prepaperAdap.notifyDataSetChanged();

                }
            }
        });
        db.collection("PrevPaper").document(Subject).collection("MID-SEM").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot d : queryDocumentSnapshots) {

                        prevPaper.add(new PrevPaper(d.getId(),d.get(FieldPath.of("Date/Time")).toString(),Subject,d.get("Year").toString(),"MID-SEM",  Uri.parse( d.get("URL").toString())));
                        prepaperAdap.notifyDataSetChanged();
                    }
                }
        });
    }
}