package com.elatesoftware.grandcapital.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;

public class GrandCapitalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int WHITE_ROW_COLOR =  GrandCapitalApplication.getAppContext().getResources().getColor(R.color.dealingListItemColor);
    private static final int TRANSPARENT_ROW_COLOR = Color.TRANSPARENT;
    public static final int ORDER_EVEN = 0;
    public static final int ORDER_ODD = 1;

    public int order = ORDER_ODD;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position % 2 == order) {
            holder.itemView.setBackgroundColor(WHITE_ROW_COLOR);
        } else {
            holder.itemView.setBackgroundColor(TRANSPARENT_ROW_COLOR);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
