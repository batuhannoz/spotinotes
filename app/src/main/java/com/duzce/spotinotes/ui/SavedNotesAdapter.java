package com.duzce.spotinotes.ui;

// NoteAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duzce.spotinotes.R;
import com.duzce.spotinotes.db.Note;

import java.util.List;

public class SavedNotesAdapter extends RecyclerView.Adapter<SavedNotesAdapter.NoteViewHolder> {

    private List<Note> noteList;

    public SavedNotesAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.trackNameTextView.setText(note.getTrackName());
        holder.artistNameTextView.setText(note.getArtistName());
        holder.noteTextView.setText(note.getNote());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView trackNameTextView;
        TextView artistNameTextView;
        TextView noteTextView;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            trackNameTextView = itemView.findViewById(R.id.trackNameTextView);
            artistNameTextView = itemView.findViewById(R.id.artistNameTextView);
            noteTextView = itemView.findViewById(R.id.noteTextView);
        }
    }
}