package com.example.brewlogi;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChartFragment extends Fragment {


    // Move the chart setup and calculation logic to a separate method
    private void setupPieChart(List<String> stallNames, List<Integer> cansDistributed) {
        int totalCansDistributed = 0;
        for (Integer can : cansDistributed) {
            totalCansDistributed += can;
        }


        // Create a PieChart object
        PieChart pieChart = getView().findViewById(R.id.pie_chart);

        // Create pie entries
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < stallNames.size(); i++) {
            pieEntries.add(new PieEntry(cansDistributed.get(i), stallNames.get(i)));
        }

        // Create a PieDataSet
        PieDataSet dataSet = new PieDataSet(pieEntries, "Stall Distribution");


        // Set dataset properties
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.WHITE);

        // Create a PieData object and set the dataset
        PieData data = new PieData(dataSet);

        // Set additional chart properties
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleRadius(50f); // Set the hole radius to 0 to remove the center hole

        // Set the data to the pie chart
        pieChart.setData(data);
        pieChart.invalidate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);
        TextView total = rootView.findViewById(R.id.total);


        DatabaseReference database = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Inventory");

        DatabaseReference databaset = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Inventory").child("Total Stock");

        databaset.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cansDisttt = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataSnapshot dataSnapshot1 = dataSnapshot.child("Cans distributed");
                    Integer cansDist = dataSnapshot1.getValue(Integer.class);
                    if (cansDist != null) {
                        cansDisttt += cansDist.intValue();
                    }
                }
                total.setText("Total Cans Distributed: " + String.valueOf(cansDisttt)); // Set the text of the TextView with the value of cansDisttt

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        List<String> stallNames = new ArrayList<>();
        List<Integer> cansDistributed = new ArrayList<>();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String stall = dataSnapshot.getKey();
                    if (!stall.equals("Total Stock")) {
                        stallNames.add(stall);
                        Integer cansDistt = 0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            DataSnapshot dataSnapshot2 = dataSnapshot1.child("Cans distributed");
                            Integer cansDist = dataSnapshot2.getValue(Integer.class);
                            cansDistt += cansDist;
                        }
                        cansDistributed.add(cansDistt);
                    }
                }

                // Call the chart setup method here
                setupPieChart(stallNames, cansDistributed);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the onCancelled event if needed
            }
        });

        return rootView;
    }

}