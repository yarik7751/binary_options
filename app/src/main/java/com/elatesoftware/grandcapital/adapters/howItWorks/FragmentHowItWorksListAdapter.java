package com.elatesoftware.grandcapital.adapters.howItWorks;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.adapters.GrandCapitalListAdapter;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
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

            QuestionFragment questionFragment = new QuestionFragment();
            questionFragment.setArguments(bundle);
            BaseActivity.changeMainFragment(questionFragment);
        });
    }

    @Override
    public int getItemCount() {
        return mQuestionsNames.length;
    }
}
