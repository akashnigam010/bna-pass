package in.bananaa.pass.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import in.bananaa.pass.HomeActivity;
import in.bananaa.pass.R;
import in.bananaa.pass.dto.user.LoginResponse;
import in.bananaa.pass.util.Constant;
import in.bananaa.pass.util.PreferenceManager;
import in.bananaa.pass.util.URLs;
import in.bananaa.pass.util.Utils;

public class LoginActivity extends AppCompatActivity {

    private AppCompatActivity mContext;
    private TextView tvTitle;
    private TextInputLayout tilUserId;
    private AutoCompleteTextView mUserIdView;
    private TextInputLayout tilPassword;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (PreferenceManager.isUserLoggedIn()) {
            startHomeActivity();
        }

        mUserIdView = findViewById(R.id.userId);
        tilUserId =  findViewById(R.id.tilUserId);
        tilPassword = findViewById(R.id.tilPassword);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mSignInButton = findViewById(R.id.signInButton);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        customizeToolbar();
        setFont();
    }

    private Toolbar customizeToolbar() {
        Toolbar toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        tvTitle = findViewById(R.id.login_toolbar_title);
        return toolbar;
    }

    private void attemptLogin() {
        if (!Utils.checkIfInternetConnectedAndToast(this)) {
            return;
        }

        mUserIdView.setError(null);
        mPasswordView.setError(null);

        String userId = mUserIdView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(userId)) {
            mUserIdView.setError(getString(R.string.error_user_id_required));
            focusView = mUserIdView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_password_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            try {
                showProgress(true);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", userId);
                jsonObject.put("password", password);
                StringEntity entity = new StringEntity(jsonObject.toString());
                AsyncHttpClient client = new AsyncHttpClient();
                client.setTimeout(Constant.TIMEOUT);
                client.post(LoginActivity.this, URLs.LOGIN, entity, "application/json", new LoginActivity.LoginResponseHandler());
            } catch (Exception e) {
                Utils.exceptionOccurred(this, e);
            }
        }
    }

    public class LoginResponseHandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            showProgress(false);
            LoginResponse response = new Gson().fromJson(new String(responseBody), LoginResponse.class);
            if (response.isResult()) {
                PreferenceManager.putLoginDetails(response);
                startHomeActivity();
            } else {
                Utils.responseError(mContext, response);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            showProgress(false);
            Utils.responseFailure(mContext);
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void setFont() {
        tvTitle.setTypeface(Utils.getRegularFont(this));
        mUserIdView.setTypeface(Utils.getRegularFont(this));
        mPasswordView.setTypeface(Utils.getRegularFont(this));
        mSignInButton.setTypeface(Utils.getRegularFont(this));
        tilUserId.setTypeface(Utils.getRegularFont(this));
        tilPassword.setTypeface(Utils.getRegularFont(this));
    }
}

