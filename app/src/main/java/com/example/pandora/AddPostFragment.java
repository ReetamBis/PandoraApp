package com.example.pandora;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class AddPostFragment extends Fragment {

    Button prev,notes,tut;
    public AddPostFragment() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        prev = (Button) view.findViewById(R.id.prev);
        notes = (Button) view.findViewById(R.id.notes);
        tut = (Button) view.findViewById(R.id.tut);

        prev.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(getActivity(),com.example.pandora.question_paper_upload.class);
                startActivity(intent);
            }
        });

        notes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(getActivity(),com.example.pandora.notes_upload.class);
                startActivity(intent);
            }
        });

        tut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(getActivity(),com.example.pandora.Tutorial.class);
                startActivity(intent);
            }
        });

        return view;

    }
}