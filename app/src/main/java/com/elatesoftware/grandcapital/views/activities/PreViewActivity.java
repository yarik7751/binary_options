package com.elatesoftware.grandcapital.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Дарья Высокович on 17.05.2017.
 */

public class PreViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(PreViewActivity.this, BaseActivity.class));
    }
}
