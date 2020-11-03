package com.example.pandora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;

public class AllOptions extends AppCompatActivity {

    RecyclerView allsub;
    Spinner subwise,yeawise,typewise;
    String category;
    FirebaseFirestore db ;
    HomePageRecycleAdapter subjectAdapter;
    ArrayList<String> subjects=new ArrayList<>();
    EditText subsearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_options);
        Intent intent =getIntent();
        category=intent.getStringExtra("Category");
        db=FirebaseFirestore.getInstance();
        allsub=findViewById(R.id.allsub);
        allsub.setLayoutManager(new GridLayoutManager(this,3));
        subsearch=findViewById(R.id.subjectsearch);
        subjectAdapter=new HomePageRecycleAdapter(this,subjects,new HomePageRecycleAdapter.clicklistener() {
            @Override
            public void onclick(int pos) {
                Intent intent;
                if(category.equals("PrevPaper")){
                 intent=new Intent(getBaseContext(),com.example.pandora.AvailableSubjectResource.class);
                }
                else if(category.equals("Notes"))
                {
                    intent=new Intent(getBaseContext(),com.example.pandora.AvailableSubjectNotes.class);
                }
                else
                {
                    intent=new Intent(getBaseContext(),com.example.pandora.AvailableSubjectResource.class);
                }
                intent.putExtra("Category",category);
                intent.putExtra("Subject",subjects.get(pos));
                startActivity(intent);
            }
        });

    }

    public void search(View view)
    {
        if(subsearch.getText()!=null)
        {
            String s=subsearch.getText().toString().trim();
        }
    }

    public void fillsubjects()
    {
        Log.i("MyLog:","Quering");
        db.collection(category).get(Source.CACHE).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.i("MyLog:","Query Length:"+queryDocumentSnapshots.size());
                for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                    subjects.add(d.getId());
                }
                subjectAdapter.notifyDataSetChanged();
            }
        });
    }
}