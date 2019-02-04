package com.enrich.salonapp.ui.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.AppointmentModel;
import com.enrich.salonapp.data.model.AppointmentResponseModel;
import com.enrich.salonapp.data.remote.RemoteDataSource;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.AppointmentAdapter;
import com.enrich.salonapp.ui.contracts.AppointmentContracts;
import com.enrich.salonapp.ui.presenters.AppointmentPresenter;
import com.enrich.salonapp.util.EndlessRecyclerOnScrollListener;
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

public class AppointmentsActivity extends BaseActivity implements AppointmentContracts.View {

    @BindView(R.id.drawer_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.appointments_recycler_view)
    RecyclerView appointmentsRecyclerView;

    @BindView(R.id.past_appointment_container)
    RelativeLayout pastAppointmentContainer;

    @BindView(R.id.current_appointment_container)
    RelativeLayout currentAppointmentContainer;

    @BindView(R.id.no_appointments_text_view)
    TextView noAppointmentsTextView;

    @BindView(R.id.current_appointment_label)
    TextView currentAppointmentLabel;

    @BindView(R.id.past_appointment_label)
    TextView pastAppointmentLabel;

    @BindView(R.id.past_appointment_indicator)
    View pastAppointmentIndicator;

    @BindView(R.id.current_appointment_indicator)
    View currentAppointmentIndicator;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    DataRepository dataRepository;
    AppointmentPresenter appointmentPresenter;

    boolean isCurrent;

    EnrichApplication application;
    Tracker mTracker;

    AppointmentAdapter appointmentAdapter;

    private int pageSize = 0;
    private int pageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        // SEND ANALYTICS
        application = (EnrichApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Appointment List Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
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

        collapsingToolbarLayout.setTitle("APPOINTMENT");

        dataRepository = Injection.provideDataRepository(this, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        appointmentPresenter = new AppointmentPresenter(this, dataRepository);

        pastAppointmentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCurrent = false;
                Map<String, String> map = new HashMap<>();
                map.put("Page", "" + pageIndex);
                map.put("Size", "9");
                getAppointments(isCurrent, map);
//                appointmentPresenter.getAppointments(AppointmentsActivity.this, RemoteDataSource.HOST + RemoteDataSource.GET_PAST_APPOINTMENT, map);
                changeButtonState(false);
            }
        });

        currentAppointmentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCurrent = true;
                Map<String, String> map = new HashMap<>();
                map.put("Page", "" + pageIndex);
                map.put("Size", "9");
                getAppointments(isCurrent, map);
//                appointmentPresenter.getAppointments(AppointmentsActivity.this, RemoteDataSource.HOST + RemoteDataSource.GET_UPCOMING_APPOINTMENT, map);
                changeButtonState(true);
            }
        });

        isCurrent = true;
        Map<String, String> map = new HashMap<>();
        map.put("Page", "" + pageIndex);
        map.put("Size", "9");
        getAppointments(isCurrent, map);

        appointmentsRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if (!(pageSize < 8)) {
                    pageIndex++;

                    Map<String, String> map = new HashMap<>();
                    map.put("Page", "" + pageIndex);
                    map.put("Size", "9");
                    getAppointments(isCurrent, map);
                }
            }
        });
    }

    private void getAppointments(boolean isCurrent, Map<String, String> map) {
        if (isCurrent) {
            appointmentPresenter.getAppointments(AppointmentsActivity.this, RemoteDataSource.HOST + RemoteDataSource.GET_UPCOMING_APPOINTMENT, map);
        } else {
            appointmentPresenter.getAppointments(AppointmentsActivity.this, RemoteDataSource.HOST + RemoteDataSource.GET_PAST_APPOINTMENT, map);
        }

        appointmentAdapter = new AppointmentAdapter(this, new ArrayList<AppointmentModel>(), isCurrent);
        appointmentsRecyclerView.setAdapter(appointmentAdapter);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void changeButtonState(boolean isCurrent) {
        if (isCurrent) {
            currentAppointmentIndicator.setBackgroundColor(Color.parseColor("#d69e5c"));
            currentAppointmentLabel.setTextColor(Color.parseColor("#d69e5c"));

            pastAppointmentIndicator.setBackgroundColor(Color.parseColor("#ffffff"));
            pastAppointmentLabel.setTextColor(Color.parseColor("#9E9E9E"));
        } else {
            currentAppointmentIndicator.setBackgroundColor(Color.parseColor("#ffffff"));
            currentAppointmentLabel.setTextColor(Color.parseColor("#9E9E9E"));

            pastAppointmentIndicator.setBackgroundColor(Color.parseColor("#d69e5c"));
            pastAppointmentLabel.setTextColor(Color.parseColor("#d69e5c"));
        }
    }

    @Override
    public void showAppointments(AppointmentResponseModel model) {
        if (model.Appointments.size() != 0) {
            appointmentsRecyclerView.setVisibility(View.VISIBLE);
            noAppointmentsTextView.setVisibility(View.GONE);

            appointmentAdapter.updateList(model.Appointments);
        } else {
            appointmentsRecyclerView.setVisibility(View.GONE);
            noAppointmentsTextView.setVisibility(View.VISIBLE);
        }
    }
}
