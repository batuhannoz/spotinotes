package com.duzce.spotinotes.db.note;

import android.content.Context;

import androidx.room.Room;

import java.util.List;
import java.util.concurrent.Executors;

public class NoteRepository {
    private NoteDao noteDao;

    public NoteRepository(Context context) {
        NoteDatabase db = Room.databaseBuilder(context, NoteDatabase.class, "note-db")
                .allowMainThreadQueries()
                .build();
        Executors.newSingleThreadExecutor().execute(() -> noteDao = db.noteDao());
        noteDao = db.noteDao();
    }

    public List<Note> getAllNotes() {
        return noteDao.getAllNotes();
    }

    public List<Note> searchNotes(String query) {

        return noteDao.searchNotes("%" + query + "%");
    }

    public void updateNote(Note note) {noteDao.update(note);}

    public long insertNote(Note note) {
        return noteDao.insert(note);
    }

    public void deleteNote(Note note) {
        noteDao.delete(note);
    }
}
