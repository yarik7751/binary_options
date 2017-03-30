package com.elatesoftware.grandcapital.adapters.in_out;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.adapters.GrandCapitalListAdapter;
import com.elatesoftware.grandcapital.api.pojo.InOutAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.services.InOutService;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ярослав Левшунов on 06.03.2017.
 */

public class InOutAdapter extends GrandCapitalListAdapter {

    public static final String TAG = "InOutAdapter_LOGS";

    static final int DOWN = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.dealingListDownOrderColor);
    static final int UP = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.dealingListUpOrderColor);

    ArrayList<InOutAnswer> deposits;
    ArrayList<InOutAnswer> withdraws;
    ArrayList<InOutAnswer> allTransactions;

    public InOutAdapter(ArrayList<InOutAnswer> deposits, ArrayList<InOutAnswer> withdraws) {
        this.deposits = deposits;
        this.withdraws = withdraws;

        allTransactions = new ArrayList<>();

        for(InOutAnswer ans : deposits) {
            ans.setTransaction(InOutService.DEPOSIT);
            allTransactions.add(ans);
        }

        for(InOutAnswer ans : withdraws) {
            ans.setTransaction(InOutService.WITHDRAW);
            allTransactions.add(ans);
        }

        Collections.sort(allTransactions);

        Log.d(TAG, "allTransactions.size(): " + allTransactions.size());
    }

    @Override
    public InOutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_in_out, parent, false);
        return new InOutHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        InOutHolder answerHolder = (InOutHolder) holder;

        String transaction = allTransactions.get(position).getTransaction();
        String sign = "+";
        int color = UP;
        if(transaction.equals(InOutService.WITHDRAW)) {
            color = DOWN;
            sign = "-";
        }
        answerHolder.tvAmount.setTextColor(color);
        answerHolder.tvAmount.setText(sign + allTransactions.get(position).getAmountMoney().getDisplay());
        answerHolder.tvPaymentSystem.setText(allTransactions.get(position).getPaymentSystem().toUpperCase());
        answerHolder.tvPublicComment.setText(allTransactions.get(position).getPublicComment());

        //тест, поменять
        answerHolder.tvDate.setText(allTransactions.get(position).getCreationTs());
    }

    @Override
    public int getItemCount() {
        return allTransactions != null ? allTransactions.size() : 0;
        //return 4;
    }

    public class InOutHolder extends RecyclerView.ViewHolder {

        TextView tvAmount, tvPaymentSystem, tvPublicComment, tvDate;

        public InOutHolder(View itemView) {
            super(itemView);

            tvAmount = (TextView) itemView.findViewById(R.id.tv_amount);
            tvPaymentSystem = (TextView) itemView.findViewById(R.id.tv_payment_system);
            tvPublicComment = (TextView) itemView.findViewById(R.id.tv_public_comment);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }
}
