package com.enrich.salonapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.AddressModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.util.EnrichUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.enrich.salonapp.ui.activities.AddAddressActivity.ADD_ADDRESS;

public class AddressSelectorActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.address_select_home)
    TextView addressSelectHome;

    @BindView(R.id.address_select_work)
    TextView addressSelectWork;

    @BindView(R.id.address_select_other)
    TextView addressSelectOther;

    @BindView(R.id.add_home_address)
    TextView addHomeAddress;

    @BindView(R.id.add_work_address)
    TextView addWorkAddress;

    @BindView(R.id.add_other_address)
    TextView addOtherAddress;

    @BindView(R.id.address_home_radio)
    CheckBox addressHomeRadio;

    @BindView(R.id.address_work_radio)
    CheckBox addressWorkRadio;

    @BindView(R.id.address_other_radio)
    CheckBox addressOtherRadio;

    @BindView(R.id.address_selected_proceed)
    Button addressSelectedProceed;

    EnrichApplication application;
    Tracker mTracker;

    AddressModel selectedAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_selector);

        // SEND ANALYTICS
        application = (EnrichApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Offers List Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

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

        collapsingToolbarLayout.setTitle("PICK ONE");

        setAddress();

        addHomeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AddressSelectorActivity.this, AddAddressActivity.class);
                intent.putExtra("AddressType", AddAddressActivity.ADDRESS_HOME);
                intent.putExtra("Address", EnrichUtils.getHomeAddress(AddressSelectorActivity.this));
                startActivityForResult(intent, ADD_ADDRESS);
            }
        });

        addWorkAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressSelectorActivity.this, AddAddressActivity.class);
                intent.putExtra("AddressType", AddAddressActivity.ADDRESS_WORK);
                intent.putExtra("Address", EnrichUtils.getWorkAddress(AddressSelectorActivity.this));
                startActivityForResult(intent, ADD_ADDRESS);
            }
        });

        addOtherAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressSelectorActivity.this, AddAddressActivity.class);
                intent.putExtra("AddressType", AddAddressActivity.ADDRESS_OTHER);
                intent.putExtra("Address", EnrichUtils.getOtherAddress(AddressSelectorActivity.this));
                startActivityForResult(intent, ADD_ADDRESS);
            }
        });

        addressHomeRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedAddress(EnrichUtils.getHomeAddress(AddressSelectorActivity.this));
            }
        });

        addressWorkRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedAddress(EnrichUtils.getWorkAddress(AddressSelectorActivity.this));
            }
        });

        addressOtherRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedAddress(EnrichUtils.getOtherAddress(AddressSelectorActivity.this));
            }
        });

        addressSelectedProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedAddress != null) {
                    Intent intent = new Intent(AddressSelectorActivity.this, BookingSummaryActivity.class);
                    intent.putExtra("SelectedAddress", selectedAddress);
                    startActivity(intent);
                }
            }
        });
    }

    private void setAddress() {
        // CHECK ADDRESS
        if (EnrichUtils.getHomeAddress(this) == null) {
            addressHomeRadio.setEnabled(false);
        } else {
            addressHomeRadio.setEnabled(true);
        }

        if (EnrichUtils.getWorkAddress(this) == null) {
            addressWorkRadio.setEnabled(false);
        } else {
            addressWorkRadio.setEnabled(true);
        }

        if (EnrichUtils.getOtherAddress(this) == null) {
            addressOtherRadio.setEnabled(false);
        } else {
            addressOtherRadio.setEnabled(true);
        }

        ArrayList<AddressModel> list = EnrichUtils.getUserData(this).GuestAddress;
        for (int i = 0; i < list.size(); i++) {
            switch (list.get(i).AddressType) {
                case AddAddressActivity.ADDRESS_HOME:
                    if (list.get(i).Location.isEmpty()) {
                        addHomeAddress.setVisibility(View.VISIBLE);
                        addressSelectHome.setVisibility(View.GONE);
                    } else {
                        addHomeAddress.setVisibility(View.GONE);
                        addressSelectHome.setVisibility(View.VISIBLE);
                        addressSelectHome.setText(list.get(i).Location);
                    }
                    break;
                case AddAddressActivity.ADDRESS_WORK:
                    if (list.get(i).Location.isEmpty()) {
                        addWorkAddress.setVisibility(View.VISIBLE);
                        addressSelectWork.setVisibility(View.GONE);
                    } else {
                        addWorkAddress.setVisibility(View.GONE);
                        addressSelectWork.setVisibility(View.VISIBLE);
                        addressSelectWork.setText(list.get(i).Location);
                    }
                    break;
                case AddAddressActivity.ADDRESS_OTHER:
                    if (list.get(i).Location.isEmpty()) {
                        addOtherAddress.setVisibility(View.VISIBLE);
                        addressSelectOther.setVisibility(View.GONE);
                    } else {
                        addOtherAddress.setVisibility(View.GONE);
                        addressSelectOther.setVisibility(View.VISIBLE);
                        addressSelectOther.setText(list.get(i).Location);
                    }
                    break;
            }
        }
    }

    private void setSelectedAddress(AddressModel model) {
        switch (model.AddressType) {
            case AddAddressActivity.ADDRESS_HOME:
                addressHomeRadio.setChecked(true);
                addressWorkRadio.setChecked(false);
                addressOtherRadio.setChecked(false);

                selectedAddress = model;
                break;
            case AddAddressActivity.ADDRESS_WORK:
                addressHomeRadio.setChecked(false);
                addressWorkRadio.setChecked(true);
                addressOtherRadio.setChecked(false);

                selectedAddress = model;
                break;
            case AddAddressActivity.ADDRESS_OTHER:
                addressHomeRadio.setChecked(false);
                addressWorkRadio.setChecked(false);
                addressOtherRadio.setChecked(true);

                selectedAddress = model;
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ADDRESS) {
            if (resultCode == Activity.RESULT_OK) {
                setAddress();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
