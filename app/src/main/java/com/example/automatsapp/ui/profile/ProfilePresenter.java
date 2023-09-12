package com.example.automatsapp.ui.profile;

import android.content.Intent;
import android.net.Uri;

import com.example.automatsapp.model.Post;
import com.example.automatsapp.model.User;
import com.example.automatsapp.persistence.Repository;

import java.util.ArrayList;

class ProfilePresenter implements Repository.UserInfoListener {
   private ProfileActivity view;
   private boolean isInHistory;
   public ProfilePresenter(ProfileActivity view) {
      this.view = view;
      isInHistory=false;
      Repository repository = Repository.getInstance();
      repository.registerUserListener(this);
      view.setUserData(User.getInstance().getUsername(), User.getInstance().getDate(), User.getInstance().getEmail());
      repository.loadProfilePicture();
      repository.readPostHistory();
      view.loadPage();
      view.registerAsListener();
   }
   @Override
   public void onUserInfoChange(User user) {
   }

   public void goToCameraOrGallery(int resultCode, Intent data, int resultOk, int cameraRequestCode, int galleryRequestCode, int requestCode) {
      if (resultCode== resultOk && data != null){
         if (requestCode == cameraRequestCode){
            view.uploadImageFromCamera(data);

         }
         else if (requestCode == galleryRequestCode){
            view.uploadImageFromGallery(data);
         }
      }
   }
   public void uploadImageFromGallery(Uri uri){
      Repository.getInstance().uploadProfilePictureFromGallery(uri);
   }

   @Override
   public void onProfilePictureChange(Uri uri) {
      view.setProfilePicture(uri);
   }

   @Override
   public void onPostHistoryChange(ArrayList<Post> posts) {
      view.loadPostHistory(posts);
   }

   public void postHistoryClicked() {
      if (isInHistory){
         view.changeToProfile();
         isInHistory=false;
      }
      else{
         view.changeToHistory();
         isInHistory=true;
      }
   }

   public void postClicked(int index) {
      view.navigateToPost(index);
   }
}
