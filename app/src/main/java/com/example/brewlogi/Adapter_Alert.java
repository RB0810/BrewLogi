package com.example.brewlogi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Alert extends RecyclerView.Adapter<Adapter_Alert.MyViewHolder>{

    Context context;
    ArrayList<String> list;

    public Adapter_Alert(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_Alert.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_alert,parent,false);
        return new Adapter_Alert.MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull Adapter_Alert.MyViewHolder holder, int position) {
        String alert = list.get(position);
        holder.alert.setText(alert.toUpperCase());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView alert;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            alert = itemView.findViewById(R.id.alert);
        }
    }
}


