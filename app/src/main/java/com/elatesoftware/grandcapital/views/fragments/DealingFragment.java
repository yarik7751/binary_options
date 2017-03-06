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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.services.OrdersService;
import com.elatesoftware.grandcapital.views.activities.SignInActivity;
import com.elatesoftware.grandcapital.views.items.CustomDialog;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.adapters.dealing.FragmentDealingCloseOrdersAdapter;
import com.elatesoftware.grandcapital.adapters.dealing.FragmentDealingOpenOrdersAdapter;
import com.elatesoftware.grandcapital.api.GrandCapitalApi;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealingFragment extends Fragment {

    private TabLayout mTabs;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mFirstColumnHeader;
    private TextView mSecondColumnHeader;
    private TextView mThirdColumnHeader;
    private TextView mFourthColumnHeader;
    private TextView mFifthColumnHeader;
    private LinearLayout mListLayout;
    private LinearLayout mNoOrdersLayout;
    private LinearLayout mProgressLayout;

    public static final int OPEN_TAB_POSITION = 0;
    public static final int CLOSE_TAB_POSITION = 1;

    private GetResponseOrdersBroadcastReceiver mOrdersBroadcastReceiver;
    private static int currentTabPosition = 0;

    private static DealingFragment fragment = null;
    public static DealingFragment getInstance() {
        if (fragment == null) {
            fragment = new DealingFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dealing, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_dealing));
        BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_OTHER_FRAGMENT);
        BaseActivity.getToolbar().switchTab(BaseActivity.DEALING_POSITION);

        mListLayout = (LinearLayout) view.findViewById(R.id.fragment_dealing_list);
        mNoOrdersLayout = (LinearLayout) view.findViewById(R.id.fragment_dealing_no_elements_layout);
        mProgressLayout = (LinearLayout) view.findViewById(R.id.fragment_dealing_progress_bar);

        initTabs();
        initListHeaders();
        updateData();
    }

    private void initTabs() {
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.ordersListOpen);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mTabs = (TabLayout) getView().findViewById(R.id.dealingTabs);
        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabPosition = mTabs.getSelectedTabPosition();
                mProgressLayout.setVisibility(View.VISIBLE);
                mListLayout.setVisibility(View.INVISIBLE);
                updateData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initListHeaders() {
        mFirstColumnHeader = (TextView) getView().findViewById(R.id.tv_dealing_header_col1_active);
        mSecondColumnHeader = (TextView) getView().findViewById(R.id.tv_dealing_header_col2_open);
        mThirdColumnHeader = (TextView) getView().findViewById(R.id.fragment_dealing_header_column_3);
        mFourthColumnHeader = (TextView) getView().findViewById(R.id.fragment_dealing_header_column_4);
        mFifthColumnHeader = (TextView) getView().findViewById(R.id.fragment_dealing_header_column_5);
    }

    private void updateData() {
        Intent intentService = new Intent(getActivity(), OrdersService.class);
        getActivity().startService(intentService);
    }

    private void onFailRequest() {
        if (isAdded()) {
            mProgressLayout.setVisibility(View.GONE);
            CustomDialog.showDialogInfo(getActivity(),
                    getString(R.string.request_error_title),
                    getString(R.string.request_error_text));
        }
    }

    private void checkOrders(List<OrderAnswer> currentOrders) {
        if (currentOrders.size() == 0) {
            mListLayout.setVisibility(View.GONE);
            mNoOrdersLayout.setVisibility(View.VISIBLE);
        } else {
            mListLayout.setVisibility(View.VISIBLE);
            mNoOrdersLayout.setVisibility(View.GONE);
        }
    }

    private void setListHeader(int currentTabPosition) {
        if (isAdded()) {
            switch (currentTabPosition) {
                case OPEN_TAB_POSITION:
                    setOpenOrdersHeaders();
                    break;
                case CLOSE_TAB_POSITION:
                    setCloseOrdersHeaders();
                    break;
            }
        }
    }

    private void setOpenOrdersHeaders() {
        mFirstColumnHeader.setText(getString(R.string.dealing_page_open_active));
        mSecondColumnHeader.setText(getString(R.string.dealing_page_open_open));
        mThirdColumnHeader.setText(getString(R.string.dealing_page_open_market));
        mFourthColumnHeader.setText(getString(R.string.dealing_page_open_amount));
        mFifthColumnHeader.setText(getString(R.string.dealing_page_open_time));
    }

    private void setCloseOrdersHeaders() {
        mFirstColumnHeader.setText(getString(R.string.dealing_page_close_active));
        mSecondColumnHeader.setText(getString(R.string.dealing_page_close_open));
        mThirdColumnHeader.setText(getString(R.string.dealing_page_close_win));
        mFourthColumnHeader.setText(getString(R.string.dealing_page_close_invest));
        mFifthColumnHeader.setText(getString(R.string.dealing_page_close_income));
    }

    private List<OrderAnswer> findOrders(List<OrderAnswer> orders, int currentTabPosition) {
        List<OrderAnswer> closeOrders = new ArrayList();
        List<OrderAnswer> openOrders = new ArrayList();
        for (OrderAnswer order : orders) {
            if(order.getOptionsData() != null) {
                if(order.getCloseTime().equals("1970-01-01T00:00:00")){
                    openOrders.add(order);
                }else{
                    closeOrders.add(order);
                }
            }
        }
        if (currentTabPosition == OPEN_TAB_POSITION) {
            return openOrders;
        }else{
            return closeOrders;
        }
    }

    public class GetResponseOrdersBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(OrdersService.RESPONSE);
            if (response != null) {
                if(response.equals("200")){
                    if(OrderAnswer.getInstance() != null){
                        List<OrderAnswer> orders = OrderAnswer.getInstance();
                        List<OrderAnswer> currentOrders = findOrders(orders, currentTabPosition);
                        checkOrders(currentOrders);
                        mAdapter = currentTabPosition == OPEN_TAB_POSITION
                                ? new FragmentDealingOpenOrdersAdapter(currentOrders)
                                : new FragmentDealingCloseOrdersAdapter(currentOrders);
                    }
                    mProgressLayout.setVisibility(View.GONE);
                    mRecyclerView.setAdapter(mAdapter);
                    setListHeader(currentTabPosition);
                }
            } else {
                onFailRequest();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mOrdersBroadcastReceiver = new GetResponseOrdersBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(OrdersService.ACTION_SERVICE_ORDERS);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mOrdersBroadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mOrdersBroadcastReceiver);
    }
}
