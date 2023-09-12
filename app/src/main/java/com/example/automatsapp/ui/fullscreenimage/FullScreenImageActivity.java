package com.example.automatsapp.ui.fullscreenimage;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.automatsapp.R;

public class FullScreenImageActivity extends AppCompatActivity {

    private FullScreenImagePresenter presenter;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        presenter = new FullScreenImagePresenter(this);
    }
    public void loadImage(){
        imageView = findViewById(R.id.imageview_fullscreenimage_image);
        Uri imageUri = getIntent().getData();
        Glide.with(this).load(imageUri).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public boolean isUriNull(){
        return getIntent().getData()==null;
    }

    public void loadDefaultImage() {
        imageView = findViewById(R.id.imageview_fullscreenimage_image);
        imageView.setImageResource(R.drawable.questionmark);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}