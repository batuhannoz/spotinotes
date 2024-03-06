package com.duzce.spotinotes;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.duzce.spotinotes.ui.Profile;
import com.duzce.spotinotes.ui.SavedNotes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main,new SavedNotes()).commit();
        navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.navigation_saved_notes) {
                    selectedFragment = new SavedNotes();
                } else if (item.getItemId() == R.id.navigation_profile) {
                    selectedFragment = new Profile();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment_activity_main, selectedFragment)
                            .commit();
                }
                return true;
            }
        });
    }
}
