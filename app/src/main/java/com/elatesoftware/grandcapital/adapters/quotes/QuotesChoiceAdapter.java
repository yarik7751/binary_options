package com.elatesoftware.grandcapital.adapters.quotes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.adapters.GrandCapitalListAdapter;
import com.elatesoftware.grandcapital.api.pojo.Instrument;
import com.elatesoftware.grandcapital.models.QuoteType;
import com.elatesoftware.grandcapital.utils.ConventString;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Дарья Высокович on 18.05.2017.
 */

public class QuotesChoiceAdapter extends GrandCapitalListAdapter {

    public static final String TAG = "QuotesAdapter_TAG";

    private static int selectedPosition = 0;
    private String selectedInstData;
    private Context context;

    private List<QuoteType> listQuotes;
    private List<QuoteType> listSelectQuotes;
    private List<QuoteType> listNoSelectQuotes;

    private static final int VIEW_TYPE_QUOTE_STAR = 0;
    private static final int VIEW_TYPE_QUOTE_NO_STAR = 1;
    private static final int VIEW_TYPE_STAR = 2;
    private static final int VIEW_TYPE_NO_STAR = 3;

    private final OnItemClickListener listenerItem;
    public interface OnItemClickListener {
        void onItemClick(QuoteType s, View view);
    }

    public QuotesChoiceAdapter(Context context, List<Instrument> instr, String symbol, OnItemClickListener listenerItem) {
        order = ORDER_EVEN;
        this.context = context;
        selectedInstData = CustomSharedPreferences.getSelectedQuotes(this.context);
        this.listenerItem = listenerItem;
        if(instr != null) {
            listQuotes = new ArrayList<>();
            listSelectQuotes = new ArrayList<>();
            listNoSelectQuotes = new ArrayList<>();
            for (Instrument inst : instr) {
                if(inst.getColor() == 0) {
                    inst.setColor(this.context.getResources().getColor(R.color.menuAccountBalanceTextColor));
                }
                if(selectedInstData.contains(inst.getSymbol().toUpperCase() + ";")){
                    listSelectQuotes.add(new QuoteType(inst, VIEW_TYPE_QUOTE_STAR));
                }else{
                    listNoSelectQuotes.add(new QuoteType(inst, VIEW_TYPE_QUOTE_NO_STAR));
                }
            }
            if(listQuotes != null){
                listQuotes.add(new QuoteType(null, VIEW_TYPE_STAR));
                if(listSelectQuotes != null){
                    listQuotes.addAll(listSelectQuotes);
                }
                listQuotes.add(new QuoteType(null, VIEW_TYPE_NO_STAR));
                if(listNoSelectQuotes != null){
                    listQuotes.addAll(listNoSelectQuotes);
                }
                listNoSelectQuotes.clear();
                listSelectQuotes.clear();
                for(QuoteType quoteType: listQuotes){
                    if(quoteType.getInstrumentQuote() != null && quoteType.getInstrumentQuote().getSymbol().equals(symbol)){
                        selectedPosition = listQuotes.indexOf(quoteType);
                    }
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        QuoteType quoteType = getQuoteType(position);
        return quoteType.getTypeQuote();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_QUOTE_STAR:
                return new QuotesStarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote_choice_select, parent, false));
            case VIEW_TYPE_QUOTE_NO_STAR:
                return new QuotesNoStarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote_choice_no_select, parent, false));
            case VIEW_TYPE_STAR:
                return new StarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_starred, parent, false));
            case VIEW_TYPE_NO_STAR:
                return new NoStarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_starred, parent, false));
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        QuoteBaseViewHolder baseViewHolder = (QuoteBaseViewHolder) holder;
        final QuoteType quoteType = getQuoteType(position);
        switch (baseViewHolder.getItemViewType()) {
            case VIEW_TYPE_QUOTE_STAR:
                QuotesStarViewHolder quotesHolder = (QuotesStarViewHolder) holder;
                quotesHolder.tvCurrency.setText(quoteType.getInstrumentQuote().getSymbol().toUpperCase());
                quotesHolder.tvAsk.setText(ConventString.getRoundNumber(5, quoteType.getInstrumentQuote().getAsk()));
                quotesHolder.tvAsk.setTextColor(quoteType.getInstrumentQuote().getColor());

                quotesHolder.flQuote.setOnClickListener(v -> {
                    selectedPosition = listQuotes.indexOf(quoteType);
                    quotesHolder.imgIsSelected.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_done_white_24dp));
                    ((QuotesStarViewHolder) holder).bind(quoteType, listenerItem);
                    notifyDataSetChanged();
                });
                if(position == selectedPosition){
                    quotesHolder.imgIsSelected.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_done_white_24dp));
                }else{
                    quotesHolder.imgIsSelected.setImageDrawable(null);
                }
                break;
            case VIEW_TYPE_QUOTE_NO_STAR:
                QuotesNoStarViewHolder quotesHolder1 = (QuotesNoStarViewHolder) holder;
                quotesHolder1.tvCurrency.setText(quoteType.getInstrumentQuote().getSymbol().toUpperCase());
                quotesHolder1.tvAsk.setText(ConventString.getRoundNumber(5, quoteType.getInstrumentQuote().getAsk()));
                quotesHolder1.tvAsk.setTextColor(quoteType.getInstrumentQuote().getColor());

                quotesHolder1.flQuote.setOnClickListener(v -> {
                    selectedPosition = listQuotes.indexOf(quoteType);
                    quotesHolder1.imgIsSelected.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_done_white_24dp));
                    ((QuotesNoStarViewHolder) holder).bind(quoteType, listenerItem);
                    notifyDataSetChanged();
                });
                if(position == selectedPosition){
                    quotesHolder1.imgIsSelected.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_done_white_24dp));
                }else{
                    quotesHolder1.imgIsSelected.setImageDrawable(null);
                }
                break;
            case VIEW_TYPE_STAR:
                break;
            case VIEW_TYPE_NO_STAR:
                break;
            default:
                break;
        }
    }

    public void setSelectedInstruments(List<Instrument> list) {
        if(listQuotes == null){
            listQuotes = new ArrayList<>();
        }
        listQuotes.clear();
        listNoSelectQuotes.clear();
        listSelectQuotes.clear();
        if(list != null) {
            listQuotes = new ArrayList<>();
            listSelectQuotes = new ArrayList<>();
            listNoSelectQuotes = new ArrayList<>();
            for (Instrument inst : list) {
                if(inst.getColor() == 0) {
                    inst.setColor(this.context.getResources().getColor(R.color.menuAccountBalanceTextColor));
                }
                if(selectedInstData.contains(inst.getSymbol().toUpperCase() + ";")){
                    listSelectQuotes.add(new QuoteType(inst, VIEW_TYPE_QUOTE_STAR));
                }else{
                    listNoSelectQuotes.add(new QuoteType(inst, VIEW_TYPE_QUOTE_NO_STAR));
                }
            }
            if(listQuotes != null){
                listQuotes.add(new QuoteType(null, VIEW_TYPE_STAR));
                if(listSelectQuotes != null){
                    listQuotes.addAll(listSelectQuotes);
                }
                listQuotes.add(new QuoteType(null, VIEW_TYPE_NO_STAR));
                if(listNoSelectQuotes != null){
                    listQuotes.addAll(listNoSelectQuotes);
                }
                listNoSelectQuotes.clear();
                listSelectQuotes.clear();
            }
        }
    }

    @Override
    public int getItemCount() {
        return listQuotes == null ? 0 : listQuotes.size();
    }
    QuoteType getQuoteType(int position){
        return listQuotes.get(position);
    }
    static class QuoteBaseViewHolder extends RecyclerView.ViewHolder {
        public QuoteBaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class QuotesStarViewHolder extends QuoteBaseViewHolder {
        View itemView;
        TextView tvCurrency, tvAsk;
        ImageView imgIsSelected;
        FrameLayout flQuote;

        QuotesStarViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvCurrency = (TextView) itemView.findViewById(R.id.tv_currency);
            tvAsk = (TextView) itemView.findViewById(R.id.tv_ask);
            imgIsSelected = (ImageView) itemView.findViewById(R.id.img_is_selected);
            flQuote = (FrameLayout) itemView.findViewById(R.id.fl_quote);
        }
        public void bind(final QuoteType s, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(s, itemView));
        }
    }
    private class QuotesNoStarViewHolder extends QuoteBaseViewHolder {
        View itemView;
        TextView tvCurrency, tvAsk;
        ImageView imgIsSelected;
        FrameLayout flQuote;

        QuotesNoStarViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvCurrency = (TextView) itemView.findViewById(R.id.tv_currency);
            tvAsk = (TextView) itemView.findViewById(R.id.tv_ask);
            imgIsSelected = (ImageView) itemView.findViewById(R.id.img_is_selected);
            flQuote = (FrameLayout) itemView.findViewById(R.id.fl_quote);
        }
        public void bind(final QuoteType s, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(s, itemView));
        }
    }
    private class StarViewHolder extends QuoteBaseViewHolder {
        View itemView;
        StarViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
    private class NoStarViewHolder extends QuoteBaseViewHolder {
        View itemView;
        NoStarViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}