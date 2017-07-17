package com.elatesoftware.grandcapital.adapters.quotes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.adapters.GrandCapitalListAdapter;
import com.elatesoftware.grandcapital.api.pojo.Instrument;
import com.elatesoftware.grandcapital.utils.ConventString;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.utils.GoogleAnalyticsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ярослав Левшунов on 02.03.2017.
 */

public class QuotesAdapter extends GrandCapitalListAdapter {

    public static final String TAG = "QuotesAdapter_TAG";

    public static final int ALL_QUOTES = 1;
    public static final int SELECT_QUOTES = 2;

    private Context context;
    private List<Instrument> selectedInstruments;
    private OnSharedPreferencesChange onSharedPreferencesChange;

    private int variant;

    public QuotesAdapter(Context ctx, List<Instrument> instruments, int variant, OnSharedPreferencesChange onSharedPreferencesChange) {
        order = ORDER_EVEN;
        List<Instrument> instruments1 = instruments;
        this.variant = variant;
        this.onSharedPreferencesChange = onSharedPreferencesChange;
        context = ctx;
        if(instruments1 != null) {
            selectedInstruments = new ArrayList<>();
            for (Instrument inst : instruments1) {
                String selectedInstData = CustomSharedPreferences.getSelectedQuotes(context);
                if(inst.getColor() == 0) {
                    inst.setColor(this.context.getResources().getColor(R.color.menuAccountBalanceTextColor));
                }
                if(variant == ALL_QUOTES) {
                    if (!selectedInstData.contains(inst.getSymbol() + ";")) {
                        selectedInstruments.add(inst);
                    }
                } else if(variant == SELECT_QUOTES) {
                    if (selectedInstData.contains(inst.getSymbol() + ";")) {
                        selectedInstruments.add(inst);
                    }
                }
            }
        }
    }

    @Override
    public QuotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote, parent, false);
        return new QuotesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        QuotesViewHolder quotesHolder = (QuotesViewHolder) holder;
        quotesHolder.tvCurrency.setText(selectedInstruments.get(position).getSymbol().toUpperCase());
        quotesHolder.tvAsk.setText(ConventString.getRoundNumber(5, selectedInstruments.get(position).getAsk()));
        quotesHolder.tvAsk.setTextColor(selectedInstruments.get(position).getColor());
        if(variant == SELECT_QUOTES) {
            quotesHolder.imgIsSelected.setImageResource(R.drawable.ic_star_white_24dp);
        } else if(variant == ALL_QUOTES) {
            quotesHolder.imgIsSelected.setImageResource(R.drawable.ic_star_border_white_24dp);
        }

        quotesHolder.imgIsSelected.setOnClickListener(v -> {
            String selectedQuotes = CustomSharedPreferences.getSelectedQuotes(context);
            if(variant == ALL_QUOTES) {
                if(!selectedQuotes.contains(selectedInstruments.get(position).getSymbol())) {
                    selectedQuotes += selectedInstruments.get(position).getSymbol() + ";";
                }
                CustomSharedPreferences.saveSelectedQuotes(context, selectedQuotes);
                GoogleAnalyticsUtil.sendEvent(GoogleAnalyticsUtil.ANALYTICS_QUOTES_SCREEN, GoogleAnalyticsUtil.ANALYTICS_BUTTON_FAVORITES, selectedInstruments.get(position).getSymbol(), null);
            } else if(variant == SELECT_QUOTES) {
                selectedQuotes = selectedQuotes.replace(selectedInstruments.get(position).getSymbol() + ";", "");
                CustomSharedPreferences.saveSelectedQuotes(context, selectedQuotes);
                GoogleAnalyticsUtil.sendEvent(GoogleAnalyticsUtil.ANALYTICS_QUOTES_SCREEN, GoogleAnalyticsUtil.ANALYTICS_BUTTON_DELETE_FAVORITES, selectedInstruments.get(position).getSymbol(), null);
            }
            onSharedPreferencesChange.onChange();
        });
    }

    @Override
    public int getItemCount() {
        return selectedInstruments == null ? 0 : selectedInstruments.size();
    }

    private class QuotesViewHolder extends RecyclerView.ViewHolder {
         View itemView;
         TextView tvCurrency, tvAsk;
         ImageView imgIsSelected;

         QuotesViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvCurrency = (TextView) itemView.findViewById(R.id.tv_currency);
            tvAsk = (TextView) itemView.findViewById(R.id.tv_ask);
            imgIsSelected = (ImageView) itemView.findViewById(R.id.img_is_selected);
        }
    }
}
