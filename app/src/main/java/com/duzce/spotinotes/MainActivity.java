package com.duzce.spotinotes;

import static com.duzce.spotinotes.db.TokenManager.getAccessToken;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentTransaction;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.navigation.NavController;

import com.adamratzman.spotify.SpotifyApi;
import com.adamratzman.spotify.SpotifyApiOptions;
import com.adamratzman.spotify.SpotifyAppApi;
import com.adamratzman.spotify.SpotifyClientApi;
import com.adamratzman.spotify.javainterop.SpotifyContinuation;
import com.adamratzman.spotify.models.CurrentlyPlayingObject;
import com.adamratzman.spotify.models.CurrentlyPlayingType;
import com.adamratzman.spotify.models.SpotifyContext;
import com.adamratzman.spotify.models.Token;
import com.adamratzman.spotify.models.Track;
import com.duzce.spotinotes.ui.CreateNote;
import com.duzce.spotinotes.ui.Player;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static SpotifyClientApi spotifyApi;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spotifyApi = new SpotifyClientApi(
                null,
                null,
                new Token(getAccessToken(this), "Bearer", 3600, null, null),
                new SpotifyApiOptions()
                );

        // Set Player
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.player_fragment, new Player())
                .commit();

        // Setup navigation
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_profile,
                R.id.navigation_saved_notes)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_activity_fragment_container);
        NavController navController = navHostFragment.getNavController();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        fab = findViewById(R.id.fab);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (navDestination.getId() == R.id.navigation_profile) {
                    fab.animate().setDuration(300).scaleX(0).scaleY(0).alpha(1)
                            .setInterpolator(new LinearOutSlowInInterpolator()).start();
                    fab.setVisibility(View.INVISIBLE);
                } else if (navDestination.getId() == R.id.navigation_saved_notes) {
                    fab.animate().setDuration(300).scaleX(1).scaleY(1).alpha(1)
                            .setInterpolator(new LinearOutSlowInInterpolator()).start();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fab.getVisibility() == View.INVISIBLE) return;
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
