package com.elatesoftware.grandcapital.adapters.quotes;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.adapters.GrandCapitalListAdapter;
import com.elatesoftware.grandcapital.api.pojo.Instrument;
import com.elatesoftware.grandcapital.models.QuoteType;
import com.elatesoftware.grandcapital.utils.ConventImage;
import com.elatesoftware.grandcapital.utils.ConventString;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Дарья Высокович on 18.05.2017.
 */

public class QuotesChoiceAdapter extends GrandCapitalListAdapter {

    public static final String TAG = "QuotesAdapter_TAG";

    private static String selectedQuote;
    private String selectedInstData;
    private Context context;

    private List<QuoteType> listQuotes = new ArrayList<>();
    private List<QuoteType> listSelectQuotes = new ArrayList<>();
    private List<QuoteType> listNoSelectQuotes = new ArrayList<>();

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
        listNoSelectQuotes.clear();
        listSelectQuotes.clear();
        for(Instrument i : instr){
            if(i.getColor() == 0) {
                i.setColor(this.context.getResources().getColor(R.color.menuAccountBalanceTextColor));
            }
            if(selectedInstData.contains(i.getSymbol().toUpperCase() + ";")){
                listSelectQuotes.add(new QuoteType(i, VIEW_TYPE_QUOTE_STAR));
            }else{
                listNoSelectQuotes.add(new QuoteType(i, VIEW_TYPE_QUOTE_NO_STAR));
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
            selectedQuote = symbol;
            for(int i = 0; i < listQuotes.size(); i++){
                if(listQuotes.get(i).getInstrumentQuote() != null && listQuotes.get(i).getInstrumentQuote().getSymbol().equals(selectedQuote)){
                    listQuotes.get(i).setSelected(true);
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listQuotes.get(position).getTypeQuote();
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
        QuoteBaseViewHolder baseViewHolder = (QuoteBaseViewHolder) holder;
        final QuoteType quoteType = getQuoteType(position);
        switch (baseViewHolder.getItemViewType()) {
            case VIEW_TYPE_QUOTE_STAR:
                QuotesStarViewHolder quotesHolder = (QuotesStarViewHolder) holder;
                quotesHolder.tvCurrency.setText(quoteType.getInstrumentQuote().getSymbol().toUpperCase());
                quotesHolder.tvAsk.setText(ConventString.getRoundNumber(5, quoteType.getInstrumentQuote().getAsk()));
                quotesHolder.tvAsk.setTextColor(quoteType.getInstrumentQuote().getColor());
                //quotesHolder.tvDescription.setText(quoteType.getInstrumentQuote().getGroup());
                if(quoteType.isSelected()){
                    quotesHolder.imgIsSelected.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_done_white_24dp));
                }else{
                    quotesHolder.imgIsSelected.setImageDrawable(null);
                }
                if(position% 2 != 0){
                    quotesHolder.llContainer.setBackgroundColor(ConventImage.getColorWithAlpha(context.getResources().getColor(R.color.menuAccountBalanceTextColor), 0.2f));
                }else{
                    quotesHolder.llContainer.setBackgroundColor(Color.TRANSPARENT);
                }
                quotesHolder.bind(quoteType, listenerItem);
                break;
            case VIEW_TYPE_QUOTE_NO_STAR:
                QuotesNoStarViewHolder quotesHolder1 = (QuotesNoStarViewHolder) holder;
                quotesHolder1.tvCurrency.setText(quoteType.getInstrumentQuote().getSymbol().toUpperCase());
                quotesHolder1.tvAsk.setText(ConventString.getRoundNumber(5, quoteType.getInstrumentQuote().getAsk()));
                quotesHolder1.tvAsk.setTextColor(quoteType.getInstrumentQuote().getColor());
               // quotesHolder1.tvDescription.setText(quoteType.getInstrumentQuote().getGroup());

                if(quoteType.isSelected()){
                    quotesHolder1.imgIsSelected.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_done_white_24dp));
                }else{
                    quotesHolder1.imgIsSelected.setImageDrawable(null);
                }
                if(position% 2 != 0){
                    quotesHolder1.llContainer.setBackgroundColor(ConventImage.getColorWithAlpha(context.getResources().getColor(R.color.menuAccountBalanceTextColor), 0.2f));
                }else{
                    quotesHolder1.llContainer.setBackgroundColor(Color.TRANSPARENT);
                }
                quotesHolder1.bind(quoteType, listenerItem);
                break;
            case VIEW_TYPE_STAR:
                break;
            case VIEW_TYPE_NO_STAR:
                break;
            default:
                break;
        }
    }
    public void setListQuotes(List<Instrument> list) {
        listQuotes.clear();
        listSelectQuotes.clear();
        listNoSelectQuotes.clear();
        for(Instrument i : list){
            if(i.getColor() == 0) {
                i.setColor(this.context.getResources().getColor(R.color.menuAccountBalanceTextColor));
            }
            if(selectedInstData.contains(i.getSymbol().toUpperCase() + ";")){
                listSelectQuotes.add(new QuoteType(i, VIEW_TYPE_QUOTE_STAR));
            }else{
                listNoSelectQuotes.add(new QuoteType(i, VIEW_TYPE_QUOTE_NO_STAR));
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
            for(int i = 0; i < listQuotes.size(); i++){
                if(listQuotes.get(i).getInstrumentQuote() != null && listQuotes.get(i).getInstrumentQuote().getSymbol().equals(selectedQuote)){
                    listQuotes.get(i).setSelected(true);
                }
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
        TextView tvCurrency, tvAsk/*, tvDescription*/;
        ImageView imgIsSelected;
        FrameLayout flQuote;
        LinearLayout llContainer;

        QuotesStarViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvCurrency = (TextView) itemView.findViewById(R.id.tv_currency);
            tvAsk = (TextView) itemView.findViewById(R.id.tv_ask);
            imgIsSelected = (ImageView) itemView.findViewById(R.id.img_is_selected);
            flQuote = (FrameLayout) itemView.findViewById(R.id.fl_quote);
            llContainer = (LinearLayout) itemView.findViewById(R.id.container_item_quote);
            //tvDescription = (TextView) itemView.findViewById(R.id.tv_currency_description);
        }
        public void bind(final QuoteType quoteType, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> {
                for(QuoteType quote: listQuotes){
                    if(quote.isSelected()){
                        quote.setSelected(false);
                        break;
                    }
                }
                quoteType.setSelected(true);
                selectedQuote = quoteType.getInstrumentQuote().getSymbol();
                notifyDataSetChanged();
                listener.onItemClick(quoteType, itemView);
            });
        }
    }
    private class QuotesNoStarViewHolder extends QuoteBaseViewHolder {
        View itemView;
        TextView tvCurrency, tvAsk/*, tvDescription*/;
        ImageView imgIsSelected;
        FrameLayout flQuote;
        LinearLayout llContainer;

        QuotesNoStarViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvCurrency = (TextView) itemView.findViewById(R.id.tv_currency);
            tvAsk = (TextView) itemView.findViewById(R.id.tv_ask);
            imgIsSelected = (ImageView) itemView.findViewById(R.id.img_is_selected);
            flQuote = (FrameLayout) itemView.findViewById(R.id.fl_quote);
            llContainer = (LinearLayout) itemView.findViewById(R.id.container_item_quote);
            //tvDescription = (TextView) itemView.findViewById(R.id.tv_currency_description);
        }
        public void bind(final QuoteType quoteType, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> {
                for(QuoteType quote: listQuotes){
                    if(quote.isSelected()){
                        quote.setSelected(false);
                        break;
                    }
                }
                quoteType.setSelected(true);
                selectedQuote = quoteType.getInstrumentQuote().getSymbol();
                notifyDataSetChanged();
                listener.onItemClick(quoteType, itemView);
            });
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