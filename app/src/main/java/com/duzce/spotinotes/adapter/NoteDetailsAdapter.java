package com.duzce.spotinotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.duzce.spotinotes.R;
import com.duzce.spotinotes.db.note.Note;
import com.duzce.spotinotes.db.note.NoteRepository;
import com.duzce.spotinotes.ui.NoteDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class NoteDetailsAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private Context context;

    public List<Note> noteList;

    private NoteRepository repository;

    private NoteDetails noteDetails;

    public NoteDetailsAdapter(Context context, List<Note> noteList, NoteDetails noteDetails) {
        this.context = context;
        this.noteList = noteList;
        this.noteDetails = noteDetails;
        repository = new NoteRepository(context);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        Picasso
                .get()
                .load(note.getTrackImageUrl())
                .transform(new RoundedCornersTransformation(50, 0))
                .into(holder.savedNoteImageView);
        holder.trackNameTextView.setText(note.getTrackName());
        holder.artistNameTextView.setText(note.getArtistName());
        holder.noteTextView.setText(note.getNote());
        holder.deleteNoteButton.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage(R.string.noteDelete);
            alertDialogBuilder.setPositiveButton(R.string.noteDeleteYes,
                    (arg0, arg1) -> {
                        repository.deleteNote(note);
                        noteList.remove(holder.getLayoutPosition());
                        notifyItemRemoved(holder.getLayoutPosition());
                        Toast.makeText(context, R.string.noteDeleteTitle, Toast.LENGTH_SHORT).show();
                    }
            );
            alertDialogBuilder.setNegativeButton(R.string.noteDeleteCancel, (dialog, which) -> dialog.dismiss());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        holder.dateTimeTexView.setText(note.getNotedDateTime());

        holder.itemView.setOnClickListener(v -> {
            noteDetails.setNote(note);
            noteDetails.onStart();
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }



}