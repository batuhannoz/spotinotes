package com.duzce.spotinotes.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.duzce.spotinotes.R;
import com.duzce.spotinotes.adapter.NoteDetailsAdapter;
import com.duzce.spotinotes.adapter.SavedNotesAdapter;
import com.duzce.spotinotes.db.Note;
import com.duzce.spotinotes.db.NotesQueryClass;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class NoteDetails extends Fragment {

    private Note note;

    private NotesQueryClass notesDb;

    private RecyclerView recyclerView;

    private Button openWithSpotifyButton;

    private ImageView detailedNoteImageView;

    private TextView detailedNoteNameTextView;

    private TextView detailedNoteArtistTextView;

    private TextView detailedNoteTextView;

    private NoteDetailsAdapter adapter;

    private SavedNotesAdapter parentAdapter;




    public NoteDetails(Note note, NotesQueryClass notesDb, SavedNotesAdapter parentAdapter) {
        this.note = note;
        this.notesDb = notesDb;
        this.parentAdapter = parentAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_details, container, false);
        // Set the layout parameters to match the window size
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(params);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        parentAdapter.noteList = notesDb.getAllNotes();
        parentAdapter.notifyDataSetChanged();
        if (note == null && notesDb == null) return;

        recyclerView = getView().findViewById(R.id.recycler_view_same_track_saved_notes);
        openWithSpotifyButton = getView().findViewById(R.id.open_with_spotify_button);
        detailedNoteImageView = getView().findViewById(R.id.detailed_note_image_view);
        detailedNoteNameTextView = getView().findViewById(R.id.detailed_note_name_text_view);
        detailedNoteArtistTextView = getView().findViewById(R.id.detailed_note_artist_text_view);
        detailedNoteTextView = getView().findViewById(R.id.detailed_note_text_view);

        Picasso
                .get()
                .load(note.getTrackImageUrl())
                .transform(new RoundedCornersTransformation(50, 0))
                .into(detailedNoteImageView);

        detailedNoteNameTextView.setText(note.getTrackName());
        detailedNoteArtistTextView.setText(note.getArtistName());
        detailedNoteTextView.setText(note.getNote());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Note> noteList = parentAdapter.noteList.stream().filter(
                note ->
                        note.getId() != this.note.getId() &&
                                ( Objects.equals(note.getTrackName(), this.note.getTrackName())) ||
                                Objects.equals(note.getArtistName(), this.note.getArtistName()) )
                .collect(Collectors.toList());

        adapter = new NoteDetailsAdapter(getContext(), noteList, parentAdapter, this);

        recyclerView.setAdapter(adapter);

        openWithSpotifyButton.setOnClickListener(v -> Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        parentAdapter.noteList = notesDb.getAllNotes();
        parentAdapter.notifyDataSetChanged();
    }

    public void setNote(Note note) {this.note = note;}

}
