package in.bananaa.pass;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import in.bananaa.pass.activity.LoginActivity;
import in.bananaa.pass.dto.scan.ScanResponse;
import in.bananaa.pass.util.Constant;
import in.bananaa.pass.util.PreferenceManager;
import in.bananaa.pass.util.URLs;
import in.bananaa.pass.util.Utils;

import static in.bananaa.pass.util.Constant.HOME_TO_SCAN_REQ_CODE;

public class HomeActivity extends AppCompatActivity {
    private AppCompatActivity mContext;
    private TextView tvTitle;
    private ImageButton logoutBtn;
    private FloatingActionButton scanButton;

    private LinearLayout userProfile;
    private TextView userName;
    private TextView userLocation;
    private TextView welcomeMessage;
    private LinearLayout memberProfile;
    private ImageView ivImage;
    private TextView name;
    private TextView startDate;
    private TextView startDateLabel;
    private TextView endDate;
    private TextView endDateLabel;
    private TextView access;
    private TextView accessLabel;
    private TextView reason;

    private View mProgressView;
    private RelativeLayout homeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userProfile = findViewById(R.id.userProfile);
        userName = findViewById(R.id.userName);
        userLocation = findViewById(R.id.userLocation);
        welcomeMessage = findViewById(R.id.welcomeMessage);

        memberProfile = findViewById(R.id.memberProfile);
        ivImage = findViewById(R.id.ivImage);
        name = findViewById(R.id.name);
        startDate = findViewById(R.id.startDate);
        startDateLabel = findViewById(R.id.startDateLabel);
        endDate = findViewById(R.id.endDate);
        endDateLabel = findViewById(R.id.endDateLabel);
        access = findViewById(R.id.access);
        accessLabel = findViewById(R.id.accessLabel);
        access = findViewById(R.id.access);
        reason = findViewById(R.id.reason);
        scanButton = findViewById(R.id.scanButton);
        mProgressView = findViewById(R.id.home_progress);
        homeLayout = findViewById(R.id.homeLayout);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, ScanActivity.class);
                startActivityForResult(i, HOME_TO_SCAN_REQ_CODE);
            }
        });
        customizeToolbar();
        setFont();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == HOME_TO_SCAN_REQ_CODE && resultCode == Activity.RESULT_OK) {
            checkinUser(data.getStringExtra(ScanActivity.SCAN_CODE));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkinUser(String scanCode) {
        if (!Utils.checkIfInternetConnectedAndToast(this)) {
            return;
        }

        try {
            showProgress(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", scanCode);
            StringEntity entity = new StringEntity(jsonObject.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("Authorization", "Bearer " + PreferenceManager.getAccessToken());
            client.setTimeout(Constant.TIMEOUT);
            client.post(HomeActivity.this, URLs.DO_SCAN, entity, "application/json", new HomeActivity.ScanResponseHandler());
        } catch (Exception e) {
            Utils.exceptionOccurred(this, e);
        }
    }

    public class ScanResponseHandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            showProgress(false);
            ScanResponse response = new Gson().fromJson(new String(responseBody), ScanResponse.class);
            if (response.isResult()) {
                setDetails(response);
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

    private void setDetails(ScanResponse response) {
        Glide.with(this).load(response.getMember().getImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivImage) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivImage.setImageDrawable(circularBitmapDrawable);
            }
        });

        name.setText(response.getMember().getFirstName() + " " + response.getMember().getLastName());
        startDate.setText(response.getStartDate());
        endDate.setText(response.getEndDate());
        if (response.isAllowed()) {
            access.setText(Constant.ALLOWED);
            access.setTextColor(getResources().getColor(R.color.green));
            reason.setVisibility(View.GONE);
        } else {
            access.setText(Constant.NOT_ALLOWED);
            access.setTextColor(getResources().getColor(R.color.red));
            reason.setVisibility(View.VISIBLE);
            reason.setText(response.getReason());
            reason.setTextColor(getResources().getColor(R.color.red));
        }

        userProfile.setVisibility(View.GONE);
        memberProfile.setVisibility(View.VISIBLE);
    }

    private Toolbar customizeToolbar() {
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        tvTitle = findViewById(R.id.home_toolbar_title);
        logoutBtn = findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManager.resetUser();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        return toolbar;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            homeLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            homeLayout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    homeLayout.setVisibility(show ? View.GONE : View.VISIBLE);
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
            homeLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void setFont() {
        tvTitle.setTypeface(Utils.getRegularFont(this));
        userName.setTypeface(Utils.getBold(this));
        userLocation.setTypeface(Utils.getRegularFont(this));
        welcomeMessage.setTypeface(Utils.getRegularFont(this));
        name.setTypeface(Utils.getBold(this));
        startDateLabel.setTypeface(Utils.getBold(this));
        startDate.setTypeface(Utils.getRegularFont(this));
        endDateLabel.setTypeface(Utils.getBold(this));
        endDate.setTypeface(Utils.getRegularFont(this));
        accessLabel.setTypeface(Utils.getBold(this));
        access.setTypeface(Utils.getBold(this));
        endDateLabel.setTypeface(Utils.getBold(this));
        reason.setTypeface(Utils.getBold(this));
    }
}
