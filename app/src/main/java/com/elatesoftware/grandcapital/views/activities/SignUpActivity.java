package com.elatesoftware.grandcapital.views.activities;

import android.os.Bundle;
import android.widget.TextView;
import com.elatesoftware.grandcapital.R;

public class SignUpActivity extends CustomFontsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView mSignInLink = (TextView) findViewById(R.id.signup_signin_link);
        mSignInLink.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
