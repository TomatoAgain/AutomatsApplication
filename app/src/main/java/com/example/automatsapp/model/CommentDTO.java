package com.example.automatsapp.model;

public class CommentDTO {
    private User user;
    private String description;
    private int commentIndent;
    private long date;
    private int commentIndex;
    private int commentId;
    public CommentDTO(){

    }
    public CommentDTO(Comment comment, int commentId) {
        this.description = comment.getDescription();
        this.user = comment.getUser();
        this.date = comment.getDate();
        this.commentIndent = comment.getCommentIndent();
        this.commentIndex = comment.getCommentIndex();
        this.commentId = commentId;
    }

    public User getUser() {
        return user;
    }

    public String getDescription() {
        return description;
    }

    public long getDate(){return date;}

    public void setUser(User user) {
        this.user = user;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getCommentIndex() {
        return commentIndex;
    }

    public void setCommentIndex(int commentIndex) {
        this.commentIndex = commentIndex;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getCommentIndent() {
        return commentIndent;
    }
    public void setCommentIndent(int commentIndent) {
        this.commentIndent = commentIndent;
    }
}
