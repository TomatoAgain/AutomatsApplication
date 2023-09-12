package com.example.automatsapp.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automatsapp.NewCommentWorker;
import com.example.automatsapp.model.Notification;
import com.example.automatsapp.model.Post;
import com.example.automatsapp.model.User;
import com.example.automatsapp.ui.profile.ProfileActivity;
import com.example.automatsapp.ui.login.LoginActivity;
import com.example.automatsapp.ui.post.PostActivity;
import com.example.automatsapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity implements OnItemClickListener, OnNotificationClickListener {

    private MainPresenter presenter;
    private Dialog uploadPostDialog;
    private Dialog notificationsDialog;
    private ArrayList<Post> posts;
    private ArrayList<Post> filteredPosts;
    private PostAdapter postAdapter;
    private ArrayList<Notification> notifications;
    private NotificationAdapter notificationAdapter;
    public final int GALLERY_REQUEST_CODE = 0;
    private Uri postImage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this);
    }

    public PostAdapter getAdapter() {
        return postAdapter;
    }

    public void onItemClick(int index){
       Intent intent = new Intent(this, PostActivity.class);
       intent.putExtra("id", filteredPosts.get(index).getPostId());
       startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.checkNotNull(resultCode, data, RESULT_OK);
    }
    public void uploadImage(Intent data){
        Uri uri = data.getData();
        ImageView imageView = uploadPostDialog.findViewById(R.id.imageview_postdialog_image);
        imageView.setImageURI(uri);
        postImage = uri;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem logoutButton = menu.findItem(R.id.menu_logout);
        logoutButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return false;
            }
        });
        MenuItem profileButton = menu.findItem(R.id.menu_profile);
        profileButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return false;
            }
        });
        MenuItem notificationsItem = menu.findItem(R.id.action_notifications);
        notificationsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                notificationsItem.setIcon(R.drawable.ic_baseline_notifications_24);
                presenter.notificationsButtonClicked();
                return false;
            }
        });

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filteredPosts.clear();
                for (int i = 0; i < posts.size(); i++){
                      if (posts.get(i).getTitle().contains(s)){
                          filteredPosts.add(posts.get(i));
                      }
                }
                postAdapter.notifyDataSetChanged();
                return false;
            }
        });
        if (User.getInstance().getHasNewNotifications()){
             notificationsItem.setIcon(R.drawable.ic_baseline_notifications_active_24);
        }
        return true;
    }
    public void loadPage(){
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0069c0")));
        RecyclerView recyclerView = findViewById(R.id.recyclerview_main_posts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        posts = new ArrayList<>();
        filteredPosts = new ArrayList<>();
        postAdapter = new PostAdapter(filteredPosts);
        recyclerView.setAdapter(postAdapter);
        uploadPostDialog = new Dialog(this);
        uploadPostDialog.setContentView(R.layout.upload_post);
        notificationsDialog = new Dialog(this);
        notificationsDialog.setContentView(R.layout.dialog_notifications);
        Button button = findViewById(R.id.button_main_post);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onAddPostClicked();
            }
        });
        notifications = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notifications);
        RecyclerView notificationsRecyclerView = notificationsDialog.findViewById(R.id.recyclerview_notificationsdialog_notifications);
        RecyclerView.LayoutManager notificationsLayoutManager = new LinearLayoutManager(this);
        notificationsRecyclerView.setLayoutManager(notificationsLayoutManager);
        notificationsRecyclerView.setAdapter(notificationAdapter);
    }
    public void openPostDialog(){
        uploadPostDialog.show();
        postImage = null;
        ImageView postImage = uploadPostDialog.findViewById(R.id.imageview_postdialog_image);
        postImage.setImageResource(R.drawable.plus);
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });
        Button upload = uploadPostDialog.findViewById(R.id.button_postdialog_post);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onUploadClicked();
            }
        });
    }

    public void uploadPost(){
        EditText postTitle = uploadPostDialog.findViewById(R.id.edittext_dialog_title);
        EditText postDescription = uploadPostDialog.findViewById(R.id.edittext_postdialog_description);
        Post post = new Post(postTitle.getText().toString(), User.getInstance(), postDescription.getText().toString(), System.currentTimeMillis(), 0, postImage);
        uploadPostDialog.dismiss();
        postTitle.setText("");
        postDescription.setText("");
        presenter.uploadPost(post);
    }
    public void showPosts() {
        postAdapter.notifyDataSetChanged();
    }

    public void loadPosts(ArrayList<Post> postArrayList){
        posts.clear();
        posts.addAll(postArrayList);
        filteredPosts.clear();
        filteredPosts.addAll(posts);
        postAdapter.notifyDataSetChanged();
    }
    public Uri getPostImage(){
        return this.postImage;
    }


    public void OpenNotifications() {
        notificationsDialog.show();
        TextView empty = notificationsDialog.findViewById(R.id.textview_notificationdialog_empty);
        if(notifications.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
        }
        else{
            empty.setVisibility(View.INVISIBLE);
        }
    }

    public void loadNotifications(ArrayList<Notification> notifications) {
        this.notifications.clear();
        this.notifications.addAll(notifications);
        notificationAdapter.notifyDataSetChanged();
    }

    public void showNotifications() {
        notificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNotificationClick(int index) {
        presenter.onNotificationClicked(index);
    }

    public void moveToPost(int index) {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("id", notifications.get(index).getPostId());
        startActivity(intent);
    }

    public void setListeners() {
        postAdapter.registerListener(this);
        notificationAdapter.registerListener(this);
    }

    public void activateWorker() {
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(NewCommentWorker.class, 1, TimeUnit.HOURS).build();
        WorkManager.getInstance(this).enqueue(periodicWorkRequest);
    }

    public String getPostTitle() {
        EditText title = uploadPostDialog.findViewById(R.id.edittext_dialog_title);
        return title.getText().toString();
    }
    public String getPostDescription() {
        EditText title = uploadPostDialog.findViewById(R.id.edittext_postdialog_description);
        return title.getText().toString();
    }

    public void notifyTextboxEmpty() {
        Toast.makeText(this, "Title and description are mandatory", LENGTH_LONG).show();
    }
}