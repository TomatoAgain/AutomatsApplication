package com.example.automatsapp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class User {
   private static User instance = null;
   private String username;
   private String email;
   private String date;
   private String userId;
   private ArrayList<NotificationDTO> notifications;
   private boolean hasNewNotifications;

   private User() {
        this.username = "";
        this.date="";
        this.email = "";
        this.userId="";
        this.notifications = new ArrayList<>();
        this.hasNewNotifications = false;

   }
   public static User getInstance(){
      if (instance == null){
         instance = new User();
      }
      return instance;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getDate() {
      return date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getUserId() {
      return userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }
   public void setCurrentDate(){
      Date c = Calendar.getInstance().getTime();
      SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
      this.date = df.format(c);
   }

   public ArrayList<NotificationDTO> getNotifications() {
      return notifications;
   }

   public void setNotifications(ArrayList<NotificationDTO> notifications) {
      this.notifications = notifications;
   }
   public void addNotification(Notification notification){
      this.notifications.add(new NotificationDTO(notification.getDescription(), notification.getUsername(), "placeholder", notification.getDate(), notification.getPostId()));
   }

   public boolean getHasNewNotifications() {
      return hasNewNotifications;
   }

   public void setHasNewNotifications(boolean hasNewNotifications) {
      this.hasNewNotifications = hasNewNotifications;
   }
}
