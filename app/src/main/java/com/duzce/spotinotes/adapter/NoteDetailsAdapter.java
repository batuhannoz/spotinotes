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
import com.duzce.spotinotes.db.Note;
import com.duzce.spotinotes.db.NotesQueryClass;
import com.duzce.spotinotes.ui.NoteDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class NoteDetailsAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private Context context;

    public List<Note> noteList;

    private NotesQueryClass notesDb;

    private SavedNotesAdapter parentAdapter;

    private NoteDetails noteDetails;

    public NoteDetailsAdapter(Context context, List<Note> noteList, SavedNotesAdapter parentAdapter, NoteDetails noteDetails) {
        this.context = context;
        this.noteList = noteList;
        this.parentAdapter = parentAdapter;
        this.noteDetails = noteDetails;
        notesDb = new NotesQueryClass(context);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);

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
            alertDialogBuilder.setMessage("Bu Notu Silmek istediginize emin misiniz?"); // TODO
            alertDialogBuilder.setPositiveButton("Evet", // TODO
                    (arg0, arg1) -> {
                        long count = notesDb.deleteNote(note.getId());
                        if(count>0) {
                            noteList.remove(holder.getLayoutPosition());
                            notifyItemRemoved(holder.getLayoutPosition());

                            Toast.makeText(context, "Note deleted successfully", Toast.LENGTH_SHORT).show(); // TODO
                        } else
                            Toast.makeText(context, "Note not deleted. Something wrong!", Toast.LENGTH_SHORT).show(); // TODO
                    }
            );
            alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss()); // TODO
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

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