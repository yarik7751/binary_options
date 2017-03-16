package com.elatesoftware.grandcapital.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class AndroidUtils {

    /**
     * Получение dp по пикселам
     * @param dp - пикселы
     * @return
     */
    public static int dp(int dp) {
        return Math.round(dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Получение dp по пикселам
     * @param dp - пикселы
     * @return
     */
    public static int dp(float dp) {
        return Math.round(dp * Resources.getSystem().getDisplayMetrics().density);
    }


    /**
     * Спрятать клавиатуру
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    /**
     * Показать клавиатуру
     * @param activity
     */
    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Подключен ли телефон к интернету
     * @param context
     * @return - true/false
     */
    public static boolean isNetworkOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && (netInfo.isConnectedOrConnecting() || netInfo.isAvailable())) {
                return true;
            }

            netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            } else {
                netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    /**
     * Закачка файла по url
     * @param context
     * @param uri - uri, которое пропарсило url файла
     */
    public static void downloadFile(Context context, Uri uri) {
        DownloadManager.Request request = new DownloadManager.Request(uri);
        //куда скачивать
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment());
        //уведомление
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //закачка
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        dm.enqueue(request);
    }

    /**
     * Возвращает ширину и высоту экрана
     * @param context
     * @return - {width, height}
     */
    public static int[] getWindowsSizeParams(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        return new int[]{width, height};
    }

    /**
     * Копирует строку в буфер обмена
     * @param context
     * @param str - строка
     */
    public static void copyToClipboard(Context context, String str) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("", str);
        clipboardManager.setPrimaryClip(clipData);
    }

    public static int getStatusBarHeight(final Context context) {
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            return resources.getDimensionPixelSize(resourceId);
        else
            return (int) Math.ceil((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25) * resources.getDisplayMetrics().density);
    }
}
