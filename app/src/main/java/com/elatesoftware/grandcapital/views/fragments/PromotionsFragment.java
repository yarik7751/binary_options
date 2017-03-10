package com.elatesoftware.grandcapital.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.adapters.howItWorks.FragmentHowItWorksListAdapter;
import com.elatesoftware.grandcapital.adapters.promotions.FragmentPromotionsAdapter;
import com.elatesoftware.grandcapital.api.pojo.BinaryOptionAnswer;
import com.elatesoftware.grandcapital.api.pojo.QuestionsAnswer;
import com.elatesoftware.grandcapital.services.BinaryOptionService;
import com.elatesoftware.grandcapital.services.QuestionsService;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;

public class PromotionsFragment extends Fragment {

    public static final String TAG = "PromotionsFragment";

    //private TabLayout promotionsTabs;
    private RecyclerView rvPromotions;
    private LinearLayout llProgress;

    GetResponseBinaryOptionBroadcastReceiver getResponseBinaryOptionBroadcastReceiver;

    private static PromotionsFragment fragment = null;
    public static PromotionsFragment getInstance() {
        if (fragment == null) {
            fragment = new PromotionsFragment();
        }
        return fragment;
    }


    @Override
    public void onStart() {
        super.onStart();
        getResponseBinaryOptionBroadcastReceiver = new GetResponseBinaryOptionBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(BinaryOptionService.ACTION_SERVICE_BINARY_OPTION);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(getResponseBinaryOptionBroadcastReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_promotions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_promotions));
        BaseActivity.getToolbar().hideTabs();
        BaseActivity.getToolbar().setBurgerType(ToolbarFragment.BURGER_OPEN_MENU);

        //promotionsTabs = (TabLayout) view.findViewById(R.id.promotionsTabs);
        rvPromotions = (RecyclerView) view.findViewById(R.id.promotionsListOpen);
        rvPromotions.setLayoutManager(new LinearLayoutManager(getContext()));
        llProgress = (LinearLayout) view.findViewById(R.id.fragment_dealing_progress_bar);

        loadBinaryOptionData();
        /*promotionsTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "position: " + promotionsTabs.getSelectedTabPosition());
                int position = promotionsTabs.getSelectedTabPosition();
                if(position == 0) {
                    loadBinaryOptionData();
                } else {
                    loadForexData();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });*/
    }

    private void loadBinaryOptionData() {
        getActivity().startService(new Intent(getActivity(), BinaryOptionService.class));
    }

    private void loadForexData() {

    }

    public class GetResponseBinaryOptionBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(QuestionsService.RESPONSE);
            llProgress.setVisibility(View.GONE);
            if(response != null) {
                if(response.equals("400")) {
                    Log.d(TAG, "BinaryOptionAnswer questions ERROR: 400");
                } else if(response.equals("200")) {
                    Log.d(TAG, "BinaryOptionAnswer: " + BinaryOptionAnswer.getInstance());
                    Log.d(TAG, "BinaryOptionAnswer size: " + BinaryOptionAnswer.getInstance().getElements().size());
                    Log.d(TAG, "BinaryOptionAnswer elements: " + BinaryOptionAnswer.getInstance().getElements());

                    rvPromotions.setAdapter(new FragmentPromotionsAdapter(getActivity(), BinaryOptionAnswer.getInstance()));
                } else {
                    Log.d(TAG, "BinaryOptionAnswer questions ERROR: " + response);
                }
            }
        }
    }
}
