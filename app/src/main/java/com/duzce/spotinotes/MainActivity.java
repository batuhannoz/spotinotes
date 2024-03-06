package com.duzce.spotinotes;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.duzce.spotinotes.ui.CreateNote;
import com.duzce.spotinotes.ui.Player;
import com.duzce.spotinotes.ui.Profile;
import com.duzce.spotinotes.ui.SavedNotes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Player
        getSupportFragmentManager().beginTransaction().replace(R.id.player_fragment, new Player()).commit();
        // Set default main fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_fragment_container, new SavedNotes()).commit();
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
                            .replace(R.id.main_activity_fragment_container, selectedFragment)
                            .commit();
                }
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNote fullScreenFragment = new CreateNote();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down);
                transaction.add(android.R.id.content, fullScreenFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
