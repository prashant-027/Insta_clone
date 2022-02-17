package com.example.juston;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.webkit.WebViewClient;
import android.webkit.WebViewClient;

public class instapage extends AppCompatActivity {

    WebView webview2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instapage);

        webview2 = findViewById(R.id.Webview2);

        WebSettings webSettings = webview2.getSettings();
        webview2.getSettings().setJavaScriptEnabled(true);

        WebViewClient  webViewClient = new WebViewClient();
        webview2.setWebViewClient(webViewClient);

        webview2.loadUrl("https://www.instagram.com/");
    }



}