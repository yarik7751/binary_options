package com.elatesoftware.grandcapital.views.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.AuthorizationAnswer;
import com.elatesoftware.grandcapital.models.User;
import com.elatesoftware.grandcapital.services.SignInService;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.views.items.CustomDialog;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;

public class SignInActivity extends CustomFontsActivity {

    private EditText etLogin;
    private EditText etPassword;
    private Button btnSignIn;
    private LinearLayout llProgress;
    private TextView tvSignUp;
    private TextView tvForgotPassword;
    private TextInputLayout tilLogin;
    private TextInputLayout tilPassword;
    private GetResponseSignInBroadcastReceiver mSignInBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        llProgress = (LinearLayout) findViewById(R.id.fragment_dealing_progress_bar);
        etLogin = (EditText) findViewById(R.id.login);
        etPassword = (EditText) findViewById(R.id.password);
        tvSignUp = (TextView) findViewById(R.id.signup_link);
        btnSignIn = (Button) findViewById(R.id.email_sign_in_button);
        tilLogin = (TextInputLayout) findViewById(R.id.emailEditTextLayout);
        tilPassword = (TextInputLayout) findViewById(R.id.passwordEditTextLayout);
        tvForgotPassword = (TextView) findViewById(R.id.forgot_password_link);

        tvSignUp.setOnClickListener(v -> {
            /*Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(i);*/
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.URL_GRAND_CAPITAL_SIGN_UP));
            startActivity(browserIntent);
        });
        btnSignIn.setOnClickListener(view -> signIn());
        tilLogin.setErrorEnabled(true);
        tilPassword.setErrorEnabled(true);

        /*etLogin.setText("10031740");
        etPassword.setText("2nFaxHcy");*/

        etLogin.setOnFocusChangeListener((v, hasFocus) -> tilLogin.setError(null));
        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO || event == null || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                signIn();
                View view = SignInActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });
        etPassword.setOnFocusChangeListener((v, hasFocus) -> tilPassword.setError(null));
        tvForgotPassword.setOnClickListener(v -> {
//            llProgress.setVisibility(View.VISIBLE);
//            Intent intent = new Intent(SignInActivity.this, WebActivity.class);
//            startActivity(intent);
//            llProgress.setVisibility(View.GONE);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.link_company)));
            startActivity(browserIntent);
        });
    }

    public void signIn() {
        btnSignIn.setEnabled(false);
        if (checkConnection()) {
            final String login = etLogin.getText().toString();
            String password = etPassword.getText().toString();
            if (checkInput(login, password)) {
                llProgress.setVisibility(View.VISIBLE);
                Intent intentMyIntentService = new Intent(this, SignInService.class);
                startService(intentMyIntentService.putExtra(SignInService.LOGIN, login).putExtra(SignInService.PASSWORD, password));
            } else {
                llProgress.setVisibility(View.GONE);
                btnSignIn.setEnabled(true);
            }
        } else {
            llProgress.setVisibility(View.GONE);
            btnSignIn.setEnabled(true);
            CustomDialog.showDialogInfo(this, getString(R.string.no_internet_connection_title),
                                             getString(R.string.no_internet_connection_text));
        }
    }

    public class GetResponseSignInBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(SignInService.RESPONSE);
            llProgress.setVisibility(View.GONE);
            if (response != null) {
                if (response.equals(Const.RESPONSE_CODE_ERROR)) {
                    CustomDialog.showDialogInfo(SignInActivity.this, getString(R.string.error),
                            getString(R.string.no_correct_values));
                } else if(response.equals(Const.RESPONSE_CODE_SUCCESS)){
                    final String login = etLogin.getText().toString();
                    if(AuthorizationAnswer.getInstance() != null){
                        User currentUser = new User(login, AuthorizationAnswer.getInstance().getServerName(),
                                                           AuthorizationAnswer.getInstance().getToken());
                        CustomSharedPreferences.saveUser(getApplicationContext(), currentUser);
                        CustomSharedPreferences.setIntervalAdvertising(SignInActivity.this, -1);
                        showTerminalActivity();
                    }
                }
            } else {
                llProgress.setVisibility(View.GONE);
                CustomDialog.showDialogInfo(SignInActivity.this, getString(R.string.request_error_title),
                                                                 getString(R.string.request_error_text));
            }
            btnSignIn.setEnabled(true);
        }
    }

    private boolean checkInput(String login, String password) {
        boolean answer = true;
        if (login.isEmpty()) {
            tilLogin.setError(getString(R.string.error_incorrent_login_input));
            answer = false;
        }
        if (password.isEmpty()) {
            tilPassword.setError(getString(R.string.error_incorrent_password_input));
            answer = false;
        }
        if (!answer) {
            return false;
        }
        tilLogin.setError(null);
        tilPassword.setError(null);
        return true;
    }

    public boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void showTerminalActivity() {
        Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSignInBroadcastReceiver = new GetResponseSignInBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(SignInService.ACTION_SERVICE_SIGN_IN);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mSignInBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mSignInBroadcastReceiver);
    }
}

