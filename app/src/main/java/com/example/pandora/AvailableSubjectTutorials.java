package com.example.pandora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AvailableSubjectTutorials extends AppCompatActivity {
    RecyclerView listItem;
    ArrayList<Item> subs=new ArrayList<>();
    String category,subject;
    FirebaseFirestore db;
    ListItemAdapter subsAdap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_subject_tutorials);
        Intent intent=getIntent();
        category=intent.getStringExtra("Category");
        subject=intent.getStringExtra("Subject");

        db=FirebaseFirestore.getInstance();
        subsAdap=new ListItemAdapter(this, subs, new ListItemAdapter.onitemclicklistener() {
            @Override
            public void onClick(Uri url, String name) {
                Intent openlink=new Intent(Intent.ACTION_VIEW,url);
                startActivity(openlink);
            }
        });
        listItem=findViewById(R.id.listItems);
        listItem.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        listItem.setAdapter(subsAdap);
        load();
    }
    public void load() {
        subs.clear();
        subsAdap.notifyDataSetChanged();

        db.collection("Tutorials").document(subject).collection(subject).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                    subs.add(new Item(d.getId(), Uri.parse(d.get("Link").toString())));
                    //notes.add(new Notes(correct(d.getId()), d.get(FieldPath.of("Date/Time")).toString(), Subject, d.get("Year").toString(), "END-SEM", Uri.parse(d.get("URL").toString())));
                    subsAdap.notifyDataSetChanged();
                }
            }
        });
    }
}