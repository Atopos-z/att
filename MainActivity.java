package com.example.a15654.myapplication5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.CollationElementIterator;

public class MainActivity extends AppCompatActivity {
    private WebView WebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main);
        WebView = (WebView) findViewById( R.id.wvshow);
        WebSettings webSettings = WebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        WebView.addJavascriptInterface( new showCamera(),"android");
        WebView.loadUrl( "file:///android_asset/html/test.html" );
        //WebView.setWebViewClient(new WebViewClient());
        //WebView.loadUrl("http://www.baidu.com");
        //Button sendRequest = (Button) findViewById( R.id.send_request );
        //TextView responseText = (TextView) findViewById( R.id.response_text );
       // sendRequest.setOnClickListener( (View.OnClickListener) this );
    }
    public final class showCamera {
        @JavascriptInterface
        public void showCamera() {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent,0);
        }
    }


}
