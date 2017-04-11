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
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.fragments.HowItWorksFragment;
import com.elatesoftware.grandcapital.views.fragments.QuestionFragment;
import com.google.android.gms.analytics.HitBuilders;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_how_it_works_list_item, parent, false);
        return new FragmentHowItWorksListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        super.onBindViewHolder(holder, position);
        FragmentHowItWorksListViewHolder questionHolder = (FragmentHowItWorksListViewHolder) holder;
        questionHolder.mQuestionName.setText(mQuestionsNames[position]);
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(QuestionFragment.HEADER_TEXT, mQuestionsNames[position]);
            bundle.putString(QuestionFragment.CONTENT_TEXT, mQuestionsContent[position]);

            GrandCapitalApplication.getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory(Const.ANALYTICS_QUESTION_SCREEN)
                    .setAction(Const.ANALYTICS_LIST_QUESTION)
                    .setLabel(mQuestionsNames[position])
                    .build()
            );

            BaseActivity.sMainTagFragment = HowItWorksFragment.class.getName();
            QuestionFragment questionFragment = new QuestionFragment();
            questionFragment.setArguments(bundle);
            BaseActivity.addNextFragment(questionFragment);
        });
    }

    @Override
    public int getItemCount() {
        return mQuestionsNames.length;
    }
}
