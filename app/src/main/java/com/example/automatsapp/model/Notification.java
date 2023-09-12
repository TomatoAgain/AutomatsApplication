package com.example.automatsapp.model;

import android.net.Uri;

public class Notification {
    private String description;
    private String username;
    private Uri image;
    private long date;
    private String postId;

    public Notification(String description, String username, Uri image, long date, String postId) {
        this.description = description;
        this.username = username;
        this.image = image;
        this.date = date;
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

}
