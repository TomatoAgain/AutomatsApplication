package com.example.automatsapp.model;

public class NotificationDTO {
    private String description;
    private String username;
    private String image;
    private long date;
    private String postId;

    public NotificationDTO(){

    }
    public NotificationDTO(String description, String username, String image, long date, String postId) {
        this.description = description;
        this.username = username;
        this.image = image;
        this.date = date;
        this.postId = postId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

}
