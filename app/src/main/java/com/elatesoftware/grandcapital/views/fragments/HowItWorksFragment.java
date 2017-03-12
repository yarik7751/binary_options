package com.elatesoftware.grandcapital.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.QuestionsAnswer;
import com.elatesoftware.grandcapital.services.QuestionsService;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.adapters.howItWorks.FragmentHowItWorksListAdapter;

public class HowItWorksFragment extends Fragment {

    public static final String TAG = "HowItWorksFragment";

    private RecyclerView mRecyclerView;
    private LinearLayout llProgress;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private GetResponseQuestionsBroadcastReceiver mQuestionsBroadcastReceiver;

    private static HowItWorksFragment fragment = null;
    public static HowItWorksFragment getInstance() {
        if (fragment == null) {
            fragment = new HowItWorksFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_how_it_works, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_how_it_works));
        BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_OTHER_FRAGMENT);
        BaseActivity.getToolbar().deselectAll();
        BaseActivity.getToolbar().setBurgerType(ToolbarFragment.BURGER_OPEN_MENU);
        /*BaseActivity.getToolbar().mTabLayout.setOnLoadData(new OnLoadData() {
            @Override
            public void loadData() {
                BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_OTHER_FRAGMENT);
            }
        });*/

        llProgress = (LinearLayout) getView().findViewById(R.id.fragment_dealing_progress_bar);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.fragment_how_it_works_questions_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //String[] questionsNames = getActivity().getResources().getStringArray(R.array.how_it_works_page_questions);
        //String[] questionsContents = getActivity().getResources().getStringArray(R.array.how_it_works_page_questions_content);
        //mRecyclerView.setAdapter(new FragmentHowItWorksListAdapter(questionsNames, questionsContents));

        Intent pageIntent = new Intent(getActivity(), QuestionsService.class);
        pageIntent.putExtra(QuestionsService.PAGE, 1);
        getActivity().startService(pageIntent);
        llProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mQuestionsBroadcastReceiver = new GetResponseQuestionsBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(QuestionsService.ACTION_SERVICE_QUESTIONS);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mQuestionsBroadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mQuestionsBroadcastReceiver);
        super.onStop();
    }

    public class GetResponseQuestionsBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(QuestionsService.RESPONSE);
            llProgress.setVisibility(View.GONE);
            if(response != null) {
                if(response.equals("400")) {
                    Log.d(TAG, "QuestionsAnswer questions ERROR: 400");
                } else if(response.equals("200")) {
                    Log.d(TAG, "QuestionsAnswer: " + QuestionsAnswer.getInstance());
                    Log.d(TAG, "QuestionsAnswer questions: " + QuestionsAnswer.getInstance().getQuestions());

                    mRecyclerView.setAdapter(new FragmentHowItWorksListAdapter(QuestionsAnswer.getInstance()));
                } else {
                    Log.d(TAG, "QuestionsAnswer questions ERROR: " + response);
                }
            }
        }
    }
}
