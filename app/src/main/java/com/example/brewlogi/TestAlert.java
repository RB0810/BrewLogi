package com.example.brewlogi;

import android.app.Application;

import com.onesignal.OneSignal;

public class TestAlert extends Application {
    private static final String ONESIGNAL_APP_ID = "f75c91de-4302-459d-ad26-b0103d0a39e7";

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }
}
