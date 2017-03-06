package com.elatesoftware.grandcapital.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.adapters.quotes.OnSharedPreferencesChange;
import com.elatesoftware.grandcapital.adapters.quotes.QuotesAdapter;
import com.elatesoftware.grandcapital.api.pojo.InfoAnswer;
import com.elatesoftware.grandcapital.api.pojo.Instrument;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.services.InfoUserService;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;

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

    private List<Instrument> lastInstruments;
    private GetResponseInfoBroadcastReceiver mInfoBroadcastReceiver;

    Handler handler = new Handler();
    Runnable runnableQuotes = new Runnable() {
        @Override
        public void run() {
            Intent intentMyIntentService = new Intent(getContext(), InfoUserService.class);
            getActivity().startService(intentMyIntentService);
            handler.postDelayed(runnableQuotes, INTERVAL);
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
    public void onResume() {
        super.onResume();
        mInfoBroadcastReceiver = new GetResponseInfoBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(InfoUserService.ACTION_SERVICE_GET_INFO);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mInfoBroadcastReceiver, intentFilter);
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

        if(InfoAnswer.getInstance() != null) {
            lastInstruments = InfoAnswer.getInstance().getInstruments();
            setData(lastInstruments);
            handler.postDelayed(runnableQuotes, INTERVAL);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableQuotes);
        getActivity().unregisterReceiver(mInfoBroadcastReceiver);
    }

    private void setData(List<Instrument> quotes) {
        rvAllQuotes.setAdapter(new QuotesAdapter(getActivity(), quotes, QuotesAdapter.ALL_QUOTES, onSharedPreferencesChange));
        rvSelectedQuotes.setAdapter(new QuotesAdapter(getActivity(), quotes, QuotesAdapter.SELECT_QUOTES, onSharedPreferencesChange));
    }

    OnSharedPreferencesChange onSharedPreferencesChange = new OnSharedPreferencesChange() {
        @Override
        public void onChange() {
            Log.d(TAG, "onChange()");
            setData(lastInstruments);
        }
    };

    public class GetResponseInfoBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra(InfoUserService.RESPONSE_INFO) != null && intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY) != null){
                if(intent.getStringExtra(InfoUserService.RESPONSE_INFO).equals("200") && intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY).equals("200")){
                    if(InfoAnswer.getInstance() != null) {
                        Log.d(TAG, "comparisonQuotes onReceive");
                        List<Instrument> newInstruments = InfoAnswer.getInstance().getInstruments();
                        comparisonQuotes(newInstruments);
                    }
                }
            }
        }
    }

    private void comparisonQuotes(List<Instrument> newInstruments) {
        for(int i = 0; i < lastInstruments.size(); i++) {
            if(newInstruments.get(i).getSymbol().equals("EURUSD")) {
                Log.d(TAG, "EURUSD: " + newInstruments.get(i).getAsk().doubleValue());
            }
            if(lastInstruments.get(i).getAsk().doubleValue() < newInstruments.get(i).getAsk().doubleValue()) {
                newInstruments.get(i).setColor(UP_TEXT_COLOR);
            } else if(lastInstruments.get(i).getAsk().doubleValue() > newInstruments.get(i).getAsk().doubleValue()) {
                newInstruments.get(i).setColor(DOWN_TEXT_COLOR);
            } else {
                newInstruments.get(i).setColor(lastInstruments.get(i).getColor());
            }
        }
        lastInstruments = newInstruments;
        setData(lastInstruments);
    }
}
