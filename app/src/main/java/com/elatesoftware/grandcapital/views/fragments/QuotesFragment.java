package com.elatesoftware.grandcapital.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.adapters.quotes.OnSharedPreferencesChange;
import com.elatesoftware.grandcapital.adapters.quotes.QuotesAdapter;
import com.elatesoftware.grandcapital.api.pojo.InfoAnswer;
import com.elatesoftware.grandcapital.api.pojo.Instrument;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.services.InfoUserService;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuotesFragment extends Fragment {

    public static final String TAG = "QuotesFragment_TAG";
    public static final int INTERVAL = 3000;

    static final int DOWN_TEXT_COLOR = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.dealingListDownOrderColor);
    static final int UP_TEXT_COLOR = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.dealingListUpOrderColor);

    private RecyclerView rvSelectedQuotes, rvAllQuotes;
    private LinearLayout llProgressBar;

    private List<Instrument> lastInstruments;
    private GetResponseInfoBroadcastReceiver mInfoBroadcastReceiver;

    private QuotesAdapter adapterSelected;
    private QuotesAdapter adapterAllSelected;

    private Handler handler;

    private Runnable runnableQuotes = new Runnable() {
        @Override
        public void run() {
            Intent intentService = new Intent(getActivity().getApplicationContext(), InfoUserService.class);
            getActivity().startService(intentService);
            handler.postDelayed(runnableQuotes, INTERVAL);
        }
    };

    private OnSharedPreferencesChange onSharedPreferencesChange = new OnSharedPreferencesChange() {
        @Override
        public void onChange() {
            setData(lastInstruments);
        }
    };

    private static QuotesFragment fragment = null;
    public static QuotesFragment getInstance() {
        if (fragment == null) {
            fragment = new QuotesFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quotes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().switchTab(BaseActivity.QUOTES_POSITION);
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_quotes));
        BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_OTHER_FRAGMENT);
        BaseActivity.getToolbar().switchTab(5);

        rvAllQuotes = (RecyclerView) view.findViewById(R.id.rv_all_quotes);
        rvSelectedQuotes = (RecyclerView) view.findViewById(R.id.rv_selected_quotes);
        llProgressBar = (LinearLayout) view.findViewById(R.id.layout_progress_bar);

        rvAllQuotes.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvSelectedQuotes.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvAllQuotes.setNestedScrollingEnabled(false);
        rvSelectedQuotes.setNestedScrollingEnabled(false);

        handler = new Handler();
        lastInstruments = new ArrayList<>();
        if(InfoAnswer.getInstance() != null) {
            lastInstruments = InfoAnswer.getInstance().getInstruments();
        }
        adapterSelected = new QuotesAdapter(getActivity(), lastInstruments, QuotesAdapter.SELECT_QUOTES, onSharedPreferencesChange);
        adapterAllSelected = new QuotesAdapter(getActivity(), lastInstruments, QuotesAdapter.ALL_QUOTES, onSharedPreferencesChange);

        rvAllQuotes.setAdapter(adapterAllSelected);
        rvSelectedQuotes.setAdapter(adapterSelected);
    }

    @Override
    public void onResume() {
        super.onResume();
        mInfoBroadcastReceiver = new GetResponseInfoBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(InfoUserService.ACTION_SERVICE_GET_INFO);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mInfoBroadcastReceiver, intentFilter);
        //llProgressBar.setVisibility(View.VISIBLE);
        handler.postDelayed(runnableQuotes, INTERVAL);
    }

    @Override
    public void onStop() {
        handler.removeCallbacks(runnableQuotes);
        getActivity().unregisterReceiver(mInfoBroadcastReceiver);
        super.onStop();
    }

    private void setData(List<Instrument> quotes) {
        adapterSelected.setList(quotes);
        adapterSelected.notifyDataSetChanged();

        adapterAllSelected.setList(quotes);
        adapterAllSelected.notifyDataSetChanged();
    }

    public class GetResponseInfoBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String responseInfo = intent.getStringExtra(InfoUserService.RESPONSE_INFO);
            String responseSummary = intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY);
            if(responseInfo != null && responseSummary != null && responseInfo.equals(Const.RESPONSE_CODE_SUCCESS) && responseSummary.equals(Const.RESPONSE_CODE_SUCCESS)){
                if(InfoAnswer.getInstance() != null) {
                    comparisonQuotes(InfoAnswer.getInstance().getInstruments());
                }
            }
        }
    }

    private void comparisonQuotes(List<Instrument> newInstruments) {
        if(newInstruments != null  && lastInstruments != null) {
            for (int i = 0; i < lastInstruments.size(); i++) {
                if(newInstruments.get(i) != null) {
                    switch (lastInstruments.get(i).getAsk().compareTo(newInstruments.get(i).getAsk())){
                        case 0:
                            newInstruments.get(i).setColor(lastInstruments.get(i).getColor());
                            break;
                        case 1:
                            newInstruments.get(i).setColor(DOWN_TEXT_COLOR);
                            break;
                        case -1:
                            newInstruments.get(i).setColor(UP_TEXT_COLOR);
                            break;
                        default:
                            newInstruments.get(i).setColor(lastInstruments.get(i).getColor());
                            break;
                    }
                } else {
                    return;
                }
            }
            lastInstruments = newInstruments;
            setData(lastInstruments);
            llProgressBar.setVisibility(View.GONE);
        }
    }
}
