package com.elatesoftware.grandcapital.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.utils.ConventString;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;

public class QuestionFragment extends Fragment {

    public static final String TAG = "QuestionFragment";

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
        mHeader = getArguments().getString(HEADER_TEXT);
        mContent = getArguments().getString(CONTENT_TEXT);
        mContent = ConventString.getContentQuestions(mContent);
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().setPageTitle(mHeader);
        BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_EMPTY_FRAGMENT);
        BaseActivity.getToolbar().deselectAll();
        BaseActivity.getToolbar().setBurgerType(ToolbarFragment.BURGER_BACK_PRESSED_ACTIVITY);

        TextView contentText  = (TextView) view.findViewById(R.id.fragment_question_content);
        contentText.setText(mContent);
    }
}
