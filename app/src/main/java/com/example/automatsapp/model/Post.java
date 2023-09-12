package com.example.automatsapp.model;


import android.net.Uri;

import com.example.automatsapp.model.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Post {
    private String title;
    private User user;
    private String description;
    private long date;
    private String postId;
    private int commentsCount;
    private ArrayList<Comment> comments;
    private Uri uri;
 public Post(){
 }
    public Post(String title, User user, String description, long date, int commentsCount, Uri uri) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.date = date;
        this.postId = "";
        this.commentsCount = commentsCount;
        this.comments=new ArrayList<>();
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public User getUser() {
        return user;
    }

    public String getDescription() {
        return description;
    }

    public long getDate(){return date;}

    public String getPostId(){
         return this.postId;
    }

    public ArrayList<Comment> getComments(){
        return this.comments;
    }
    public void setComments(ArrayList<Comment> comments){
     if (this.comments!=null)
       this.comments.clear();
       this.comments.addAll(comments);
    }

    public int getCommentsCount(){return commentsCount;}

    public void addComment(Comment comment){}

    public Uri getUri(){return uri;}

    public void setUri(Uri uri){
     this.uri = uri;
    }

    public void setPostId(String id){
     this.postId = id;
    }


}
