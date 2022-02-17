package com.example.juston;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton btnfacebook;
    ImageButton btninsta;
    ImageButton btnirctc;
    ImageButton btnmmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnfacebook = findViewById(R.id.btnfacebook);
        btninsta = findViewById(R.id.btninsta);
        btnirctc = findViewById(R.id.btnirctc);
        btnmmt = findViewById(R.id.btnmmt);

        btnfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, facebookpage.class);
                startActivity(intent);
            }
        });

        btninsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, instapage.class);
                startActivity(intent);
            }
        });

        btnirctc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, irctcpage.class);
                startActivity(intent);
            }
        });

        btnmmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, mmtpage.class);
                startActivity(intent);
            }
        });
    }
}