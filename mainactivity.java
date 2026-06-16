package com.samed.panel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private final Handler reloadHandler = new Handler();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ekranı uyanık tut ve Tam Ekran yap
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // KONUM İZNİ İSTE (Sadece bir kez sorar)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        webView = findViewById(R.id.webview);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setDatabaseEnabled(true);
        
        // KONUM AYARLARI
        ws.setGeolocationEnabled(true);
        ws.setGeolocationDatabasePath(getFilesDir().getPath());

        ws.setUserAgentString("Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Mobile Safari/537.36");

        webView.setWebViewClient(new WebViewClient());

        // Web sayfasından gelen konum isteklerini otomatik onayla
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                // Konum isteğine "EVET" cevabı ver
                callback.invoke(origin, true, false);
            }
        });

        webView.loadUrl("https://samedonline.gt.tc/boot.php");
        startAutoRefresh();

        // Geri tuşunu tamamen engelleme (Kiosk Modu için yeni standart yöntem)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Boş bırakıldı: Geri tuşuna basıldığında hiçbir şey yapma, uygulamada kal.
            }
        });
    }

    private void startAutoRefresh() {
        reloadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (webView != null) {
                    webView.reload();
                }
                reloadHandler.postDelayed(this, 300000); // 5 Dakika
            }
        }, 300000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reloadHandler.removeCallbacksAndMessages(null); // Hafıza sızıntısını önler
    }
}
