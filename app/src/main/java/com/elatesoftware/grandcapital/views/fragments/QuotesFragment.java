package com.elatesoftware.grandcapital.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuotesFragment extends Fragment {

    private static QuotesFragment fragment = null;
    public static QuotesFragment getInstance() {
        if (fragment == null) {
            fragment = new QuotesFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quotes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().switchTab(BaseActivity.QUOTES_POSITION);
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_quotes));
    }
}
