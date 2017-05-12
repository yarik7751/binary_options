package com.elatesoftware.grandcapital.services;

import android.app.IntentService;
import android.content.Intent;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;
import com.elatesoftware.grandcapital.api.pojo.EarlyClosureAnswer;
import com.elatesoftware.grandcapital.api.pojo.InfoAnswer;
import com.elatesoftware.grandcapital.api.pojo.Instrument;
import com.elatesoftware.grandcapital.utils.Const;

/**
 * Created by Ярослав Левшунов on 23.03.2017.
 */

public class EarlyClosureService extends IntentService {

    public final static String RESPONSE = "response";
    public static final String ACTION_SERVICE_EARLY_CLOSURE= "com.elatesoftware.grandcapital.services.EarlyClosureService";
    private final static String NAME_STREAM = "EarlyClosureService";

    public final static String SYMBOL = "symbol";
    public final static String IS_AMERICAN = "isAmerican";
    public final static String TIME = "time";
    public final static String PERCENT = "percent";
    boolean isAmerican = false;

    public EarlyClosureService() {
        super(NAME_STREAM);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String symbol = intent.getStringExtra(SYMBOL);
        String response = GrandCapitalApi.getEarlyClosureAnswer();
        int time = intent.getIntExtra(TIME, 0);
        int percent = parseResponse(response, symbol, time);

        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_EARLY_CLOSURE);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);

        responseIntent.putExtra(RESPONSE, response);
        responseIntent.putExtra(IS_AMERICAN, isAmerican);
        responseIntent.putExtra(PERCENT, percent);

        sendBroadcast(responseIntent);
    }

    private int parseResponse(String response, String symbol, int timeValue) {
        if (response != null && response.equals(Const.RESPONSE_CODE_SUCCESS) && EarlyClosureAnswer.getInstance() != null &&
                InfoAnswer.getInstance() != null && InfoAnswer.getInstance().getGroup() != null) {
            for (Instrument instrument : EarlyClosureAnswer.getInstance().getInstruments()) {
                if (symbol != null && !symbol.equals("") && instrument.getSymbol().contains(symbol)) {
                    String typeOption = InfoAnswer.getInstance().getGroup().getOptionsStyle();
                    int percent = 100;
                    if (typeOption.contains(Const.TYPE_OPTION_AMERICAN)) {
                        isAmerican = true;
                        percent = instrument.getWinFull();
                    } else if (typeOption.contains(Const.TYPE_OPTION_EUROPEAN)) {
                        isAmerican = false;
                        if (timeValue < 5) {
                            percent = instrument.getWinLt5();
                        } else if (timeValue >= 5 && timeValue < 15) {
                            percent = instrument.getWin5();
                        } else if (timeValue >= 15 && timeValue < 30) {
                            percent = instrument.getWin15();
                        } else {
                            percent = instrument.getWin30();
                        }
                    }
                    return percent;
                }
            }
        }
        return 0;
    }
}
