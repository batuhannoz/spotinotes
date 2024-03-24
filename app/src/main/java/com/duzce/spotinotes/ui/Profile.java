package com.duzce.spotinotes.ui;

import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duzce.spotinotes.MainActivity;
import com.duzce.spotinotes.R;
import com.squareup.picasso.Picasso;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import se.michaelthelin.spotify.model_objects.specification.User;

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

        User u = getCurrentUsersProfile_Async();

        Picasso
                .get()
                .load(u.getImages()[1].getUrl())
                .placeholder(getContext().getDrawable(R.drawable.account_circle))
                .transform(new CropCircleTransformation())
                .into(ProfileImage);
        DisplayNameText.setText(u.getDisplayName());
        EmailText.setText(u.getEmail());
    }
    public static User getCurrentUsersProfile_Async() {
        User user = null;
        try {
            user = MainActivity.spotifyApi.getCurrentUsersProfile().build().executeAsync().join();
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
        return user;
    }
}