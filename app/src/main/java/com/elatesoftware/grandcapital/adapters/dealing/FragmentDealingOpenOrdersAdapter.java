package com.elatesoftware.grandcapital.adapters.dealing;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.utils.ConventString;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FragmentDealingOpenOrdersAdapter extends FragmentDealingOrdersAdapter {

    private OnCloseDealing onCloseDealing;

    public FragmentDealingOpenOrdersAdapter(List<OrderAnswer> orderList, OnCloseDealing _onCloseDealing) {
        super(orderList);
        onCloseDealing = _onCloseDealing;
        order = ORDER_EVEN;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        FragmentDealingViewHolder orderHolder = (FragmentDealingViewHolder) holder;
        orderHolder.mFirstColumn.setText(orderList.get(position).getSymbol());
        orderHolder.mSecondColumn.setText(ConventString.getRoundNumber(5, orderList.get(position).getOpenPrice()));
        orderHolder.mThirdColumn.setText(ConventString.getRoundNumber(5, orderList.get(position).getClosePrice()));

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

        if(!GrandCapitalApplication.isTypeOptionAmerican) {
            orderHolder.slDealing.setSwipeEnabled(false);
        }

        orderHolder.imgCloseDealing.setTag(orderList.get(position));
        orderHolder.imgCloseDealing.setOnClickListener(v -> {
            orderHolder.slDealing.close();
            onCloseDealing.onClose(v);
        });
    }

    public void updateAdapter(List<OrderAnswer> _orderList) {
        orderList = _orderList;
        notifyDataSetChanged();
    }

    public interface OnCloseDealing {
        void onClose(View v);
    }
}
