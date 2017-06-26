package com.elatesoftware.grandcapital.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.utils.AndroidUtils;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.items.resideMenu.ResideMenu;
import com.elatesoftware.grandcapital.views.items.tooltabsview.ToolTabsView;
import com.elatesoftware.grandcapital.views.items.tooltabsview.adapter.ToolTabsViewAdapter;

public class ToolbarFragment extends Fragment {

    private ResideMenu mResideMenu;
    private BaseActivity mParentActivity;

    public ToolTabsView mTabLayout;
    private TextView mPageTitle;
    private ImageView imgBurger;
    private RelativeLayout rlBurger;

    //private static final float TRANSPARENT_TAB_ICON_VALUE = 0.5f;
   // private static final float NOT_TRANSPARENT_TAB_ICON_VALUE = 1f;

    public static final int TOOLBAR_TERMINALE_FRAGMENT = 101;
    public static final int TOOLBAR_OTHER_FRAGMENT = 102;
    public static final int TOOLBAR_EMPTY_FRAGMENT = 103;
    public static final int TOOLBAR_REFRESH_FRAGMENT = 104;

    public static final int BURGER_OPEN_MENU = 201;
    public static final int BURGER_BACK_PRESSED_ACTIVITY = 202;
    public static final int BURGER_BACK_PRESSED = 203;
    private int burgerType = BURGER_OPEN_MENU;

    private static ToolbarFragment fragment = null;

    View.OnClickListener burgerClick = v -> {
        AndroidUtils.hideKeyboard(getActivity());
        if (burgerType == BURGER_OPEN_MENU) {
            mResideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
        }
        if (burgerType == BURGER_BACK_PRESSED_ACTIVITY) {
            getActivity().onBackPressed();
        }
        if (burgerType == BURGER_BACK_PRESSED) {
            BaseActivity.fragmentManager.popBackStack();
            BaseActivity.getToolbar().setBurgerType(ToolbarFragment.BURGER_OPEN_MENU);
            BaseActivity.getToolbar().setPageTitle(getActivity().getResources().getString(R.string.toolbar_name_terminal));
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
    };

    public static ToolbarFragment getInstance() {
        if (fragment == null) {
            fragment = new ToolbarFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setUpViews();
        return inflater.inflate(R.layout.fragment_toolbar, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        imgBurger = (ImageView) mParentActivity.findViewById(R.id.menu_burger);
        rlBurger = (RelativeLayout) mParentActivity.findViewById(R.id.rl_burger);
        rlBurger.setOnClickListener(burgerClick);
        imgBurger.setOnClickListener(burgerClick);
        mPageTitle = (TextView) getView().findViewById(R.id.page_title);
        setupToolbar();
    }

    private void setupToolbar() {
        int[] drawableResources = {
                R.drawable.signal,
                R.drawable.terminal,
                CustomSharedPreferences.getAmtOpenDealings(getContext()) == 0 ? R.drawable.order : R.drawable.order_active,
                R.drawable.arrowdown,
                R.drawable.quotes
        };
        mTabLayout = (ToolTabsView) getView().findViewById(R.id.tabLayout);
        mTabLayout.setVisibility(View.VISIBLE);

        mTabLayout.setAdapter(new ToolTabsViewAdapter(getContext(), drawableResources));
        mTabLayout.setOnLoadData(() -> {

        });
        mTabLayout.setOnChooseTab((view, position) -> mParentActivity.setMain(position));
    }

    public void setBurgerType(int _burgerType) {
        burgerType = _burgerType;
        if (burgerType == BURGER_OPEN_MENU) {
            imgBurger.setImageResource(R.drawable.ic_menu);
        }
        if (burgerType == BURGER_BACK_PRESSED_ACTIVITY) {
            imgBurger.setImageResource(R.drawable.ic_back);
        }
        if (burgerType == BURGER_BACK_PRESSED) {
            imgBurger.setImageResource(R.drawable.ic_back);
        }
    }

    public void setDealingSelectIcon() {
        mTabLayout.setIcon(BaseActivity.DEALING_POSITION, R.drawable.order_active);
    }

    public void setDealingIcon() {
        mTabLayout.setIcon(BaseActivity.DEALING_POSITION, R.drawable.order);
    }

    public void switchTab(int position) {
        if (position >= 0 && position < 5) {
            showTabs();
            mTabLayout.selectTab(position);
        }
    }
    /**
     * скрыть табы в зависимости от типа фрагмента
     * @param type
     */
    public void hideTabsByType(int type) {
        if (mTabLayout.animation != null) {
            if (mTabLayout.animation.hasStarted()) {
                mTabLayout.animation.cancel();
            }
        }
        if(mTabLayout == null) {
            return;
        }
        mTabLayout.setVisibility(View.VISIBLE);
        mTabLayout.showAllTabs();
        showTabs();
        switch (type) {
            case TOOLBAR_TERMINALE_FRAGMENT:
                mTabLayout.showTab(BaseActivity.SIGNAL_POSITION);
                mTabLayout.showTab(BaseActivity.TERMINAL_POSITION);
                mTabLayout.showTab(BaseActivity.DEALING_POSITION);
                mTabLayout.hideTab(BaseActivity.REFRESH_POSITION);
                mTabLayout.showTab(BaseActivity.QUOTES_POSITION);
                break;
            case TOOLBAR_OTHER_FRAGMENT:
                mTabLayout.hideTab(BaseActivity.SIGNAL_POSITION);
                mTabLayout.showTab(BaseActivity.TERMINAL_POSITION);
                mTabLayout.showTab(BaseActivity.DEALING_POSITION);
                mTabLayout.hideTab(BaseActivity.REFRESH_POSITION);
                mTabLayout.showTab(BaseActivity.QUOTES_POSITION);
                break;
            case TOOLBAR_EMPTY_FRAGMENT:
                //mTabLayout.setVisibility(View.INVISIBLE);
                //hideTabs();
                mTabLayout.hideTab(BaseActivity.SIGNAL_POSITION);
                mTabLayout.hideTab(BaseActivity.TERMINAL_POSITION);
                mTabLayout.hideTab(BaseActivity.DEALING_POSITION);
                mTabLayout.hideTab(BaseActivity.REFRESH_POSITION);
                mTabLayout.hideTab(BaseActivity.QUOTES_POSITION);
                break;
            case TOOLBAR_REFRESH_FRAGMENT:
                mTabLayout.hideTab(BaseActivity.SIGNAL_POSITION);
                mTabLayout.showTab(BaseActivity.TERMINAL_POSITION);
                mTabLayout.showTab(BaseActivity.DEALING_POSITION);
                mTabLayout.showTab(BaseActivity.REFRESH_POSITION);
                mTabLayout.showTab(BaseActivity.QUOTES_POSITION);
                break;
            default:
                break;
        }

        ((RelativeLayout.LayoutParams) mPageTitle.getLayoutParams()).rightMargin = AndroidUtils.dp(180 - mTabLayout.getHideTabsCount() * 170 / 5);
    }

    public void deselectAll() {
        mTabLayout.deselectAllTabs();
    }

    public void hideTabs() {
        if (mTabLayout.getVisibility() == View.VISIBLE) {
            mTabLayout.setVisibility(View.GONE);
            mTabLayout.getLayoutParams().width = 0;
            ((RelativeLayout.LayoutParams) mPageTitle.getLayoutParams()).rightMargin = AndroidUtils.dp(1);
        }
    }

    public void showTabs() {
        //if (mTabLayout.getVisibility() == View.INVISIBLE || mTabLayout.getVisibility() == View.GONE) {
            mTabLayout.setVisibility(View.VISIBLE);
            mTabLayout.getLayoutParams().width = AndroidUtils.dp(170);
            ((RelativeLayout.LayoutParams) mPageTitle.getLayoutParams()).rightMargin = AndroidUtils.dp(180);
        //}
    }

    public void setPageTitle(String title) {
        if (mPageTitle != null) {
            if (!title.isEmpty()) {
                mPageTitle.setText(title);
            }
        }
    }

    private void setUpViews() {
        mParentActivity = (BaseActivity) getActivity();
        mResideMenu = mParentActivity.getResideMenu();
    }
}
