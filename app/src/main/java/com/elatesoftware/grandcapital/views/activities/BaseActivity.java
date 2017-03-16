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
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.models.User;
import com.elatesoftware.grandcapital.services.CheckDealingService;
import com.elatesoftware.grandcapital.services.InfoUserService;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.views.fragments.DealingFragment;
import com.elatesoftware.grandcapital.views.fragments.DepositFragment;
import com.elatesoftware.grandcapital.views.fragments.HowItWorksFragment;
import com.elatesoftware.grandcapital.views.fragments.PromotionsFragment;
import com.elatesoftware.grandcapital.views.fragments.QuotesFragment;
import com.elatesoftware.grandcapital.views.fragments.SupportFragment;
import com.elatesoftware.grandcapital.views.fragments.TerminalFragment;
import com.elatesoftware.grandcapital.views.fragments.ToolbarFragment;
import com.elatesoftware.grandcapital.views.items.CustomDialog;
import com.elatesoftware.grandcapital.views.items.ResideMenu.ResideMenu;
import com.elatesoftware.grandcapital.views.items.ResideMenu.ResideMenuItem;
import com.elatesoftware.grandcapital.views.items.ResideMenu.ResideMenuItemWithMark;
import com.elatesoftware.grandcapital.views.items.tooltabsview.adapter.OnChangePosition;

public class BaseActivity extends CustomFontsActivity {

    public static final String TAG = "BaseActivity_TAG";

    public static FragmentManager fragmentManager;
    public static Context context;
    public static String sMainTagFragment = "";

    public ResideMenu mResideMenu;
    private ResideMenuItem mTerminal;
    private ResideMenuItem mSupport;
    private ResideMenuItemWithMark mDealing;
    private ResideMenuItem mQuotes;
    private ResideMenuItem mHowItWorks;
    private ResideMenuItem mPromotions;
    //private ResideMenuItem mAccounts;
    //private ResideMenuItem mSettings;
    private ResideMenuItem mLogout;
    private View mDeposit;
    private TerminalFragment terminalFragment;

    private static ToolbarFragment toolbar;

    public static boolean backToRootFragment = false;

    public static final int SIGNAL_POSITION = 0;
    public static final int TERMINAL_POSITION = 1;
    public static final int DEALING_POSITION = 2;
    public static final int REFRESH_POSITION = 3;
    public static final int QUOTES_POSITION = 4;

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
            context = this;
            setupMenu();
            if (savedInstanceState == null) {
                toolbar = new ToolbarFragment();
                changeToolbarFragment(toolbar);
                //changeMainFragment(TerminalFragment.getInstance());
                terminalFragment = setTerminalFragment();
                getInfoUser();
                startService(new Intent(this, CheckDealingService.class));
            }
        }
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

    public void setDealings() {
        mDealing.setValue(CustomSharedPreferences.getAmtCloseDealings(this));
    }

    private void addItems() {
        mTerminal = new ResideMenuItem(this, getString(R.string.menu_item_terminal));
        mDealing = new ResideMenuItemWithMark(this, getString(R.string.menu_item_dealing));
        mQuotes = new ResideMenuItem(this, getString(R.string.menu_item_quotes));
        mHowItWorks = new ResideMenuItem(this, getString(R.string.menu_item_how_it_works));
        mPromotions = new ResideMenuItem(this, getString(R.string.menu_item_promotions));
        //mAccounts = new ResideMenuItem(this, getString(R.string.menu_item_accounts));
        mSupport = new ResideMenuItem(this, getString(R.string.menu_item_support));
        //mSettings = new ResideMenuItem(this, getString(R.string.menu_item_settings));
        mLogout = new ResideMenuItem(this, getString(R.string.menu_item_logout));
        mDeposit = mResideMenu.tvDepositMenu;

        mTerminal.setOnClickListener(menuClickListener);
        mDealing.setOnClickListener(menuClickListener);
        mQuotes.setOnClickListener(menuClickListener);
        mHowItWorks.setOnClickListener(menuClickListener);
        mPromotions.setOnClickListener(menuClickListener);
        //mAccounts.setOnClickListener(menuClickListener);
        mSupport.setOnClickListener(menuClickListener);
        //mSettings.setOnClickListener(menuClickListener);
        mLogout.setOnClickListener(menuClickListener);
        mDeposit.setOnClickListener(menuClickListener);

        mResideMenu.addMenuItem(mTerminal, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mDealing, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mQuotes, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mHowItWorks, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mPromotions, ResideMenu.DIRECTION_LEFT);
        //mResideMenu.addMenuItem(mAccounts, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mSupport, ResideMenu.DIRECTION_LEFT);
        //mResideMenu.addMenuItem(mSettings, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mLogout, ResideMenu.DIRECTION_LEFT);
    }
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            //mDealing.setValue(9);
            setDealings();
            mResideMenu.setScrolling(true);
        }
        @Override
        public void closeMenu() {
            if(TerminalFragment.isOpen) {
                mResideMenu.setScrolling(false);
            } else {
                mResideMenu.setScrolling(true);
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mResideMenu.dispatchTouchEvent(ev);
    }

    private View.OnClickListener menuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == mLogout) {
                CustomDialog.showDialogLogout(BaseActivity.this);
            }else if (view == mTerminal) {
                //changeMainFragment(TerminalFragment.getInstance());
                terminalFragment = setTerminalFragment();
            } else if (view == mSupport) {
                changeMainFragment(new SupportFragment());
            } else if (view == mDealing) {
                changeMainFragment(new DealingFragment());
            } else if (view == mQuotes) {
                changeMainFragment(new QuotesFragment());
            } else if (view == mHowItWorks) {
                changeMainFragment(new HowItWorksFragment());
            } else if (view == mPromotions) {
                changeMainFragment(new PromotionsFragment());
            } /*else if (view == mAccounts) {
                changeMainFragment(new AccountsFragment());
            }*/ /*else if (view == mSettings) {
                changeMainFragment(new SettingsFragment());
            }*/ else if (view == mDeposit) {
                changeMainFragment(new DepositFragment());
            }
            mResideMenu.closeMenu();
        }
    };

    public void setMain(int i) {
        getToolbar().mTabLayout.setOnChangePosition(new OnChangePosition() {
            @Override
            public void changePosition() {
                Log.d(TAG, "mTabLayout.setOnChangePosition");

                switch (i){
                    case SIGNAL_POSITION:
                        Log.d(TAG, "SIGNAL_POSITION");
                        if(TerminalFragment.isOpen && terminalFragment != null) {
                            terminalFragment.showSignalsPanel();
                        }
                        break;
                    case TERMINAL_POSITION:
                        Log.d(TAG, "TERMINAL_POSITION");
                        terminalFragment = setTerminalFragment();
                        if(!terminalFragment.isDirection) {
                            terminalFragment.showSignalsPanel();
                        }
                        break;
                    case DEALING_POSITION:
                        Log.d(TAG, "DEALING_POSITION");
                        changeMainFragment(new DealingFragment());
                        break;
                    case REFRESH_POSITION:
                        Log.d(TAG, "REFRESH_POSITION");
                        //??????
                        break;
                    case QUOTES_POSITION:
                        Log.d(TAG, "QUOTES_POSITION");
                        changeMainFragment(new QuotesFragment());
                        break;
                }
            }
        });
    }

    public static void changeMainFragment(Fragment targetFragment) {
        backToRootFragment = true;
        fragmentManager.popBackStack();
        onSwitchFragment(targetFragment, targetFragment.getClass().getName(), true, true, R.id.content);
        Log.d(TAG, "BackStackEntryCount: " + fragmentManager.getBackStackEntryCount());
    }

    public static void addNextFragment(Fragment fragment) {
        onSwitchFragment(fragment, fragment.getClass().getName(), true, true, R.id.content);
    }

    public static TerminalFragment setTerminalFragment() {
        Log.d(TAG, "setTerminalFragment()");
        TerminalFragment fragment = TerminalFragment.getInstance();
        if(backToRootFragment) {
            clearFragmentBackStack();
            Log.d(TAG, "clearFragmentBackStack()");
        }
        backToRootFragment = false;
        if(fragmentManager.findFragmentByTag(fragment.getClass().getName()) == null) {
            onSwitchFragment(fragment, fragment.getClass().getName(), false, true, R.id.content);
        } else {
            setToolBarTerminalInfo();
        }
        return fragment;
    }

    private static void clearFragmentBackStack() {
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

    public static void onSwitchFragment(Fragment fragment, String tag, boolean add, boolean anim, int res) {
        FragmentManager fm = fragmentManager;
        FragmentTransaction tr = fm.beginTransaction();
        if(anim) {
            tr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }
        tr.add(res, fragment, tag);
        if (add) {
            try {
                tr.addToBackStack(tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        tr.commit();
    }

    private static void changeToolbarFragment(Fragment targetFragment) {
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
        if(toolbar != null) {
            return toolbar;
        } else {
            toolbar = new ToolbarFragment();
            changeToolbarFragment(toolbar);
            return toolbar;
        }
    }

    private boolean isAuth() {
        if (User.getInstance() != null) {
            return true;
        }
        return CustomSharedPreferences.isSaveUserInPreferences(getApplicationContext());
    }

    private void getInfoUser(){
        Intent intentMyIntentService = new Intent(this, InfoUserService.class);
        startService(intentMyIntentService);
    }

    private static void setToolBarTerminalInfo() {
        BaseActivity.getToolbar().setPageTitle(context.getResources().getString(R.string.toolbar_name_terminal));
        BaseActivity.getToolbar().mTabLayout.setOnLoadData(() -> {
            BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_TERMINALE_FRAGMENT);
            BaseActivity.getToolbar().switchTab(BaseActivity.TERMINAL_POSITION);
        });
        try {
            BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_TERMINALE_FRAGMENT);
            BaseActivity.getToolbar().switchTab(BaseActivity.TERMINAL_POSITION);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    private static void setToolbarInfoByTag() {
        if(sMainTagFragment.equals(PromotionsFragment.class.getName())) {
            BaseActivity.getToolbar().setPageTitle(context.getResources().getString(R.string.toolbar_name_promotions));
            BaseActivity.getToolbar().hideTabs();
            BaseActivity.getToolbar().setBurgerType(ToolbarFragment.BURGER_OPEN_MENU);
        }
        if(sMainTagFragment.equals(HowItWorksFragment.class.getName())) {
            BaseActivity.getToolbar().setPageTitle(context.getResources().getString(R.string.toolbar_name_how_it_works));
            BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_OTHER_FRAGMENT);
            BaseActivity.getToolbar().deselectAll();
            BaseActivity.getToolbar().setBurgerType(ToolbarFragment.BURGER_OPEN_MENU);
        }
        sMainTagFragment = "";
    }

    @Override
    public void onBackPressed() {
        if(mResideMenu.isOpened()) {
            mResideMenu.closeMenu();
        }
        if(backToRootFragment) {
            if(!TextUtils.isEmpty(sMainTagFragment)) {
                setToolbarInfoByTag();
            } else {
                Log.d(TAG, "backToRootFragment (onBackPressed): true");
                setToolBarTerminalInfo();
                backToRootFragment = false;
            }
        }
        super.onBackPressed();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        Log.d(TAG, "BackStackEntryCount (onBackPressed): " + backStackEntryCount);
        /*if(backStackEntryCount == 0) {
            if(backToRootFragment) {
                setTerminalFragment();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }*/
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mInfoBroadcastReceiver);
        super.onDestroy();
    }

    public class GetResponseInfoBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra(InfoUserService.RESPONSE_INFO) != null && intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY) != null){
                if(intent.getStringExtra(InfoUserService.RESPONSE_INFO).equals("200") && intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY).equals("200")){
                    mResideMenu.refreshBalanceUser();
                    mResideMenu.refreshNameUser();
                    CustomSharedPreferences.updateInfoUser(getApplicationContext());
                }
            }
        }
    }


}
