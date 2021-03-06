package com.elatesoftware.grandcapital.adapters.howItWorks;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.adapters.GrandCapitalListAdapter;
import com.elatesoftware.grandcapital.api.pojo.QuestionsAnswer;
import com.elatesoftware.grandcapital.utils.GoogleAnalyticsUtil;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.fragments.FAQFragment;
import com.elatesoftware.grandcapital.views.fragments.QuestionFragment;

public class FragmentHowItWorksListAdapter extends GrandCapitalListAdapter {

    private String[] mQuestionsNames;
    private String[] mQuestionsContent;

    private class FragmentHowItWorksListViewHolder extends RecyclerView.ViewHolder {

        private TextView mQuestionName;
        private View mItemLayout;

        FragmentHowItWorksListViewHolder(View v) {
            super(v);
            mQuestionName = (TextView)itemView.findViewById(R.id.fragment_how_it_works_questions_name);
            mItemLayout = itemView.findViewById(R.id.fragment_how_it_works_questions_list_layout);
        }
    }

    public FragmentHowItWorksListAdapter(String[] mQuestionsNames, String[] mQuestionsContent) {
        this.mQuestionsNames = mQuestionsNames;
        this.mQuestionsContent = mQuestionsContent;
    }

    public FragmentHowItWorksListAdapter(QuestionsAnswer questionsAnswer) {
        mQuestionsNames = new String[questionsAnswer.getQuestions().size()];
        mQuestionsContent = new String[questionsAnswer.getQuestions().size()];

        for (int i = 0; i < mQuestionsNames.length; i++) {
            mQuestionsNames[i] = questionsAnswer.getQuestions().get(i).getQuestion();
            mQuestionsContent[i] = questionsAnswer.getQuestions().get(i).getAnswer();
        }
    }

    @Override
    public FragmentHowItWorksListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_how_it_works, parent, false);
        return new FragmentHowItWorksListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        super.onBindViewHolder(holder, position);
        FragmentHowItWorksListViewHolder questionHolder = (FragmentHowItWorksListViewHolder) holder;
        questionHolder.mQuestionName.setText(mQuestionsNames[position]);
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(QuestionFragment.NUMBER_QUESTION, position);
            bundle.putString(QuestionFragment.HEADER_TEXT, mQuestionsNames[position]);
            bundle.putString(QuestionFragment.CONTENT_TEXT, mQuestionsContent[position]);

            GoogleAnalyticsUtil.sendEvent(GoogleAnalyticsUtil.ANALYTICS_QUESTION_SCREEN, GoogleAnalyticsUtil.ANALYTICS_LIST_QUESTION, mQuestionsNames[position], null);

            BaseActivity.sMainTagFragment = FAQFragment.class.getName();
            QuestionFragment questionFragment = new QuestionFragment();
            questionFragment.setArguments(bundle);
            BaseActivity.addNextFragment(questionFragment);
        });
    }

    @Override
    public int getItemCount() {
        if(mQuestionsNames == null){
            return 0;
        }
        return mQuestionsNames.length;
    }
}
