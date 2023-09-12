package com.example.automatsapp.ui.main;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automatsapp.R;
import com.example.automatsapp.model.DateConvertClass;
import com.example.automatsapp.model.Notification;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{

    private ArrayList<Notification> notifications;
    private OnNotificationClickListener listener;

    public NotificationAdapter(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }
    public void registerListener(OnNotificationClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View notificationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem_notification, parent, false);
        return new NotificationAdapter.NotificationViewHolder(notificationView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification currentNotification = notifications.get(position);
        holder.descriptionTextView.setText(currentNotification.getUsername()+" commented: "+currentNotification.getDescription());
        if(currentNotification.getImage()!=null){
            holder.profileImageView.setImageURI(currentNotification.getImage());
        }
        else{
            holder.profileImageView.setImageResource(R.drawable.defaultprofilepicture);
        }
        holder.dateTextView.setText(DateConvertClass.convertDate(currentNotification.getDate()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNotificationClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        public TextView descriptionTextView;
        public ImageView profileImageView;
        public TextView dateTextView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.textview_notificationitem_description);
            profileImageView = itemView.findViewById(R.id.imageview_notificationitem_image);
            dateTextView = itemView.findViewById(R.id.textview_notificationitem_date);
        }
    }
}
