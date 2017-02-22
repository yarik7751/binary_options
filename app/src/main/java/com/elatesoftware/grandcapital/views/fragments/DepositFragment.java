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
 * Created by Дарья Высокович on 21.02.2017.
 */

public class DepositFragment  extends Fragment {

    public DepositFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deposit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_deposit));
    }
}
