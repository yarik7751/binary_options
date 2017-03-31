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
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.services.DeleteDealingService;
import com.elatesoftware.grandcapital.services.OrdersService;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.views.items.CustomDialog;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.adapters.dealing.FragmentDealingCloseOrdersAdapter;
import com.elatesoftware.grandcapital.adapters.dealing.FragmentDealingOpenOrdersAdapter;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;

import java.util.ArrayList;
import java.util.List;

public class DealingFragment extends Fragment {

    public static final String TAG = "DealingFragment_Logs";
    private static List<OrderAnswer> currentOrders = new ArrayList<>();

    private TabLayout mTabs;
    private RecyclerView mRecyclerView;
    private FragmentDealingOpenOrdersAdapter mAdapterOpen;
    private FragmentDealingCloseOrdersAdapter mAdapterClose;
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
    private GetResponseDeleteDealingBroadcastReceiver mDeleteDealingBroadcastReceiver;
    private static int currentTabPosition = 0;

    public static boolean sIsOpen = false;

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
        cleanCloseDealings();
    }

    @Override
    public void onResume() {
        super.onResume();
        sIsOpen = true;
        mOrdersBroadcastReceiver = new GetResponseOrdersBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(OrdersService.ACTION_SERVICE_ORDERS);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mOrdersBroadcastReceiver, intentFilter);

        mDeleteDealingBroadcastReceiver = new GetResponseDeleteDealingBroadcastReceiver();
        IntentFilter intentFilterDeleteDealing = new IntentFilter(DeleteDealingService.ACTION_SERVICE_DELETE_FEALING);
        intentFilterDeleteDealing.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mDeleteDealingBroadcastReceiver, intentFilterDeleteDealing);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        getActivity().unregisterReceiver(mOrdersBroadcastReceiver);
        getActivity().unregisterReceiver(mDeleteDealingBroadcastReceiver);
        sIsOpen = false;
        super.onPause();

    }
    private void requestOrders(){
        Intent intentService = new Intent(getActivity(), OrdersService.class);
        intentService.putExtra(OrdersService.FUNCTION, OrdersService.GET_ALL_ORDERS);
        getActivity().startService(intentService);
    }

    private void requestDeleteDealing(OrderAnswer order) {
        Intent intentService = new Intent(getActivity(), DeleteDealingService.class);
        intentService.putExtra(Const.ACTION, DeleteDealingService.ACTION_SERVICE_DELETE_FEALING);
        getActivity().startService(intentService.putExtra(DeleteDealingService.TICKET, order.getTicket()));
    }

    private void cleanCloseDealings() {
        CustomSharedPreferences.setAmtCloseDealings(getContext(), 0);
        ((BaseActivity) getActivity()).setDealings();
        BaseActivity.getToolbar().setDealingIcon();
    }

    private void initTabs() {
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.ordersListOpen);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mTabs = (TabLayout) getView().findViewById(R.id.dealingTabs);
        mTabs.getTabAt(currentTabPosition).select();
        requestOrders();
        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                requestOrders();
                mNoOrdersLayout.setVisibility(View.GONE);
                currentTabPosition = mTabs.getSelectedTabPosition();
                mProgressLayout.setVisibility(View.VISIBLE);
                mListLayout.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mTabs.getTabAt(currentTabPosition).select();
    }

    private void initListHeaders() {
        mFirstColumnHeader = (TextView) getView().findViewById(R.id.tv_dealing_header_col1_active);
        mSecondColumnHeader = (TextView) getView().findViewById(R.id.tv_dealing_header_col2_open);
        mThirdColumnHeader = (TextView) getView().findViewById(R.id.fragment_dealing_header_column_3);
        mFourthColumnHeader = (TextView) getView().findViewById(R.id.fragment_dealing_header_column_4);
        mFifthColumnHeader = (TextView) getView().findViewById(R.id.fragment_dealing_header_column_5);
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

    public class GetResponseOrdersBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(OrdersService.RESPONSE);
            if (response != null && response.equals(Const.RESPONSE_CODE_SUCCESS)){
                if(OrderAnswer.getInstance() != null){
                    List<OrderAnswer> orders = OrderAnswer.getInstance();
                    currentOrders = OrderAnswer.filterOrders(orders, currentTabPosition);
                    checkOrders(currentOrders);
                    if(currentTabPosition == OPEN_TAB_POSITION) {
                        if(mAdapterOpen == null) {
                            mAdapterClose = null;
                            mAdapterOpen = new FragmentDealingOpenOrdersAdapter(currentOrders, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d(TAG, "delete dealing");
                                    OrderAnswer order = (OrderAnswer) v.getTag();
                                    requestDeleteDealing(order);
                                }
                            });
                            mRecyclerView.setAdapter(mAdapterOpen);
                        } else {
                            mAdapterOpen.updateAdapter(currentOrders);
                        }
                    } else {
                        mAdapterOpen = null;
                        if(mAdapterClose == null) {
                            mAdapterClose = new FragmentDealingCloseOrdersAdapter(currentOrders);
                            mRecyclerView.setAdapter(mAdapterClose);
                        }
                        //cleanCloseDealings();
                    }
                }
                mProgressLayout.setVisibility(View.GONE);
                setListHeader(currentTabPosition);
            } else {
                if (isAdded()) {
                    mProgressLayout.setVisibility(View.GONE);
                    CustomDialog.showDialogInfo(getActivity(),
                            getString(R.string.request_error_title),
                            getString(R.string.request_error_text));
                }
            }
        }
    }

    public class GetResponseDeleteDealingBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "GetResponseDeleteDealingBroadcastReceiver");
            String response = intent.getStringExtra(DeleteDealingService.RESPONSE);
            if (response == null || !response.equals(Const.CODE_SUCCESS_DELETE_DEALING)) {
                Log.d(TAG, "CODE_SUCCESS_DELETE_DEALING ERROR");
            } else {
                Log.d(TAG, "CODE_SUCCESS_DELETE_DEALING");
                requestOrders();
            }
        }
    }
}
