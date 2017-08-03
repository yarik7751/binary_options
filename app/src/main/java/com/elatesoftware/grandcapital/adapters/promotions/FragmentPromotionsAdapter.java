package com.elatesoftware.grandcapital.adapters.promotions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.adapters.GrandCapitalListAdapter;
import com.elatesoftware.grandcapital.api.pojo.BinaryOptionAnswer;
import com.elatesoftware.grandcapital.utils.ConventString;
import com.elatesoftware.grandcapital.utils.GoogleAnalyticsUtil;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.fragments.PromotionsFragment;
import com.elatesoftware.grandcapital.views.fragments.QuestionFragment;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promotions, parent, false);
        return new FragmentPromotionsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        FragmentPromotionsViewHolder promotionsViewHolder = (FragmentPromotionsViewHolder) holder;

        Gson gson = new Gson();
        String name = binaryOptionAnswer.getElements().get(position).getShortDescriptionEn().toUpperCase();
        String longDescription = binaryOptionAnswer.getElements().get(position).getLongDescriptionEn();
        String jsonStr = gson.toJson(binaryOptionAnswer.getElements().get(position), BinaryOptionAnswer.Element.class);
        try {
            JSONObject elem = new JSONObject(jsonStr);
            String language = ConventString.getLanguageForPromotions();
            String nameParam = "short_description_" + language;
            String longDescriptionParam = "long_description_" + language;
            /*if(language.equals("zh") || language.equals("cn")) {
                nameParam = "short_description_zh_cn";
                longDescription = "long_description_zh_cn";
            }*/
            name = TextUtils.isEmpty(elem.getString(nameParam)) ? name : elem.getString(nameParam);
            name = name.toUpperCase();
            longDescription = TextUtils.isEmpty(elem.getString(longDescriptionParam)) ? longDescription : elem.getString(longDescriptionParam);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        promotionsViewHolder.tvName.setText(name);
        promotionsViewHolder.imgLink.setOnClickListener(v -> {
            BaseActivity.sMainTagFragment = PromotionsFragment.class.getName();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(binaryOptionAnswer.getElements().get(position).getPic()));
            context.startActivity(browserIntent);
//            WebFragment webFragment = WebFragment.getInstance(binaryOptionAnswer.getElements().get(position).getPic());
//            BaseActivity.addNextFragment(webFragment);
            /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(binaryOptionAnswer.getElements().get(position).getPic()));
            context.startActivity(browserIntent);*/
        });
        String finalLongDescription = longDescription;
        String finalName = name;
        promotionsViewHolder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(QuestionFragment.PROMOTIONS_JSON_DATA, jsonStr);
            bundle.putInt(QuestionFragment.NUMBER_QUESTION, position);
            bundle.putString(QuestionFragment.HEADER_TEXT, finalName);
            bundle.putString(QuestionFragment.CONTENT_TEXT, finalLongDescription);

            GoogleAnalyticsUtil.sendEvent(GoogleAnalyticsUtil.ANALYTICS_PROMOTIONS_SCREEN, GoogleAnalyticsUtil.ANALYTICS_LIST_PROMOTION, binaryOptionAnswer.getElements().get(position).getShortDescription(), null);

            BaseActivity.sMainTagFragment = PromotionsFragment.class.getName();
            QuestionFragment questionFragment = new QuestionFragment();
            questionFragment.setArguments(bundle);
            BaseActivity.addNextFragment(questionFragment);
        });
    }
    public void setList(BinaryOptionAnswer answer){
        binaryOptionAnswer = answer;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(binaryOptionAnswer == null ){
            return 0;
        }
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
