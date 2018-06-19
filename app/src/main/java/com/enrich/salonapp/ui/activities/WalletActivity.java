package com.enrich.salonapp.ui.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.Wallet.WalletCenterModel;
import com.enrich.salonapp.data.model.Wallet.WalletHistoryModel;
import com.enrich.salonapp.data.model.Wallet.WalletModel;
import com.enrich.salonapp.ui.adapters.WalletHistoryAdapter;
import com.enrich.salonapp.ui.contracts.WalletContract;
import com.enrich.salonapp.ui.presenters.WalletPresenter;
import com.enrich.salonapp.util.mvp.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.supercharge.shimmerlayout.ShimmerLayout;

public class WalletActivity extends BaseActivity implements WalletContract.View {

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

    WalletPresenter walletPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        ButterKnife.bind(this);

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
        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.otf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTypeface(tf);

        collapsingToolbarLayout.setTitle("Wallet");

        WalletModel walletModel = new WalletModel();
        walletModel.Amount = 154.56;
        walletModel.WalletValidityDate = "31/12/2018";

        showWallet(walletModel);
        showWalletHistory(new WalletHistoryModel());
    }

    @Override
    public void showWallet(WalletModel model) {
        if (model != null) {
            String amountStr = ""+model.Amount;
            String[] amountSplit = amountStr.split("\\.");

            String rs = amountSplit[0];
            String paisa = amountSplit[1];

            walletAmountRs.setText(rs);
            walletAmountPaisa.setText(paisa);
        }
    }

    @Override
    public void showWalletHistory(WalletHistoryModel model) {
        walletLoader.setVisibility(View.GONE);
        walletRecyclerView.setVisibility(View.VISIBLE);

        WalletCenterModel walletCenterModel = new WalletCenterModel("Enrich - Tandon Mall");

        ArrayList<WalletHistoryModel> list = new ArrayList<>();
        list.add(new WalletHistoryModel(54.56, "07/06/2018", "HairCut by Master Stylist", walletCenterModel));
        list.add(new WalletHistoryModel(54.56, "07/06/2018", "HairCut by Master Stylist", walletCenterModel));
        list.add(new WalletHistoryModel(54.56, "07/06/2018", "HairCut by Master Stylist", walletCenterModel));
        list.add(new WalletHistoryModel(54.56, "07/06/2018", "HairCut by Master Stylist", walletCenterModel));
        list.add(new WalletHistoryModel(54.56, "07/06/2018", "HairCut by Master Stylist", walletCenterModel));
        list.add(new WalletHistoryModel(54.56, "07/06/2018", "HairCut by Master Stylist", walletCenterModel));
        list.add(new WalletHistoryModel(54.56, "07/06/2018", "HairCut by Master Stylist", walletCenterModel));
        list.add(new WalletHistoryModel(54.56, "07/06/2018", "HairCut by Master Stylist", walletCenterModel));
        list.add(new WalletHistoryModel(54.56, "07/06/2018", "HairCut by Master Stylist", walletCenterModel));
        list.add(new WalletHistoryModel(54.56, "07/06/2018", "HairCut by Master Stylist", walletCenterModel));
        list.add(new WalletHistoryModel(54.56, "07/06/2018", "HairCut by Master Stylist", walletCenterModel));
        list.add(new WalletHistoryModel(54.56, "07/06/2018", "HairCut by Master Stylist", walletCenterModel));

        WalletHistoryAdapter adapter = new WalletHistoryAdapter(this, list);
        walletRecyclerView.setAdapter(adapter);
        walletRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}
