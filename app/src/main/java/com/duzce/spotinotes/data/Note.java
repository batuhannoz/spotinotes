package com.duzce.spotinotes.data;

public class Note {
    private String name;
    private int pictureResId;

    public Note(String name, int pictureResId) {
        this.name = name;
        this.pictureResId = pictureResId;
    }

    public String getName() {
        return name;
    }

    public int getPictureResId() {
        return pictureResId;
    }
}
