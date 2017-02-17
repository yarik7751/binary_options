package com.elatesoftware.grandcapital.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;

public class SignUpActivity extends CustomFontsActivity {

    private TextView mSignInLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mSignInLink = (TextView) findViewById(R.id.signup_signin_link);
        mSignInLink.setOnClickListener(v -> showSigninActivity());
    }

    private void showSigninActivity() {
        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(i);
    }
}
