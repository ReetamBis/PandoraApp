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
                Intent intent=new Intent(getBaseContext(),com.example.pandora.AvailableSubjectResource.class);
                intent.putExtra("Category",category);
                intent.putExtra("Subject",subjects.get(pos));
                startActivity(intent);
            }
        });
        allsub.setAdapter(subjectAdapter);
        fillsubjects();
        subsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                Log.i("MyLog:",  s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i("MyLog after:",  editable.toString());
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