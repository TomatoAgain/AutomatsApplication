package com.example.automatsapp.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.automatsapp.model.User;
import com.example.automatsapp.persistence.Repository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class LoginPresenter implements Repository.LoginListener{
    private boolean inRegister;
    private LoginActivity view;

    public LoginPresenter(LoginActivity view) {
        this.view = view;
        loadEmail();
        this.inRegister = false;
        view.loadPage();
        Repository.getInstance().registerLoginListener(this);
    }

    public void changeScreen() {
        if (!inRegister) {
            view.changeToRegister();
            this.inRegister = true;
        } else {
            view.changeToLogin();
            this.inRegister = false;
        }
    }
    public void moveToLogin(){
        view.changeToLogin();
    }
    public void saveEmail() {
        SharedPreferences.Editor editor = view.getSharedPreferences("loginData", Context.MODE_PRIVATE).edit();
        editor.putString("email", view.getEmail());
        editor.commit();
    }

    public void loadEmail() {
        view.setEmail(view.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("email", ""));
    }

    public void login() {
        if (view.getEmail().length()>0 && view.getPassword().length()>6) {
            view.getAuth().signInWithEmailAndPassword(view.getEmail(), view.getPassword())
                    .addOnCompleteListener(view, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                saveEmail();
                                Repository.getInstance().setUser();
                            } else {
                                FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                Toast.makeText(view, "Failed Registration: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            String error = "";
            if (view.getEmail().length()==0)
                error = "Email can't be empty";
            else if (view.getPassword().length()<6)
                error = "Password has to be longer than 6 characters";
            view.displayError(error);
        }
    }
    public void register() {
        if (view.getPassword().equals(view.getConfirmPassword())) {
            if (!view.getEmail().equals("") && !view.getPassword().equals("") && view.getUsername().length()>0) {
                view.getAuth().createUserWithEmailAndPassword(view.getEmail(), view.getPassword())
                        .addOnCompleteListener(view, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User user = User.getInstance();
                                    user.setUsername(view.getUsername());
                                    user.setEmail(view.getEmail());
                                    user.setCurrentDate();
                                    user.setUserId(FirebaseAuth.getInstance().getUid());
                                    Repository.getInstance().createUser(user);
                                    view.requestPermissionsAndMoveToMain();
                                } else {
                                    Toast.makeText(view, "failed :(", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
            else{
                view.displayError("Some fields are empty");
            }
        }
        else{
            view.displayError("Passwords don't match");
        }
    }

    @Override
    public void onDataReceived() {
        view.requestPermissionsAndMoveToMain();
    }
}
