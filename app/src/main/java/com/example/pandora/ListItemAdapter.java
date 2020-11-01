package com.example.pandora;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Placeholder;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;
import java.util.ArrayList;

public class ListItemAdapter  extends RecyclerView.Adapter<ListItemAdapter.PlaceHolder>
{
    Context context;
    ArrayList<PrevPaper> prevPapers;
    public interface onitemclicklistener
    {
        public void onClick(Uri url,String name);
    }
    onitemclicklistener onitemclicklistener;

    public ListItemAdapter(Context context, ArrayList<PrevPaper> prevPapers, ListItemAdapter.onitemclicklistener onitemclicklistener) {
        this.context = context;
        this.prevPapers = prevPapers;
        this.onitemclicklistener = onitemclicklistener;
    }

    public class PlaceHolder extends RecyclerView.ViewHolder
    {
        TextView sub;
        ImageView down;
        public PlaceHolder(View itemView)
        {
            super(itemView);
            down=itemView.findViewById(R.id.download);
            sub=itemView.findViewById(R.id.prepapername);
        }
    }
    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new PlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder holder, int position) {
            holder.sub.setText(prevPapers.get(position).getFilename());
            holder.sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onitemclicklistener.onClick(prevPapers.get(position).getUrl(),prevPapers.get(position).getFilename());
                }
            });
    }

    @Override
    public int getItemCount() {
        return prevPapers.size();
    }


}
