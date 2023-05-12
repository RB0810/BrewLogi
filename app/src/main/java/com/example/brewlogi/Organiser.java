package com.example.brewlogi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Organiser extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    DashboardFragment dashboardFragment = new DashboardFragment();
    AlertFragment alertFragment = new AlertFragment();
    DistributionFragment distributionFragment = new DistributionFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,dashboardFragment).commit();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,dashboardFragment).commit();

                        return true;
                    case R.id.alert:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,alertFragment).commit();
                        return true;
                    case R.id.distribution:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,distributionFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}