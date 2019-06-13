package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.Wallet.WalletCenterModel;
import com.enrich.salonapp.data.model.Wallet.WalletHistoryModel;
import com.enrich.salonapp.data.model.Wallet.WalletModel;
import com.enrich.salonapp.data.model.Wallet.WalletResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.WalletAdapter;
import com.enrich.salonapp.ui.adapters.WalletHistoryAdapter;
import com.enrich.salonapp.ui.contracts.WalletContract;
import com.enrich.salonapp.ui.fragments.LoginBottomSheetFragment;
import com.enrich.salonapp.ui.presenters.WalletPresenter;
import com.enrich.salonapp.util.DividerItemDecoration;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.LoginListener;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.supercharge.shimmerlayout.ShimmerLayout;

public class WalletActivity extends BaseActivity implements WalletContract.View, LoginListener {

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.wallet_recycler_view)
    RecyclerView walletRecyclerView;

    @BindView(R.id.wallet_loader)
    ShimmerLayout walletLoader;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.wallet_amount_rs)
    TextView walletAmountRs;

    @BindView(R.id.wallet_amount_paisa)
    TextView walletAmountPaisa;

    @BindView(R.id.no_cashback_available)
    TextView noCashbackAvailable;

    @BindView(R.id.wallet_recycler_view_container)
    LinearLayout walletRecyclerViewContainer;

    @BindView(R.id.sign_in_container)
    LinearLayout signInContainer;

    @BindView(R.id.wallet_container)
    LinearLayout walletContainer;

    @BindView(R.id.wallet_login_button)
    Button walletLoginButton;

    DataRepository dataRepository;
    WalletPresenter walletPresenter;

    EnrichApplication application;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        ButterKnife.bind(this);

        // SEND ANALYTICS
        application = (EnrichApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Wallet Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        assert toolbar != null;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitleTextColor(Color.parseColor("#000000"));

        assert collapsingToolbarLayout != null;
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);
        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTypeface(tf);

        collapsingToolbarLayout.setTitle("WALLET");

        dataRepository = Injection.provideDataRepository(this, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        walletPresenter = new WalletPresenter(this, dataRepository);

        if (EnrichUtils.getUserData(WalletActivity.this) != null) {
            walletContainer.setVisibility(View.VISIBLE);
            signInContainer.setVisibility(View.GONE);

            Map<String, String> map = new HashMap<>();
            map.put("GuestId", EnrichUtils.getUserData(this).Id);

            walletPresenter.getWallet(this, map);
        } else {
            walletContainer.setVisibility(View.GONE);
            signInContainer.setVisibility(View.VISIBLE);

            walletLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginBottomSheetFragment.getInstance(WalletActivity.this).show(getSupportFragmentManager(), "login_bottomsheet_fragment");
                }
            });
        }
    }

    @Override
    public void showWallet(WalletResponseModel model) {
        if (!model.Wallets.isEmpty()) {
            walletLoader.setVisibility(View.GONE);
            walletRecyclerViewContainer.setVisibility(View.VISIBLE);
            noCashbackAvailable.setVisibility(View.GONE);
            Collections.sort( model.Wallets,Collections.reverseOrder());
            WalletAdapter adapter = new WalletAdapter(this, model.Wallets);
            walletRecyclerView.setAdapter(adapter);
            walletRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            walletRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

            double amount = 0;

            for (int i = 0; i < model.Wallets.size(); i++) {
                amount += model.Wallets.get(i).Amount;
            }

            String amountStr = "" + amount;

            String[] amountStrSplit = amountStr.split("\\.");

            String rs = amountStrSplit[0];
            String paise = amountStrSplit[1];

            walletAmountRs.setText(rs);
            walletAmountPaisa.setText("." + paise);
        } else {
            walletLoader.setVisibility(View.GONE);
            walletRecyclerViewContainer.setVisibility(View.GONE);
            noCashbackAvailable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wallet_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.wallet_menu) {
            if (EnrichUtils.getUserData(WalletActivity.this) != null) {
                Intent intent = new Intent(WalletActivity.this, WalletHistoryActivity.class);
                startActivity(intent);
            } else {
                showToastMessage("Please login to see your wallet history.");
            }
        }
        return false;
    }

    @Override
    public void onLoginSuccessful() {
        if (EnrichUtils.getUserData(this) != null) {
            walletContainer.setVisibility(View.VISIBLE);
            signInContainer.setVisibility(View.GONE);

            Map<String, String> map = new HashMap<>();
            map.put("GuestId", EnrichUtils.getUserData(this).Id);

            walletPresenter.getWallet(this, map);
        }
    }
}
