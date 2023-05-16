package com.example.brewlogi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class Adapter_Dashboard extends RecyclerView.Adapter<Adapter_Dashboard.MyViewHolder>{

    Context context;
    ArrayList<String> list;

    public Adapter_Dashboard(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_dashboard,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String stall = list.get(position);
        holder.StallName.setText(stall.toUpperCase());

        // Inside your RecyclerView adapter's onBindViewHolder() method
        holder.ParentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedStallName = list.get(position);
                Intent intent = new Intent(context, StallPage.class);
                intent.putExtra("stallName", selectedStallName);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView StallName;
        CardView ParentCard;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            StallName = itemView.findViewById(R.id.stallname);
            ParentCard = itemView.findViewById(R.id.parentCard);
        }
    }
}
