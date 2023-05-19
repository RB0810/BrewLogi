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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        DatabaseReference totalStockRef = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Inventory").child("Total Stock");


        totalStockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> entryNames = new ArrayList<>();
                List<Integer> cansDistributed = new ArrayList<>();
                List<Integer> cansLeft = new ArrayList<>();

                for (DataSnapshot entrySnapshot : snapshot.getChildren()) {
                    String entryName = entrySnapshot.getKey();
                    int cansDist = entrySnapshot.child("Cans distributed").getValue(Integer.class);
                    int cansL = entrySnapshot.child("Cans left").getValue(Integer.class);

                    entryNames.add(entryName);
                    cansDistributed.add(cansDist);
                    cansLeft.add(cansL);
                }

                // Call the bar chart setup method here
                setupBarChart(entryNames, cansDistributed, cansLeft);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the onCancelled event if needed
            }
        });

        DatabaseReference database = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Inventory");

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

    private void setupBarChart(List<String> entryNames, List<Integer> cansDistributed, List<Integer> cansLeft) {
        BarChart barChart = getView().findViewById(R.id.bar_chart);

        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < entryNames.size(); i++) {
            entries.add(new BarEntry(i, new float[]{cansDistributed.get(i), cansLeft.get(i)}));
        }

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(new int[]{Color.parseColor("#4cd038"), Color.parseColor("#ffbf00")}); // Set different colors for bars
        dataSet.setStackLabels(new String[]{"Cans Distributed", "Cans Left"});

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.4f);

        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(true);

        // Set the product names as X-axis labels
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(entryNames));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(entryNames.size());
        xAxis.setAxisMinimum(-0.5f); // Adjust the X-axis offset

        // Disable grid lines for both X-axis and Y-axis
        xAxis.setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(true);
        barChart.getAxisRight().setDrawGridLines(true);

        // Adjust the X-axis offset to reduce the gap between zero and the label
        barChart.setExtraOffsets(0f, 0f, 0f, 5f); // Adjust the last parameter as needed

        barChart.invalidate();
    }




    // Move the chart setup and calculation logic to a separate method
    private void setupPieChart(List<String> stallNames, List<Integer> cansDistributed) {
        int totalCansDistributed = 0;
        for (Integer can : cansDistributed) {
            totalCansDistributed += can;
        }
        TextView total = getView().findViewById(R.id.total);
        total.setText("Total Cans Sold: " + String.valueOf(totalCansDistributed));



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



}