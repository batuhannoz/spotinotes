package com.duzce.spotinotes.ui;

import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duzce.spotinotes.MainActivity;
import com.duzce.spotinotes.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ResourceBundle;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;

import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import se.michaelthelin.spotify.model_objects.specification.Track;

public class Player extends Fragment {
    private ImageView CurrentTrackImage;
    private TextView CurrentTrackText;
    private MaterialButton PlayPauseButton;
    private MaterialButton PreviousButton;
    private MaterialButton NextButton;
    private boolean IsPlaying = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        CurrentTrackText = getView().findViewById(R.id.current_track_text);
        CurrentTrackImage = getView().findViewById(R.id.current_track_image);
        PlayPauseButton = getView().findViewById(R.id.play_pause_track_button);
        PreviousButton = getView().findViewById(R.id.previous_track_button);
        NextButton = getView().findViewById(R.id.next_track_button);
        PlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsPlaying) {
                    pauseUsersPlayback_Async();
                } else {
                    startResumeUsersPlayback_Async();
                }
                new Thread(() -> {
                    DelayedRefreshPlayer();
                }).start();
            }
        });
        PreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipUsersPlaybackToPreviousTrack_Async();
                new Thread(() -> {
                    DelayedRefreshPlayer();
                }).start();
            }
        });
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipUsersPlaybackToNextTrack_Async();
                new Thread(() -> {
                    DelayedRefreshPlayer();
                }).start();
            }
        });
        RefreshPlayer();
    }
    private void RefreshPlayer() {
        CurrentlyPlayingContext ctx = getInformationAboutUsersCurrentPlayback_Async();
        if (ctx != null) {
            Track t = (Track) ctx.getItem();
            Picasso.get().load(t.getAlbum().getImages()[0].getUrl()).into(CurrentTrackImage);

            CurrentTrackText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            CurrentTrackText.setSelected(true);
            CurrentTrackText.setSingleLine(true);
            CurrentTrackText.setText(new StringBuilder()
                    .append(t.getName())
                    .append(" - ")
                    .append(t.getArtists()[0].getName()));

            IsPlaying = ctx.getIs_playing();
            if (IsPlaying) {
                PlayPauseButton.setIcon(
                        ResourcesCompat.getDrawable(
                                getResources(),
                                R.drawable.pause_circle,
                                null
                        ));
            } else {
                PlayPauseButton.setIcon(
                        ResourcesCompat.getDrawable(
                                getResources(),
                                R.drawable.play_circle,
                                null
                        ));
            }
        }
    }
    public void DelayedRefreshPlayer() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RefreshPlayer();
            }
        }, 500);
    }
    public static CurrentlyPlayingContext getInformationAboutUsersCurrentPlayback_Async() {
        CurrentlyPlayingContext ctx = null;
        try {
            ctx = MainActivity.spotifyApi
                    .getInformationAboutUsersCurrentPlayback()
                    .build()
                    .executeAsync()
                    .join();
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
        return ctx;
    }
    public static void skipUsersPlaybackToNextTrack_Async() {
        try {
             MainActivity.spotifyApi
                     .skipUsersPlaybackToNextTrack()
                     .build()
                     .executeAsync()
                     .join();
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }
    public static void skipUsersPlaybackToPreviousTrack_Async() {
        try {
            MainActivity.spotifyApi
                    .skipUsersPlaybackToPreviousTrack()
                    .build()
                    .executeAsync()
                    .join();
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }
    public static void pauseUsersPlayback_Async() {
        try {
            MainActivity.spotifyApi
                    .pauseUsersPlayback()
                    .build()
                    .executeAsync()
                    .join();
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }
    public static void startResumeUsersPlayback_Async() {
        try {
            MainActivity.spotifyApi
                    .startResumeUsersPlayback()
                    .build()
                    .executeAsync()
                    .join();
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }
}