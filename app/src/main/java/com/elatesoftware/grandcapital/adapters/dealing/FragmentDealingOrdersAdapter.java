package com.elatesoftware.grandcapital.adapters.dealing;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.adapters.GrandCapitalListAdapter;
import com.elatesoftware.grandcapital.api.pojo.Order;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;

import java.util.List;

class FragmentDealingOrdersAdapter extends GrandCapitalListAdapter {

    List<Order> orderList;

    static final int DOWN_TEXT_COLOR = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.dealingListDownOrderColor);
    static final int UP_TEXT_COLOR = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.dealingListUpOrderColor);

    static final Drawable DOWN_DRAWABLE = GrandCapitalApplication.getAppContext().getResources().getDrawable(R.drawable.down);
    static final Drawable UP_DRAWABLE = GrandCapitalApplication.getAppContext().getResources().getDrawable(R.drawable.up);

    FragmentDealingOrdersAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public FragmentDealingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_dealing_list_item, parent, false);
        return new FragmentDealingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
