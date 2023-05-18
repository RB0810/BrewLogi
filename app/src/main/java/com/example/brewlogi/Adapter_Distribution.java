package com.example.brewlogi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Distribution extends RecyclerView.Adapter<Adapter_Distribution.MyViewHolder>{

    Context context;
    ArrayList<String> list;

    public Adapter_Distribution(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_Distribution.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_alert,parent,false);
        return new Adapter_Distribution.MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull Adapter_Distribution.MyViewHolder holder, int position) {
        String stallname = list.get(position);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        Button restock;

        TextView restockno;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            restock = itemView.findViewById(R.id.restock);
            restockno = itemView.findViewById(R.id.restocknumber);
        }
    }
}



