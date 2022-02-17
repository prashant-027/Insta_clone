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




public class facebookpage extends AppCompatActivity {

    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebookpage);

        webview = findViewById(R.id.Webview1);

        WebSettings webSettings = webview.getSettings();
        webview.getSettings().setJavaScriptEnabled(true);

        WebViewClient webViewClient = new WebViewClient();
        webview.setWebViewClient(webViewClient);

        webview.loadUrl("https://www.facebook.com/");
    }



}