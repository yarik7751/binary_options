package com.elatesoftware.grandcapital.utils;

import android.util.TypedValue;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;

/**
 * Created by Дарья Высокович on 28.03.2017.
 */

public class Const {

    public final static String SYMBOL = "EURUSD";
    public final static String CMD_HEIGHT = "0";
    public final static String CMD_LOWER = "1";

    public final static String RESPONSE_CODE_SUCCESS = "200";
    public final static String CODE_SUCCESS_DELETE_DEALING = "204";
    public final static String RESPONSE_CODE_ERROR = "400";
    public final static String ACTION = "ACTION";

    public final static String TYPE_OPTION_AMERICAN = "american";
    public final static String TYPE_OPTION_EUROPEAN = "european";

    public final static String URL_GRAND_CAPITAL_SIGN_UP = "https://grand.capital/4";
    public final static String URL_GRAND_CAPITAL_ACCOUNT = "http://grandcapital.ru/account/";
    public final static String URL_GRAND_CAPITAL_DEPOSIT = "/payments/deposit";
    public final static String URL_GRAND_CAPITAL_WITHDRAW = "/payments/withdraw";

    public final static int INTERVAL_ANIM_PANEL = 200;
    public final static int INTERVAL_ITEM = 100;
    public final static int MAX_TIME_MIN = 2880;

    public final static String TREK_ANALITICS = "UA-96895164-2";
    public final static String CHART_WIDGET_ID = "0a9ecd18-54cc-4ecc-9b36-fabce04aa3b8";

    public final static float OFFSET_CHART_Y =  getOffsetChartY();

    public final static String ANALYTICS_QUOTES_SCREEN = "QUOTES_SCREEN";
    public final static String ANALYTICS_BUTTON_FAVORITES = "BUTTON_FAVORITES";
    public final static String ANALYTICS_BUTTON_DELETE_FAVORITES = "BUTTON_DELETE_FAVORITES";

    public final static String ANALYTICS_SUPPORT_SCREEN = "SUPPORT_SCREEN";
    public final static String ANALYTICS_BUTTON_SEND_MESSAGE = "BUTTON_SEND_MESSAGE";

    public final static String ANALYTICS_TERMINAL_SCREEN = "TERMINAL_SCREEN";
    public final static String ANALYTICS_BUTTON_CHANGE_ACTIVE = "BUTTON_CHANGE_ACTIVE";
    public final static String ANALYTICS_BUTTON_CHANGE_AMOUNT_INVESTMENTS = "BUTTON_CHANGE_AMOUNT_INVESTMENTS";
    public final static String ANALYTICS_BUTTON_CHANGE_TIME = "BUTTON_CHANGE_TIME";
    public final static String ANALYTICS_BUTTON_UP = "BUTTON_UP";
    public final static String ANALYTICS_BUTTON_DOWN = "BUTTON_DOWN";
    public final static String ANALYTICS_BUTTON_CLOSE_DEALINGS = "BUTTON_CLOSE_DEALINGS";
    public final static String ANALYTICS_BUTTON_WIDGET_SIGNALS = "BUTTON_WIDGET_SIGNALS";

    public final static String ANALYTICS_DEALINGS_SCREEN = "DEALINGS_SCREEN";
    public final static String ANALYTICS_TAB_OPEN_DEALINGS = "TAB_OPEN_DEALINGS";
    public final static String ANALYTICS_TAB_CLOSE_DEALINGS = "TAB_CLOSE_DEALINGS";

    public final static String ANALYTICS_IN_OUT_SCREEN = "IN_OUT_SCREEN";
    public final static String ANALYTICS_BUTTON_WITHDRAW = "BUTTON_WITHDRAW";
    public final static String ANALYTICS_BUTTON_DEPOSIT = "BUTTON_DEPOSIT";

    public final static String ANALYTICS_QUESTION_SCREEN = "QUESTION_SCREEN";
    public final static String ANALYTICS_LIST_QUESTION = "LIST_QUESTION";

    public final static String ANALYTICS_PROMOTIONS_SCREEN = "PROMOTIONS_SCREEN";
    public final static String ANALYTICS_LIST_PROMOTION = "LIST_PROMOTION";

    public final static String ANALYTICS_EVENT_EXIT = "EVENT_EXIT";

    private static float getOffsetChartY(){
        TypedValue typedValue = new TypedValue();
        GrandCapitalApplication.getAppContext().getResources().getValue(R.dimen.offset_y_chart, typedValue, true);
        return typedValue.getFloat();
    }
}
