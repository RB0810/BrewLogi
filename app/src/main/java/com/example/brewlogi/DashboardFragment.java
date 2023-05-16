package com.example.brewlogi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    DatabaseReference database;
    RecyclerView recyclerView;
    Adapter_Dashboard adapterDashboard;
    ArrayList<String> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        database = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Inventory");
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        list = new ArrayList<>();
        adapterDashboard = new Adapter_Dashboard(rootView.getContext(), list);
        recyclerView.setAdapter(adapterDashboard);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String stall = dataSnapshot.getKey();
                    list.add(stall);
                }
                adapterDashboard.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return rootView;
    }
}