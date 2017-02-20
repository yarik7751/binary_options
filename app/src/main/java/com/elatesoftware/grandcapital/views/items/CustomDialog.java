package com.elatesoftware.grandcapital.views.items;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.widget.Button;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.models.User;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.views.activities.SignInActivity;

public class CustomDialog {

    public static void showDialogInfo(Activity activity, String title, String message){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.item_dialog_information);

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
        dialog.setContentView(R.layout.item_dialog_logout);

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
        dialog.setContentView(R.layout.item_dialog_logout);

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
            activity.finish();
        });
        tvCancel.setOnClickListener(v -> {
            dialog.cancel();
        });
        dialog.show();
    }
}
