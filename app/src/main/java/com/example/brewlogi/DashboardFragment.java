package com.example.brewlogi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DashboardFragment extends Fragment {

    DatabaseReference roomTypeRef = FirebaseDatabase.getInstance("https://console.firebase.google.com/u/0/project/hacksingapore-14b13/database/hacksingapore-14b13-default-rtdb/data/~2F")
            .getReference("Stall A");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }
}