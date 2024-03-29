package com.duzce.spotinotes.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.duzce.spotinotes.R;
import com.duzce.spotinotes.db.Note;
import com.duzce.spotinotes.db.NotesQueryClass;
import com.duzce.spotinotes.ui.NoteDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class SavedNotesAdapter extends RecyclerView.Adapter<SavedNotesAdapter.NoteViewHolder> {

    private Context context;

    public List<Note> noteList;

    private NotesQueryClass notesDb;

    public SavedNotesAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
        notesDb = new NotesQueryClass(context);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_note, parent, false);
        return new NoteViewHolder(view, ((FragmentActivity)context).getSupportFragmentManager());
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
            alertDialogBuilder.setMessage("Bu Notu Silmek istediginize emin misiniz?"); // TODO
            alertDialogBuilder.setPositiveButton("Evet",
                    (arg0, arg1) -> {
                        long count = notesDb.deleteNote(note.getId());
                        if(count>0) {
                            noteList.remove(holder.getLayoutPosition());
                            notifyItemRemoved(holder.getLayoutPosition());
                            Toast.makeText(context, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(context, "Note not deleted. Something wrong!", Toast.LENGTH_SHORT).show();
                    }
            );
            alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView trackNameTextView;
        TextView artistNameTextView;
        TextView noteTextView;
        Button deleteNoteButton;
        ImageView savedNoteImageView;

        NoteViewHolder(@NonNull View itemView, FragmentManager fragmentManager) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                NoteDetails fullScreenFragment = new NoteDetails();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down);
                transaction.add(android.R.id.content, fullScreenFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            });
            savedNoteImageView = itemView.findViewById(R.id.saved_note_image_view);
            trackNameTextView = itemView.findViewById(R.id.track_name_text_view);
            artistNameTextView = itemView.findViewById(R.id.artist_name_text_view);
            noteTextView = itemView.findViewById(R.id.noteTextView);
            deleteNoteButton = itemView.findViewById(R.id.deleteNoteButton);
        }
    }

}