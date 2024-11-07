package com.example.techmovee;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PageBI extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_bi);

        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Habilita o JavaScript
        webSettings.setLoadWithOverviewMode(true); // Ajusta o conteúdo para caber na tela
        webSettings.setUseWideViewPort(true); // Usa uma viewport ampla para ajuste
        webSettings.setBuiltInZoomControls(true); // Habilita o controle de zoom
        webSettings.setDisplayZoomControls(false); // Remove os botões de zoom visíveis

        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://app.powerbi.com/view?r=eyJrIjoiYTQ2OWE3YzUtMDA1OC00NjdjLTk3YzktNzlmYjk0Y2M4ODRmIiwidCI6ImIxNDhmMTRjLTIzOTctNDAyYy1hYjZhLTFiNDcxMTE3N2FjMCJ9 ");

    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}