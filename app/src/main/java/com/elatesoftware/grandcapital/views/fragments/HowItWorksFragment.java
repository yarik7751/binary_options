package com.elatesoftware.grandcapital.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.adapters.howItWorks.FragmentHowItWorksListAdapter;

public class HowItWorksFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public HowItWorksFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_how_it_works, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_how_it_works));

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.fragment_how_it_works_questions_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        String[] questionsNames = getActivity().getResources().getStringArray(R.array.how_it_works_page_questions);
        String[] questionsContents = getActivity().getResources().getStringArray(R.array.how_it_works_page_questions_content);
        mRecyclerView.setAdapter(new FragmentHowItWorksListAdapter(questionsNames, questionsContents));
    }
}
