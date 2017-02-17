package com.elatesoftware.grandcapital.adapters.dealing;

import android.support.v7.widget.RecyclerView;
import com.elatesoftware.grandcapital.api.pojo.Order;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FragmentDealingCloseOrdersAdapter extends FragmentDealingOrdersAdapter {

    public FragmentDealingCloseOrdersAdapter(List<Order> orderList) {
        super(orderList);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        FragmentDealingViewHolder orderHolder = (FragmentDealingViewHolder) holder;

        orderHolder.mFirstColumn.setText(orderList.get(position).getSymbol().replace("_OP", ""));
        orderHolder.mSecondColumn.setText(orderList.get(position).getOpenPrice().toString());
        orderHolder.mThirdColumn.setText(orderList.get(position).getClosePrice().toString());

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        orderHolder.mFourthColumn.setText(String.format("$%s", numberFormat.format(((double)orderList.get(position).getVolume()) / 100)));

        String sign = "";
        if(orderList.get(position).getProfit() < 0){
            sign = "-";
        }
        orderHolder.mFifthColumn.setText(String.format(sign + "$%s", numberFormat.format(Math.abs(orderList.get(position).getProfit()))));

        if(orderList.get(position).getProfit() < 0){
            orderHolder.mThirdColumn.setTextColor(DOWN_TEXT_COLOR);
            orderHolder.mArrow.setImageDrawable(DOWN_DRAWABLE);
        }
        else{
            orderHolder.mThirdColumn.setTextColor(UP_TEXT_COLOR);
            orderHolder.mArrow.setImageDrawable(UP_DRAWABLE);
        }
    }
}
