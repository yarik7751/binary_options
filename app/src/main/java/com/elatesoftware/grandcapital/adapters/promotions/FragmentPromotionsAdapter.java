package com.elatesoftware.grandcapital.adapters.promotions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.adapters.GrandCapitalListAdapter;
import com.elatesoftware.grandcapital.api.pojo.BinaryOptionAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.fragments.PromotionsFragment;
import com.elatesoftware.grandcapital.views.fragments.QuestionFragment;
import com.elatesoftware.grandcapital.views.fragments.WebFragment;
import com.google.android.gms.analytics.HitBuilders;

/**
 * Created by Ярослав Левшунов on 24.02.2017.
 */

public class FragmentPromotionsAdapter extends GrandCapitalListAdapter {

    private Context context;
    private BinaryOptionAnswer binaryOptionAnswer;

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
                BaseActivity.sMainTagFragment = PromotionsFragment.class.getName();
                WebFragment webFragment = WebFragment.getInstance(binaryOptionAnswer.getElements().get(position).getPic());
                BaseActivity.addNextFragment(webFragment);
                /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(binaryOptionAnswer.getElements().get(position).getPic()));
                context.startActivity(browserIntent);*/
            }
        });
        promotionsViewHolder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(QuestionFragment.HEADER_TEXT, binaryOptionAnswer.getElements().get(position).getShortDescription().toUpperCase());
            bundle.putString(QuestionFragment.CONTENT_TEXT, binaryOptionAnswer.getElements().get(position).getLongDescription());

            GrandCapitalApplication.getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory(Const.ANALYTICS_PROMOTIONS_SCREEN)
                    .setAction(Const.ANALYTICS_LIST_PROMOTION)
                    .setLabel(binaryOptionAnswer.getElements().get(position).getShortDescription())
                    .build()
            );

            BaseActivity.sMainTagFragment = PromotionsFragment.class.getName();
            QuestionFragment questionFragment = new QuestionFragment();
            questionFragment.setArguments(bundle);
            BaseActivity.addNextFragment(questionFragment);
        });
    }

    @Override
    public int getItemCount() {
        return binaryOptionAnswer.getElements().size();
    }

    private class FragmentPromotionsViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public ImageView imgLink;
        public View itemView;

        public FragmentPromotionsViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            imgLink = (ImageView) itemView.findViewById(R.id.img_link);
        }
    }
}
