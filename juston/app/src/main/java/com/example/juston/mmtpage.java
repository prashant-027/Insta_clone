package com.example.juston;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class mmtpage extends AppCompatActivity {

    WebView webview4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mmtpage);

        webview4 = findViewById(R.id.Webview4);

        WebSettings webSettings = webview4.getSettings();
        webview4.getSettings().setJavaScriptEnabled(true);

        WebViewClient webViewClient = new WebViewClient();
        webview4.setWebViewClient(webViewClient);

        webview4.loadUrl("https://www.programiz.com");
    }
}