package com.duzce.spotinotes.db;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotesQueryClass {

    private Context context;

    private SQLiteDatabase db;

    public NotesQueryClass(Context context){
        this.context = context;
    }

    @SuppressLint("Range")
    public List<Note> getAllNotes() {
        SQLiteDatabase db = NotesDbHelper.getInstance(context).getWritableDatabase();

        List<Note> noteList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Config.TABLE_NOTE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_TRACK_NAME)),
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_TRACK_URL)),
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_TRACK_IMAGE_URL)),
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_ARTIST_NAME)),
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_ARTIST_URL)),
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_PREVIEW_URL)),
                        cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PROGRESS_MS)),
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_LYRICS)),
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_NOTE)),
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_NOTED_LYRICS))
                );
                note.setId(cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ID)));
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return noteList;
    }

    @SuppressLint("Range")
    public List<Note> searchNotes(String query) {
        SQLiteDatabase db = NotesDbHelper.getInstance(context).getWritableDatabase();

        List<Note> noteList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Config.TABLE_NOTE + " WHERE " +
                Config.COLUMN_TRACK_NAME + " LIKE '%" + query + "%' OR " +
                Config.COLUMN_ARTIST_NAME + " LIKE '%" + query + "%' OR " +
                Config.COLUMN_LYRICS + " LIKE '%" + query + "%' OR " +
                Config.COLUMN_NOTE + " LIKE '%" + query + "%'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_TRACK_NAME)),
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_TRACK_URL)),
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_TRACK_IMAGE_URL)),
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_ARTIST_NAME)),
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_ARTIST_URL)),
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_PREVIEW_URL)),
                        cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PROGRESS_MS)),
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_LYRICS)),
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_NOTE)),
                        cursor.getString(cursor.getColumnIndex(Config.COLUMN_NOTED_LYRICS))
                );
                note.setId(cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ID)));
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return noteList;
    }

    public long insertNote(Note note) {
        SQLiteDatabase db = NotesDbHelper.getInstance(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Config.COLUMN_TRACK_NAME, note.getTrackName());
        values.put(Config.COLUMN_TRACK_URL, note.getTrackUrl());
        values.put(Config.COLUMN_TRACK_IMAGE_URL, note.getTrackImageUrl());
        values.put(Config.COLUMN_ARTIST_NAME, note.getArtistName());
        values.put(Config.COLUMN_ARTIST_URL, note.getArtistUrl());
        values.put(Config.COLUMN_PREVIEW_URL, note.getPreviewUrl());
        values.put(Config.COLUMN_PROGRESS_MS, note.getProgressMs());
        values.put(Config.COLUMN_LYRICS, note.getLyrics());
        values.put(Config.COLUMN_NOTE, note.getNote());
        values.put(Config.COLUMN_NOTED_LYRICS, note.getNotedLyrics());

        return db.insert(Config.TABLE_NOTE, null, values);
    }

    public int deleteNote(int noteId) {
        SQLiteDatabase db = NotesDbHelper.getInstance(context).getWritableDatabase();
        return db.delete(Config.TABLE_NOTE, Config.COLUMN_ID + "=?", new String[]{String.valueOf(noteId)});
    }

}
