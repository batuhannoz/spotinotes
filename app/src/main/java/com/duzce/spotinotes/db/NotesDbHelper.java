package com.duzce.spotinotes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDbHelper extends SQLiteOpenHelper {

    public NotesDbHelper(Context context) {
        super(context, Config.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + Config.TABLE_NOTE + " (" +
                Config.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Config.COLUMN_TRACK_NAME + " TEXT, " +
                Config.COLUMN_TRACK_URL + " TEXT, " +
                Config.COLUMN_TRACK_IMAGE_URL + " TEXT, " +
                Config.COLUMN_ARTIST_NAME + " TEXT, " +
                Config.COLUMN_ARTIST_URL + " TEXT, " +
                Config.COLUMN_PREVIEW_URL + " TEXT, " +
                Config.COLUMN_PROGRESS_MS + " INTEGER, " +
                Config.COLUMN_LYRICS + " TEXT, " +
                Config.COLUMN_NOTE + " TEXT, " +
                Config.COLUMN_NOTED_LYRICS + " TEXT" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_NOTE);
        onCreate(db);
    }
}
