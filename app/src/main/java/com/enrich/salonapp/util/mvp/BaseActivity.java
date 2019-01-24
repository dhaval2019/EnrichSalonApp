package com.enrich.salonapp.util.mvp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.enrich.salonapp.ui.activities.HomeActivity;
import com.enrich.salonapp.util.EnrichUtils;

import es.dmoral.toasty.Toasty;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (BaseActivity.this instanceof HomeActivity)
                    Toasty.success(BaseActivity.this, "Success !", Toasty.LENGTH_LONG, true).show();
            }
        }, 3000);
    }
}
