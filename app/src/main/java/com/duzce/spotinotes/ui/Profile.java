package com.duzce.spotinotes.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adamratzman.spotify.javainterop.SpotifyContinuation;
import com.adamratzman.spotify.models.SpotifyUserInformation;
import com.duzce.spotinotes.MainActivity;
import com.duzce.spotinotes.R;
import com.squareup.picasso.Picasso;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Profile extends Fragment {
    private ImageView ProfileImage;
    private TextView EmailText;
    private TextView DisplayNameText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        ProfileImage = getView().findViewById(R.id.profile_image);
        DisplayNameText = getView().findViewById(R.id.display_name_text);
        EmailText = getView().findViewById(R.id.email_text);

        MainActivity.spotifyApi.getUsers().getClientProfile(new SpotifyContinuation<SpotifyUserInformation>() {
            @Override
            public void onSuccess(SpotifyUserInformation user) {
                DisplayNameText.setText(user.getDisplayName());
                EmailText.setText(user.getEmail());

                new Handler(Looper.getMainLooper()).post(() -> {
                    Picasso
                            .get()
                            .load(user.getImages().get(1).getUrl())
                            .placeholder(getResources().getDrawable(R.drawable.account_circle, null))
                            .transform(new CropCircleTransformation())
                            .into(ProfileImage);
                });
            }
            @Override
            public void onFailure(@NonNull Throwable throwable) {}
        });
    }
}