package com.elatesoftware.grandcapital.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;

public class QuestionFragment extends Fragment {

    private String mHeader;
    private String mContent;

    public static String HEADER_TEXT = "header_text_question_fragment";
    public static String CONTENT_TEXT = "content_text_question_fragment";

    public QuestionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mHeader = this.getArguments().getString(HEADER_TEXT);
        this.mContent = this.getArguments().getString(CONTENT_TEXT);
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().setPageTitle(mHeader);
        TextView contentText  = (TextView) view.findViewById(R.id.fragment_question_content);
        contentText.setText(mContent);
    }
}
