package com.example.brewlogi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    DatabaseReference database;
    RecyclerView recyclerView;
    Adapter_Dashboard adapterDashboard;
    ArrayList<String> list;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_LOCATION_PERMISSION = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        database = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Inventory");
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        list = new ArrayList<>();
        adapterDashboard = new Adapter_Dashboard(rootView.getContext(), list);
        recyclerView.setAdapter(adapterDashboard);

        // Add button click listener
        ImageButton addButton = rootView.findViewById(R.id.add_button_stall);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and show the dialog box
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_add_stall, null);
                builder.setView(dialogView);

                EditText input = dialogView.findViewById(R.id.edit_stall_name);

                // Set up the buttons
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String stallName = input.getText().toString();

                        if (!stallName.isEmpty()) {
                            requestLocationUpdates(stallName);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Show the dialog box
                builder.show();
            }
        });



        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String stall = dataSnapshot.getKey();
                    if (!list.contains(stall)) {
                        list.add(stall);
                    }
                }
                adapterDashboard.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return rootView;
    }

    private void requestLocationUpdates(final String stallName) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            Location location = locationResult.getLastLocation();
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                addStall(stallName, latitude, longitude);
                            }
                        }
                    },
                    null);
        } else {
            Toast.makeText(requireContext(), "Please grant location permission so that we can get the location of your stall", Toast.LENGTH_SHORT).show();
        }
    }

    private void addStall(String stallName, double latitude, double longitude) {
        DatabaseReference newStallRef = database.child(stallName);
        newStallRef.setValue(true);

        DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("Location").child(stallName);
        locationRef.child("Latitude").setValue(latitude);
        locationRef.child("Longitude").setValue(longitude);
    }
}
