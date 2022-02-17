package com.prashant.instaclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.prashant.instaclone.ReusableCode.ReusableCodeForAll;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    TextView createacc;
    TextInputLayout Email, Pass;
    Button login;
    FirebaseAuth mAuth;
    String email;
    String password;
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        createacc = findViewById(R.id.signup);

        try {
            Email = findViewById(R.id.login_email);
            Pass = findViewById(R.id.login_password);
            login = findViewById(R.id.Login_btn);


            mAuth = FirebaseAuth.getInstance();

            login.setOnClickListener(v -> {

                email = Objects.requireNonNull(Email.getEditText()).getText().toString().trim();
                password = Objects.requireNonNull(Pass.getEditText()).getText().toString().trim();
                if (isValid()) {

                    final ProgressDialog mDialog = new ProgressDialog(Login.this);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setCancelable(false);
                    mDialog.setMessage("Logging in...");
                    mDialog.show();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            mDialog.dismiss();
                            Toast.makeText(Login.this, "You are logged in", Toast.LENGTH_SHORT).show();
                            Intent z = new Intent(Login.this, Home.class);
                            startActivity(z);
                            finish();
                            overridePendingTransition(0,0);

                        } else {

                            mDialog.dismiss();
                            ReusableCodeForAll.ShowAlert(Login.this, "Error", Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });

                }
            });

            createacc.setOnClickListener(v -> {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public boolean isValid() {
        Email.setErrorEnabled(false);
        Email.setError("");
        Pass.setErrorEnabled(false);
        Pass.setError("");

        boolean isvalidemail = false, isvalidpassword = false, isvalid = false;
        if (TextUtils.isEmpty(email)) {
            Email.setErrorEnabled(true);
            Email.setError("Email is required");
        } else {
            if (email.matches(emailpattern)) {
                isvalidemail = true;
            } else {
                Email.setErrorEnabled(true);
                Email.setError("Enter a valid Email Address");
            }

        }
        if (TextUtils.isEmpty(password)) {
            Pass.setErrorEnabled(true);
            Pass.setError("Password is required");
        } else {
            isvalidpassword = true;
        }
        isvalid = isvalidemail && isvalidpassword;
        return isvalid;
    }

}