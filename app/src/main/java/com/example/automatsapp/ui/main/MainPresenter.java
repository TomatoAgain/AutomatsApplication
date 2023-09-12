package com.example.automatsapp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.example.automatsapp.model.Notification;
import com.example.automatsapp.model.Post;
import com.example.automatsapp.model.User;
import com.example.automatsapp.persistence.Repository;

import java.util.ArrayList;

public class MainPresenter implements Repository.PostListListener, Repository.NotificationListListener{

    private MainActivity view;

    public MainPresenter(MainActivity activity){
        this.view=activity;
        view.loadPage();
        view.setListeners();
        Repository.getInstance().registerPostListListener(this);
        Repository.getInstance().registerNotificationListListener(this);
        Repository.getInstance().readPosts();
        Repository.getInstance().readNotifications();
        view.showPosts();
        if (!view.getSharedPreferences("WorkerActivation", Context.MODE_PRIVATE).getBoolean("isActivated", false) && (ActivityCompat.checkSelfPermission(view.getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)){
            SharedPreferences.Editor editor = view.getSharedPreferences("WorkerActivation", Context.MODE_PRIVATE).edit();
            editor.putBoolean("isActivated", true);
            editor.commit();
            view.activateWorker();
        }
    }
    public void checkNotNull(int resultCode, Intent data, int resultOk) {
        if (resultCode == resultOk && data!=null) {
            view.uploadImage(data);
        }
    }
    public void uploadPost(Post post){
        Repository.getInstance().uploadPost(post);
    }
    public void onUploadClicked(){
        if (view.getPostTitle().length()>0 && view.getPostDescription().length()>0) {
            view.uploadPost();
        }
        else
        view.notifyTextboxEmpty();
    }

    @Override
    public void onPostListChange(ArrayList<Post> posts) {
        view.loadPosts(posts);
    }

    public void onAddPostClicked() {
        view.openPostDialog();
    }

    @Override
    public void setUi() {
        view.showPosts();
    }

    public void notificationsButtonClicked(){
        view.OpenNotifications();
        Repository.getInstance().updateNotificationsStatus();
    }

    @Override
    public void onNotificationListChange(ArrayList<Notification> notifications) {
        view.loadNotifications(notifications);
        for(Notification notification : notifications)
         User.getInstance().addNotification(notification);
    }

    @Override
    public void updateNotificationsUi() {
         view.showNotifications();
    }

    public void onNotificationClicked(int index) {
        view.moveToPost(index);
    }
}
