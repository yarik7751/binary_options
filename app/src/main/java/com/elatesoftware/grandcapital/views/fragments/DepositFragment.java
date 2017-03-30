package com.elatesoftware.grandcapital.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.adapters.in_out.InOutAdapter;
import com.elatesoftware.grandcapital.api.pojo.InOutAnswer;
import com.elatesoftware.grandcapital.models.User;
import com.elatesoftware.grandcapital.services.InOutService;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.items.CustomDialog;

import java.util.ArrayList;

/**
 * Created by Дарья Высокович on 21.02.2017.
 */

public class DepositFragment  extends Fragment {

    public static final String TAG = "DepositFragment_LOG";

    private RecyclerView rvIoOut;
    private Button btnDeposit;
    private TextView tvWithdraw;
    private LinearLayout llProgress, llWithdraw;

    private ArrayList<InOutAnswer> deposits;
    private ArrayList<InOutAnswer> withdraws;

    private GetMoneyTransactionBroadcastReceiver mMoneyTransactionBroadcastReceiver;

    private static DepositFragment fragment = null;
    public static DepositFragment getInstance() {
        if (fragment == null) {
            fragment = new DepositFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deposit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_deposit));
        BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_OTHER_FRAGMENT);
        BaseActivity.getToolbar().deselectAll();

        rvIoOut = (RecyclerView) view.findViewById(R.id.rv_inout);
        btnDeposit = (Button) view.findViewById(R.id.btn_deposit);
        tvWithdraw = (TextView) view.findViewById(R.id.tv_withdraw);
        llWithdraw = (LinearLayout) view.findViewById(R.id.ll_withdraw);

        btnDeposit.setOnClickListener(v -> {
            BaseActivity.sMainTagFragment = DepositFragment.class.getName();
            WebFragment webFragment = WebFragment.getInstance(Const.URL_GRAND_CAPITAL_ACCOUNT + User.getInstance().getLogin() + Const.URL_GRAND_CAPITAL_DEPOSIT);
            BaseActivity.addNextFragment(webFragment);
        });

        tvWithdraw.setOnClickListener(v -> {

        });

        llWithdraw.setOnClickListener(v -> {
            BaseActivity.sMainTagFragment = DepositFragment.class.getName();
            WebFragment webFragment = WebFragment.getInstance(Const.URL_GRAND_CAPITAL_ACCOUNT + User.getInstance().getLogin() + Const.URL_GRAND_CAPITAL_WITHDRAW);
            BaseActivity.addNextFragment(webFragment);
        });

        rvIoOut.setLayoutManager(new LinearLayoutManager(getContext()));
        testMethod();
        //rvIoOut.setAdapter(new InOutAdapter());
        //query(InOutService.DEPOSIT);
        //llProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMoneyTransactionBroadcastReceiver = new GetMoneyTransactionBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(InOutService.ACTION_SERVICE_IN_OUT);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mMoneyTransactionBroadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mMoneyTransactionBroadcastReceiver);
        super.onStop();
    }

    //тестовый метод
    private void testMethod() {
        InOutAnswer inOutAnswer1d = new InOutAnswer(new InOutAnswer.AmountMoney("$122000"), "VISA", "Ok", "03.05.2014");
        InOutAnswer inOutAnswer2d = new InOutAnswer(new InOutAnswer.AmountMoney("$65"), "VISA", "Ok", "07.05.2014");
        InOutAnswer inOutAnswer3d = new InOutAnswer(new InOutAnswer.AmountMoney("$48"), "VISA", "Ok", "12.05.2014");
        InOutAnswer inOutAnswer4d = new InOutAnswer(new InOutAnswer.AmountMoney("$12"), "Master card", "Ok", "03.04.2014");
        deposits = new ArrayList<>();
        deposits.add(inOutAnswer1d);
        deposits.add(inOutAnswer2d);
        deposits.add(inOutAnswer3d);
        deposits.add(inOutAnswer4d);

        InOutAnswer inOutAnswer1w = new InOutAnswer(new InOutAnswer.AmountMoney("$12"), "VISA", "Ok", "04.05.2014");
        InOutAnswer inOutAnswer2w = new InOutAnswer(new InOutAnswer.AmountMoney("$65"), "VISA", "Ok", "08.05.2014");
        InOutAnswer inOutAnswer3w = new InOutAnswer(new InOutAnswer.AmountMoney("$48"), "VISA", "Ok", "01.05.2014");
        InOutAnswer inOutAnswer4w = new InOutAnswer(new InOutAnswer.AmountMoney("$500"), "VISA", "Ok", "01.05.2014");
        withdraws = new ArrayList<>();
        withdraws.add(inOutAnswer1w);
        withdraws.add(inOutAnswer2w);
        withdraws.add(inOutAnswer3w);
        withdraws.add(inOutAnswer4w);

        rvIoOut.setAdapter(new InOutAdapter(deposits, withdraws));
    }

    private void query(String transaction) {
        Intent intent = new Intent(getContext(), InOutService.class);
        intent.putExtra(InOutService.TRANSACTION, transaction);
        getActivity().startService(intent);
    }

    public class GetMoneyTransactionBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(InOutService.RESPONSE);
            if (response != null && response.equals(Const.RESPONSE_CODE_SUCCESS)){
                if(InOutAnswer.getInstance() != null) {
                    String transaction = intent.getStringExtra(InOutService.TRANSACTION);
                    if(transaction.equals(InOutService.DEPOSIT)) {
                        deposits = InOutAnswer.getInstance();
                        query(InOutService.WITHDRAW);
                    }
                    if(transaction.equals(InOutService.WITHDRAW)) {
                        withdraws = InOutAnswer.getInstance();
                        rvIoOut.setAdapter(new InOutAdapter(deposits, withdraws));
                        llProgress.setVisibility(View.INVISIBLE);
                    }
                }
            } else {
                CustomDialog.showDialogInfo(getActivity(),
                        getString(R.string.request_error_title),
                        getString(R.string.request_error_text));
            }
        }
    }
}
