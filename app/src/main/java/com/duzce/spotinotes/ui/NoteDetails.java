package com.duzce.spotinotes.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
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
import com.duzce.spotinotes.api.lyrics.LyricsApi;
import com.duzce.spotinotes.db.note.Note;
import com.duzce.spotinotes.db.note.NoteRepository;
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

    private Button editNoteButton;

    private Button openLyricsButton;

    private TextView lyricsTextView;

    private Button deleteDetailedNoteButton;

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
        if (note == null && repository == null) return;

        recyclerView = getView().findViewById(R.id.recycler_view_same_track_saved_notes);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        openWithSpotifyButton = getView().findViewById(R.id.open_with_spotify_button);
        shareNoteButton = getView().findViewById(R.id.share_note_button);
        deleteDetailedNoteButton = getView().findViewById(R.id.delete_detailed_note_button);
        openLyricsButton = getView().findViewById(R.id.open_lyrics_button);
        editNoteButton = getView().findViewById(R.id.edit_note_button);
        detailedNoteImageView = getView().findViewById(R.id.detailed_note_image_view);
        detailedNoteNameTextView = getView().findViewById(R.id.detailed_note_name_text_view);
        detailedNoteArtistTextView = getView().findViewById(R.id.detailed_note_artist_text_view);
        detailedNoteTextView = getView().findViewById(R.id.detailed_note_text_view);
        detailedoteDateTimeTextView = getView().findViewById(R.id.detailed_note_date_time_text_view);
        lyricsTextView =  getView().findViewById(R.id.lyrics_text_view);

        deleteDetailedNoteButton.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage("Bu Notu Silmek istediginize emin misiniz?"); // TODO language
            alertDialogBuilder.setPositiveButton("Evet", // TODO language
                    (arg0, arg1) -> {
                        repository.deleteNote(note);
                        getActivity().getOnBackPressedDispatcher().onBackPressed();
                        Toast.makeText(getContext(), "Note deleted successfully", Toast.LENGTH_SHORT).show(); // TODO language
                    }
            );
            alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss()); // TODO language
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        if (!Objects.equals(note.getNotedLyrics(), "")) {
            lyricsTextView.setText(note.getNotedLyrics());
        }

        editNoteButton.setOnClickListener(v -> {
            UpdateNote updateNote = new UpdateNote(note, repository, this);
            updateNote.show(getParentFragmentManager(), "Update Note"); // TODO language
        });

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

        adapter = new NoteDetailsAdapter(getContext(), noteList, this);

        recyclerView.setAdapter(adapter);

        openWithSpotifyButton.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage("You will be directed to the spotify application to open the song."); // TODO language
            alertDialogBuilder.setPositiveButton("Redirect", // TODO language
                    (arg0, arg1) -> {
                        final String spotifyContent = note.getTrackUrl();
                        final String branchLink = "https://spotify.link/content_linking?~campaign=" + getContext().getPackageName() + "&$deeplink_path=" + spotifyContent + "&$fallback_url=" + spotifyContent;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(branchLink));
                        startActivity(intent);
                    }
            );
            alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()); // TODO language
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        openLyricsButton.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage("Sarki Sozlerini notunuza kaydetmek istediginize emin misiniz?"); // TODO language
            alertDialogBuilder.setPositiveButton("Kaydet", // TODO language
                    (arg0, arg1) -> {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        LyricsApi.LyricsResponse response = LyricsApi.LyricsMatcher(note.getTrackName(), note.getArtistName());
                        note.setNotedLyrics(response.getMessage().getBody().getLyrics().getLyrics_body());
                        lyricsTextView.setText(note.getNotedLyrics());
                        repository.updateNote(note);
                    }
            );
            alertDialogBuilder.setNegativeButton("İptal", (dialog, which) -> dialog.dismiss()); // TODO language
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        shareNoteButton.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    note.getTrackName()+" - "+note.getArtistName()+": ("+note.getTrackUrl()+") "+
                    "İste bu harika sarkiyi dinlerken senin icin aldigim not: "+ note.getNote() // TODO language
            );
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
