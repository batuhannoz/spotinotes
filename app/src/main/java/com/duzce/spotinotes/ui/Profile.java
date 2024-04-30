package com.duzce.spotinotes.ui;

import static androidx.core.app.ActivityCompat.recreate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.adamratzman.spotify.javainterop.SpotifyContinuation;
import com.adamratzman.spotify.models.SpotifyUserInformation;
import com.duzce.spotinotes.MainActivity;
import com.duzce.spotinotes.R;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Profile extends Fragment {
    private ImageView ProfileImage;
    private TextView EmailText;
    private TextView DisplayNameText;
    private ToggleButton ToggleDarkMode;
    private SharedPreferences sharedPreferences;
    private final String selectedLanguageKey = "selected_language";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);


        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        String selectedLanguage = sharedPreferences.getString(selectedLanguageKey, "en");

        if (selectedLanguage.equals("tr")) {
            setLocale("tr");
        } else {
            setLocale("en");
        }
        final String[] languagesArray = getResources().getStringArray(R.array.languages_array);

        Spinner languageSpinner = rootView.findViewById(R.id.language_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, languagesArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = (String) parent.getItemAtPosition(position);
                if (selectedLanguage.equals(getString(R.string.english))) {
                    changeLanguage("en");
                    restartActivity();
                } else if (selectedLanguage.equals(getString(R.string.turkce))) {
                    changeLanguage("tr");
                    restartActivity();
                }
                refreshUI();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;
    }

    private void changeLanguage(String languageCode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(selectedLanguageKey, languageCode);
        editor.apply();
        setLocale(languageCode);

        String selectedLanguage = languageCode.equals("en") ? getString(R.string.english) : getString(R.string.turkce);
        Toast.makeText(getContext(), requireContext().getText(R.string.SelectedLanguage) + selectedLanguage, Toast.LENGTH_SHORT).show();

        refreshUI();
    }

    private void refreshUI() {

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
    private void restartActivity() {
        Intent intent = requireActivity().getIntent();
        requireActivity().finish();
        startActivity(intent);
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = requireContext().getResources().getConfiguration();
        config.setLocale(locale);
        requireContext().getResources().updateConfiguration(config, requireContext().getResources().getDisplayMetrics());
    }


    @Override
    public void onStart() {
        super.onStart();
        ProfileImage = getView().findViewById(R.id.profile_image);
        DisplayNameText = getView().findViewById(R.id.display_name_text);
        EmailText = getView().findViewById(R.id.email_text);
        ToggleDarkMode = getView().findViewById(R.id.toggleDarkMode);

        SharedPreferences themeSharedPreferences = requireActivity().getSharedPreferences("theme", 0);
        boolean isDarkModeEnabled = themeSharedPreferences.getBoolean("isDarkModeEnabled", false);
        ToggleDarkMode.setChecked(isDarkModeEnabled);

        ToggleDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToggleDarkMode.isChecked()) {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                SharedPreferences.Editor editor = themeSharedPreferences.edit();
                editor.putBoolean("isDarkModeEnabled", ToggleDarkMode.isChecked());
                editor.apply();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
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
            public void onFailure(@NonNull Throwable throwable) {
                Log.i("TAG", "onFailure: "+throwable);
            }
        });
    }
}