package com.elatesoftware.grandcapital.views.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.models.User;
import com.elatesoftware.grandcapital.services.InfoUserService;
import com.elatesoftware.grandcapital.views.fragments.AccountsFragment;
import com.elatesoftware.grandcapital.views.fragments.AddFundsFragment;
import com.elatesoftware.grandcapital.views.fragments.DealingFragment;
import com.elatesoftware.grandcapital.views.fragments.HowItWorksFragment;
import com.elatesoftware.grandcapital.views.fragments.PromotionsFragment;
import com.elatesoftware.grandcapital.views.fragments.QuotesFragment;
import com.elatesoftware.grandcapital.views.fragments.SettingsFragment;
import com.elatesoftware.grandcapital.views.fragments.ToolbarFragment;
import com.elatesoftware.grandcapital.views.fragments.WithdrawFundsFragment;
import com.elatesoftware.grandcapital.views.items.CustomDialog;
import com.elatesoftware.grandcapital.views.items.ResideMenu.ResideMenu;
import com.elatesoftware.grandcapital.views.items.ResideMenu.ResideMenuItem;
import com.elatesoftware.grandcapital.views.items.ResideMenu.ResideMenuItemWithMark;

import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;

public class BaseActivity extends CustomFontsActivity {

    public static FragmentManager fragmentManager;
    private ResideMenu mResideMenu;
    private ResideMenuItem mAddFunds;
    private ResideMenuItem mWithdraw;
    private ResideMenuItemWithMark mDealing;
    private ResideMenuItem mQuotes;
    private ResideMenuItem mHowItWorks;
    private ResideMenuItem mPromotions;
    private ResideMenuItem mAccounts;
    private ResideMenuItem mSettings;
    private ResideMenuItem mLogout;

    private static ToolbarFragment toolbar;

    public static final int SIGNAL_POSITION = 0;
    public static final int TERMINAL_POSITION = 1;
    public static final int DEALING_POSITION = 2;
    public static final int QUOTES_POSITION = 3;

    private GetResponseInfoBroadcastReceiver mInfoBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mInfoBroadcastReceiver = new GetResponseInfoBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(InfoUserService.ACTION_SERVICE_GET_INFO);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mInfoBroadcastReceiver, intentFilter);

        if (!isAuth()) {
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
            finish();
        } else {
            fragmentManager = getSupportFragmentManager();
            setupMenu();
            if (savedInstanceState == null) {
                toolbar = new ToolbarFragment();
                changeToolbarFragment(toolbar);
                changeMainFragment(new AddFundsFragment());
            }
            getInfoUser();
        }
    }
    public void getInfoUser(){
        Intent intentMyIntentService = new Intent(this, InfoUserService.class);
        startService(intentMyIntentService);
    }

    private void setupMenu() {
        mResideMenu = new ResideMenu(this);
        mResideMenu.setBackground(R.drawable.menu_bg);
        mResideMenu.attachToActivity(this);
        mResideMenu.setMenuListener(menuListener);
        mResideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        mResideMenu.setScaleValue(0.75f);
        addItems();
    }

    private void addItems() {
        mAddFunds = new ResideMenuItem(this, getString(R.string.menu_item_add_funds));
        mWithdraw = new ResideMenuItem(this, getString(R.string.menu_item_withdraw_funds));
        mDealing = new ResideMenuItemWithMark(this, getString(R.string.menu_item_dealing));
        mQuotes = new ResideMenuItem(this, getString(R.string.menu_item_quotes));
        mHowItWorks = new ResideMenuItem(this, getString(R.string.menu_item_how_it_works));
        mPromotions = new ResideMenuItem(this, getString(R.string.menu_item_promotions));
        mAccounts = new ResideMenuItem(this, getString(R.string.menu_item_accounts));
        mSettings = new ResideMenuItem(this, getString(R.string.menu_item_settings));
        mLogout = new ResideMenuItem(this, getString(R.string.menu_item_logout));

        mAddFunds.setOnClickListener(menuClickListener);
        mWithdraw.setOnClickListener(menuClickListener);
        mDealing.setOnClickListener(menuClickListener);
        mQuotes.setOnClickListener(menuClickListener);
        mHowItWorks.setOnClickListener(menuClickListener);
        mPromotions.setOnClickListener(menuClickListener);
        mAccounts.setOnClickListener(menuClickListener);
        mSettings.setOnClickListener(menuClickListener);
        mLogout.setOnClickListener(menuClickListener);

        mResideMenu.addMenuItem(mAddFunds, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mWithdraw, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mDealing, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mQuotes, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mHowItWorks, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mPromotions, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mAccounts, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mSettings, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mLogout, ResideMenu.DIRECTION_LEFT);
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            mDealing.setValue(9);
        }
        @Override
        public void closeMenu() {
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mResideMenu.dispatchTouchEvent(ev);
    }

    private View.OnClickListener menuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == mAddFunds) {
                changeMainFragment(new AddFundsFragment());
            } else if (view == mWithdraw) {
                changeMainFragment(new WithdrawFundsFragment());
            } else if (view == mDealing) {
                changeMainFragment(new DealingFragment());
            } else if (view == mQuotes) {
                changeMainFragment(new QuotesFragment());
            } else if (view == mHowItWorks) {
                changeMainFragment(new HowItWorksFragment());
            } else if (view == mPromotions) {
                changeMainFragment(new PromotionsFragment());
            } else if (view == mAccounts) {
                changeMainFragment(new AccountsFragment());
            } else if (view == mSettings) {
                changeMainFragment(new SettingsFragment());
            } else if (view == mLogout) {
                CustomDialog.showDialogLogout(BaseActivity.this);
            }
            mResideMenu.closeMenu();
        }
    };

    public void setMain(int i) {
        switch (i){
            case SIGNAL_POSITION:
                changeMainFragment(new AddFundsFragment());
                break;
            case TERMINAL_POSITION:
                changeMainFragment(new AddFundsFragment());
                break;
            case DEALING_POSITION:
                changeMainFragment(new DealingFragment());
                break;
            case QUOTES_POSITION:
                changeMainFragment(new QuotesFragment());
                break;
        }
    }

    public static void changeMainFragment(Fragment targetFragment) {
        //Опасно! Не ясно, что делает эта строка
       // mResideMenu.clearIgnoredViewList();
        fragmentManager.beginTransaction()
                .replace(R.id.content, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("fragment")
                .commit();
    }

    private void changeToolbarFragment(Fragment targetFragment) {
        //Опасно! Не ясно, что делает эта строка
       // mResideMenu.clearIgnoredViewList();
        fragmentManager.beginTransaction()
                .replace(R.id.toolbar, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }

    public ResideMenu getResideMenu() {
        return mResideMenu;
    }

    public static ToolbarFragment getToolbar(){
        return toolbar;
    }

    private boolean isAuth() {
        if (User.getInstance() != null) {
            return true;
        }
        return CustomSharedPreferences.isSaveUserInPreferences(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        int count = fragmentManager.getBackStackEntryCount();
        if (count < 3) {
            fragmentManager.popBackStackImmediate();
            fragmentManager.popBackStackImmediate();
            super.onBackPressed();
        } else {
            fragmentManager.popBackStack();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mInfoBroadcastReceiver);
        super.onDestroy();
    }

    public class GetResponseInfoBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mResideMenu.refreshNameUser();
            mResideMenu.refreshBalanceUser();

            CustomSharedPreferences.updateInfoUser(getApplicationContext());
        }
    }
}
