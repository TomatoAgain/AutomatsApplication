package com.example.automatsapp.ui.post;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.automatsapp.model.Comment;
import com.example.automatsapp.R;
import com.example.automatsapp.model.DateConvertClass;
import com.example.automatsapp.model.Post;
import com.example.automatsapp.model.User;
import com.example.automatsapp.ui.fullscreenimage.FullScreenImageActivity;
import com.example.automatsapp.ui.othersprofile.OthersProfileActivity;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity implements onCommentAttributesClickListener {

    private PostPresenter presenter;
    private ArrayList<Comment> comments;
    private CommentAdapter adapter;
    private Dialog dialog;
    public final int GALLERY_REQUEST_CODE = 0;
    private Post parentPost;
    private Uri image = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        presenter = new PostPresenter(this);
    }
    public void loadPage(){
        RecyclerView commentSection = findViewById(R.id.recyclerview_comments);
        commentSection.setVisibility(View.INVISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        commentSection.setLayoutManager(layoutManager);
        comments = new ArrayList<>();
        adapter = new CommentAdapter(comments);
        adapter.registerOnReplyClickListener(this);
        commentSection.setAdapter(adapter);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.upload_comment);
        Button comment = findViewById(R.id.button_post_comment);
        comment.setVisibility(View.GONE);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadComment(comments.size(), 0, getPosterId());
            }
        });

        Button commentButton = findViewById(R.id.button_post_comments);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.changeLayout();
            }
        });
        TextView usernameTextView = findViewById(R.id.textview_post_username);
        usernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.usernameClicked();
            }
        });
        ImageView postImage = findViewById(R.id.imageview_post_image);
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.postImageClicked();
            }
        });
    }
    public void uploadComment(int index, int indent, String commentedTo){
        dialog.show();
        image = null;
        ImageView commentImage = dialog.findViewById(R.id.imageview_commentdialog_image);
        commentImage.setImageResource(R.drawable.plus);
        commentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });
        Button upload = dialog.findViewById(R.id.button_commentdialog_post);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText description = dialog.findViewById(R.id.edittext_commentdialog_description);
                presenter.uploadComment(new Comment(User.getInstance(), description.getText().toString(), System.currentTimeMillis(), indent, index, image), comments.size(), commentedTo);
                dialog.dismiss();
                description.setText("");
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.checkNotNull(resultCode, data, RESULT_OK);
    }
    public void uploadImage(Intent data){
        Uri uri = data.getData();
        ImageView commentImageView = dialog.findViewById(R.id.imageview_commentdialog_image);
        commentImageView.setImageURI(uri);
        image = uri;
    }
    public void goToComments(){
        ConstraintLayout rootLayout = findViewById(R.id.layout_post_root);
        ConstraintSet set = new ConstraintSet();
            set.clone(this, R.layout.post_comments);
            set.applyTo(rootLayout);
            Button comments = findViewById(R.id.button_post_comments);
            comments.setText("Back to post");
            Button comment = findViewById(R.id.button_post_comment);
            comment.setVisibility(View.VISIBLE);
            TextView description = findViewById(R.id.textview_post_description);
            description.setVisibility(View.INVISIBLE);
            RecyclerView commentSection = findViewById(R.id.recyclerview_comments);
            commentSection.setVisibility(View.VISIBLE);
            TransitionManager.beginDelayedTransition(rootLayout);
        }
        public void goToPost(){
            ConstraintLayout rootLayout = findViewById(R.id.layout_post_root);
            ConstraintSet set = new ConstraintSet();
            set.clone(this, R.layout.activity_post);
            set.applyTo(rootLayout);
            Button comments = findViewById(R.id.button_post_comments);
            comments.setText("Comments");
            Button comment = findViewById(R.id.button_post_comment);
            comment.setVisibility(View.GONE);
            TransitionManager.beginDelayedTransition(rootLayout);
        }

    public void loadPostData(){
        TextView title = findViewById(R.id.textview_post_title);
        title.setText(parentPost.getTitle());
        TextView description = findViewById(R.id.textview_post_description);
        Log.e("helooohofohohohoh123", parentPost.getDescription());
        description.setText(parentPost.getDescription());
        TextView username = findViewById(R.id.textview_post_username);
        username.setText(parentPost.getUser().getUsername());
        TextView date = findViewById(R.id.textview_post_date);
        date.setText(DateConvertClass.convertDate(parentPost.getDate()));
        ImageView image = findViewById(R.id.imageview_post_image);
        image.setImageURI(parentPost.getUri());
    }

    public String getPostId() {
        Intent intent = getIntent();
        String postId = intent.getStringExtra("id");
        return postId;
    }
    public void setParentPost(Post post){
        this.parentPost= post;
    }

    public void setPostImage(){
        ImageView image = findViewById(R.id.imageview_post_image);
        image.setImageURI(parentPost.getUri());
    }

    public void loadImagelessPostData() {
        TextView title = findViewById(R.id.textview_post_title);
        title.setText(parentPost.getTitle());
        TextView description = findViewById(R.id.textview_post_description);
        description.setText(parentPost.getDescription());
        Log.e("helooohofohohohoh123",parentPost.getDescription());
        TextView username = findViewById(R.id.textview_post_username);
        username.setText(parentPost.getUser().getUsername());
        TextView date = findViewById(R.id.textview_post_date);
        date.setText(DateConvertClass.convertDate(parentPost.getDate()));
        ImageView image = findViewById(R.id.imageview_post_image);
        image.setImageResource(R.drawable.questionmark);
    }

    public void loadComments(ArrayList<Comment> commentArrayList){
        comments.clear();
        comments.addAll(commentArrayList);
        adapter.notifyDataSetChanged();
    }
    public void updateUi() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onReplyClicked(int index, int indent) {
        uploadComment(index, indent, comments.get(index-1).getUser().getUserId());
    }
    public void moveToOtherProfile(){
        Intent intent = new Intent(this, OthersProfileActivity.class);
        intent.putExtra("userId", parentPost.getUser().getUserId());
        intent.putExtra("username", parentPost.getUser().getUsername());
        intent.putExtra("date", parentPost.getUser().getDate());
        intent.putExtra("email", parentPost.getUser().getEmail());
        startActivity(intent);
    }

    @Override
    public void onUsernameClicked(int index) {
        Intent intent = new Intent(this, OthersProfileActivity.class);
        intent.putExtra("userId", comments.get(index).getUser().getUserId());
        intent.putExtra("username", comments.get(index).getUser().getUsername());
        intent.putExtra("date", comments.get(index).getUser().getDate());
        intent.putExtra("email", comments.get(index).getUser().getEmail());
        startActivity(intent);

    }

    @Override
    public void onImageClicked(int index) {
        Intent intent = new Intent(this, FullScreenImageActivity.class);
        intent.setData(comments.get(index).getCommentImage());
        startActivity(intent);
    }

    public void openFullScreenImage() {
        Intent intent = new Intent(this, FullScreenImageActivity.class);
        intent.setData(parentPost.getUri());
        startActivity(intent);
    }
    public String getPosterId(){
        return this.parentPost.getUser().getUserId();
    }
}
