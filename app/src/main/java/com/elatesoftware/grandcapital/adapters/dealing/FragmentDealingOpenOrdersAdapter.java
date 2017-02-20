package com.elatesoftware.grandcapital.adapters.dealing;

import android.support.v7.widget.RecyclerView;
import com.elatesoftware.grandcapital.api.pojo.Order;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.elatesoftware.grandcapital.utils.ConventDate;

public class FragmentDealingOpenOrdersAdapter extends FragmentDealingOrdersAdapter {

    public FragmentDealingOpenOrdersAdapter(List<Order> orderList) {
        super(orderList);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        FragmentDealingViewHolder orderHolder = (FragmentDealingViewHolder) holder;
        orderHolder.mFirstColumn.setText(orderList.get(position).getSymbol().replace("_OP", ""));
        orderHolder.mSecondColumn.setText(String.valueOf(orderList.get(position).getOpenPrice()));
        orderHolder.mThirdColumn.setText(String.valueOf(orderList.get(position).getClosePrice()));

        /**объем приходит умноженный на 100*/
        double amount = ((double) orderList.get(position).getVolume()) / 100;
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String numberAsString = numberFormat.format(amount);

        orderHolder.mFourthColumn.setText(String.format("$%s", numberAsString));
        orderHolder.mFifthColumn.setText(ConventDate.getConventDate(orderList.get(position).getOptionsData().getExpirationTime()));

        if (orderList.get(position).getOpenPrice() < orderList.get(position).getClosePrice()) {
            orderHolder.mThirdColumn.setTextColor(DOWN_TEXT_COLOR);
            orderHolder.mArrow.setImageDrawable(DOWN_DRAWABLE);
        } else {
            orderHolder.mThirdColumn.setTextColor(UP_TEXT_COLOR);
            orderHolder.mArrow.setImageDrawable(UP_DRAWABLE);
        }
    }
}
