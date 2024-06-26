package com.duzce.spotinotes.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.duzce.spotinotes.R;
import com.duzce.spotinotes.adapter.SavedNotesAdapter;
import com.duzce.spotinotes.db.note.Note;
import com.duzce.spotinotes.db.note.NoteRepository;

import java.util.ArrayList;
import java.util.List;

public class SavedNotes extends Fragment {

    private NoteRepository repository;

    private EditText searchEditText;

    private SavedNotesAdapter adapter;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        repository = new NoteRepository(getContext());
        View rootView = inflater.inflate(R.layout.fragment_saved_notes, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view_saved_notes);
        searchEditText = rootView.findViewById(R.id.searchEditText);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Note> noteList = repository.getAllNotes();
        adapter = new SavedNotesAdapter(getContext(), noteList);
        recyclerView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().trim().toLowerCase();
                if (searchText.isEmpty()) {
                    adapter.noteList = repository.getAllNotes();
                } else {
                    List<Note> filteredList = new ArrayList<>();
                    for (Note note : repository.getAllNotes()) {
                        String trackName = note.getTrackName().toLowerCase();
                        String artistName = note.getArtistName().toLowerCase();
                        String noteText = note.getNote().toLowerCase();
                        if (trackName.contains(searchText) || artistName.contains(searchText) || noteText.contains(searchText)) {
                            filteredList.add(note);
                        }
                    }
                    adapter.noteList = filteredList;
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void CreateNote(Note note) {
        long i = repository.insertNote(note);
        note.setId((int) i);
        adapter.noteList.add(note);
        adapter.notifyItemInserted(adapter.getItemCount());
        recyclerView.smoothScrollToPosition(adapter.getItemCount());
    }
}
