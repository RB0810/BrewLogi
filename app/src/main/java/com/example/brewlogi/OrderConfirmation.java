package com.example.brewlogi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderConfirmation extends AppCompatActivity {

    private TextView orderQuantityTextView;
    private TextView locationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_confirmation);

        orderQuantityTextView = findViewById(R.id.orderquantity);
        locationTextView = findViewById(R.id.location);

        Intent intent = getIntent();
        int numberValue = intent.getIntExtra("numberValue", 0);
        String productName = intent.getStringExtra("ProductName");
        orderQuantityTextView.setText(String.valueOf(numberValue) + " " + productName + " beer");

        getLocation();
    }

    public void getLocation() {
        // Create a LocationManager instance
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Check if the location providers are enabled
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        // Initialize the location listener
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when the user's location changes
                LocationChecker locationChecker = new LocationChecker();
                locationChecker.findNearestStall(location, new LocationChecker.OnNearestStallListener() {
                    @Override
                    public void onNearestStallFound(String nearestStall) {
                        // Display the nearest stall name in a Toast message
                        Toast.makeText(OrderConfirmation.this, "Nearest Stall: " + nearestStall, Toast.LENGTH_LONG).show();
                        // Set the nearest stall name on the location TextView
                        locationTextView.setText("Nearest Stall: " + nearestStall);
                    }

                    @Override
                    public void onDatabaseError(String errorMessage) {
                        // Handle the database error, if any
                    }
                });
                locationManager.removeUpdates(this); // Stop listening for location updates
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Request location updates
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } else if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            } else {
                // Neither GPS nor network location providers are enabled
                // Handle the case appropriately, e.g., show an error message
            }
        } else {
            // Location permission not granted
            // Handle the case appropriately, e.g., show an error message or request permission again
        }
    }
}

class LocationChecker {
    private DatabaseReference databaseReference;
    private String nearestStall = "";

    public LocationChecker() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Location");
    }

    public String getNearestStall() {
        return nearestStall;
    }

    public void findNearestStall(Location userLocation, final OnNearestStallListener listener) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double nearestDistance = Double.MAX_VALUE;

                for (DataSnapshot stallSnapshot : dataSnapshot.getChildren()) {
                    String stallName = stallSnapshot.getKey();
                    double stallLatitude = stallSnapshot.child("latitude").getValue(Double.class);
                    double stallLongitude = stallSnapshot.child("longitude").getValue(Double.class);

                    double distance = Math.sqrt(Math.pow((userLocation.getLatitude() - stallLatitude), 2)
                            + Math.pow((userLocation.getLongitude() - stallLongitude), 2));

                    if (distance < nearestDistance) {
                        nearestDistance = distance;
                        nearestStall = stallName;
                    }
                }

                // Pass the nearest stall to the listener
                listener.onNearestStallFound(nearestStall);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
                listener.onDatabaseError(databaseError.getMessage());
            }
        });
    }

    public interface OnNearestStallListener {
        void onNearestStallFound(String nearestStall);
        void onDatabaseError(String errorMessage);
    }
}

