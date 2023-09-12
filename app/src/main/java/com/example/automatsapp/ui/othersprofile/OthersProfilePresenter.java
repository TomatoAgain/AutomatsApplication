package com.example.automatsapp.ui.othersprofile;

import android.net.Uri;

import com.example.automatsapp.persistence.Repository;

public class OthersProfilePresenter implements Repository.OtherUserInfoListener {
    private OthersProfileActivity view;
    public OthersProfilePresenter(OthersProfileActivity view){
        this.view = view;
        view.loadUserData();
        Repository.getInstance().registerOtherUserListener(this);
        Repository.getInstance().loadOtherUserProfilePicture(view.getUserId());
    }

    @Override
    public void onProfilePictureChange(Uri uri) {
        view.setProfilePicture(uri);
    }

    public void goBackButtonClicked() {
        view.navigateToPost();
    }
}
