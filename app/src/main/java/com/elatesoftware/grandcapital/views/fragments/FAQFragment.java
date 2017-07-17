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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.QuestionsAnswer;
import com.elatesoftware.grandcapital.services.QuestionsService;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.adapters.howItWorks.FragmentHowItWorksListAdapter;
import com.elatesoftware.grandcapital.views.items.CustomDialog;

public class FAQFragment extends Fragment {

    public static final String TAG = "FAQFragment";
    private RecyclerView mRecyclerView;
    private LinearLayout llProgress;

    private GetResponseQuestionsBroadcastReceiver mQuestionsBroadcastReceiver;

    private static FAQFragment fragment = null;
    public static FAQFragment getInstance() {
        if (fragment == null) {
            fragment = new FAQFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_faq, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_how_it_works));
        BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_OTHER_FRAGMENT);
        BaseActivity.getToolbar().deselectAll();
        BaseActivity.getToolbar().setBurgerType(ToolbarFragment.BURGER_OPEN_MENU);

        llProgress = (LinearLayout) getView().findViewById(R.id.layout_progress_bar);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.fragment_how_it_works_questions_list);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        mQuestionsBroadcastReceiver = new GetResponseQuestionsBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(QuestionsService.ACTION_SERVICE_QUESTIONS);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mQuestionsBroadcastReceiver, intentFilter);

        Intent pageIntent = new Intent(getActivity(), QuestionsService.class);
        pageIntent.putExtra(QuestionsService.PAGE, 1);
        getActivity().startService(pageIntent);
        llProgress.setVisibility(View.VISIBLE);
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
            if(response != null && response.equals(Const.RESPONSE_CODE_SUCCESS)) {
                mRecyclerView.setAdapter(new FragmentHowItWorksListAdapter(QuestionsAnswer.getInstance()));
            } else {
                CustomDialog.showDialogInfo(getActivity(), getString(R.string.request_error_title), getString(R.string.request_error_text));
            }
        }
    }
}
