package com.duzce.spotinotes;

import static com.duzce.spotinotes.db.TokenManager.getAccessToken;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.navigation.NavController;

import com.adamratzman.spotify.SpotifyApiOptions;
import com.adamratzman.spotify.SpotifyClientApi;
import com.adamratzman.spotify.models.Token;
import com.duzce.spotinotes.ui.CreateNote;
import com.duzce.spotinotes.ui.Player;
import com.duzce.spotinotes.ui.SavedNotes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {

    public static SpotifyClientApi spotifyApi;

    private FloatingActionButton fab;

    private Player player;

    private NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create Spotify Api with Bearer Token
        spotifyApi = new SpotifyClientApi(
                null,
                null,
                new Token(getAccessToken(this), "Bearer", 3600, null, null),
                new SpotifyApiOptions()
                );

        // Set Player Fragment
        player = new Player();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.player_fragment, player)
                .commit();

        // Setup navigation
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_profile,
                R.id.navigation_saved_notes)
                .build();
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_activity_fragment_container);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Setup Floating Action Button
        fab = findViewById(R.id.fab);
        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.navigation_profile) {
                fab.animate().setDuration(300).scaleX(0).scaleY(0).alpha(1)
                        .setInterpolator(new LinearOutSlowInInterpolator()).start();
                fab.setClickable(false);
            } else if (navDestination.getId() == R.id.navigation_saved_notes) {
                fab.animate().setDuration(300).scaleX(1).scaleY(1).alpha(1)
                        .setInterpolator(new LinearOutSlowInInterpolator()).start();
                fab.setClickable(true);
            }
        });

        fab.setOnClickListener(v -> {
            SavedNotes savedNotes = (SavedNotes) navHostFragment.getChildFragmentManager().getFragments().get(0);
            CreateNote createNote = CreateNote.newInstance(savedNotes, player);
            createNote.show(getSupportFragmentManager(), "Create Note"); // TODO
        });
    }

}
