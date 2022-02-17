package com.example.juston;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

public class WebViewClient {
    public Activity activity = null;

    public WebViewClient(Activity activity) {
        this.activity = activity;
    }

    public boolean ShouldOverrideUrlLoading(WebView webView1, String url){
        if (url.indexOf("https://www.facebook.com/") > -1) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        return false;
    }


    public void ShouldOverrideUrlLoading(WebView webView2, String url){
        if (url.indexOf("https://www.instagram.com/") > -1) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }

    public void ShouldOverrideUrlLoading(WebView webView3, String url){
        if (url.indexOf("https://www.irctc.co.in/") > -1) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }

    public boolean ShouldOverrideUrlLoading(WebView webView4, String url){
        if (url.indexOf("https://www.programiz.com/") > -1) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        return false;
    }

}

