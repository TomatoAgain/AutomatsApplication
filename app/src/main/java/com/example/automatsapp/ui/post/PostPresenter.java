package com.example.automatsapp.ui.post;

import android.content.Intent;
import android.util.Log;

import com.example.automatsapp.model.Comment;
import com.example.automatsapp.model.Notification;
import com.example.automatsapp.model.Post;
import com.example.automatsapp.model.User;
import com.example.automatsapp.persistence.Repository;

import java.util.ArrayList;

public class PostPresenter implements Repository.PostInfoListener, Repository.CommentListListener {
    private PostActivity view;
    private boolean inComments;
    public PostPresenter(PostActivity view){
        this.view=view;
        inComments=false;
        view.loadPage();
        Repository.getInstance().registerPostInfoListener(this);
        Repository.getInstance().readPostData(view.getPostId());
        Repository.getInstance().registerCommentListListener(this);
        Repository.getInstance().readComments(view.getPostId());
    }
    public void changeLayout(){
        if (inComments){
            inComments=false;
            view.goToPost();
        }
        else{
            inComments=true;
            view.goToComments();
        }
    }
    public void checkNotNull(int resultCode, Intent data, int resultOk) {
        if (resultCode == resultOk && data!=null) {
            view.uploadImage(data);
        }
    }
    public void uploadComment(Comment comment, int index, String posterId){
        Repository.getInstance().uploadComment(comment, view.getPostId(), index);
        Repository.getInstance().addNotification(new Notification(comment.getDescription(), comment.getUser().getUsername(), comment.getCommentImage(), comment.getDate(), view.getPostId()), posterId, User.getInstance().getUserId(), User.getInstance().getNotifications().size());
    }
    @Override
    public void onPostInfoChange(Post post) {
        view.setParentPost(post);
        if (post.getUri()!=null)
            view.loadPostData();
        else{
            view.loadImagelessPostData();
        }
    }

    @Override
    public void setUi() {
        view.setPostImage();
    }

    @Override
    public void onCommentListChange(ArrayList<Comment> comments) {
        view.loadComments(comments);
    }

    @Override
    public void updateCommentsUi() {
      view.updateUi();
    }

    public void usernameClicked(){
        view.moveToOtherProfile();
    }

    public void postImageClicked() {
        view.openFullScreenImage();
    }

}
