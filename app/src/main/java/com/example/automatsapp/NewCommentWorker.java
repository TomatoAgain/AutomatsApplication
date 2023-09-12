package com.example.automatsapp;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.automatsapp.model.User;
import com.example.automatsapp.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewCommentWorker extends Worker {;
    private static final String CHANNEL_ID = "myChannel";
    private static final int NOTIFICATION_ID = 0;
    private static final int INTENT_REQUEST_CODE = 1;

    public NewCommentWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference("users/"+FirebaseAuth.getInstance().getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user.getHasNewNotifications()) {
                        if ((ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "notifications", NotificationManager.IMPORTANCE_HIGH);
                                channel.setDescription("Channelis");
                                NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
                                notificationManager.createNotificationChannel(channel);
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), INTENT_REQUEST_CODE, intent, FLAG_IMMUTABLE);
                                Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID).setSmallIcon(R.drawable.appiconnew).setContentText("You have unchecked notifications!").setContentIntent(pendingIntent).build();
                                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                                notificationManagerCompat.notify(NOTIFICATION_ID, notification);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return Result.success();
    }

    @Override
    public void onStopped() {
        super.onStopped();
    }
}
