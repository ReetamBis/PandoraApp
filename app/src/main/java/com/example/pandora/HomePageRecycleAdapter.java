package com.example.pandora;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class HomePageRecycleAdapter extends RecyclerView.Adapter<HomePageRecycleAdapter.PlaceHolder>
{
    interface clicklistener
    {
        public void onclick(int pos);
    }
    Context context;
    ArrayList<String> subject=new ArrayList<>();
    clicklistener cl;
    public HomePageRecycleAdapter(Context context,ArrayList<String> sub,clicklistener cl)
    {
        this.context=context;
        subject=sub;
        this.cl=cl;
    }
    public class PlaceHolder extends RecyclerView.ViewHolder
    {
        TextView sub;
        CardView card;
        public PlaceHolder(View itemView)
        {
            super(itemView);
            card=itemView.findViewById(R.id.homecard);
            sub=itemView.findViewById(R.id.cardsubject);
        }
    }

    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.individualcard, parent, false);
        return new PlaceHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder holder, int position) {
        holder.sub.setText(subject.get(position));
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.card.setCardBackgroundColor(color);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cl.onclick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subject.size();
    }


}
