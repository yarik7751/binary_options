package com.elatesoftware.grandcapital.api.pojo;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.*;

public class InOutAnswer implements Comparable {

    public static final String TAG = "InOutAnswer_LOGS";

    @SerializedName("account")
    @Expose
    private Integer account;

    @SerializedName("account__mt4_id")
    @Expose
    private Integer accountMt4Id;

    @SerializedName("amount_money")
    @Expose
    private AmountMoney amountMoney;

    @SerializedName("payment_system")
    @Expose
    private String paymentSystem;

    @SerializedName("public_comment")
    @Expose
    private String publicComment;

    @SerializedName("creation_ts")
    @Expose
    private String creationTs;

    private String transaction;

    public InOutAnswer(AmountMoney amountMoney, String paymentSystem, String publicComment, String creationTs) {
        this.amountMoney = amountMoney;
        this.paymentSystem = paymentSystem;
        this.publicComment = publicComment;
        this.creationTs = creationTs;
    }

    private static ArrayList<InOutAnswer> inOutAnswer = null;
    public static void setInstance(ArrayList<InOutAnswer> _inOutAnswer) {
        inOutAnswer = _inOutAnswer;
    }
    public static ArrayList<InOutAnswer> getInstance() {
        return inOutAnswer;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public Integer getAccountMt4Id() {
        return accountMt4Id;
    }

    public void setAccountMt4Id(Integer accountMt4Id) {
        this.accountMt4Id = accountMt4Id;
    }

    public AmountMoney getAmountMoney() {
        return amountMoney;
    }

    public void setAmountMoney(AmountMoney amountMoney) {
        this.amountMoney = amountMoney;
    }

    public String getPaymentSystem() {
        return paymentSystem;
    }

    public void setPaymentSystem(String paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    public String getPublicComment() {
        return publicComment;
    }

    public void setPublicComment(String publicComment) {
        this.publicComment = publicComment;
    }

    public String getCreationTs() {
        return creationTs;
    }

    public void setCreationTs(String creationTs) {
        this.creationTs = creationTs;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    @Override
    public int compareTo(Object obj) {
        InOutAnswer inOutAnswer = (InOutAnswer) obj;
        int answer = 0;
        String[] elemsThisDate = getCreationTs().split("\\.");
        Log.d(TAG, "getCreationTs(): " + getCreationTs());
        Log.d(TAG, "elemsThisDate.split: " + Arrays.toString(elemsThisDate));
        String[] elemsDate = inOutAnswer.getCreationTs().split("\\.");
        answer = elemsThisDate[2].compareTo(elemsDate[2]);
        if(answer == 0) {
            answer = elemsThisDate[1].compareTo(elemsDate[1]);
            if(answer == 0) {
                answer = elemsThisDate[0].compareTo(elemsDate[0]);
            }
        }
        return answer;
    }

    public static class AmountMoney {

        @SerializedName("display")
        @Expose
        private String display;

        @SerializedName("amount")
        @Expose
        private Integer amount;

        @SerializedName("currency")
        @Expose
        private Currency currency;

        public AmountMoney(String display) {
            this.display = display;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public Currency getCurrency() {
            return currency;
        }

        public void setCurrency(Currency currency) {
            this.currency = currency;
        }
    }
}
