package com.enrich.salonapp.util.mvp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.enrich.salonapp.R;

import butterknife.BindView;

public class BaseActivity extends AppCompatActivity implements IBaseView {

//    @BindView(R.id.progressBar)
//    protected ProgressBar progressBar;

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setProgressBar(boolean show) {
//        if (show) {
//            progressBar.setVisibility(View.VISIBLE);
//        } else {
//            progressBar.setVisibility(View.GONE);
//        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
