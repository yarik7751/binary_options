package com.elatesoftware.grandcapital.views.items;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.widget.Button;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.activities.SignInActivity;
import com.google.android.gms.analytics.HitBuilders;

public class CustomDialog {

    private static Dialog dialogOpenReal;

    public static void showDialogInfo(Activity activity, String title, String message){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_information);

        TextView tvOk = (TextView) dialog.findViewById(R.id.tvOk);
        TextView tvInfoMsg = (TextView) dialog.findViewById(R.id.tvInfoMsg);
        TextView tvToolbarDialog = (TextView) dialog.findViewById(R.id.tvToolbarDialog);

        tvInfoMsg.setText(message);
        tvToolbarDialog.setText(title);

        tvOk.setOnClickListener(v -> {
            dialog.cancel();
        });
        dialog.show();
    }
    public static void showDialog(Activity activity, SslErrorHandler handler, String title, String message){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_logout);

        TextView tvOk = (TextView) dialog.findViewById(R.id.tvOk);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        TextView tvInfoMsg = (TextView) dialog.findViewById(R.id.tvInfoMsg);
        TextView tvToolbarDialog = (TextView) dialog.findViewById(R.id.tvToolbarDialog);

        tvInfoMsg.setText(message);
        tvToolbarDialog.setText(title);

        tvOk.setOnClickListener(v -> {
            handler.proceed();
        });
        tvCancel.setOnClickListener(v -> {
            handler.cancel();
            dialog.cancel();
        });
        dialog.show();
    }
    public static void showDialogLogout(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_logout);

        TextView tvOk = (TextView) dialog.findViewById(R.id.tvOk);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        TextView tvInfoMsg = (TextView) dialog.findViewById(R.id.tvInfoMsg);
        TextView tvToolbarDialog = (TextView) dialog.findViewById(R.id.tvToolbarDialog);

        tvInfoMsg.setText(activity.getResources().getString(R.string.logout));
        tvToolbarDialog.setText(activity.getResources().getString(R.string.caution));

        tvOk.setOnClickListener(v -> {
            CustomSharedPreferences.deleteInfoUser(activity.getApplicationContext());
            Intent intent = new Intent(activity.getApplicationContext(), SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
            CustomSharedPreferences.setIntervalAdvertising(activity.getApplicationContext(), -1);
            GrandCapitalApplication.getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory(Const.ANALYTICS_EVENT_EXIT)
                    .build()
            );
            dialog.cancel();
            BaseActivity.removeTerminal();
            activity.finish();
        });
        tvCancel.setOnClickListener(v -> {
            dialog.cancel();
        });
        dialog.show();
    }

    public static Dialog showDialogCloseDealing(Activity activity, View.OnClickListener listenerOk, View.OnClickListener listenerOkNoRepeat) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_close_dealing);

        TextView tvOk = (TextView) dialog.findViewById(R.id.tvOk);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        TextView tvMaybe = (TextView) dialog.findViewById(R.id.tvMaybe);
        TextView tvToolbarDialog = (TextView) dialog.findViewById(R.id.tvToolbarDialog);

        tvToolbarDialog.setText(activity.getResources().getString(R.string.clode_dealing_title));

        tvOk.setOnClickListener(listenerOk);
        tvCancel.setOnClickListener(v -> dialog.cancel());
        tvMaybe.setOnClickListener(listenerOkNoRepeat);
        dialog.show();
        return dialog;
    }

    public static Dialog showDialogOpenAccount(Activity activity, View.OnClickListener listnerOpenAccount) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_open_account);

        Button btnOpenAccount = (Button) dialog.findViewById(R.id.btn_open_accaunt);
        TextView tvLater = (TextView) dialog.findViewById(R.id.tv_later);

        tvLater.setOnClickListener(v -> dialog.cancel());
        btnOpenAccount.setOnClickListener(listnerOpenAccount);
        dialog.show();
        return dialog;
    }

    public static void showViewOpenRealAccount(Activity activity){
        if (CustomSharedPreferences.getIntervalAdvertising(activity) >= 0) {
            CustomSharedPreferences.setIntervalAdvertising(activity, CustomSharedPreferences.getIntervalAdvertising(activity) + 1);
            if (CustomSharedPreferences.getIntervalAdvertising(activity) >= 3) {
                CustomSharedPreferences.setIntervalAdvertising(activity, 0);
                dialogOpenReal = CustomDialog.showDialogOpenAccount(activity, v -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.URL_GRAND_CAPITAL_SIGN_UP));
                    activity.startActivity(browserIntent);
                    dialogOpenReal.cancel();
                });
            }
        }
    }
}
