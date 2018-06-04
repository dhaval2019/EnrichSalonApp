package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.AuthenticationModel;
import com.enrich.salonapp.data.model.AuthenticationRequestModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.contracts.AuthenticationTokenContract;
import com.enrich.salonapp.ui.presenters.AuthenticationTokenPresenter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;

public class SplashActivity extends BaseActivity implements AuthenticationTokenContract.View {

    AuthenticationTokenPresenter authenticationTokenPresenter;

    DataRepository dataRepository;

    GuestModel guestModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        EnrichUtils.changeStatusBarColor(getWindow());

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this, mainUiThread, threadExecutor, null);

        authenticationTokenPresenter = new AuthenticationTokenPresenter(this, dataRepository);

        guestModel = EnrichUtils.getUserData(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getAuthenticationToken();
            }
        }, 2000);
    }

    private void getAuthenticationToken() {
        if (guestModel != null) {
            AuthenticationRequestModel authenticationRequestModel = new AuthenticationRequestModel();
            authenticationRequestModel.username = EnrichUtils.getUserData(SplashActivity.this).UserName;
            authenticationRequestModel.password = EnrichUtils.getUserData(SplashActivity.this).Password;
            authenticationTokenPresenter.getAuthenticationToken(SplashActivity.this, authenticationRequestModel, false);
        } else {
            Intent intent = new Intent(SplashActivity.this, SliderActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void saveAuthenticationToken(AuthenticationModel model) {
        if (model.accessToken != null) {
            EnrichUtils.saveAuthenticationModel(SplashActivity.this, model);

            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            EnrichUtils.showMessage(this, "Please restart the app.");
        }
    }
}
