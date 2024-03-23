package com.duzce.spotinotes.ui;

import static com.duzce.spotinotes.db.TokenManager.getAccessToken;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duzce.spotinotes.R;
import com.squareup.picasso.Picasso;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;

public class Player extends Fragment {
    public ImageView CurrentTrackImage ;
    public TextView CurrentTrackText;
    private static SpotifyApi spotifyApi;
    private static GetUsersCurrentlyPlayingTrackRequest getUsersCurrentlyPlayingTrackRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(getAccessToken(getContext()))
                .build();
        CurrentTrackText = getView().findViewById(R.id.current_track_text);
        CurrentTrackImage = getView().findViewById(R.id.current_song_image);
        getInformationAboutUsersCurrentPlayback_Async();
    }

    public void getInformationAboutUsersCurrentPlayback_Async() {
        try {
            CurrentlyPlayingContext currentlyPlayingContext =
                    spotifyApi.getInformationAboutUsersCurrentPlayback().build().executeAsync().join();

            Track t = (Track) currentlyPlayingContext.getItem();

            Log.i("TAG", "getInformationAboutUsersCurrentPlayback_Async: "+t.getAlbum().getImages()[0].getUrl());

            Picasso
                    .get()
                    .load(t.getAlbum().getImages()[0].getUrl())
                    .into(CurrentTrackImage);

            CurrentTrackText.setText(t.getName() + " - " + t.getArtists()[0].getName());

        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }
}