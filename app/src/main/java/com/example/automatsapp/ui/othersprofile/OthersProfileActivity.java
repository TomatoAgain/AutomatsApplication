package com.example.automatsapp.ui.othersprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.automatsapp.R;
import com.example.automatsapp.ui.post.PostActivity;

public class OthersProfileActivity extends AppCompatActivity {

    private OthersProfilePresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);
        presenter = new OthersProfilePresenter(this);
    }
    public void loadUserData(){
        Intent intent = getIntent();
        TextView profileTitle = findViewById(R.id.textview_othersprofile_title);
        profileTitle.setText(intent.getStringExtra("username")+"'s Profile");
        TextView username = findViewById(R.id.textview_othersprofile_username);
        username.setText(intent.getStringExtra("username"));
        TextView email = findViewById(R.id.textview_othersprofile_email);
        email.setText(intent.getStringExtra("email"));
        TextView date = findViewById(R.id.textview_othersprofile_createdon);
        date.setText(intent.getStringExtra("date"));
        ImageButton back = findViewById(R.id.imagebutton_othersprofile_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               presenter.goBackButtonClicked();
            }
        });
    }
    public void setProfilePicture(Uri uri) {
        ImageView profilePicture = findViewById(R.id.imageview_othersprofile_avatar);
        profilePicture.setImageURI(uri);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.image_fadein);
        profilePicture.startAnimation(myFadeInAnimation);
    }
    public String getUserId(){
        Intent intent = getIntent();
        return intent.getStringExtra("userId");
    }
    public void navigateToPost(){
        finish();
    }
}