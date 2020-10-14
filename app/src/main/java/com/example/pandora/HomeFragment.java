package com.example.pandora;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    HomePageRecycleAdapter prepaperAdap,notesAdap,resourcesAdap,researchAdap;
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
        //prepaper.setLayoutManager(new GridLayoutManager(this.getActivity(),6));
        prepaper.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false));
        notes.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false));
        research.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false));
        resources.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false));
        prepaperAdap=new HomePageRecycleAdapter(this.getActivity(),subject);
        prepaper.setAdapter(prepaperAdap);
        notesAdap=new HomePageRecycleAdapter(this.getActivity(),subject);
        researchAdap=new HomePageRecycleAdapter(this.getActivity(),subject);
        resourcesAdap=new HomePageRecycleAdapter(this.getActivity(),subject);
        notes.setAdapter(prepaperAdap);
        resources.setAdapter(prepaperAdap);
        research.setAdapter(prepaperAdap);

        createsubList();
        return view;
    }
    public  void createsubList()
    {
        subject.add("DSA");
        subject.add("DAA");
        subject.add("DBMS");
        subject.add("CN");
        prepaperAdap.notifyDataSetChanged();
        notesAdap.notifyDataSetChanged();
        resourcesAdap.notifyDataSetChanged();
        researchAdap.notifyDataSetChanged();
    }
}