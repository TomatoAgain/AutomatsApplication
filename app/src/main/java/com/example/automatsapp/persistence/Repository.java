package com.example.automatsapp.persistence;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.automatsapp.model.Comment;
import com.example.automatsapp.model.CommentDTO;
import com.example.automatsapp.model.Notification;
import com.example.automatsapp.model.NotificationDTO;
import com.example.automatsapp.model.Post;
import com.example.automatsapp.model.PostDTO;
import com.example.automatsapp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Repository {

    public interface UserInfoListener {
        void onUserInfoChange(User user);
        void onProfilePictureChange(Uri uri);
        void onPostHistoryChange(ArrayList<Post> posts);
    }
    public interface PostListListener{
        void onPostListChange(ArrayList<Post> posts);
        void setUi();
    }
    public interface PostInfoListener{
        void onPostInfoChange(Post post);
        void setUi();
    }
    public interface CommentListListener{
        void onCommentListChange(ArrayList<Comment> comments);
        void updateCommentsUi();
    }
    public interface OtherUserInfoListener{
        void onProfilePictureChange(Uri uri);
    }
    public interface NotificationListListener{
        void onNotificationListChange(ArrayList<Notification> notifications);
        void updateNotificationsUi();
    }
    public interface LoginListener{
        void onDataReceived();
    }

    private static Repository instance = null;
    private UserInfoListener userInfoListener;
    private PostListListener postListListener;
    private PostInfoListener postInfoListener;
    private CommentListListener commentListListener;
    private OtherUserInfoListener otherUserInfoListener;
    private NotificationListListener notificationListListener;
    private LoginListener loginListener;

    private final int POST_LIST_LISTENER_REQUEST_CODE = 0;
    private final int POST_INFO_LISTENER_REQUEST_CODE = 1;

    private Repository() {
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public void createUser(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userData = database.getReference("users/" + FirebaseAuth.getInstance().getUid());
        userData.setValue(user);
    }

    public void uploadPost(Post post){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postData = database.getReference("posts").push();
        post.setPostId(postData.getKey()+"");
        PostDTO postDTO = new PostDTO(post);
        postData.setValue(postDTO);
        uploadPostImage(post.getUri(), post.getPostId());
    }
    public void uploadComment(Comment comment, String postId, int commentsCount){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postCommentCount = database.getReference("posts/"+postId+"/commentsCount");
        postCommentCount.setValue(commentsCount+1);
        DatabaseReference postData = database.getReference("posts/" + postId +"/comments/"+commentsCount);
        CommentDTO commentDTO = new CommentDTO(comment, commentsCount);
        postData.setValue(commentDTO);
        uploadCommentImage(comment.getCommentImage(), postId, commentsCount);
    }
    public void addNotification(Notification notification, String userId, String commenterId, int index){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference indexReference = database.getReference("users/"+userId);
        indexReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                DatabaseReference databaseReference = database.getReference("users/"+user.getUserId()+"/notifications/" + user.getNotifications().size());
                databaseReference.setValue(new NotificationDTO(notification.getDescription(), notification.getUsername(), commenterId, notification.getDate(), notification.getPostId()));
                DatabaseReference hasNewNotifsReference = database.getReference("users/"+user.getUserId()+"/hasNewNotifications");
                hasNewNotifsReference.setValue(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void setUser(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userData = database.getReference("users/" + FirebaseAuth.getInstance().getUid());
        userData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                User.getInstance().setUsername(user.getUsername());
                User.getInstance().setEmail(user.getEmail());
                User.getInstance().setDate(user.getDate());
                User.getInstance().setUserId(user.getUserId());
                User.getInstance().setNotifications(user.getNotifications());
                User.getInstance().setHasNewNotifications(user.getHasNewNotifications());
                Log.e("HELLOLOOOWSDOSOD", ""+user.getHasNewNotifications());
                loginListener.onDataReceived();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void registerUserListener(UserInfoListener listener) {
        this.userInfoListener = listener;
    }
    public void registerPostListListener(PostListListener listener){
        this.postListListener = listener;
    }
    public void registerPostInfoListener(PostInfoListener listener){
        this.postInfoListener = listener;
    }
    public void registerCommentListListener(CommentListListener listener){
        this.commentListListener = listener;
    }
    public void registerOtherUserListener(OtherUserInfoListener listener){
        this.otherUserInfoListener = listener;
    }
    public void registerNotificationListListener(NotificationListListener listener){
        this.notificationListListener = listener;
    }
    public void registerLoginListener(LoginListener listener){
        this.loginListener = listener;
    }


    public void loadOtherUserProfilePicture(String userId){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profilepictures/"+userId);
        try {
            File localFile = File.createTempFile("profilepictures", "jpg");
            final Uri fileUri = Uri.fromFile(localFile);
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    otherUserInfoListener.onProfilePictureChange(fileUri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void uploadProfilePictureFromGallery(Uri uri){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profilepictures/"+FirebaseAuth.getInstance().getUid());
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        });
    }
    public void loadProfilePicture(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profilepictures/"+FirebaseAuth.getInstance().getUid());
        try {
            File localFile = File.createTempFile("profilepictures", "jpg");
            final Uri fileUri = Uri.fromFile(localFile);
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                     userInfoListener.onProfilePictureChange(fileUri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readPostHistory(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://automatsappuserauthentication-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference postsData = firebaseDatabase.getReference("posts");
        ArrayList<Post> posts = new ArrayList<>();
        postsData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                posts.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        PostDTO postDTO = dsp.getValue(PostDTO.class);
                        if (postDTO.getUser().getUserId().equals(User.getInstance().getUserId())) {
                            Post post = new Post(postDTO.getTitle(), postDTO.getUser(), postDTO.getDescription(), postDTO.getDate(), postDTO.getCommentsCount(), null);
                            if (postDTO.getComments() != null)
                                for (int i = 0; i < postDTO.getComments().size(); i++) {
                                    post.addComment(postDTO.getComments().get(i));
                                }
                            post.setPostId(postDTO.getPostId());
                            setPostImage(post, POST_LIST_LISTENER_REQUEST_CODE);
                            posts.add(0, post);
                        }

                    }
                }
                userInfoListener.onPostHistoryChange(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readPosts(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://automatsappuserauthentication-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference postsData = firebaseDatabase.getReference("posts");
        ArrayList<Post> posts = new ArrayList<>();
        postsData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                posts.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        PostDTO postDTO = dsp.getValue(PostDTO.class);
                        Post post = new Post(postDTO.getTitle(), postDTO.getUser(), postDTO.getDescription(), postDTO.getDate(), postDTO.getCommentsCount(), null);
                        if (postDTO.getComments()!=null)
                        for (int i = 0; i < postDTO.getComments().size(); i++){
                            post.addComment(postDTO.getComments().get(i));
                        }
                        post.setPostId(postDTO.getPostId());
                        setPostImage(post, POST_LIST_LISTENER_REQUEST_CODE);
                        posts.add(0, post);

                    }
                }
                postListListener.onPostListChange(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readNotifications(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://automatsappuserauthentication-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference notificationsData = firebaseDatabase.getReference("users/"+User.getInstance().getUserId()+"/notifications");
        ArrayList<Notification> notifications = new ArrayList<>();
        notificationsData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifications.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        NotificationDTO notificationDTO = dsp.getValue(NotificationDTO.class);
                        Notification notification = new Notification(notificationDTO.getDescription(), notificationDTO.getUsername(), null, notificationDTO.getDate(), notificationDTO.getPostId());
                        setNotificationImage(notification, notificationDTO.getImage());
                        notifications.add(0, notification);
                    }
                }
                notificationListListener.onNotificationListChange(notifications);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void uploadPostImage(Uri uri, String postId){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/"+ postId);
        if (uri!=null)
          storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        });
    }
    public void setNotificationImage(Notification notification, String userId){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profilepictures/"+userId);
        try {
            File localFile = File.createTempFile("profilepictures", "jpg");
            final Uri fileUri = Uri.fromFile(localFile);
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    notification.setImage(fileUri);
                    notificationListListener.updateNotificationsUi();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setPostImage(Post post, int requestCode){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/"+post.getPostId());
        try {
            File localFile = File.createTempFile("images", "jpg");
            final Uri fileUri = Uri.fromFile(localFile);
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    post.setUri(fileUri);
                    Log.e(Repository.this.getClass().getSimpleName(), "successful download");
                    if (requestCode == POST_LIST_LISTENER_REQUEST_CODE)
                    postListListener.setUi();
                    else if(requestCode == POST_INFO_LISTENER_REQUEST_CODE)
                        postInfoListener.setUi();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                   post.setUri(null);
                    Log.e(Repository.this.getClass().getSimpleName(), "failed download");
                    postListListener.setUi();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readPostData(String postId){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://automatsappuserauthentication-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference postData = firebaseDatabase.getReference("posts/" + postId);
        postData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostDTO postDTO = snapshot.getValue(PostDTO.class);
                Post post = new Post(postDTO.getTitle(), postDTO.getUser(), postDTO.getDescription(), postDTO.getDate(), postDTO.getCommentsCount(), null);
                post.setPostId(postId);
                setPostImage(post, POST_INFO_LISTENER_REQUEST_CODE);
                postInfoListener.onPostInfoChange(post);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void readComments(String postId){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://automatsappuserauthentication-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference commentsData = firebaseDatabase.getReference("posts/" + postId + "/comments");
        ArrayList<Comment> comments = new ArrayList<>();
        commentsData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        CommentDTO commentDTO = dsp.getValue(CommentDTO.class);
                        Comment comment = new Comment(commentDTO.getUser(), commentDTO.getDescription(), commentDTO.getDate(), commentDTO.getCommentIndent(), commentDTO.getCommentIndex(), null);
                        comments.add(comment.getCommentIndex(), comment);
                        setCommentImage(comment, postId, commentDTO.getCommentId());
                    }
                }
                commentListListener.onCommentListChange(comments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void uploadCommentImage(Uri uri, String postId, int commentIndex){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/"+ postId+commentIndex);
        if (uri!=null)
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                }
            });
    }
    public void setCommentImage(Comment comment, String parentPostId, int commentIndex){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/"+parentPostId+commentIndex);
        try {
            File localFile = File.createTempFile("images", "jpg");
            final Uri fileUri = Uri.fromFile(localFile);
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    comment.setCommentImage(fileUri);
                    commentListListener.updateCommentsUi();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    comment.setCommentImage(null);
                    commentListListener.updateCommentsUi();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void updateNotificationsStatus() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://automatsappuserauthentication-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseReference = firebaseDatabase.getReference("users/"+User.getInstance().getUserId()+"/hasNewNotifications");
        databaseReference.setValue(false);
    }

}
