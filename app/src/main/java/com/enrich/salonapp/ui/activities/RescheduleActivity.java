package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.CenterDetailModel;
import com.enrich.salonapp.data.model.ServiceViewModel;
import com.enrich.salonapp.data.model.TherapistModel;
import com.enrich.salonapp.ui.adapters.AppointmentAdapter;
import com.enrich.salonapp.ui.adapters.RescheduleServiceListAdapter;
import com.enrich.salonapp.ui.adapters.TherapistListAdapter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RescheduleActivity extends BaseActivity {

    @BindView(R.id.reschedule_services_recycler_view)
    RecyclerView appointmentsRecyclerView;

    @BindView(R.id.no_services_text_view)
    TextView noAppointmentsTextView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.service_cart_container)
    LinearLayout serviceCartContainer;

    @BindView(R.id.service_total_price)
    TextView serviceTotalPrice;

    @BindView(R.id.service_total_items)
    TextView serviceTotalItems;

    @BindView(R.id.reschedule_service_next)
    TextView rescheduleServiceNext;

    DataRepository dataRepository;

    boolean isHomeSelected;

    EnrichApplication application;
    Tracker mTracker;

    RescheduleServiceListAdapter rescheduleServiceListAdapter;

    ArrayList<ServiceViewModel> list;
    private BottomSheetDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule);

        list = EnrichUtils.newGson().fromJson(getIntent().getStringExtra("RescheduleServiceList"), AppointmentAdapter.RescheduleServiceList.class).serviceList;
        isHomeSelected = getIntent().getBooleanExtra("isHomeSelected", false);

        application = (EnrichApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Reschedule Services List Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(" ");
        assert toolbar != null;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        toolbar.setTitle(" ");

        rescheduleServiceListAdapter = new RescheduleServiceListAdapter(this, list);
        appointmentsRecyclerView.setAdapter(rescheduleServiceListAdapter);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rescheduleServiceNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!application.isCartEmpty()) {
                    showMoreServicesDialog();
                }
            }
        });

        updateCart();
    }

    private void showMoreServicesDialog() {
//        View view = inflater.inflate(R.layout.therapist_list_dialog, null);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.add_more_services_dialog);

        dialog.setCancelable(false);

        TextView cancel = dialog.findViewById(R.id.add_more_service_cancel);
        TextView yes = dialog.findViewById(R.id.add_more_service_ok);
        TextView no = dialog.findViewById(R.id.add_more_service_proceed);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RescheduleActivity.this, ServiceListActivity.class);
                intent.putExtra("isHomeSelected", isHomeSelected);
                intent.putExtra("isRebook", true);
                startActivity(intent);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RescheduleActivity.this, DateSelectorActivity.class);
                intent.putExtra("isHomeSelected", isHomeSelected);
                intent.putExtra("isRebook", true);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void updateCart() {
        EnrichApplication application = (EnrichApplication) getApplication();
        if (application.getCartItems().size() == 0) {
            serviceCartContainer.setVisibility(View.GONE);
        } else {
            serviceCartContainer.setVisibility(View.VISIBLE);
            serviceTotalPrice.setText(getResources().getString(R.string.Rs) + " " + (int) application.getTotalPrice());
            serviceTotalItems.setText("" + application.getCartItems().size());
        }
    }
}
