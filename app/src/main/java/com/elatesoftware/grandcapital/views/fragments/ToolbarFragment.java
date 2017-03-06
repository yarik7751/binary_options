package com.elatesoftware.grandcapital.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.items.ResideMenu.ResideMenu;
import com.elatesoftware.grandcapital.views.items.tooltabsview.ToolTabsView;
import com.elatesoftware.grandcapital.views.items.tooltabsview.adapter.OnChooseTab;
import com.elatesoftware.grandcapital.views.items.tooltabsview.adapter.OnLoadData;
import com.elatesoftware.grandcapital.views.items.tooltabsview.adapter.ToolTabsViewAdapter;

public class ToolbarFragment extends Fragment {

    private ResideMenu mResideMenu;
    private BaseActivity mParentActivity;

    public ToolTabsView mTabLayout;
    private TextView mPageTitle;
    private ImageView imgBurger;

    private static final float TRANSPARENT_TAB_ICON_VALUE = 0.5f;
    private static final float NOT_TRANSPARENT_TAB_ICON_VALUE = 1f;

    public static final int TOOLBAR_TERMINALE_FRAGMENT = 101;
    public static final int TOOLBAR_OTHER_FRAGMENT = 102;
    public static final int TOOLBAR_EMPTY_FRAGMENT = 103;
    public static final int TOOLBAR_REFRESH_FRAGMENT = 104;

    public static final int BURGER_OPEN_MENU = 201;
    public static final int BURGER_BACK_PRESSED = 202;
    private int burgerType = BURGER_OPEN_MENU;

    private static ToolbarFragment fragment = null;

    public static ToolbarFragment getInstance() {
        if (fragment == null) {
            fragment = new ToolbarFragment();
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setUpViews();
        View res = inflater.inflate(R.layout.fragment_toolbar, container, false);
        return res;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        imgBurger = (ImageView) mParentActivity.findViewById(R.id.menu_burger);
        //imgBurger.setOnClickListener(v -> mResideMenu.openMenu(ResideMenu.DIRECTION_LEFT));
        imgBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (burgerType == BURGER_OPEN_MENU) {
                    mResideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                }
                if (burgerType == BURGER_BACK_PRESSED) {
                    getActivity().onBackPressed();
                }
            }
        });
        mPageTitle = (TextView) getView().findViewById(R.id.page_title);
        setupToolbar();
    }

    private void setupToolbar() {
        int[] drawableResources = {
                R.drawable.signal,
                R.drawable.terminal,
                R.drawable.order,
                R.drawable.arrowdown,
                R.drawable.quotes
        };
        mTabLayout = (ToolTabsView) getView().findViewById(R.id.tabLayout);
        mTabLayout.setVisibility(View.VISIBLE);

        mTabLayout.setAdapter(new ToolTabsViewAdapter(getContext(), drawableResources));
        mTabLayout.setOnLoadData(new OnLoadData() {
            @Override
            public void loadData() {

            }
        });

        mTabLayout.setOnChooseTab(new OnChooseTab() {
            @Override
            public void onChoose(View view, int position) {
                mParentActivity.setMain(position);
            }
        });

        /*mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ImageView image = (ImageView) tab.getCustomView();
                image.setAlpha(NOT_TRANSPARENT_TAB_ICON_VALUE);
                mParentActivity.setMain(mTabLayout.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ImageView image = (ImageView) tab.getCustomView();
                image.setAlpha(TRANSPARENT_TAB_ICON_VALUE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                ImageView image = (ImageView) tab.getCustomView();
                image.setAlpha(NOT_TRANSPARENT_TAB_ICON_VALUE);
            }
        });

        if(mTabLayout.getTabCount() == drawableResources.length){
            for(int i = 0; i < mTabLayout.getTabCount(); i++){
                ImageView icon = new ImageView(this.getContext());
                icon.setImageDrawable(getResources().getDrawable(drawableResources[i]));
                icon.setAlpha(TRANSPARENT_TAB_ICON_VALUE);
                mTabLayout.getTabAt(i).setCustomView(icon);
            }
        }
        mTabLayout.getTabAt(0).select();*/
    }

    public void setBurgerType(int _burgerType) {
        burgerType = _burgerType;
        if (burgerType == BURGER_OPEN_MENU) {
            imgBurger.setImageResource(R.drawable.menu_icon);
        }
        if (burgerType == BURGER_BACK_PRESSED) {
            imgBurger.setImageResource(R.drawable.ic_keyboard_arrow_left_white_36dp);
        }
    }

    public void switchTab(int position) {
        if (position >= 0 && position < 5) {
            showTabs();
            //mTabLayout.getTabAt(position).select();
            mTabLayout.selectTab(position);
        }
    }

    /**
     * скрыть табы в зависимости от типа фрагмента
     *
     * @param type
     */
    public void hideTabsByType(int type) {
        if (mTabLayout.animation != null) {
            if (mTabLayout.animation.hasStarted()) {
                mTabLayout.animation.cancel();
            }
        }

        mTabLayout.setVisibility(View.VISIBLE);
        mTabLayout.showAllTabs();
        switch (type) {
            case TOOLBAR_TERMINALE_FRAGMENT:
                mTabLayout.showTab(0);
                mTabLayout.showTab(1);
                mTabLayout.showTab(2);
                mTabLayout.hideTab(3);
                mTabLayout.showTab(4);
                break;
            case TOOLBAR_OTHER_FRAGMENT:
                mTabLayout.hideTab(0);
                mTabLayout.showTab(1);
                mTabLayout.showTab(2);
                mTabLayout.hideTab(3);
                mTabLayout.showTab(4);
                break;

            case TOOLBAR_EMPTY_FRAGMENT:
                mTabLayout.setVisibility(View.INVISIBLE);
                break;
            case TOOLBAR_REFRESH_FRAGMENT:
                mTabLayout.hideTab(0);
                mTabLayout.showTab(1);
                mTabLayout.showTab(2);
                mTabLayout.showTab(3);
                mTabLayout.showTab(4);
                break;
        }
    }

    public void deselectAll() {
        mTabLayout.deselectAllTabs();
    }

    public void hideTabs() {
        if (mTabLayout.getVisibility() == View.VISIBLE) {
            mTabLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void showTabs() {
        if (mTabLayout.getVisibility() == View.INVISIBLE) {
            mTabLayout.setVisibility(View.VISIBLE);
        }
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
