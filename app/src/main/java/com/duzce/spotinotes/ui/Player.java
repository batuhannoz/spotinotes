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
import com.adamratzman.spotify.models.CurrentlyPlayingObject;
import com.adamratzman.spotify.models.CurrentlyPlayingType;
import com.adamratzman.spotify.models.Episode;
import com.adamratzman.spotify.models.Playable;
import com.adamratzman.spotify.models.Track;
import com.duzce.spotinotes.MainActivity;
import com.duzce.spotinotes.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import kotlin.Unit;

public class Player extends Fragment {
    private ImageView CurrentTrackImage;
    private TextView CurrentTrackText;
    private MaterialButton PlayPauseButton;
    private MaterialButton PreviousButton;
    private MaterialButton NextButton;
    private boolean IsPlaying = false;
    public CurrentlyPlayingType currentlyPlayingType = null;
    public Playable currentlyPlayingItem = null;

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
        PlayPauseButton.setOnClickListener(v -> {
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
        });
        PreviousButton.setOnClickListener(v ->
                MainActivity.spotifyApi.getPlayer().skipBehind(null, new SpotifyContinuation<String>() {
                    @Override
                    public void onSuccess(String s) {RefreshPlayer();}
                    @Override
                    public void onFailure(@NonNull Throwable throwable) {}
                })
        );
        NextButton.setOnClickListener(v ->
                MainActivity.spotifyApi.getPlayer().skipForward(null, new SpotifyContinuation<String>() {
                    @Override
                    public void onSuccess(String s) {RefreshPlayer();}
                    @Override
                    public void onFailure(@NonNull Throwable throwable) {}
                })
        );
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
                        currentlyPlayingItem = ctx.getItem();
                        try {
                            if (ctx.getItem().getType().equals("track")) {
                                currentlyPlayingType = CurrentlyPlayingType.Track;
                                Track t = (Track) currentlyPlayingItem;
                                UpdatePlayerUI(
                                        t.getName()
                                                +" - "
                                                +t.getArtists().get(0).getName(),
                                        t.getAlbum().getImages().get(0).getUrl(),
                                        ctx.isPlaying()
                                );
                            } else if (ctx.getItem().getType().equals("episode")) {
                                currentlyPlayingType = CurrentlyPlayingType.Episode;
                                Episode e = (Episode) currentlyPlayingItem;
                                UpdatePlayerUI(
                                        e.getName()
                                                + " - "
                                                + e.getShow().getPublisher(),
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
