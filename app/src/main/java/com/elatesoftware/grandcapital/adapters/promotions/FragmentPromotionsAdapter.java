package com.elatesoftware.grandcapital.adapters.promotions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.adapters.GrandCapitalListAdapter;
import com.elatesoftware.grandcapital.api.pojo.BinaryOptionAnswer;

/**
 * Created by Ярослав Левшунов on 24.02.2017.
 */

public class FragmentPromotionsAdapter extends GrandCapitalListAdapter {

    private Context context;
    private BinaryOptionAnswer binaryOptionAnswer;

    private class FragmentPromotionsViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public ImageView imgLink;

        public FragmentPromotionsViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            imgLink = (ImageView) itemView.findViewById(R.id.img_link);
        }
    }

    public FragmentPromotionsAdapter(Context context, BinaryOptionAnswer binaryOptionAnswer) {
        this.context = context;
        this.binaryOptionAnswer = binaryOptionAnswer;
    }

    @Override
    public FragmentPromotionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_promotions_item, parent, false);
        return new FragmentPromotionsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        FragmentPromotionsViewHolder promotionsViewHolder = (FragmentPromotionsViewHolder) holder;
        promotionsViewHolder.tvName.setText(binaryOptionAnswer.getElements().get(position).getShortDescription().toUpperCase());
        promotionsViewHolder.imgLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(binaryOptionAnswer.getElements().get(position).getPic()));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return binaryOptionAnswer.getElements().size();
    }
}
