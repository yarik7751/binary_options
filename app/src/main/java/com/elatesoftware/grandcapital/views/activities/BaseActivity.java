package com.elatesoftware.grandcapital.views.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.socket.WebSocketApi;
import com.elatesoftware.grandcapital.models.User;
import com.elatesoftware.grandcapital.services.CheckDealingService;
import com.elatesoftware.grandcapital.services.InfoUserService;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.utils.GoogleAnalyticsUtil;
import com.elatesoftware.grandcapital.views.fragments.DealingFragment;
import com.elatesoftware.grandcapital.views.fragments.DepositFragment;
import com.elatesoftware.grandcapital.views.fragments.FAQFragment;
import com.elatesoftware.grandcapital.views.fragments.PromotionsFragment;
import com.elatesoftware.grandcapital.views.fragments.QuotesChoiceFragment;
import com.elatesoftware.grandcapital.views.fragments.QuotesFragment;
import com.elatesoftware.grandcapital.views.fragments.SupportFragment;
import com.elatesoftware.grandcapital.views.fragments.TerminalFragment;
import com.elatesoftware.grandcapital.views.fragments.ToolbarFragment;
import com.elatesoftware.grandcapital.views.fragments.WebFragment;
import com.elatesoftware.grandcapital.views.items.CustomDialog;
import com.elatesoftware.grandcapital.views.items.resideMenu.ResideMenu;
import com.elatesoftware.grandcapital.views.items.resideMenu.ResideMenuItem;
import com.elatesoftware.grandcapital.views.items.resideMenu.ResideMenuItemWithMark;

public class BaseActivity extends CustomFontsActivity {

    public static final String TAG = "BaseActivity_TAG";
    public static FragmentManager fragmentManager;
    public static Fragment currentFragment;
    public Context context;
    private boolean isRestared;
    public static String sMainTagFragment = "";
    public static String sCurrentTagFragment = "";
    public static String sSymbolCurrent = "";

    private static ResideMenu mResideMenu;
    private ResideMenuItem mTerminal;
    private ResideMenuItem mSupport;
    private ResideMenuItem mDepositWithdraw;
    private ResideMenuItemWithMark mDealing;
    private ResideMenuItem mQuotes;
    private ResideMenuItem mHowItWorks;
    private ResideMenuItem mPromotions;
    //private ResideMenuItem mAccounts;
    //private ResideMenuItem mSettings;
    private LinearLayout llShadow;
    private ResideMenuItem mLogout;
    private View mDeposit;

    private static ToolbarFragment toolbar;

    public static boolean backToRootFragment = false;

    public static final int SIGNAL_POSITION = 0;
    public static final int TERMINAL_POSITION = 1;
    public static final int DEALING_POSITION = 2;
    public static final int REFRESH_POSITION = 3;
    public static final int QUOTES_POSITION = 4;

    private GetResponseInfoBroadcastReceiver mInfoBroadcastReceiver;

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
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
    private View.OnClickListener menuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == mLogout) {
                WebSocketApi.closeSocket();
                CustomDialog.showDialogLogout(BaseActivity.this);
            }else if (view == mTerminal && !TerminalFragment.getInstance().isVisible()){
                changeMainFragment(TerminalFragment.getInstance());
            } else if (view == mSupport) {
                changeMainFragment(new SupportFragment());
            } else if (view == mDealing) {
                changeMainFragment(new DealingFragment());
            } else if (view == mQuotes) {
                changeMainFragment(new QuotesFragment());
            } else if (view == mHowItWorks) {
                changeMainFragment(new FAQFragment());
            } else if (view == mPromotions) {
                changeMainFragment(new PromotionsFragment());
            } else if (view == mDepositWithdraw) {
                changeMainFragment(new DepositFragment());
            }else if (view == mDeposit) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.URL_GRAND_CAPITAL_ACCOUNT + User.getInstance().getLogin() + Const.URL_GRAND_CAPITAL_DEPOSIT));
                startActivity(browserIntent);
                GoogleAnalyticsUtil.sendEvent(GoogleAnalyticsUtil.ANALYTICS_IN_OUT_SCREEN, GoogleAnalyticsUtil.ANALYTICS_BUTTON_DEPOSIT, null, null);
            }
            mResideMenu.closeMenu();
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        isRestared = true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        llShadow = (LinearLayout) findViewById(R.id.ll_shadow);
        if (!isAuth()) {
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
            finish();
        } else {
            fragmentManager = getSupportFragmentManager();
            context = this;
            setupMenu();
            //if (savedInstanceState == null) {
                toolbar = ToolbarFragment.getInstance();
                changeToolbarFragment(toolbar);
                if(currentFragment == null) {
                    currentFragment = TerminalFragment.getInstance();
                }
                changeMainFragment(currentFragment);
                getInfoUser();
                startService(new Intent(this, CheckDealingService.class));
            //}
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if(CustomSharedPreferences.isChangedLanguage(this)) {
            CustomSharedPreferences.setLanguage(this);
            restartActivity();
        }*/
        mInfoBroadcastReceiver = new GetResponseInfoBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(InfoUserService.ACTION_SERVICE_GET_INFO);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mInfoBroadcastReceiver, intentFilter);
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRestared = false;
        unregisterReceiver(mInfoBroadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        if(mResideMenu.isOpenedMenu()) {
            mResideMenu.closeMenu();
        }
        if(!setBackActionByCurrFragment()) {
            sCurrentTagFragment = "";
            return;
        }
        if(backToRootFragment) {
            if(!TextUtils.isEmpty(sMainTagFragment)) {
                fragmentManager.popBackStack();
                setToolbarInfoByTag();
            } else {
                setToolBarTerminalInfo();
                changeMainFragment(TerminalFragment.getInstance());
                backToRootFragment = false;
            }
        } else {
            goHome();
        }
    }

    public void hideShadow() {
        llShadow.setVisibility(View.GONE);
    }

    public void showShadow() {
        llShadow.setVisibility(View.VISIBLE);
    }

    private void setupMenu() {
        mResideMenu = new ResideMenu(this);
        mResideMenu.setBackground(R.drawable.bg_menu2);
        mResideMenu.attachToActivity(this);
        mResideMenu.setMenuListener(menuListener);
        mResideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        mResideMenu.setScaleValue(0.75f);
        addItems();
    }

    public void setDealings() {
        mDealing.setValue(CustomSharedPreferences.getAmtOpenDealings(this));
        BaseActivity.getToolbar().setDealingIcon();
    }

    private void addItems() {
        mTerminal = new ResideMenuItem(this, getString(R.string.menu_item_terminal));
        mDealing = new ResideMenuItemWithMark(this, getString(R.string.menu_item_dealing));
        mQuotes = new ResideMenuItem(this, getString(R.string.menu_item_quotes));
        mHowItWorks = new ResideMenuItem(this, getString(R.string.menu_item_how_it_works));
        mPromotions = new ResideMenuItem(this, getString(R.string.menu_item_promotions));
        //mAccounts = new ResideMenuItem(this, getString(R.string.menu_item_accounts));
        mSupport = new ResideMenuItem(this, getString(R.string.menu_item_support));
        mDepositWithdraw = new ResideMenuItem(this, getString(R.string.toolbar_name_deposit));
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
        mDepositWithdraw.setOnClickListener(menuClickListener);
        //mSettings.setOnClickListener(menuClickListener);
        mLogout.setOnClickListener(menuClickListener);
        mDeposit.setOnClickListener(menuClickListener);

        mResideMenu.addMenuItem(mTerminal, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mDealing, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mQuotes, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mDepositWithdraw, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mHowItWorks, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mPromotions, ResideMenu.DIRECTION_LEFT);
        //mResideMenu.addMenuItem(mAccounts, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mSupport, ResideMenu.DIRECTION_LEFT);
        //mResideMenu.addMenuItem(mSettings, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(mLogout, ResideMenu.DIRECTION_LEFT);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mResideMenu.dispatchTouchEvent(ev);
    }

    public void setMain(int i) {
        getToolbar().mTabLayout.setOnChangePosition(() -> {
            Log.d(TAG, "mTabLayout.setOnChangePosition");
            switch (i){
                case SIGNAL_POSITION:
                    Log.d(TAG, "SIGNAL_POSITION");
                    if(TerminalFragment.getInstance() != null && TerminalFragment.isOpen) {
                        TerminalFragment.getInstance().showSignalsPanel();
                    }
                    GoogleAnalyticsUtil.sendEvent(GoogleAnalyticsUtil.ANALYTICS_TERMINAL_SCREEN, GoogleAnalyticsUtil.ANALYTICS_BUTTON_WIDGET_SIGNALS, null, null);
                    break;
                case TERMINAL_POSITION:
                    Log.d(TAG, "TERMINAL_POSITION");
                    if(!TerminalFragment.getInstance().isVisible()){
                        changeMainFragment(TerminalFragment.getInstance());
                    }else if(TerminalFragment.getInstance() != null && !TerminalFragment.getInstance().isDirection) {
                        TerminalFragment.getInstance().showSignalsPanel();
                    }
                    break;
                case DEALING_POSITION:
                    Log.d(TAG, "DEALING_POSITION");
                    changeMainFragment(new DealingFragment());
                    break;
                case REFRESH_POSITION:
                    Log.d(TAG, "REFRESH_POSITION");
                    break;
                case QUOTES_POSITION:
                    Log.d(TAG, "QUOTES_POSITION");
                    changeMainFragment(new QuotesFragment());
                    break;
            }
        });
    }

    public static void changeMainFragment(Fragment targetFragment) {
        backToRootFragment = true;
        fragmentManager.popBackStack();
        if(targetFragment instanceof  TerminalFragment){
            if(backToRootFragment) {
                clearFragmentBackStack();
            }
            backToRootFragment = false;
        }
        onSwitchFragment(targetFragment, targetFragment.getClass().getName(), true, true, R.id.content);
        currentFragment = targetFragment;
    }

    public static void addNextFragment(Fragment fragment) {
        onSwitchFragment(fragment, fragment.getClass().getName(), true, true, R.id.content);
        currentFragment = fragment;
    }

    public static void clearFragmentBackStack() {
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
        tr.replace(res, fragment, tag);
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
        fragmentManager.beginTransaction()
            .replace(R.id.toolbar, targetFragment, "fragment")
            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit();
    }

    public static ResideMenu getResideMenu() {
        return mResideMenu;
    }

    public static ToolbarFragment getToolbar(){
        if(toolbar != null) {
            return toolbar;
        } else {
            toolbar = ToolbarFragment.getInstance();
            changeToolbarFragment(toolbar);
            return toolbar;
        }
    }

    private boolean isAuth() {
        return User.getInstance() != null || CustomSharedPreferences.isSaveUserInPreferences(getApplicationContext());
    }

    private void getInfoUser(){
        Intent intentMyIntentService = new Intent(this, InfoUserService.class);
        startService(intentMyIntentService);
    }

    private void setToolBarTerminalInfo() {
        getToolbar().setPageTitle(context.getResources().getString(R.string.toolbar_name_terminal));
        getToolbar().mTabLayout.setOnLoadData(() -> {
            getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_TERMINALE_FRAGMENT);
            getToolbar().switchTab(BaseActivity.TERMINAL_POSITION);
        });
        try {
            getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_TERMINALE_FRAGMENT);
            getToolbar().switchTab(BaseActivity.TERMINAL_POSITION);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    private void setToolbarInfoByTag() {
        if(sMainTagFragment.equals(PromotionsFragment.class.getName())) {
            getToolbar().setPageTitle(context.getResources().getString(R.string.toolbar_name_promotions));
            getToolbar().hideTabs();
            getToolbar().setBurgerType(ToolbarFragment.BURGER_OPEN_MENU);
        }else if(sMainTagFragment.equals(FAQFragment.class.getName())) {
            getToolbar().setPageTitle(context.getResources().getString(R.string.toolbar_name_how_it_works));
            getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_OTHER_FRAGMENT);
            getToolbar().deselectAll();
            getToolbar().setBurgerType(ToolbarFragment.BURGER_OPEN_MENU);
        }else if(sMainTagFragment.equals(DepositFragment.class.getName())) {
            getToolbar().setPageTitle(context.getResources().getString(R.string.toolbar_name_deposit));
            getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_OTHER_FRAGMENT);
            getToolbar().setBurgerType(ToolbarFragment.BURGER_OPEN_MENU);
            getToolbar().deselectAll();
        }else if (sMainTagFragment.equals(QuotesChoiceFragment.class.getName())) {
            BaseActivity.getToolbar().setBurgerType(ToolbarFragment.BURGER_OPEN_MENU);
            BaseActivity.getToolbar().setPageTitle(getApplicationContext().getResources().getString(R.string.toolbar_name_terminal));
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
        sMainTagFragment = "";
    }

    private static boolean setBackActionByCurrFragment() {
        if(sCurrentTagFragment.equals(WebFragment.class.getName())) {
            if(WebFragment.sWebFragment.wvWeb.canGoBack()) {
                WebFragment.sWebFragment.wvWeb.goBack();
                return false;
            }
            return true;
        }
        return true;
    }

    private void goHome() {
        Intent showOptions = new Intent(Intent.ACTION_MAIN);
        showOptions.addCategory(Intent.CATEGORY_HOME);
        startActivity(showOptions);
    }

    public class GetResponseInfoBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra(InfoUserService.RESPONSE_INFO) != null && intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY) != null){
                if(intent.getStringExtra(InfoUserService.RESPONSE_INFO).equals(Const.RESPONSE_CODE_SUCCESS) && intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY).equals(Const.RESPONSE_CODE_SUCCESS)){
                    mResideMenu.refreshBalanceUser();
                    mResideMenu.refreshNameUser();
                }
            }
        }
    }
}
