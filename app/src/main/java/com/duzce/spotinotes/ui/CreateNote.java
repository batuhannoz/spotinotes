package com.duzce.spotinotes.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.adamratzman.spotify.models.CurrentlyPlayingType;
import com.adamratzman.spotify.models.Episode;
import com.adamratzman.spotify.models.Track;
import com.duzce.spotinotes.R;
import com.duzce.spotinotes.db.note.Note;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateNote extends DialogFragment {

    SavedNotes savedNotes;
    Player player;

    public CreateNote(SavedNotes savedNotes, Player player) {
        this.savedNotes = savedNotes;
        this.player = player;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_note, container, false);
        Button createButton = view.findViewById(R.id.create_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);
        EditText noteText = view.findViewById(R.id.note_text);
        createButton.setOnClickListener(v -> {
            String note = noteText.getText().toString();
            if (note.isEmpty()) {
                Toast.makeText(getContext(), R.string.noteCreatedInfo, Toast.LENGTH_SHORT).show(); // TODO language
                return;
            }
            if (player.currentlyPlayingType == CurrentlyPlayingType.Track) {
                Track t = (Track) player.currentlyPlayingItem;
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                String currentDateTime = dateFormat.format(currentDate);
                savedNotes.CreateNote(new Note(
                        t.getName(),
                        t.getExternalUrls().get(0).getUrl(),
                        t.getAlbum().getImages().get(0).getUrl(),
                        t.getArtists().get(0).getName(),
                        t.getArtists().get(0).getExternalUrls().get(0).getUrl(),
                        t.getPreviewUrl(),
                        t.getDurationMs(),
                        "",
                        note,
                        "",
                        currentDateTime
                ));
                getDialog().dismiss();
                Toast.makeText(getContext(), R.string.noteCreatedTitle, Toast.LENGTH_SHORT).show(); // TODO language
            } else if (player.currentlyPlayingType == CurrentlyPlayingType.Episode) {
                Episode e = (Episode) player.currentlyPlayingItem;
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                String currentDateTime = dateFormat.format(currentDate);
                savedNotes.CreateNote(new Note(
                        e.getName(),
                        e.getExternalUrls().get(0).getUrl(),
                        e.getImages().get(0).getUrl(),
                        e.getShow().getPublisher(),
                        "",
                        e.getAudioPreviewUrl(),
                        e.getDurationMs(),
                        "",
                        note,
                        "",
                        currentDateTime
                ));
                getDialog().dismiss();
                Toast.makeText(getContext(), R.string.noteCreatedTitle, Toast.LENGTH_SHORT).show(); // TODO language
            }
        });
        cancelButton.setOnClickListener(v -> getDialog().dismiss());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
