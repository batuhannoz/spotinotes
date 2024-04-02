package com.duzce.spotinotes.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.duzce.spotinotes.R;
import com.duzce.spotinotes.adapter.NoteDetailsAdapter;
import com.duzce.spotinotes.adapter.SavedNotesAdapter;
import com.duzce.spotinotes.db.Note;
import com.duzce.spotinotes.db.NoteRepository;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class NoteDetails extends Fragment {

    private Note note;

    private NoteRepository repository;

    private RecyclerView recyclerView;

    private Button openWithSpotifyButton;

    private Button shareNoteButton;

    private ImageView detailedNoteImageView;

    private TextView detailedNoteNameTextView;

    private TextView detailedNoteArtistTextView;

    private TextView detailedNoteTextView;

    private TextView detailedoteDateTimeTextView;

    private NoteDetailsAdapter adapter;

    private SavedNotesAdapter parentAdapter;

    public NoteDetails(Note note, NoteRepository repository, SavedNotesAdapter parentAdapter) {
        this.note = note;
        this.repository = repository;
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

        parentAdapter.noteList = repository.getAllNotes();
        parentAdapter.notifyDataSetChanged();
        if (note == null && repository == null) return;

        recyclerView = getView().findViewById(R.id.recycler_view_same_track_saved_notes);
        openWithSpotifyButton = getView().findViewById(R.id.open_with_spotify_button);
        detailedNoteImageView = getView().findViewById(R.id.detailed_note_image_view);
        detailedNoteNameTextView = getView().findViewById(R.id.detailed_note_name_text_view);
        detailedNoteArtistTextView = getView().findViewById(R.id.detailed_note_artist_text_view);
        detailedNoteTextView = getView().findViewById(R.id.detailed_note_text_view);
        detailedoteDateTimeTextView = getView().findViewById(R.id.detailed_note_date_time_text_view);
        shareNoteButton = getView().findViewById(R.id.share_note_button);

        Picasso
                .get()
                .load(note.getTrackImageUrl())
                .transform(new RoundedCornersTransformation(50, 0))
                .into(detailedNoteImageView);

        detailedNoteNameTextView.setText(note.getTrackName());
        detailedNoteArtistTextView.setText(note.getArtistName());
        detailedNoteTextView.setText(note.getNote());
        detailedoteDateTimeTextView.setText(note.getNotedDateTime());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Note> noteList = parentAdapter.noteList.stream().filter(
                note ->
                        note.getId() != this.note.getId() &&
                                (Objects.equals(note.getTrackName(), this.note.getTrackName()) ||
                                        Objects.equals(note.getArtistName(), this.note.getArtistName())))
                .collect(Collectors.toList());

        adapter = new NoteDetailsAdapter(getContext(), noteList, parentAdapter, this);

        recyclerView.setAdapter(adapter);

        openWithSpotifyButton.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage("You will be directed to the spotify application to open the song."); // TODO
            alertDialogBuilder.setPositiveButton("Redirect", // TODO
                    (arg0, arg1) -> {
                        final String spotifyContent = note.getTrackUrl();
                        final String branchLink = "https://spotify.link/content_linking?~campaign=" + getContext().getPackageName() + "&$deeplink_path=" + spotifyContent + "&$fallback_url=" + spotifyContent;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(branchLink));
                        startActivity(intent);
                    }
            );
            alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()); // TODO
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        shareNoteButton.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, note.getNote()); // TODO
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        parentAdapter.noteList = repository.getAllNotes();
        parentAdapter.notifyDataSetChanged();
    }

    public void setNote(Note note) {this.note = note;}

}
