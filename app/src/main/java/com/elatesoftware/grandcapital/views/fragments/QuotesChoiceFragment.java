package com.elatesoftware.grandcapital.views.fragments;

/**
 * Created by Дарья Высокович on 18.05.2017.
 */

public class QuotesChoiceFragment /*extends Fragment*/ {
/*
    public static final int INTERVAL = 3000;
    private static String symbol = "";
    public static final String SYMBOL = "symbol";

    static final int DOWN_TEXT_COLOR = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.dealingListDownOrderColor);
    static final int UP_TEXT_COLOR = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.dealingListUpOrderColor);

    private RecyclerView rvSelectedQuotes, rvAllQuotes;
    private List<Instrument> lastInstruments;
    private GetResponseInfoBroadcastReceiver mInfoBroadcastReceiver;

    private QuotesChoiceAdapter quotesSelectChoiceAdapter;
    private QuotesChoiceAdapter quotesAllChoiceAdapter;

    private Handler handler = new Handler();
    private Runnable runnableQuotes = new Runnable() {
        @Override
        public void run() {
            Intent intentMyIntentService = new Intent(getActivity(), InfoUserService.class);
            getActivity().startService(intentMyIntentService);
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
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.choose_an_active));
        BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_EMPTY_FRAGMENT);
        BaseActivity.getToolbar().deselectAll();
        BaseActivity.getToolbar().setBurgerType(ToolbarFragment.BURGER_BACK_PRESSED);

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
        if(InfoAnswer.getInstance() != null  && InfoAnswer.getInstance().getInstruments() != null) {
            lastInstruments = InfoAnswer.getInstance().getInstruments();
            setData(lastInstruments);
            handler.postDelayed(runnableQuotes, INTERVAL);
        }
        Intent intentMyIntentService = new Intent(getActivity(), InfoUserService.class);
        getActivity().startService(intentMyIntentService);
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mInfoBroadcastReceiver);
        super.onStop();
    }

    private void setData(List<Instrument> quotes) {
        quotesSelectChoiceAdapter = new QuotesChoiceAdapter(getActivity(), quotes, QuotesAdapter.SELECT_QUOTES, symbol, (inst, view) -> {

        });
        quotesAllChoiceAdapter = new QuotesChoiceAdapter(getActivity(), quotes, QuotesAdapter.ALL_QUOTES, symbol, (inst, view) -> {

        });
        rvAllQuotes.setAdapter(quotesAllChoiceAdapter);
        rvSelectedQuotes.setAdapter(quotesSelectChoiceAdapter);
    }

    public class GetResponseInfoBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String responseInfo = intent.getStringExtra(InfoUserService.RESPONSE_INFO);
            String responseSummary = intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY);
            if(responseInfo != null && responseSummary != null && responseInfo.equals(Const.RESPONSE_CODE_SUCCESS) && responseSummary.equals(Const.RESPONSE_CODE_SUCCESS)){
                if(InfoAnswer.getInstance() != null) {
                    List<Instrument> newInstruments = InfoAnswer.getInstance().getInstruments();
                    comparisonQuotes(newInstruments);
                }
            }
        }
    }

    private void comparisonQuotes(List<Instrument> newInstruments) {
        if(newInstruments != null && lastInstruments != null) {
            for (int i = 0; i < lastInstruments.size(); i++) {
                if(newInstruments.get(i) != null) {
                    if (lastInstruments.get(i).getAsk() < newInstruments.get(i).getAsk()) {
                        newInstruments.get(i).setColor(UP_TEXT_COLOR);
                    } else if (lastInstruments.get(i).getAsk() > newInstruments.get(i).getAsk()) {
                        newInstruments.get(i).setColor(DOWN_TEXT_COLOR);
                    } else {
                        newInstruments.get(i).setColor(lastInstruments.get(i).getColor());
                    }
                } else {
                    return;
                }
            }
            lastInstruments = newInstruments;
            setData(newInstruments);
        }
    }*/
}
