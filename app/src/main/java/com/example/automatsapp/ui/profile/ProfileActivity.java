package com.example.automatsapp.ui.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.automatsapp.NewCommentWorker;
import com.example.automatsapp.R;
import com.example.automatsapp.model.Post;
import com.example.automatsapp.ui.automatmaker.AutomatMakerActivity;
import com.example.automatsapp.ui.main.OnItemClickListener;
import com.example.automatsapp.ui.main.PostAdapter;
import com.example.automatsapp.ui.post.PostActivity;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements OnItemClickListener {

    private ProfilePresenter presenter;
    public final int GALLERY_REQUEST_CODE = 1;
    public final int CAMERA_REQUEST_CODE = 0;
    private PostAdapter historyAdapter;
    private ArrayList<Post> postHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        presenter = new ProfilePresenter(this);
    }

    @Override
    public void onItemClick(int index) {
       presenter.postClicked(index);
    }

    public void loadPage(){
        RecyclerView recyclerView = findViewById(R.id.recyclerview_profile_posthistory);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        postHistory = new ArrayList<>();
        historyAdapter = new PostAdapter(postHistory);
        recyclerView.setAdapter(historyAdapter);
        recyclerView.setVisibility(View.GONE);
        TextView historyTitle = findViewById(R.id.textview_profilehistory_title);
        historyTitle.setVisibility(View.GONE);
        ImageButton cameraButton = findViewById(R.id.imagebutton_profile_camera);
        ImageButton galleryButton = findViewById(R.id.imagebutton_profile_gallery);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });
        ImageButton backButton = findViewById(R.id.imagebutton_profile_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button postHistory = findViewById(R.id.button_profile_history);
        postHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.postHistoryClicked();
            }
        });
        Button maker = findViewById(R.id.button_profile_automats);
        maker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, AutomatMakerActivity.class));
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.goToCameraOrGallery(resultCode, data, RESULT_OK, CAMERA_REQUEST_CODE, GALLERY_REQUEST_CODE, requestCode);
    }
    public void uploadImageFromCamera(Intent data){
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        ImageView imageView = findViewById(R.id.imageview_profile_avatar);
        imageView.setImageBitmap(bitmap);
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, null, null));
        presenter.uploadImageFromGallery(uri);
    }
    public void uploadImageFromGallery(Intent data){
        Uri uri = data.getData();
        ImageView imageView = findViewById(R.id.imageview_profile_avatar);
        imageView.setImageURI(uri);
        presenter.uploadImageFromGallery(uri);
    }
    public void setUserData(String username, String date, String email){
        TextView usernameTextView = findViewById(R.id.textview_profile_username);
        usernameTextView.setText(username);
        TextView dateTextView = findViewById(R.id.textview_profile_createdon);
        dateTextView.setText(date);
        TextView emailTextView = findViewById(R.id.textview_profile_email);
        emailTextView.setText(email);
    }

    public void setProfilePicture(Uri uri) {
        ImageView profilePicture = findViewById(R.id.imageview_profile_avatar);
        profilePicture.setImageURI(uri);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.image_fadein);
        profilePicture.startAnimation(myFadeInAnimation);
    }

    public void loadPostHistory(ArrayList<Post> posts) {
        this.postHistory.clear();
        this.postHistory.addAll(posts);
        historyAdapter.notifyDataSetChanged();

    }
    public void changeToHistory(){
        ConstraintSet set = new ConstraintSet();
        set.clone(this, R.layout.activity_profilehistory);
        set.applyTo(getRootLayout());
        TransitionManager.beginDelayedTransition(getRootLayout());
        Button history = findViewById(R.id.button_profile_history);
        history.setText("Back to Profile");
        TextView username = findViewById(R.id.textview_profile_username);
        username.setVisibility(View.GONE);
        TextView email = findViewById(R.id.textview_profile_email);
        email.setVisibility(View.GONE);
        TextView date = findViewById(R.id.textview_profile_createdon);
        date.setVisibility(View.GONE);
        TextView usernameTitle = findViewById(R.id.textview_profile_usernametitle);
        usernameTitle.setVisibility(View.GONE);
        TextView emailTitle = findViewById(R.id.textview_profile_emailtitle);
        emailTitle.setVisibility(View.GONE);
        TextView dateTitle = findViewById(R.id.textview_profile_datetitle);
        dateTitle.setVisibility(View.GONE);
        CardView wrapper = findViewById(R.id.cardview_profile_wrapper);
        wrapper.setVisibility(View.GONE);
        Button myAutomats = findViewById(R.id.button_profile_automats);
        myAutomats.setVisibility(View.GONE);
        TextView postHistoryTitle = findViewById(R.id.textview_profilehistory_title);
        postHistoryTitle.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_profile_posthistory);
        recyclerView.setVisibility(View.VISIBLE);

    }
    public void changeToProfile(){
        ConstraintSet set = new ConstraintSet();
        set.clone(this, R.layout.activity_profile);
        set.applyTo(getRootLayout());
        TransitionManager.beginDelayedTransition(getRootLayout());
        Button history = findViewById(R.id.button_profile_history);
        history.setText("My Post History");
        TextView username = findViewById(R.id.textview_profile_username);
        username.setVisibility(View.VISIBLE);
        TextView email = findViewById(R.id.textview_profile_email);
        email.setVisibility(View.VISIBLE);
        TextView date = findViewById(R.id.textview_profile_createdon);
        date.setVisibility(View.VISIBLE);
        TextView usernameTitle = findViewById(R.id.textview_profile_usernametitle);
        usernameTitle.setVisibility(View.VISIBLE);
        TextView emailTitle = findViewById(R.id.textview_profile_emailtitle);
        emailTitle.setVisibility(View.VISIBLE);
        TextView dateTitle = findViewById(R.id.textview_profile_datetitle);
        dateTitle.setVisibility(View.VISIBLE);
        CardView wrapper = findViewById(R.id.cardview_profile_wrapper);
        wrapper.setVisibility(View.VISIBLE);
        Button myAutomats = findViewById(R.id.button_profile_automats);
        myAutomats.setVisibility(View.VISIBLE);
        TextView postHistoryTitle = findViewById(R.id.textview_profilehistory_title);
        postHistoryTitle.setVisibility(View.GONE);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_profile_posthistory);
        recyclerView.setVisibility(View.GONE);
    }

    public void registerAsListener() {
        historyAdapter.registerListener(this);
    }
    public ConstraintLayout getRootLayout(){
        ConstraintLayout root = findViewById(R.id.layout_profile_root);
        return root;
    }

    public void navigateToPost(int index) {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("id", postHistory.get(index).getPostId());
        startActivity(intent);
    }
}