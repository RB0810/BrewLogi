package com.example.brewlogi;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DistributionFragment extends Fragment {
    private View rootview;
    RecyclerView recyclerView;
    Adapter_Distribution adapter_dist;
    ArrayList<String> dist = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_distribution, container, false);
        recyclerView = rootview.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootview.getContext()));
        adapter_dist = new Adapter_Distribution(rootview.getContext(), dist);
        recyclerView.setAdapter(adapter_dist);
        return rootview;

    }
}