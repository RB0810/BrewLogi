package com.example.brewlogi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.brewlogi.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderConfirmation extends AppCompatActivity {

    private TextView orderQuantityTextView;
    private TextView locationTextView;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private String productName;
    private int numberValue;

    private Boolean database2Updated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_confirmation);

        orderQuantityTextView = findViewById(R.id.orderquantity);
        locationTextView = findViewById(R.id.location);

        Intent intent = getIntent();
        numberValue = intent.getIntExtra("numberValue", 0);
        productName = intent.getStringExtra("ProductName");
        orderQuantityTextView.setText(String.valueOf(numberValue) + " " + productName + " beer");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        // Create location request
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(OrderConfirmation.this,"Please grant location permission",Toast.LENGTH_LONG).show();
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Call a method to find the nearest stall using the received location
                    findNearestStall(location.getLatitude(), location.getLongitude());
                } else {
                    // Location is null, handle the error
                    Toast.makeText(OrderConfirmation.this, "Failed to get current location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Create location callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    // Call a method to find the nearest stall using the received location
                    findNearestStall(location.getLatitude(), location.getLongitude());
                }
            }
        };

        // Request location updates
        fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, null);
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // Interval in milliseconds for location updates
        locationRequest.setFastestInterval(5000); // Fastest interval in milliseconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Request high accuracy location

        return locationRequest;
    }


    private void stopLocationUpdates() {
        // Stop location updates
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void findNearestStall(double latitude, double longitude) {
        DatabaseReference database = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Location");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double minDistance = Double.MAX_VALUE;
                String nearestStall = "";

                for (DataSnapshot stallSnapshot : dataSnapshot.getChildren()) {
                    String stallName = stallSnapshot.getKey();
                    double stallLatitude = stallSnapshot.child("Latitude").getValue(Double.class);
                    double stallLongitude = stallSnapshot.child("Longitude").getValue(Double.class);

                    // Calculate the distance between the user and the stall using a distance formula (e.g., Haversine formula)
                    double distance = calculateDistance(latitude, longitude, stallLatitude, stallLongitude);

                    // Update the nearest stall if a closer stall is found
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestStall = stallName;
                    }
                }

                // Set the nearest stall name in the locationTextView
                locationTextView.setText(nearestStall);
                DatabaseReference database2 = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("Inventory").child(nearestStall).child(productName);

                // Check if the database update has already been performed
                if (!database2Updated) {
                    database2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            DataSnapshot dataSnapshot = snapshot.child("Cans distributed");
                            Integer cans = dataSnapshot.getValue(Integer.class);
                            cans = cans + numberValue;
                            database2.child("Cans distributed").setValue(cans);
                            DataSnapshot dataSnapshot1 = snapshot.child("Cans left");
                            Integer cans1 = dataSnapshot1.getValue(Integer.class);
                            cans1 = cans1 - numberValue;
                            database2.child("Cans left").setValue(cans1);

                            // Set the flag to indicate that the database update has been performed
                            database2Updated = true;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle the error
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });
    }


    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distance in kilometers
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Location permission granted, start location updates
            startLocationUpdates();
        } else {
            // Location permission denied, handle the error
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop location updates when the activity is destroyed
        stopLocationUpdates();
    }
}
