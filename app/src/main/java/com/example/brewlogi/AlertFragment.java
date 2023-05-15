package com.example.brewlogi;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;

public class AlertFragment extends Fragment {
    private TextView alert;
    private View rootview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
       View rootview= inflater.inflate(R.layout.fragment_alert, container, false);
       alert=rootview.findViewById(R.id.alert);
        OneSignal.setNotificationWillShowInForegroundHandler(new OneSignal.OSNotificationWillShowInForegroundHandler() {
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
        });
        return rootview;
    }

    }





