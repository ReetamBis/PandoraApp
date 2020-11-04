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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class AvailableSubjectResource extends AppCompatActivity {
    TextView subname;
    Spinner typewise;
    Spinner yearwise;
    RecyclerView listitem;
    String Category,Subject;
    ArrayList<PrevPaper> prevPaper=new ArrayList<>();
    ArrayList<Item> actprevPaper=new ArrayList<>();
    ArrayList<String> type=new ArrayList<>();
    ArrayList<String> years=new ArrayList<>();
    ListItemAdapter prepaperAdap;
    ArrayAdapter<String> yearwiseAdap;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_subject_resource);
        Intent intent=getIntent();
        Category=intent.getStringExtra("Category");
        Subject=intent.getStringExtra("Subject");

        type.add("ALL");
        type.add("END_SEM");
        type.add("MID-SEM");

        prepaperAdap=new ListItemAdapter(this, actprevPaper, new ListItemAdapter.onitemclicklistener() {
            @Override
            public void onClick(Uri url,String name) {
                Log.i("My Log:","Clicked");
                Toast.makeText(getBaseContext(),"Starting Download",Toast.LENGTH_SHORT).show();
                DownloadManager downloadManager=(DownloadManager)AvailableSubjectResource.this.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request req=new DownloadManager.Request(url);
                req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                req.setDestinationInExternalFilesDir(AvailableSubjectResource.this,"Pandora/Previous Year Paper/"+Subject,name);
                downloadManager.enqueue(req);
            }
        });
        db=FirebaseFirestore.getInstance();

        subname=findViewById(R.id.subname);
        subname.setText(Category+" :: "+Subject);

        typewise=findViewById(R.id.typewise);
        ArrayAdapter<String> typewiseAdap = new ArrayAdapter<String>(this,
                R.layout.custom_spinner,type);
        typewiseAdap.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        typewise.setAdapter(typewiseAdap);

        yearwise=findViewById(R.id.yearwise);
         yearwiseAdap = new ArrayAdapter<String>(this,R.layout.custom_spinner,years);
        yearwiseAdap.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        yearwise.setAdapter(yearwiseAdap);

        listitem=findViewById(R.id.listItems);
        listitem.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        listitem.setAdapter(prepaperAdap);


        load();
    }
    public void searchitem(View view)
    {
            int s=typewise.getSelectedItemPosition();
            int s1=yearwise.getSelectedItemPosition();
            if(s==0)
            {
                if(s1==0)
                    load();
                else
                    load(yearwise.getSelectedItem().toString());
            }

            if(s==1)
            {
                if(s1==0)
                    loadEnd();
                else
                    loadEnd(yearwise.getSelectedItem().toString());
            }

            if(s==2)
            {
                if(s1==0)
                    loadMid();
                else
                    loadMid(yearwise.getSelectedItem().toString());
            }


    }
    public void load()
    {
        Log.i("My LOG:","PrevPaper"+" "+Subject+" END-SEM");
        prevPaper.clear();
        actprevPaper.clear();
        prepaperAdap.notifyDataSetChanged();
        years.clear();
        years.add("All");
        db.collection("PrevPaper").document(Subject).collection("END-SEM").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                        actprevPaper.add(new Item(correct(d.getId()),Uri.parse( d.get("URL").toString())));
                        prevPaper.add(new PrevPaper(correct(d.getId()),d.get(FieldPath.of("Date/Time")).toString(),Subject,d.get("Year").toString(),"END-SEM", Uri.parse( d.get("URL").toString())));
                        prepaperAdap.notifyDataSetChanged();
                        years.add(d.get("Year").toString());
                        yearwiseAdap.notifyDataSetChanged();

                }
            }
        });
        db.collection("PrevPaper").document(Subject).collection("MID-SEM").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                        actprevPaper.add(new Item(correct(d.getId()),Uri.parse( d.get("URL").toString())));
                        prevPaper.add(new PrevPaper(correct(d.getId()),d.get(FieldPath.of("Date/Time")).toString(),Subject,d.get("Year").toString(),"MID-SEM",  Uri.parse( d.get("URL").toString())));
                        prepaperAdap.notifyDataSetChanged();
                        if(!years.contains(d.get("Year")))
                        {
                            years.add(d.get("Year").toString());
                            yearwiseAdap.notifyDataSetChanged();
                        }
                    }
                }
        });
    }

    public void load(String yr)
    {

        Log.i("My LOG:",yr);
        actprevPaper.clear();
        prevPaper.clear();
        prepaperAdap.notifyDataSetChanged();
        db.collection("PrevPaper").document(Subject).collection("END-SEM").whereEqualTo("Year",yr).get(Source.CACHE).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                    actprevPaper.add(new Item(correct(d.getId()),Uri.parse( d.get("URL").toString())));
                    prevPaper.add(new PrevPaper(correct(d.getId()),d.get(FieldPath.of("Date/Time")).toString(),Subject,d.get("Year").toString(),"END-SEM", Uri.parse( d.get("URL").toString())));
                    prepaperAdap.notifyDataSetChanged();

                }
            }
        });
        db.collection("PrevPaper").document(Subject).collection("MID-SEM").whereEqualTo("Year",yr).get(Source.CACHE).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                    actprevPaper.add(new Item(correct(d.getId()),Uri.parse( d.get("URL").toString())));
                    prevPaper.add(new PrevPaper(correct(d.getId()),d.get(FieldPath.of("Date/Time")).toString(),Subject,d.get("Year").toString(),"MID-SEM",  Uri.parse( d.get("URL").toString())));
                    prepaperAdap.notifyDataSetChanged();
                }
            }
        });
    }

    public void loadEnd()
    {
        actprevPaper.clear();
        prevPaper.clear();
        prepaperAdap.notifyDataSetChanged();
        db.collection("PrevPaper").document(Subject).collection("END-SEM").get(Source.CACHE).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                    actprevPaper.add(new Item(correct(d.getId()),Uri.parse( d.get("URL").toString())));
                    prevPaper.add(new PrevPaper(correct(d.getId()),d.get(FieldPath.of("Date/Time")).toString(),Subject,d.get("Year").toString(),"END-SEM", Uri.parse( d.get("URL").toString())));
                    prepaperAdap.notifyDataSetChanged();

                }
            }
        });
    }

    public void loadEnd(String yr)
    {
        actprevPaper.clear();
        prevPaper.clear();
        prepaperAdap.notifyDataSetChanged();
        db.collection("PrevPaper").document(Subject).collection("END-SEM").whereEqualTo("Year",yr).get(Source.CACHE).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                    actprevPaper.add(new Item(correct(d.getId()),Uri.parse( d.get("URL").toString())));
                    prevPaper.add(new PrevPaper(correct(d.getId()),d.get(FieldPath.of("Date/Time")).toString(),Subject,d.get("Year").toString(),"END-SEM", Uri.parse( d.get("URL").toString())));
                    prepaperAdap.notifyDataSetChanged();

                }
            }
        });
    }
    public void loadMid()
    {
        actprevPaper.clear();
        prevPaper.clear();
        prepaperAdap.notifyDataSetChanged();
        db.collection("PrevPaper").document(Subject).collection("MID-SEM").get(Source.CACHE).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                    actprevPaper.add(new Item(correct(d.getId()),Uri.parse( d.get("URL").toString())));
                    prevPaper.add(new PrevPaper(correct(d.getId()),d.get(FieldPath.of("Date/Time")).toString(),Subject,d.get("Year").toString(),"MID-SEM",  Uri.parse( d.get("URL").toString())));
                    prepaperAdap.notifyDataSetChanged();
                }
            }
        });
    }

    public void loadMid(String yr)
    {
        actprevPaper.clear();
        prevPaper.clear();
        prepaperAdap.notifyDataSetChanged();
        db.collection("PrevPaper").document(Subject).collection("MID-SEM").whereEqualTo("Year",yr).get(Source.CACHE).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                    actprevPaper.add(new Item(correct(d.getId()),Uri.parse( d.get("URL").toString())));
                    prevPaper.add(new PrevPaper(correct(d.getId()),d.get(FieldPath.of("Date/Time")).toString(),Subject,d.get("Year").toString(),"MID-SEM",  Uri.parse( d.get("URL").toString())));
                    prepaperAdap.notifyDataSetChanged();
                }
            }
        });
    }

    public String correct(String s)
    {
        StringTokenizer st =new StringTokenizer(s);
        String s1=st.nextToken()+" "+st.nextToken()+" "+st.nextToken();
        return s1;
    }
}