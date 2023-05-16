package com.example.brewlogi;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;

public class AlertFragment extends Fragment {
    private TextView alert;
    private View rootview;
    RecyclerView recyclerView;
    Adapter_Alert adapter_alert;
    Integer alertNumber = 100;
    ArrayList<String> alertText = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
       View rootview= inflater.inflate(R.layout.fragment_alert, container, false);
        recyclerView = rootview.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootview.getContext()));
        adapter_alert = new Adapter_Alert(rootview.getContext(), alertText);
        recyclerView.setAdapter(adapter_alert);

       /* OneSignal.setNotificationWillShowInForegroundHandler(new OneSignal.OSNotificationWillShowInForegroundHandler() {
            @Override
            public void notificationWillShowInForeground(OSNotificationReceivedEvent osNotificationReceivedEvent) {
                OSNotification notification = osNotificationReceivedEvent.getNotification();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alert.setText(notification.getBody());

                    }
                });

                osNotificationReceivedEvent.complete(notification);

            }
        }); */
        DatabaseReference database = FirebaseDatabase.getInstance("https://hacksingapore-14b13-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Inventory");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> updatedAlerts = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String stall = dataSnapshot.getKey();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String product = dataSnapshot1.getKey();

                        DataSnapshot dataSnapshot2 = dataSnapshot1.child("Cans left");
                        Integer cansLeft = dataSnapshot2.getValue(Integer.class);

                        if (cansLeft < alertNumber) {
                            String alert = stall + ", " + product + " is low on stock";
                            updatedAlerts.add(alert);
                        }
                    }
                }

                // Remove alerts that are no longer present in the database
                List<String> alertsToRemove = new ArrayList<>(alertText);
                alertsToRemove.removeAll(updatedAlerts);
                alertText.removeAll(alertsToRemove);

                // Add new alerts that are not already in the list
                for (String alert : updatedAlerts) {
                    if (!alertText.contains(alert)) {
                        alertText.add(alert);
                    }
                }

                // Update the adapter
                adapter_alert.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return rootview;
    }

    }





