package com.duzce.spotinotes.ui;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view_saved_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Note> noteList = notesDb.getAllNotes();

        SavedNotesAdapter adapter = new SavedNotesAdapter(noteList);
        recyclerView.setAdapter(adapter);
    }
}