package com.duzce.spotinotes.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duzce.spotinotes.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    TextView trackNameTextView;
    TextView artistNameTextView;
    TextView noteTextView;
    Button deleteNoteButton;
    ImageView savedNoteImageView;
    TextView dateTimeTexView;

    NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        savedNoteImageView = itemView.findViewById(R.id.saved_note_image_view);
        trackNameTextView = itemView.findViewById(R.id.track_name_text_view);
        artistNameTextView = itemView.findViewById(R.id.artist_name_text_view);
        noteTextView = itemView.findViewById(R.id.noteTextView);
        deleteNoteButton = itemView.findViewById(R.id.deleteNoteButton);
        dateTimeTexView = itemView.findViewById(R.id.date_time_text_view);
    }
}