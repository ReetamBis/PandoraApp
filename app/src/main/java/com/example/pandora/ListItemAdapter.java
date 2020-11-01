package com.example.pandora;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ListItemAdapter  extends RecyclerView.Adapter<ListItemAdapter.PlaceHolder>
{
    Context context;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
