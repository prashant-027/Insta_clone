package com.prashant.instaclone.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.prashant.instaclone.Login;
import com.prashant.instaclone.R;

public class Account_Settings extends AppCompatActivity {

    TextView editProfile, logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_account__settings);

        editProfile = findViewById(R.id.edit_profile);
        logout = findViewById(R.id.logout);

        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Account_Settings.this, EditProfile.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        });

        logout.setOnClickListener(v -> new AlertDialog.Builder(Account_Settings.this)
                .setMessage("Are you sure you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(Account_Settings.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0,0);
                })
                .setNegativeButton("No", null)
                .show());
    }
}