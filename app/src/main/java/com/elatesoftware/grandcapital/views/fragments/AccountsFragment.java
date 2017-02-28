package com.elatesoftware.grandcapital.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;

public class AccountsFragment extends Fragment {

    private static AccountsFragment fragment = null;
    public static AccountsFragment getInstance() {
        if (fragment == null) {
            fragment = new AccountsFragment();
        }
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accounts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_accounts));
        BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_REFRESH_FRAGMENT);
        BaseActivity.getToolbar().deselectAll();
    }
}
