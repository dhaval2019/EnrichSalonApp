package com.enrich.salonapp.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.GuestInvoiceAndSpinCountModel;
import com.enrich.salonapp.data.model.GuestSpinCountResponseModel;
import com.enrich.salonapp.data.model.SpinModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.SpinRecyclerViewAdapter;
import com.enrich.salonapp.ui.contracts.BeautyAndBlingEligibilityContract;
import com.enrich.salonapp.ui.presenters.BeautyAndBlingEligibilityPresenter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class BeautyAndBlingEligibilityActivity extends BaseActivity implements BeautyAndBlingEligibilityContract.View {

    @BindView(R.id.spin_number_recycler_view)
    RecyclerView spinNumberRecyclerView;

    @BindView(R.id.invoice_amount)
    TextView invoiceAmount;

    @BindView(R.id.number_of_spins)
    TextView numberOfSpins;

    @BindView(R.id.user_name)
    TextView userName;

    @BindView(R.id.copyright_label)
    TextView copyrightLabel;

    @BindView(R.id.kpmg_label)
    TextView kpmgLabel;

    boolean isNewUser;

    private DataRepository dataRepository;

    private BeautyAndBlingEligibilityPresenter beautyAndBlingEligibilityPresenter;

    SpinRecyclerViewAdapter spinRecyclerViewAdapter;
    Tracker mTracker;
    EnrichApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_and_bling_spins);

        ButterKnife.bind(this);
        application = (EnrichApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Beauty and Bling eligibility screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);
        isNewUser = getIntent().getBooleanExtra("IsNewUser", false);

        dataRepository = Injection.provideDataRepository(this, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        beautyAndBlingEligibilityPresenter = new BeautyAndBlingEligibilityPresenter(this, dataRepository);

        Map<String, String> map = new HashMap<>();
        map.put("GuestId", EnrichUtils.getUserData(this).Id);
        beautyAndBlingEligibilityPresenter.getSpinCount(this, map);

        copyrightLabel.setText(Html.fromHtml("Copyright &#x24B8; 2018 Enrich. All rights reserved."));
        kpmgLabel.setText(Html.fromHtml("KPMG&#x00AE; Process Advisors and Evaluators for this contest"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (spinRecyclerViewAdapter != null) {
            spinRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showSpinCount(GuestSpinCountResponseModel model) {
//        if (!model.GuestSpinWheel.isEmpty()) {
        userName.setText(model.UserName);

//            GuestInvoiceAndSpinCountModel guestInvoiceAndSpinCountModel = model.GuestSpinWheel.get(0);
        GuestInvoiceAndSpinCountModel guestInvoiceAndSpinCountModel = new GuestInvoiceAndSpinCountModel();
        guestInvoiceAndSpinCountModel.CenterId = "4d9b754d-99bc-4ffe-9e92-bbca5c4c7144";
        guestInvoiceAndSpinCountModel.PurchaseType = "Service";
        guestInvoiceAndSpinCountModel.InvoiceId = "00000000-0000-0000-0000-000000000000";
        guestInvoiceAndSpinCountModel.NoOfSpin = 15;
        guestInvoiceAndSpinCountModel.RemainingSpins = 14;

        ArrayList<SpinModel> list = new ArrayList<>();
        for (int i = 0; i < guestInvoiceAndSpinCountModel.RemainingSpins; i++) {
            list.add(new SpinModel((i + 1), false));
        }


        EnrichApplication.setSpinList(list);

        spinRecyclerViewAdapter = new SpinRecyclerViewAdapter(this, EnrichApplication.getSpinList(), isNewUser, guestInvoiceAndSpinCountModel.CenterId, guestInvoiceAndSpinCountModel.PurchaseType, guestInvoiceAndSpinCountModel.InvoiceId);
        spinNumberRecyclerView.setAdapter(spinRecyclerViewAdapter);
        spinNumberRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        invoiceAmount.setText(String.format("%s %d", getResources().getString(R.string.Rs), guestInvoiceAndSpinCountModel.InvoiceAmount));
        numberOfSpins.setText(String.format("%d", guestInvoiceAndSpinCountModel.RemainingSpins));
//        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
