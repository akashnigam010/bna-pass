package in.bananaa.pass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import in.bananaa.pass.util.Utils;
import info.androidhive.barcode.BarcodeReader;

public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    public static final String SCAN_CODE = "scanCode";
    private AppCompatActivity mContext;
    private BarcodeReader barcodeReader;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
        customizeToolbar();
        setFont();
    }

    private Toolbar customizeToolbar() {
        Toolbar toolbar = findViewById(R.id.scan_toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        tvTitle = findViewById(R.id.scan_toolbar_title);
        return toolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScanned(Barcode barcode) {
        // playing barcode reader beep sound
        barcodeReader.playBeep();

        Intent intent = getIntent();
        intent.putExtra(SCAN_CODE, barcode.displayValue);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Utils.genericErrorToast(mContext, "Error occurred while scanning : " + errorMessage);
    }

    private void setFont() {
        tvTitle.setTypeface(Utils.getRegularFont(this));
    }
}
