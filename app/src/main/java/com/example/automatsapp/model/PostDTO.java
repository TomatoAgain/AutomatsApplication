package com.example.automatsapp.model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;


public class PostDTO {
      private String title;
      private User user;
      private String description;
      private long date;
      private String postId;
      private int commentsCount;
      private ArrayList<Comment> comments;
      public PostDTO(){
      }
      public PostDTO(Post post) {
         this.title = post.getTitle();
         this.description = post.getDescription();
         this.user = post.getUser();
         this.date = post.getDate();
         this.postId = post.getPostId();
         this.commentsCount = post.getCommentsCount();
         this.comments=new ArrayList<>();
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
    public int getCommentsCount(){return commentsCount;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUsername(User user) {
        this.user= user;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

      public void setPostId(String id){
         this.postId = id;
      }

      public void setComments(ArrayList<Comment> comments){
          if (this.comments!=null)
        this.comments.clear();
        this.comments = comments;
      }
}
