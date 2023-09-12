package com.example.automatsapp.model;

import android.net.Uri;

public class Comment {
   private User user;
   private String description;
   private int commentIndent;
   private long date;
   private int commentIndex;
   private Uri commentImage;
   public Comment(){

   }
   public Comment(User user, String description, long date, int commentIndent, int commentIndex, Uri uri) {
      this.description = description;
      this.commentIndent = commentIndent;
      this.user = user;
      this.date = date;
      this.commentIndex = commentIndex;
      this.commentImage = uri;
   }

   public User getUser() {
      return user;
   }

   public String getDescription() {
      return description;
   }

   public int getCommentIndent() {
      return commentIndent;
   }

   public void setCommentIndent(int commentIndent) {
      this.commentIndent = commentIndent;
   }

   public Uri getCommentImage() {
      return commentImage;
   }

   public void setCommentImage(Uri commentImage) {
      this.commentImage = commentImage;
   }

   public long getDate(){return date;}

   public void setUser(User user) {
      this.user = user;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setImage(int commentIndent) {
      this.commentIndent= commentIndent;
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
}
