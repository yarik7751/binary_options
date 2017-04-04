package com.elatesoftware.grandcapital.views.items.chart;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.utils.ConventImage;
import com.elatesoftware.grandcapital.utils.ConventString;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.items.CustomDialog;

/**
 * Created by Darya on 30.03.2017.
 */

public class ViewInfoHelper {

    private final static String TAG_OPEN_DEALING = "openDealingView";
    private final static String TAG_CLOSE_DEALING = "closeDealingView";
    private final static int INTERVAL_SHOW_LABEL = 4000;

    private View mCloseDealingView;
    private View mOpenDealingView;
    private RelativeLayout rlChart;

    public ViewInfoHelper(RelativeLayout rl){
        Context mContext = GrandCapitalApplication.getAppContext();
        rlChart = rl;
        mOpenDealingView = LayoutInflater.from(mContext).inflate(R.layout.incl_label_open_dealing, null);
        mCloseDealingView = LayoutInflater.from(mContext).inflate(R.layout.incl_label_close_dealing, null);
        mOpenDealingView.setTag(TAG_OPEN_DEALING);
        mCloseDealingView.setTag(TAG_CLOSE_DEALING);
    }

    public void showViewOpenDealing(String active, String amount, String time) {
        ((TextView) mOpenDealingView.findViewById(R.id.tvActive)).setText(active);
        ((TextView) mOpenDealingView.findViewById(R.id.tvAmount)).setText(amount);
        ((TextView) mOpenDealingView.findViewById(R.id.tvTime)).setText(time);

        View vCloseDealings = rlChart.findViewWithTag(TAG_CLOSE_DEALING);
        if(vCloseDealings != null) {
            rlChart.removeView(vCloseDealings);
        }
        if (rlChart.findViewWithTag(TAG_OPEN_DEALING) != null) {
            rlChart.updateViewLayout(mOpenDealingView, ConventImage.getRelativeParams());
        } else {
            rlChart.addView(mOpenDealingView, ConventImage.getRelativeParams());
        }
        new Handler().postDelayed(() -> rlChart.removeView(mOpenDealingView), INTERVAL_SHOW_LABEL);
    }

    private void showViewCloseDealing(OrderAnswer answer) {
        if (answer != null) {
            ((TextView) mCloseDealingView.findViewById(R.id.tvActiveValue)).setText(String.valueOf(answer.getSymbol()));
            ((TextView) mCloseDealingView.findViewById(R.id.tvPriceValue)).setText(ConventString.getRoundNumber(answer.getClosePrice()));
            ((TextView) mCloseDealingView.findViewById(R.id.tvProfitValue)).setText(String.valueOf(answer.getProfitStr()));
            View vOpenDealings = rlChart.findViewWithTag(TAG_OPEN_DEALING);
            if(vOpenDealings != null) {
                rlChart.removeView(vOpenDealings);
            }
            if (rlChart.findViewWithTag(TAG_CLOSE_DEALING) != null) {
                rlChart.updateViewLayout(mCloseDealingView, ConventImage.getRelativeParams());
            } else {
                rlChart.addView(mCloseDealingView, ConventImage.getRelativeParams());
            }
            new Handler().postDelayed(() -> rlChart.removeView(mCloseDealingView), INTERVAL_SHOW_LABEL);
        }
    }

    public void updateSettingsCloseDealing(OrderAnswer order, Activity activity){
        CustomSharedPreferences.setAmtCloseDealings(activity, CustomSharedPreferences.getAmtCloseDealings(activity) + 1);
        showViewCloseDealing(order);
        ((BaseActivity) activity).setDealings();
        BaseActivity.getToolbar().setDealingSelectIcon();
        CustomDialog.showViewOpenRealAccount(activity);
    }
}
