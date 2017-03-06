package com.elatesoftware.grandcapital.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;

public class QuestionFragment extends Fragment {

    public static final String TAG = "QuestionFragment";

    WebView wvAnswer;

    private String mHeader;
    private String mContent;

    public static String HEADER_TEXT = "header_text_question_fragment";
    public static String CONTENT_TEXT = "content_text_question_fragment";

    private static QuestionFragment fragment = null;
    public static QuestionFragment getInstance() {
        if (fragment == null) {
            fragment = new QuestionFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "QuestionFragmen onCreateView");
        this.mHeader = this.getArguments().getString(HEADER_TEXT);
        this.mContent = this.getArguments().getString(CONTENT_TEXT);
        this.mContent = Html.fromHtml(this.mContent).toString()
                .replace((char) 160, (char) 32).replace((char) 65532, (char) 32).trim().replaceAll("[\\n]{2,}", "\n");
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().setPageTitle(mHeader);
        BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_EMPTY_FRAGMENT);
        BaseActivity.getToolbar().setBurgerType(ToolbarFragment.BURGER_BACK_PRESSED);
        TextView contentText  = (TextView) view.findViewById(R.id.fragment_question_content);
        contentText.setText(mContent);
        Log.d(TAG, "mContent: " + mContent);
    }
}
