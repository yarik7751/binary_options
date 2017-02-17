package com.elatesoftware.grandcapital.views.activities;

import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.elatesoftware.grandcapital.R;

/**
 * Created by Дарья Высокович on 16.02.2017.
 */
public class WebActivity extends AppCompatActivity {

    private WebView mWebView;

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(final WebView view, final SslErrorHandler handler, SslError error) {
            AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
            builder.setTitle("Внимание!");
            builder.setMessage(getString(R.string.connection_not_secure));
            builder.setNegativeButton(getString(R.string.no), (dialog, which) -> handler.cancel());
            builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> handler.proceed());
            builder.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl( getResources().getString(R.string.link_company));
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
