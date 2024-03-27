package com.duzce.spotinotes.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duzce.spotinotes.R;
import com.duzce.spotinotes.db.Note;
import com.duzce.spotinotes.db.NotesQueryClass;

import java.util.List;

public class SavedNotes extends Fragment {
    private NotesQueryClass notesDb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        notesDb = new NotesQueryClass(getContext());
        return inflater.inflate(R.layout.fragment_saved_notes, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        /*
        List<Note> Notes = notesDb.getAllNotes();
        notesDb.insertNote(new Note(
                "Batuhan",
                "Batuhan",
                "Batuhan",
                "Batuhan",
                "Batuhan",
                "Batuhan",
                1111,
                "Batuhan",
                "Batuhan",
                "Batuhan"
                ));
        Log.i("TAG", "onStart: "+Notes.get(1).getTrackName());
        */
    }
}