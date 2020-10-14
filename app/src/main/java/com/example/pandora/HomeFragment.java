package com.example.pandora;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    RecyclerView prepaper;
    RecyclerView notes;
    RecyclerView resources;
    RecyclerView research;
    ArrayList<String> subject=new ArrayList<>();
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        prepaper=view.findViewById(R.id.prepaper);
        notes=view.findViewById(R.id.notes);
        resources=view.findViewById(R.id.resources);
        research=view.findViewById(R.id.research);


        return view;
    }
}