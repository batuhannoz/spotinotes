package com.duzce.spotinotes.ui;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.duzce.spotinotes.R;
import com.duzce.spotinotes.db.note.Note;
import com.duzce.spotinotes.db.note.NoteRepository;

public class UpdateNote extends DialogFragment {

    NoteDetails noteDetails;

    NoteRepository repository;

    Note note;

    public UpdateNote(Note note, NoteRepository repository, NoteDetails noteDetails) {
        this.note = note;
        this.repository = repository;
        this.noteDetails = noteDetails;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_note, container, false);
        Button createButton = view.findViewById(R.id.create_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);
        EditText noteText = view.findViewById(R.id.note_text);

        noteText.setText(note.getNote());

        createButton.setOnClickListener(v -> {
            note.setNote(noteText.getText().toString());
            repository.updateNote(note);
            noteDetails.onStart();
            getDialog().dismiss();
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

