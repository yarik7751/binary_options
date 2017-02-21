package com.elatesoftware.grandcapital.views.fragments;

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

public class ToolbarFragment extends Fragment {

    private ResideMenu mResideMenu;
    private BaseActivity mParentActivity;

    private TabLayout mTabLayout;
    private TextView mPageTitle;

    private static final float TRANSPARENT_TAB_ICON_VALUE = 0.5f;
    private static final float NOT_TRANSPARENT_TAB_ICON_VALUE = 1f;

    public static final int TOOLBAR_TERMINATE_FRAGMENT = 101;
    public static final int TOOLBAR_OTHER_FRAGMENT = 102;
    public static final int TOOLBAR_EMPTY_FRAGMENT = 103;

    public ToolbarFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setUpViews();
        View res = inflater.inflate(R.layout.fragment_toolbar, container, false);
        return res;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mParentActivity.findViewById(R.id.menu_burger).setOnClickListener(v -> mResideMenu.openMenu(ResideMenu.DIRECTION_LEFT));
        mPageTitle = (TextView) getView().findViewById(R.id.page_title);
        setupToolbar();
       /* mTabLayout = (TabLayout) getView().findViewById(R.id.tabLayout);
        mTabLayout.setVisibility(View.VISIBLE);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        changeFragment(TOOLBAR_TERMINATE_FRAGMENT);*/
    }
    private void setupToolbar() {
        int[] drawableResourses = {R.drawable.signal, R.drawable.terminal, R.drawable.order, R.drawable.quotes};
        mTabLayout = (TabLayout) getView().findViewById(R.id.tabLayout);
        mTabLayout.setVisibility(View.VISIBLE);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        if(mTabLayout.getTabCount() == drawableResourses.length){
            for(int i = 0; i < mTabLayout.getTabCount(); i++){
                ImageView icon = new ImageView(this.getContext());
                icon.setImageDrawable(getResources().getDrawable(drawableResourses[i]));
                icon.setAlpha(TRANSPARENT_TAB_ICON_VALUE);
                mTabLayout.getTabAt(i).setCustomView(icon);
            }
        }
        mTabLayout.getTabAt(0).select();
    }
/*
    public void changeFragment(int currentFragment){
        int[] drawables = null;
        switch (currentFragment) {
            case TOOLBAR_EMPTY_FRAGMENT:
                drawables = null;
                break;
            case TOOLBAR_OTHER_FRAGMENT:
                drawables = new int[]{R.drawable.terminal, R.drawable.order, R.drawable.quotes};
                break;
            case TOOLBAR_TERMINATE_FRAGMENT:
                drawables = new int[]{R.drawable.signal, R.drawable.terminal, R.drawable.order, R.drawable.quotes};
                break;
        }
        if(mTabLayout.getTabCount() == drawables.length){
            for(int i = 0; i < mTabLayout.getTabCount(); i++){
                ImageView icon = new ImageView(this.getContext());
                icon.setImageDrawable(getResources().getDrawable(drawables[i]));
                icon.setAlpha(TRANSPARENT_TAB_ICON_VALUE);
                mTabLayout.getTabAt(i).setCustomView(icon);
            }
        }
        mTabLayout.getTabAt(0).select();
    }*/

    public void switchTab(int position){
        if(position > 0 && position < 5) {
            showTabs();
            mTabLayout.getTabAt(position).select();
        }
    }

    public void hideTabs(){
        if(mTabLayout.getVisibility() == View.VISIBLE)
        {
            mTabLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void showTabs(){
        if(mTabLayout.getVisibility() == View.INVISIBLE)
        {
            mTabLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setPageTitle(String title){
       if(mPageTitle != null){
           if(!title.isEmpty()){
               mPageTitle.setText(title);
           }
       }
    }

    private void setUpViews() {
        mParentActivity = (BaseActivity) getActivity();
        mResideMenu = mParentActivity.getResideMenu();
    }

}
