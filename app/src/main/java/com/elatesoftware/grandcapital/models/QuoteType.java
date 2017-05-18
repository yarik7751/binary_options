package com.elatesoftware.grandcapital.models;

import com.elatesoftware.grandcapital.api.pojo.Instrument;

/**
 * Created by Darya on 19.05.2017.
 */

public class QuoteType {

    private Instrument instrumentQuote;
    private int typeQuote;

    public QuoteType() {
    }

    public QuoteType(Instrument instrument, int typeQuote) {
        this.instrumentQuote = instrument;
        this.typeQuote = typeQuote;
    }

    public int getTypeQuote() {
        return typeQuote;
    }

    public void setTypeQuote(int typeQuote) {
        this.typeQuote = typeQuote;
    }

    public Instrument getInstrumentQuote() {
        return instrumentQuote;
    }

    public void setInstrumentQuote(Instrument instrumentQuote) {
        this.instrumentQuote = instrumentQuote;
    }
}
