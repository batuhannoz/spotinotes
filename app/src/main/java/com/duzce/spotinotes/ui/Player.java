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
import com.adamratzman.spotify.models.Track;
import com.duzce.spotinotes.MainActivity;
import com.duzce.spotinotes.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.Collections;
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
        CurrentTrackImage = getView().findViewById(R.id.current_track_image);
        PlayPauseButton = getView().findViewById(R.id.play_pause_track_button);
        PreviousButton = getView().findViewById(R.id.previous_track_button);
        NextButton = getView().findViewById(R.id.next_track_button);
        PlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsPlaying) {
                    MainActivity.spotifyApi.getPlayer().pause(null, new SpotifyContinuation<Unit>() {
                        @Override
                        public void onSuccess(Unit unit) {
                            DelayedRefreshPlayer();
                        }
                        @Override
                        public void onFailure(@NonNull Throwable throwable) {}
                    });
                } else {
                    MainActivity.spotifyApi.getPlayer().resume(null, new SpotifyContinuation<Unit>() {
                        @Override
                        public void onSuccess(Unit unit) {DelayedRefreshPlayer();}
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
                    public void onSuccess(String s) {DelayedRefreshPlayer();}
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
                    public void onSuccess(String s) {DelayedRefreshPlayer();}
                    @Override
                    public void onFailure(@NonNull Throwable throwable) {return;}
                });
            }
        });
        RefreshPlayer();
    }
    @Override
    public void onResume() {
        super.onResume();
        RefreshPlayer();
    }
    private void RefreshPlayer() {
        MainActivity.spotifyApi.getPlayer().getCurrentlyPlaying(
                Collections.singletonList(CurrentlyPlayingType.Track),
                null,
                new SpotifyContinuation<CurrentlyPlayingObject>() {
                    @Override
                    public void onSuccess(CurrentlyPlayingObject ctx) {
                        Log.i("TAG", "onSuccess: "+ctx.getItem().getAsTrack().getName());
                        new Handler(Looper.getMainLooper()).post(() -> {

                            CurrentTrackText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            CurrentTrackText.setSelected(true);
                            CurrentTrackText.setSingleLine(true);
                            CurrentTrackText.setText(new StringBuilder()
                                    .append(ctx.getItem().getAsTrack().getName())
                                    .append(" - ")
                                    .append(ctx.getItem().getAsTrack().getArtists().get(1).getName()));
                            Picasso
                                    .get()
                                    .load(ctx.getItem().getAsTrack().getAlbum().getImages().get(1).getUrl())
                                    .transform(new RoundedCornersTransformation(50, 0))
                                    .into(CurrentTrackImage);
                        });
                        IsPlaying = ctx.isPlaying();
                        if (IsPlaying) {
                            PlayPauseButton.setIcon(
                                    ResourcesCompat.getDrawable(
                                            getResources(),
                                            R.drawable.pause_circle,
                                            null)
                            );
                        } else {
                            PlayPauseButton.setIcon(
                                    ResourcesCompat.getDrawable(
                                            getResources(),
                                            R.drawable.play_circle,
                                            null)
                            );
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Throwable throwable) {
                        Log.i("TAG", "onFailure: "+ throwable);
                    }
                });
    }
    public void DelayedRefreshPlayer() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RefreshPlayer();
            }
        }, 50);
    }
}