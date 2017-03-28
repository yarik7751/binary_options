package com.elatesoftware.grandcapital.views.activities;

import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.views.items.CustomDialog;

/**
 * Created by Дарья Высокович on 16.02.2017.
 */
public class WebActivity extends AppCompatActivity {

    private WebView mWebView;

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(final WebView view, final SslErrorHandler handler, SslError error) {
            CustomDialog.showDialog(WebActivity.this, handler, getResources().getString(R.string.caution), getString(R.string.connection_not_secure));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(getResources().getString(R.string.link_company));
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
