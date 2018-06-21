package in.bananaa.pass;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.bananaa.pass.activity.LoginActivity;
import in.bananaa.pass.util.PreferenceManager;
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
    private TextView name;
    private TextView startDate;
    private TextView startDateLabel;
    private TextView endDate;
    private TextView endDateLabel;
    private TextView access;
    private TextView accessLabel;
    private TextView reason;

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

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, ScanActivity.class);
                startActivityForResult(i, HOME_TO_SCAN_REQ_CODE);
            }
        });
        customizeToolbar();
        setFont();
        name.setTextColor(getResources().getColor(R.color.black));
        access.setTextColor(getResources().getColor(R.color.red));
        reason.setTextColor(getResources().getColor(R.color.red));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == HOME_TO_SCAN_REQ_CODE && resultCode == Activity.RESULT_OK) {
            Log.d("NEW", "onActivityResult: " + data.getStringExtra(ScanActivity.SCAN_CODE));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Toolbar customizeToolbar() {
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
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
