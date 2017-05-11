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
import com.elatesoftware.grandcapital.models.User;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.utils.GoogleAnalyticsUtil;
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
        promotionsViewHolder.imgLink.setOnClickListener(v -> {
            BaseActivity.sMainTagFragment = PromotionsFragment.class.getName();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(binaryOptionAnswer.getElements().get(position).getPic()));
            context.startActivity(browserIntent);
//            WebFragment webFragment = WebFragment.getInstance(binaryOptionAnswer.getElements().get(position).getPic());
//            BaseActivity.addNextFragment(webFragment);
            /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(binaryOptionAnswer.getElements().get(position).getPic()));
            context.startActivity(browserIntent);*/
        });
        promotionsViewHolder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(QuestionFragment.HEADER_TEXT, binaryOptionAnswer.getElements().get(position).getShortDescription().toUpperCase());
            bundle.putString(QuestionFragment.CONTENT_TEXT, binaryOptionAnswer.getElements().get(position).getLongDescription());

            GoogleAnalyticsUtil.sendEvent(Const.ANALYTICS_PROMOTIONS_SCREEN, Const.ANALYTICS_LIST_PROMOTION, binaryOptionAnswer.getElements().get(position).getShortDescription(), null);

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
         TextView tvName;
         ImageView imgLink;
         View itemView;

         FragmentPromotionsViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            imgLink = (ImageView) itemView.findViewById(R.id.img_link);
        }
    }
}
