package com.example.juston;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class irctcpage extends AppCompatActivity {

    WebView webview3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irctcpage);

        webview3 = findViewById(R.id.Webview3);

        WebSettings webSettings = webview3.getSettings();
        webview3.getSettings().setJavaScriptEnabled(true);

        WebViewClient webViewClient = new WebViewClient();
        webview3.setWebViewClient(webViewClient);

        webview3.loadUrl("https://www.irctc.co.in/");
    }

}