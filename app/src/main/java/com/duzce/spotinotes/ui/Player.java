package com.duzce.spotinotes.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adamratzman.spotify.javainterop.SpotifyContinuation;
import com.adamratzman.spotify.models.CurrentlyPlayingContext;
import com.adamratzman.spotify.models.CurrentlyPlayingObject;
import com.adamratzman.spotify.models.CurrentlyPlayingType;
import com.adamratzman.spotify.models.Episode;
import com.adamratzman.spotify.models.PodcastEpisodeTrack;
import com.adamratzman.spotify.models.Track;
import com.duzce.spotinotes.MainActivity;
import com.duzce.spotinotes.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.CropSquareTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import kotlin.Unit;

public class Player extends Fragment {
    public ImageView CurrentTrackImage;
    public TextView CurrentTrackText;
    public MaterialButton PlayPauseButton;
    public MaterialButton PreviousButton;
    public MaterialButton NextButton;
    public boolean IsPlaying = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        CurrentTrackText = getView().findViewById(R.id.current_track_text);
        CurrentTrackText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        CurrentTrackText.setSelected(true);
        CurrentTrackText.setSingleLine(true);
        CurrentTrackImage = getView().findViewById(R.id.current_track_image);
        PlayPauseButton = getView().findViewById(R.id.play_pause_track_button);
        PreviousButton = getView().findViewById(R.id.previous_track_button);
        NextButton = getView().findViewById(R.id.next_track_button);

        // Set click listeners for player
        PlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsPlaying) {
                    MainActivity.spotifyApi.getPlayer().pause(null, new SpotifyContinuation<Unit>() {
                        @Override
                        public void onSuccess(Unit unit) {
                            RefreshPlayer();
                        }
                        @Override
                        public void onFailure(@NonNull Throwable throwable) {}
                    });
                } else {
                    MainActivity.spotifyApi.getPlayer().resume(null, new SpotifyContinuation<Unit>() {
                        @Override
                        public void onSuccess(Unit unit) {RefreshPlayer();}
                        @Override
                        public void onFailure(@NonNull Throwable throwable) {}
                    });
                }
            }
        });
        PreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.spotifyApi.getPlayer().skipBehind(null, new SpotifyContinuation<String>() {
                    @Override
                    public void onSuccess(String s) {RefreshPlayer();}
                    @Override
                    public void onFailure(@NonNull Throwable throwable) {}
                });
            }
        });
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.spotifyApi.getPlayer().skipForward(null, new SpotifyContinuation<String>() {
                    @Override
                    public void onSuccess(String s) {RefreshPlayer();}
                    @Override
                    public void onFailure(@NonNull Throwable throwable) {return;}
                });
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        RefreshPlayer();
    }
    private void RefreshPlayer() {
        MainActivity.spotifyApi.getPlayer().getCurrentlyPlaying(
                Arrays.asList(CurrentlyPlayingType.Track, CurrentlyPlayingType.Episode),
                null,
                new SpotifyContinuation<CurrentlyPlayingObject>() {
                    @Override
                    public void onSuccess(CurrentlyPlayingObject ctx) {
                        try {
                            if (ctx.getItem().getType().equals("track")) {
                                Track t = (Track) ctx.getItem();
                                UpdatePlayerUI(
                                        t.getName()
                                                +" - "
                                                +t.getArtists().get(0).getName(),
                                        t.getAlbum().getImages().get(0).getUrl(),
                                        ctx.isPlaying()
                                );
                            } else if (ctx.getItem().getType().equals("episode")) {
                                Episode e = (Episode) ctx.getItem();
                                UpdatePlayerUI(
                                        e.getName()
                                                +" - "
                                                +e.getShow().getPublisher(),
                                        e.getImages().get(0).getUrl(),
                                        ctx.isPlaying()
                                );
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Throwable throwable) {
                        Log.i("TAG", "onFailure: " + throwable);
                    }
                });
    }
    public void UpdatePlayerUI(String displayName, String imageUri, Boolean isPlaying) {
        new Handler(Looper.getMainLooper()).post(() -> {
            CurrentTrackText.setText(displayName);
            Picasso
                    .get()
                    .load(imageUri)
                    .transform(new RoundedCornersTransformation(50, 0))
                    .into(CurrentTrackImage);
            IsPlaying = isPlaying;
            if (isPlaying) {
                PlayPauseButton.setIcon(ResourcesCompat.getDrawable(
                        getResources(),
                        R.drawable.pause_circle,
                        null)
                );
            } else {
                PlayPauseButton.setIcon(ResourcesCompat.getDrawable(
                        getResources(),
                        R.drawable.play_circle,
                        null)
                );
            }
        });
    }
}