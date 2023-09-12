package com.example.automatsapp.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automatsapp.persistence.Repository;
import com.example.automatsapp.ui.main.MainActivity;
import com.example.automatsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final int NOTIFICATION_REQUEST_ID = 0;
    private LoginPresenter presenter;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        mAuth = FirebaseAuth.getInstance();
        this.presenter = new LoginPresenter(this);

    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Repository.getInstance().setUser();
        }

    }
    public ConstraintLayout getRootLayout(){
        ConstraintLayout rootLayout = findViewById(R.id.layout_login_root);
        return rootLayout;
    }
    public void loadPage(){
        Button goToLogin = findViewById(R.id.button_logo_login);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.moveToLogin();
            }
        });
        Button registerSubmit = findViewById(R.id.button_register_submit);
        registerSubmit.setVisibility(View.GONE);
        Button noAccount = findViewById(R.id.button_login_register);
        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.changeScreen();
            }
        });
        Button login = findViewById(R.id.button_login_submit);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.login();
            }
        });
        registerSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.register();
            }
        });

    }
    public void changeToRegister(){
        ConstraintSet set = new ConstraintSet();
        set.clone(this, R.layout.activity_register);
        set.applyTo(getRootLayout());
        TransitionManager.beginDelayedTransition(getRootLayout());
        ImageView logo = findViewById(R.id.imageview_logo_logo);
        logo.setVisibility(View.GONE);
        TextView loginTitle = findViewById(R.id.textview_login_title);
        loginTitle.setVisibility(View.INVISIBLE);
        TextView registerTitle = findViewById(R.id.textview_register_title);
        registerTitle.setVisibility(View.VISIBLE);
        Button noAccount = findViewById(R.id.button_login_register);
        noAccount.setText("Back to login");
        EditText username = findViewById(R.id.edittext_register_username);
        username.setVisibility(View.VISIBLE);
        EditText confirmPass = findViewById(R.id.edittext_register_confirm);
        confirmPass.setVisibility(View.VISIBLE);
        Button registerSubmit = findViewById(R.id.button_register_submit);
        registerSubmit.setVisibility(View.VISIBLE);
        Button loginButton = findViewById(R.id.button_login_submit);
        loginButton.setVisibility(View.GONE);
        Button goToLogin = findViewById(R.id.button_logo_login);
        goToLogin.setVisibility(View.GONE);

    }
    public void changeToLogin(){
        ConstraintSet set = new ConstraintSet();
        set.clone(this, R.layout.activity_login);
        set.applyTo(getRootLayout());
        TransitionManager.beginDelayedTransition(getRootLayout());
        Button loginButton = findViewById(R.id.button_login_submit);
        loginButton.setVisibility(View.VISIBLE);
        TextView loginTitle = findViewById(R.id.textview_login_title);
        loginTitle.setVisibility(View.VISIBLE);
        TextView registerTitle = findViewById(R.id.textview_register_title);
        registerTitle.setVisibility(View.INVISIBLE);
        Button noAccount = findViewById(R.id.button_login_register);
        noAccount.setText("No Account?");
        EditText username = findViewById(R.id.edittext_register_username);
        username.setVisibility(View.INVISIBLE);
        EditText confirmPass = findViewById(R.id.edittext_register_confirm);
        confirmPass.setVisibility(View.INVISIBLE);
        Button registerSubmit = findViewById(R.id.button_register_submit);
        registerSubmit.setVisibility(View.GONE);
        Button goToLogin = findViewById(R.id.button_logo_login);
        goToLogin.setVisibility(View.GONE);
        ImageView logo = findViewById(R.id.imageview_logo_logo);
        logo.setVisibility(View.GONE);

    }
    public void requestPermissionsAndMoveToMain(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                String[] permissions = {Manifest.permission.POST_NOTIFICATIONS};
                requestPermissions(permissions, NOTIFICATION_REQUEST_ID);
            }
        }
        else{
            Toast.makeText(this, "Notifications will be sent without permission",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_REQUEST_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
        else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void displayError(String errorMessage){
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
    public FirebaseAuth getAuth(){
        return this.mAuth;
    }
    public void setEmail(String s){
        EditText email = findViewById(R.id.edittext_login_email);
        email.setText(s);
    }
    public String getEmail(){
        EditText email = findViewById(R.id.edittext_login_email);
        return email .getText().toString();
    }
    public String getPassword(){
        EditText password = findViewById(R.id.edittext_login_password);
        return password .getText().toString();
    }
    public String getUsername(){
        EditText username = findViewById(R.id.edittext_register_username);
        return username .getText().toString();
    }
    public String getConfirmPassword(){
        EditText password = findViewById(R.id.edittext_register_confirm);
        return password .getText().toString();
    }
}