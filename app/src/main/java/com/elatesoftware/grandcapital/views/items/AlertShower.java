package com.elatesoftware.grandcapital.views.items;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;

public class AlertShower {

    public static void showInfo(String title, String message, String buttonText, Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(buttonText, (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
        Button okButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            okButton.setTextColor(activity.getColor(R.color.alertButtonTextColor));
        }
        else{
            okButton.setTextColor(activity.getResources().getColor(R.color.alertButtonTextColor));
        }
    }

    public static void showDialogLogout(Context context, Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(context.getResources().getString(R.string.caution))
                .setMessage(context.getResources().getString(R.string.logout))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.yes), (dialog, which) -> {

                })
                .setNegativeButton(context.getResources().getString(R.string.no), (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
        Button okButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        Button noButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            okButton.setTextColor(activity.getColor(R.color.alertButtonTextColor));
            noButton.setTextColor(activity.getColor(R.color.alertButtonTextColor));
        }
        else{
            okButton.setTextColor(activity.getResources().getColor(R.color.alertButtonTextColor));
            noButton.setTextColor(activity.getResources().getColor(R.color.alertButtonTextColor));
        }
    }
}
