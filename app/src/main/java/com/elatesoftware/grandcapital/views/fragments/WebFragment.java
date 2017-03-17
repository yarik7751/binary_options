package com.elatesoftware.grandcapital.views.fragments;

import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.activities.WebActivity;

/**
 * Created by Ярослав Левшунов on 16.03.2017.
 */

public class WebFragment extends Fragment {

    private static final String URL = "URL";

    public static WebFragment sWebFragment;

    public WebView wvWeb;

    private String url;

    public static WebFragment getInstance(String url) {
        if(sWebFragment == null) {
            WebFragment webFragment = new WebFragment();
            Bundle bundle = new Bundle();
            bundle.putString(URL, url);
            webFragment.setArguments(bundle);
            sWebFragment = webFragment;
            return webFragment;
        } else {
            return sWebFragment;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString(URL);
        BaseActivity.sCurrentTagFragment = WebFragment.class.getName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BaseActivity.getToolbar().setPageTitle(getContext().getResources().getString(R.string.app_name));
        BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_EMPTY_FRAGMENT);
        BaseActivity.getToolbar().setBurgerType(ToolbarFragment.BURGER_BACK_PRESSED);

        wvWeb = (WebView) view.findViewById(R.id.wv_web);
        wvWeb.setWebViewClient(new MyWebViewClient());
        wvWeb.getSettings().setJavaScriptEnabled(true);
        if(!TextUtils.isEmpty(url)) {
            wvWeb.loadUrl(url);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sWebFragment = null;
        BaseActivity.sCurrentTagFragment = "";
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(final WebView view, final SslErrorHandler handler, SslError error) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.attention);
            builder.setMessage(getString(R.string.connection_not_secure));
            builder.setNegativeButton(getString(R.string.no), (dialog, which) -> handler.cancel());
            builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> handler.proceed());
            builder.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            BaseActivity.getToolbar().setPageTitle(view.getTitle());
        }
    }

}
