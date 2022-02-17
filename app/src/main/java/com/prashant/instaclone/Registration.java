package com.prashant.instaclone;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prashant.instaclone.ReusableCode.ReusableCodeForAll;
import com.prashant.instaclone.models.Passwords;
import com.prashant.instaclone.models.Users;
import com.prashant.instaclone.models.privatedetails;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Registration extends AppCompatActivity {

    TextView alreadyhaveacc;
    TextInputLayout Fname,Username, Email, Pass, Mobileno,Gender,Description,Website;
    EditText Birth;
    int year,month,day;
    Button register;
    CountryCodePicker Cpp;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String fname,username,email,pass,mobileno,gender,description,website,birth;
    String useridd;
    AnimationDrawable anim;

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        alreadyhaveacc = findViewById(R.id.AlreadyHavesignin);
        Birth = findViewById(R.id.birthdate);
        Fname = findViewById(R.id.Fullname);
        Username = findViewById(R.id.Username);
        Email = findViewById(R.id.signup_email);
        Pass = findViewById(R.id.signup_password);
        Gender = findViewById(R.id.gender);
        Mobileno = findViewById(R.id.mobilenoo);
        Description = findViewById(R.id.bio);
        Website = findViewById(R.id.website);

        Cpp = findViewById(R.id.countrycode);

        register = findViewById(R.id.signup_button);

//******************************BACKGROUND ANIMATION*************************
        RelativeLayout container = findViewById(R.id.relative_registration);

        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(6000);
        anim.setExitFadeDuration(2000);

//******************************BACKGROUND ANIMATION*************************

        Birth.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(Registration.this, (view, year, month, dayOfMonth) -> Birth.setText(dayOfMonth + "/" + (month + 1) + "/" + year),year,month,day);
            datePickerDialog.show();

        });

        alreadyhaveacc.setOnClickListener(v -> {
            Intent intent = new Intent(Registration.this, Login.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0,0);
        });


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();


        register.setOnClickListener(v -> {

            fname = Objects.requireNonNull(Fname.getEditText()).getText().toString().trim();
            username = Objects.requireNonNull(Username.getEditText()).getText().toString().trim();
            email = Objects.requireNonNull(Email.getEditText()).getText().toString().trim();
            mobileno = Objects.requireNonNull(Mobileno.getEditText()).getText().toString().trim();
            pass = Objects.requireNonNull(Pass.getEditText()).getText().toString().trim();
            gender = Objects.requireNonNull(Gender.getEditText()).getText().toString().trim();
            description = Objects.requireNonNull(Description.getEditText()).getText().toString().trim();
            birth = Birth.getText().toString().trim();
            website = Objects.requireNonNull(Website.getEditText()).getText().toString().trim();

            if (isValid()) {

                databaseReference.child("Users").orderByChild("Username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Toast.makeText(Registration.this, "Valid", Toast.LENGTH_SHORT).show();
                        if(snapshot.exists()){
                            Toast.makeText(Registration.this, "Username already exists. Please try other username.", Toast.LENGTH_SHORT).show();
                        }else {
                            final ProgressDialog mDialog = new ProgressDialog(Registration.this);
                            mDialog.setCancelable(false);
                            mDialog.setCanceledOnTouchOutside(false);
                            mDialog.setMessage("Registering please wait...");
                            mDialog.show();

                            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    useridd = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                                    addUsers(description,fname,username,website);
                                    addPrivateDetails(useridd,email,gender,birth,mobileno);
                                    addPasswords(pass);

                                    mDialog.dismiss();

                                    startActivity(new Intent(Registration.this, Home.class));
                                    finish();
                                    overridePendingTransition(0,0);

                                } else {
                                    mDialog.dismiss();
                                    ReusableCodeForAll.ShowAlert(Registration.this, "Error", Objects.requireNonNull(task.getException()).getMessage());
                                }

                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Registration.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {

                Toast.makeText(Registration.this, "Fill The form", Toast.LENGTH_SHORT).show();

            }



        });

    }

    public boolean isValid() {
        Email.setErrorEnabled(false);
        Email.setError("");
        Fname.setErrorEnabled(false);
        Fname.setError("");
        Username.setErrorEnabled(false);
        Username.setError("");
        Pass.setErrorEnabled(false);
        Pass.setError("");
        Mobileno.setErrorEnabled(false);
        Mobileno.setError("");
        Gender.setErrorEnabled(false);
        Gender.setError("");

        boolean isValidname = false, isValidemail = false, isvalidpassword = false, isvalid = false, isvalidmobileno = false, isvalidgender = false , isvalidusername=false;
        if (TextUtils.isEmpty(fname)) {
            Fname.setErrorEnabled(true);
            Fname.setError("Fullname is required");
        } else {
            isValidname = true;
        }
        if (TextUtils.isEmpty(email)) {
            Email.setErrorEnabled(true);
            Email.setError("Email is required");
        } else {
            if (email.matches(emailpattern)) {
                isValidemail = true;
            } else {
                Email.setErrorEnabled(true);
                Email.setError("Enter a valid Email Address");
            }

        }
        if (TextUtils.isEmpty(pass)) {
            Pass.setErrorEnabled(true);
            Pass.setError("Password is required");
        } else {
            if (pass.length() < 6) {
                Pass.setErrorEnabled(true);
                Pass.setError("password is too weak");
            } else {
                isvalidpassword = true;
            }
        }
        if (TextUtils.isEmpty(mobileno)) {
            Mobileno.setErrorEnabled(true);
            Mobileno.setError("Mobile number is required");
        } else {
            if (mobileno.length() < 10) {
                Mobileno.setErrorEnabled(true);
                Mobileno.setError("Invalid mobile number");
            } else {
                isvalidmobileno = true;
            }
        }
        if (TextUtils.isEmpty(gender)) {
            Gender.setErrorEnabled(true);
            Gender.setError("Field cannot be empty");
        } else {
            isvalidgender = true;
        }if (TextUtils.isEmpty(username)) {
            Username.setErrorEnabled(true);
            Username.setError("Field cannot be empty");
        } else {
            isvalidusername = true;
        }

        isvalid = isValidname && isValidemail && isvalidpassword && isvalidmobileno && isvalidgender && isvalidusername;
        return isvalid;
    }

    //******************************FUNCTIONS TO ADD DATA'S TO FIREBASE*************************
    public void addUsers(String Discription,String FullName,String Username,String Website){

        Users user = new Users(
                Discription,
                "0",
                "0",
                FullName,
                "0",
                "https://firebasestorage.googleapis.com/v0/b/instagram-clone-291e7.appspot.com/o/generalProfilePhoto%2Fdefualt_insta_pic.png?alt=media&token=e9834979-a141-48fd-87b6-a2074e7dbc9b",
                Username,
                Website,
                useridd
        );
        databaseReference.child("Users").child(useridd).setValue(user);
    }
    public void addPrivateDetails(String user_id, String email, String gender, String birthdate, String phoneNumber){

        privatedetails details = new privatedetails(
                user_id,
                email,
                gender,
                birthdate,
                phoneNumber
        );
        databaseReference.child("Privatedetails").child(useridd).setValue(details);
    }
    public void addPasswords(String passwords){

        Passwords pass = new Passwords(passwords);
        databaseReference.child("Passwords").child(useridd).setValue(pass);

    }
//*******************************************************************************

    //******************************BACKGROUND ANIMATION*************************
    // Starting animation:- start the animation on onResume.
    @Override
    protected void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    // Stopping animation:- stop the animation on onPause.
    @Override
    protected void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
    }
//****************************************************************************

}