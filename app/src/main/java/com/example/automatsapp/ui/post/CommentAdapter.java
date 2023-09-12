package com.example.automatsapp.ui.post;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automatsapp.R;
import com.example.automatsapp.model.Comment;
import com.example.automatsapp.model.DateConvertClass;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    public static final int INDENT_SIZE = 50;
    private ArrayList<Comment> comments;
    private onCommentAttributesClickListener listener;

    public CommentAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void registerOnReplyClickListener(onCommentAttributesClickListener listener){
        this.listener=listener;
    }
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem_comment, parent, false);
        return new CommentViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment currentComment = comments.get(position);
        holder.parentLayout.setLayoutParams(new ConstraintLayout.LayoutParams(new ConstraintLayout.LayoutParams( 1080 -currentComment.getCommentIndent()* INDENT_SIZE, ViewGroup.LayoutParams.WRAP_CONTENT)));
        holder.usernameTextView.setText(currentComment.getUser().getUsername());
        holder.usernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onUsernameClicked(holder.getAdapterPosition());
            }
        });
        holder.descriptionTextView.setText(currentComment.getDescription());
        if (currentComment.getCommentImage()==null) {
            holder.iconImageView.setImageResource(R.drawable.questionmark);
            holder.iconImageView.setClipToOutline(true);
        }
        else
            holder.iconImageView.setImageURI(currentComment.getCommentImage());
          holder.iconImageView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  listener.onImageClicked(holder.getAdapterPosition());
              }
          });
            holder.dateTextView.setText(DateConvertClass.convertDate(currentComment.getDate()));
        holder.replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onReplyClicked(comments.indexOf(currentComment)+1, currentComment.getCommentIndent()+1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder{

        public TextView usernameTextView;
        public TextView descriptionTextView;
        public ImageView iconImageView;
        public TextView dateTextView;
        public ConstraintLayout parentLayout;
        public Button replyButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.textview_commentitem_username);
            descriptionTextView = itemView.findViewById(R.id.textview_commentitem_description);
            iconImageView = itemView.findViewById(R.id.imageview_commentitem_image);
            dateTextView = itemView.findViewById(R.id.textview_commentitem_date);
            parentLayout = itemView.findViewById(R.id.constraintlayout_parent);
            replyButton = itemView.findViewById(R.id.button_commentitem_reply);
        }
    }


}
