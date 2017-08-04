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
import com.elatesoftware.grandcapital.adapters.quotes.QuotesChoiceAdapter;
import com.elatesoftware.grandcapital.api.pojo.InfoAnswer;
import com.elatesoftware.grandcapital.api.pojo.Instrument;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.models.QuoteType;
import com.elatesoftware.grandcapital.services.ChoiceActiveService;
import com.elatesoftware.grandcapital.services.InfoUserService;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Дарья Высокович on 18.05.2017.
 */

public class QuotesChoiceFragment extends Fragment {

    public static final int INTERVAL = 3000;
    private static String symbol = "";
    public static final String SYMBOL = "symbol";
    private LinearLayout progressBar;

    static final int DOWN_TEXT_COLOR = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.dealingListDownOrderColor);
    static final int UP_TEXT_COLOR = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.dealingListUpOrderColor);

    private RecyclerView rvQuotes;
    private QuotesChoiceAdapter quotesAdapter;
    private List<Instrument> lastInstruments;
    private GetResponseInfoBroadcastReceiver mInfoBroadcastReceiver;
    public static QuoteType currentQuoteType;

    private Handler handler = new Handler();
    private Runnable runnableQuotes = new Runnable() {
        @Override
        public void run() {
            Intent intentService = new Intent(getContext(), InfoUserService.class);
            getActivity().startService(intentService);
            handler.postDelayed(runnableQuotes, INTERVAL);
        }
    };

    private static QuotesChoiceFragment fragment = null;
    public static QuotesChoiceFragment getInstance() {
        if (fragment == null) {
            fragment = new QuotesChoiceFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        symbol = getArguments().getString(SYMBOL);
        return inflater.inflate(R.layout.fragment_quotes_choice, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().mTabLayout.setOnLoadData(() -> {
            BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.choose_an_active));
            BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_EMPTY_FRAGMENT);
            BaseActivity.getToolbar().deselectAll();
            BaseActivity.getToolbar().setBurgerType(ToolbarFragment.BURGER_BACK_PRESSED);
        });
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.choose_an_active));
        BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_EMPTY_FRAGMENT);
        BaseActivity.getToolbar().deselectAll();
        BaseActivity.getToolbar().setBurgerType(ToolbarFragment.BURGER_BACK_PRESSED);
        progressBar = (LinearLayout) view.findViewById(R.id.layout_progress_bar);

        lastInstruments = new ArrayList<>();
        rvQuotes = (RecyclerView) view.findViewById(R.id.rv_quotes);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvQuotes.setLayoutManager(mLayoutManager);
        quotesAdapter = new QuotesChoiceAdapter(getActivity(), lastInstruments, symbol, (s, v) -> {
            currentQuoteType = s;
            Intent intent = new Intent(getContext(), ChoiceActiveService.class);
            intent.putExtra(ChoiceActiveService.SYMBOL, s.getInstrumentQuote().getSymbol());
            getContext().startService(intent);

        });
        rvQuotes.setAdapter(quotesAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mInfoBroadcastReceiver = new GetResponseInfoBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(InfoUserService.ACTION_SERVICE_GET_INFO);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mInfoBroadcastReceiver, intentFilter);
    }
    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        if(InfoAnswer.getInstance() != null  && InfoAnswer.getInstance().getInstruments() != null) {
            lastInstruments = InfoAnswer.getInstance().getInstruments();
            quotesAdapter.setListQuotes(lastInstruments);
            quotesAdapter.notifyDataSetChanged();
        }
        handler.postDelayed(runnableQuotes,INTERVAL);
    }
    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mInfoBroadcastReceiver);
        handler.removeCallbacks(runnableQuotes);
        super.onStop();
    }

    private void comparisonQuotes(List<Instrument> newInstruments) {
        if(newInstruments != null && lastInstruments != null) {
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
            lastInstruments.clear();
            lastInstruments.addAll(newInstruments);
            quotesAdapter.setListQuotes(lastInstruments);
            quotesAdapter.notifyDataSetChanged();
        }
    }
    public class GetResponseInfoBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String responseInfo = intent.getStringExtra(InfoUserService.RESPONSE_INFO);
            String responseSummary = intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY);
            if(responseInfo != null && responseSummary != null && responseInfo.equals(Const.RESPONSE_CODE_SUCCESS) && responseSummary.equals(Const.RESPONSE_CODE_SUCCESS)){
                if(InfoAnswer.getInstance() != null) {
                    comparisonQuotes(InfoAnswer.getInstance().getInstruments());
                    progressBar.setVisibility(View.GONE);
                    rvQuotes.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
