package com.elatesoftware.grandcapital.adapters.quotes;

/**
 * Created by Дарья Высокович on 18.05.2017.
 */

public class QuotesChoiceAdapter /*extends GrandCapitalListAdapter */{
/*
    public static final String TAG = "QuotesAdapter_TAG";

    public static final int ALL_QUOTES = 1;
    public static final int SELECT_QUOTES = 2;

    private static String selectSymbol;
    private Context context;
    private List<Instrument> selectedInstruments;
    private final OnItemClickListener listenerItem;
    private int variant;

    public interface OnItemClickListener {
        void onItemClick(String s, View view);
    }

    public QuotesChoiceAdapter(Context context, List<Instrument> instr, int variant, String symbol,  OnItemClickListener listenerItem) {
        order = ORDER_EVEN;
        selectSymbol = symbol;
        List<Instrument> instruments = instr;
        this.variant = variant;
        this.context = context;
        this.listenerItem = listenerItem;
        if(instruments != null) {
            selectedInstruments = new ArrayList<>();
            for (Instrument inst : instruments) {
                String selectedInstData = CustomSharedPreferences.getSelectedQuotes(this.context);
                if(inst.getColor() == 0) {
                    inst.setColor(this.context.getResources().getColor(R.color.menuAccountBalanceTextColor));
                }
                if(variant == ALL_QUOTES) {
                    if (!selectedInstData.contains(inst.getSymbol().toUpperCase() + ";")) {
                        selectedInstruments.add(inst);
                    }
                } else if(variant == SELECT_QUOTES) {
                    if (selectedInstData.contains(inst.getSymbol().toUpperCase() + ";")) {
                        selectedInstruments.add(inst);
                    }
                }
            }
        }
    }

    @Override
    public QuotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote_choice, parent, false);
        return new QuotesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        QuotesViewHolder quotesHolder = (QuotesViewHolder) holder;
        quotesHolder.tvCurrency.setText(selectedInstruments.get(position).getSymbol().toUpperCase());
        quotesHolder.tvAsk.setText(ConventString.getRoundNumber(5, selectedInstruments.get(position).getAsk()));
        quotesHolder.tvAsk.setTextColor(selectedInstruments.get(position).getColor());

        quotesHolder.flQuote.setOnClickListener(v -> {
            selectSymbol = selectedInstruments.get(position).getSymbol();
            ((QuotesViewHolder) holder).bind(selectSymbol, listenerItem);
            notifyDataSetChanged();
        });
        if(selectSymbol.equals(selectedInstruments.get(position).getSymbol())){
            quotesHolder.imgIsSelected.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_done_white_24dp));
        }
    }

    public List<Instrument> getSelectedInstruments() {
        return selectedInstruments;
    }

    public void setSelectedInstruments(List<Instrument> list) {
        if(selectedInstruments == null){
            selectedInstruments = new ArrayList<>();
        }
        selectedInstruments.clear();
        this.selectedInstruments.addAll(list);
    }

    @Override
    public int getItemCount() {
        return selectedInstruments == null ? 0 : selectedInstruments.size();
    }

    private class QuotesViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvCurrency, tvAsk;
        ImageView imgIsSelected;
        FrameLayout flQuote;

        QuotesViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvCurrency = (TextView) itemView.findViewById(R.id.tv_currency);
            tvAsk = (TextView) itemView.findViewById(R.id.tv_ask);
            imgIsSelected = (ImageView) itemView.findViewById(R.id.img_is_selected);
            flQuote = (FrameLayout) itemView.findViewById(R.id.fl_quote);
        }
        public void bind(final String s, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(s, itemView));
        }
    }*/
}