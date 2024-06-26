package com.duzce.spotinotes.db.note;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String trackName;
    @NonNull
    private String trackUrl;
    @NonNull
    private String trackImageUrl;
    @NonNull
    private String artistName;

    private String artistUrl;

    private String previewUrl;
    @NonNull
    private int progressMs;

    private String lyrics;
    @NonNull
    private String note;

    private String notedLyrics;
    @NonNull
    private String notedDateTime;

    public Note(String trackName, String trackUrl, String trackImageUrl, String artistName, String artistUrl, String previewUrl, int progressMs, String lyrics, String note, String notedLyrics, String notedDateTime) {
        this.trackName = trackName;
        this.trackUrl = trackUrl;
        this.trackImageUrl = trackImageUrl;
        this.artistName = artistName;
        this.artistUrl = artistUrl;
        this.previewUrl = previewUrl;
        this.progressMs = progressMs;
        this.lyrics = lyrics;
        this.note = note;
        this.notedLyrics = notedLyrics;
        this.notedDateTime = notedDateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getTrackUrl() {
        return trackUrl;
    }

    public void setTrackUrl(String trackUrl) {
        this.trackUrl = trackUrl;
    }

    public String getTrackImageUrl() {
        return trackImageUrl;
    }

    public void setTrackImageUrl(String trackImageUrl) {
        this.trackImageUrl = trackImageUrl;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistUrl() {
        return artistUrl;
    }

    public void setArtistUrl(String artistUrl) {
        this.artistUrl = artistUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public int getProgressMs() {
        return progressMs;
    }

    public void setProgressMs(int progressMs) {
        this.progressMs = progressMs;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNotedLyrics() {
        return notedLyrics;
    }

    public void setNotedLyrics(String notedLyrics) {
        this.notedLyrics = notedLyrics;
    }

    public String getNotedDateTime() {
        return notedDateTime;
    }

    public void setNotedDateTime(String notedDateTime) {
        this.notedDateTime = notedDateTime;
    }

}