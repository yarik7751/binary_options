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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.adapters.dealing.FragmentDealingCloseOrdersAdapter;
import com.elatesoftware.grandcapital.adapters.dealing.FragmentDealingOpenOrdersAdapter;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.services.DeleteDealingService;
import com.elatesoftware.grandcapital.services.OrdersService;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.utils.GoogleAnalyticsUtil;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.items.CustomDialog;

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
    private RelativeLayout mNoOrdersLayout;
    private TextView tvEmptyDealing;
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
        ((BaseActivity) getActivity()).hideShadow();

        mListLayout = (LinearLayout) view.findViewById(R.id.fragment_dealing_list);
        mNoOrdersLayout = (RelativeLayout) view.findViewById(R.id.rl_no_elements);
        tvEmptyDealing = (TextView) mNoOrdersLayout.findViewById(R.id.tvEmptyItems);
        mProgressLayout = (LinearLayout) view.findViewById(R.id.layout_progress_bar);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.ordersListOpen);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mTabs = (TabLayout) getView().findViewById(R.id.dealingTabs);
        mTabs.getTabAt(currentTabPosition).select();
        analytics(currentTabPosition);

        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                requestOrders();
                mNoOrdersLayout.setVisibility(View.GONE);
                currentTabPosition = mTabs.getSelectedTabPosition();
                mProgressLayout.setVisibility(View.VISIBLE);
                mListLayout.setVisibility(View.INVISIBLE);
                analytics(currentTabPosition);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mTabs.getTabAt(currentTabPosition).select();
        mFirstColumnHeader = (TextView) getView().findViewById(R.id.tv_dealing_header_col1_active);
        mSecondColumnHeader = (TextView) getView().findViewById(R.id.tv_dealing_header_col2_open);
        mThirdColumnHeader = (TextView) getView().findViewById(R.id.fragment_dealing_header_column_3);
        mFourthColumnHeader = (TextView) getView().findViewById(R.id.fragment_dealing_header_column_4);
        mFifthColumnHeader = (TextView) getView().findViewById(R.id.fragment_dealing_header_column_5);
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
        IntentFilter intentFilterDeleteDealing = new IntentFilter(DeleteDealingService.ACTION_SERVICE_DELETE_DEALING);
        intentFilterDeleteDealing.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mDeleteDealingBroadcastReceiver, intentFilterDeleteDealing);

        requestOrders();
    }

    @Override
    public void onStop() {
        ((BaseActivity) getActivity()).showShadow();
        getActivity().unregisterReceiver(mOrdersBroadcastReceiver);
        getActivity().unregisterReceiver(mDeleteDealingBroadcastReceiver);
        sIsOpen = false;
        super.onStop();

    }
    private void requestOrders(){
        Intent intentService = new Intent(getActivity().getApplicationContext(), OrdersService.class);
        intentService.putExtra(OrdersService.FUNCTION, OrdersService.GET_ALL_ORDERS_DEALING);
        getActivity().startService(intentService);
    }

    private void requestDeleteDealing(OrderAnswer order) {
        Intent intentService = new Intent(getActivity().getApplicationContext(), DeleteDealingService.class);
        intentService.putExtra(Const.ACTION, DeleteDealingService.ACTION_SERVICE_DELETE_DEALING);
        getActivity().startService(intentService.putExtra(DeleteDealingService.TICKET, order.getTicket()));
    }

    private void analytics(int tabPosition) {
        GoogleAnalyticsUtil.sendEvent(
                GoogleAnalyticsUtil.ANALYTICS_DEALINGS_SCREEN,
                tabPosition == 0 ? GoogleAnalyticsUtil.ANALYTICS_TAB_OPEN_DEALINGS : GoogleAnalyticsUtil.ANALYTICS_TAB_CLOSE_DEALINGS,
                null,
                null
        );
    }

    private void checkOrders(List<OrderAnswer> currentOrders) {
        if (currentOrders.size() == 0) {
            if(currentTabPosition == OPEN_TAB_POSITION){
                tvEmptyDealing.setText(getResources().getString(R.string.empty_message_for_open_dealings));
            }else{
                tvEmptyDealing.setText(getResources().getString(R.string.empty_message_for_close_dealings));
            }
            mListLayout.setVisibility(View.GONE);
            mNoOrdersLayout.setVisibility(View.VISIBLE);
            mProgressLayout.setVisibility(View.GONE);
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
                            mAdapterOpen = new FragmentDealingOpenOrdersAdapter(currentOrders, v -> {
                                OrderAnswer order = (OrderAnswer) v.getTag();
                                long dateDifference = ConventDate.getDifferenceDate(order.getOpenTime(), order.getOptionsData().getExpirationTime());
                                if(GrandCapitalApplication.isTypeOptionAmerican &&
                                        ConventDate.getDifferenceDate(order.getOpenTime()) >= 61 &&
                                        dateDifference >= 120){
                                    mProgressLayout.setVisibility(View.VISIBLE);
                                    requestDeleteDealing(order);
                                } else {
                                    CustomDialog.showDialogInfo(getActivity(),
                                            getString(R.string.time_has_not_passed),
                                            getString(R.string.dealing_can_not_be_closed) +
                                                    (dateDifference >= 120 ? "\n" + (60 - ConventDate.getDifferenceDate(order.getOpenTime())) + " " +
                                                    getString(R.string.seconds_remaining) : ""));
                                }
                            });
                            CustomSharedPreferences.setAmtOpenDealings(getContext(), currentOrders.size());
                            ((BaseActivity) getActivity()).setDealings();
                            mRecyclerView.setAdapter(mAdapterOpen);
                        } else {
                            mAdapterOpen.updateAdapter(currentOrders);
                        }
                    } else {
                        mAdapterOpen = null;
                        mAdapterClose = new FragmentDealingCloseOrdersAdapter(currentOrders);
                        mRecyclerView.setAdapter(mAdapterClose);
                        //mProgressLayout.setVisibility(View.GONE);
                    }
                    setListHeader(currentTabPosition);
                    mProgressLayout.setVisibility(View.GONE);
                }
            } else {
                Log.d(TAG, "answer: " + response);
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
                CustomDialog.showDialogInfo(getActivity(), getResources().getString(R.string.request_error_title), getResources().getString(R.string.request_error_request));
                mProgressLayout.setVisibility(View.GONE);
            } else {
                requestOrders();
            }
        }
    }
}
