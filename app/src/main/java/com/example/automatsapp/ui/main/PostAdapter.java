package com.example.automatsapp.ui.main;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automatsapp.R;
import com.example.automatsapp.model.DateConvertClass;
import com.example.automatsapp.model.Post;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<Post> posts;
    private OnItemClickListener listener;

    public PostAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public void registerListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem_post, parent, false);
        return new PostViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post currentPost = posts.get(position);
        holder.titleTextView.setText(currentPost.getTitle());
        holder.usernameTextView.setText(currentPost.getUser().getUsername());
        holder.descriptionTextView.setText(currentPost.getDescription());
        if (currentPost.getUri()!=null){
            Animation myFadeInAnimation = AnimationUtils.loadAnimation(holder.titleTextView.getContext(), R.anim.image_fadein);
            holder.iconImageView.startAnimation(myFadeInAnimation);
            holder.iconImageView.setImageURI(currentPost.getUri());
            holder.iconImageView.setClipToOutline(true);
        }
       else{
           holder.iconImageView.setImageResource(R.drawable.questionmark);
            holder.iconImageView.setClipToOutline(true);
        }
        holder.dateTextView.setText(DateConvertClass.convertDate(currentPost.getDate()));
        holder.commentsTextView.setText(currentPost.getCommentsCount()+" comments");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });
    }
    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{

        public TextView titleTextView;
        public TextView usernameTextView;
        public TextView descriptionTextView;
        public ImageView iconImageView;
        public TextView dateTextView;
        public TextView commentsTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textview_postitem_title);
            usernameTextView = itemView.findViewById(R.id.textview_postitem_username);
            descriptionTextView = itemView.findViewById(R.id.textview_postitem_description);
            iconImageView = itemView.findViewById(R.id.imageview_postitem_image);
            dateTextView = itemView.findViewById(R.id.textview_postitem_date);
            commentsTextView = itemView.findViewById(R.id.textview_postitem_comments);
        }
    }


}
