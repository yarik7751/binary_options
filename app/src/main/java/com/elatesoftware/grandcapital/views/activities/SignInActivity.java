package com.elatesoftware.grandcapital.views.activities;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.AuthorizationAnswer;
import com.elatesoftware.grandcapital.api.pojo.InfoAnswer;
import com.elatesoftware.grandcapital.models.User;
import com.elatesoftware.grandcapital.services.InfoUserService;
import com.elatesoftware.grandcapital.services.SignInService;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.views.interfaces.OnKeyboardVisibilityListener;
import com.elatesoftware.grandcapital.views.items.CustomDialog;

public class SignInActivity extends CustomFontsActivity implements OnKeyboardVisibilityListener {

    private EditText etLogin;
    private EditText etPassword;
    private Button btnSignIn;
    private LinearLayout llProgress;
    private TextView tvSignUp;
    private TextView tvForgotPassword;
    private TextInputLayout tilLogin;
    private TextInputLayout tilPassword;
    private RelativeLayout rlLogo;
    private GetResponseSignInBroadcastReceiver mSignInBroadcastReceiver;
    private GetResponseInfoBroadcastReceiver mInfoBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        llProgress = (LinearLayout) findViewById(R.id.layout_progress_bar);
        etLogin = (EditText) findViewById(R.id.login);
        etPassword = (EditText) findViewById(R.id.password);
        tvSignUp = (TextView) findViewById(R.id.signup_link);
        btnSignIn = (Button) findViewById(R.id.email_sign_in_button);
        tilLogin = (TextInputLayout) findViewById(R.id.emailEditTextLayout);
        tilPassword = (TextInputLayout) findViewById(R.id.passwordEditTextLayout);
        tvForgotPassword = (TextView) findViewById(R.id.forgot_password_link);
        rlLogo = (RelativeLayout) findViewById(R.id.rl_logo);
        setKeyboardVisibilityListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ViewGroup rootView = (ViewGroup) findViewById(R.id.email_login_form);
            LayoutTransition layoutTransition = rootView.getLayoutTransition();
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        }
        tvSignUp.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.URL_GRAND_CAPITAL_SIGN_UP));
            startActivity(browserIntent);
        });
        btnSignIn.setOnClickListener(view -> {{
            etLogin.setText("10031740");
            etPassword.setText("2nFaxHcy");
            signIn();
        }});
        tilLogin.setErrorEnabled(true);
        tilPassword.setErrorEnabled(true);
        etLogin.setOnFocusChangeListener((v, hasFocus) -> {
            tilLogin.setError(null);
        });
        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            tilPassword.setError(null);
        });
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
        tvForgotPassword.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.link_company)));
            startActivity(browserIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mInfoBroadcastReceiver = new GetResponseInfoBroadcastReceiver();
        IntentFilter intentFilter1 = new IntentFilter(InfoUserService.ACTION_SERVICE_GET_INFO);
        intentFilter1.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mInfoBroadcastReceiver, intentFilter1);
        mSignInBroadcastReceiver = new GetResponseSignInBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(SignInService.ACTION_SERVICE_SIGN_IN);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mSignInBroadcastReceiver, intentFilter);
        animationCloseKeyboard();
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mSignInBroadcastReceiver);
        unregisterReceiver(mInfoBroadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if(visible){
            animationOpenKeyboard();
        }else{
            animationCloseKeyboard();
        }
    }
    private void setKeyboardVisibilityListener(final OnKeyboardVisibilityListener onKeyboardVisibilityListener) {
        final View parentView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private boolean alreadyOpen;
            private final int defaultKeyboardHeightDP = 100;
            private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
            private final Rect rect = new Rect();

            @Override
            public void onGlobalLayout() {
                int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, parentView.getResources().getDisplayMetrics());
                parentView.getWindowVisibleDisplayFrame(rect);
                int heightDiff = parentView.getRootView().getHeight() - (rect.bottom - rect.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == alreadyOpen) {
                    return;
                }
                alreadyOpen = isShown;
                onKeyboardVisibilityListener.onVisibilityChanged(isShown);
            }
        });
    }
    private void animationOpenKeyboard(){
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(rlLogo,
                PropertyValuesHolder.ofFloat("scaleX", 0.5f),
                PropertyValuesHolder.ofFloat("scaleY", 0.5f));
        scaleDown.setDuration(300);
        scaleDown.start();
    }
    private void animationCloseKeyboard(){
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(rlLogo,
                PropertyValuesHolder.ofFloat("scaleX", 0.7f),
                PropertyValuesHolder.ofFloat("scaleY", 0.7f));
        scaleDown.setDuration(300);
        scaleDown.start();
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
            CustomDialog.showDialogInfo(this, getString(R.string.no_internet_connection_title), getString(R.string.no_internet_connection_text));
        }
    }

    public class GetResponseSignInBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(SignInService.RESPONSE);
            llProgress.setVisibility(View.GONE);
            if (response != null) {
                if (response.equals(Const.RESPONSE_CODE_ERROR)) {
                    CustomDialog.showDialogInfo(SignInActivity.this, getString(R.string.error), getString(R.string.no_correct_values));
                } else if(response.equals(Const.RESPONSE_CODE_SUCCESS)){
                    final String login = etLogin.getText().toString();
                    if(AuthorizationAnswer.getInstance() != null){
                        User.setInstance(new User(login, AuthorizationAnswer.getInstance().getServerName(), AuthorizationAnswer.getInstance().getToken()));
                        requestInfoUser();
                    }
                }
            } else {
                llProgress.setVisibility(View.GONE);
                CustomDialog.showDialogInfo(SignInActivity.this, getString(R.string.request_error_title), getString(R.string.request_error_text));
            }
            btnSignIn.setEnabled(true);
        }
    }

    private boolean checkInput(String login, String password) {
        boolean answer = true;
        if (login == null || login.isEmpty()) {
            etLogin.setError(getString(R.string.error_incorrent_login_input));
            tilLogin.setError(getString(R.string.error_incorrent_login_input));
            answer = false;
        }
        if (password == null || password.isEmpty()) {
            tilPassword.setError(getString(R.string.error_incorrent_password_input));
            etPassword.setError(getString(R.string.error_incorrent_login_input));
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
    private void requestInfoUser(){
        Intent intentMyIntentService = new Intent(this, InfoUserService.class);
        startService(intentMyIntentService);
    }

    public class GetResponseInfoBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra(InfoUserService.RESPONSE_INFO) != null && intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY) != null){
                if(intent.getStringExtra(InfoUserService.RESPONSE_INFO).equals(Const.RESPONSE_CODE_SUCCESS) && intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY).equals(Const.RESPONSE_CODE_SUCCESS)) {
                    if (InfoAnswer.getInstance() != null
                            && InfoAnswer.getInstance().getGroup() != null
                            && InfoAnswer.getInstance().getGroup().getOptionsStyle() != null
                            && ((InfoAnswer.getInstance().getGroup().getOptionsStyle().equals("american") || (InfoAnswer.getInstance().getGroup().getOptionsStyle().equals("european"))))) {
                          CustomSharedPreferences.saveUser(getApplicationContext(), User.getInstance());
                          CustomSharedPreferences.setIntervalAdvertising(SignInActivity.this, -1);
                          showTerminalActivity();
                    }else{
                        CustomDialog.showDialogInfo(SignInActivity.this, getString(R.string.error), getString(R.string.no_correct_values));
                    }
                }
            }
        }
    }
}

