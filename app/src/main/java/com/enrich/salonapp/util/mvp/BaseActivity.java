package com.enrich.salonapp.util.mvp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.enrich.salonapp.util.EnrichUtils;

public class BaseActivity extends AppCompatActivity implements IBaseView {

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setProgressBar(boolean show) {
        if (show) {
            EnrichUtils.showProgressDialog(this);
        } else {
            EnrichUtils.cancelCurrentDialog(this);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
