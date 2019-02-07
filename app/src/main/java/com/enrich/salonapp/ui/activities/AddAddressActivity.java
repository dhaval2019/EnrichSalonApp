package com.enrich.salonapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.AddressModel;
import com.enrich.salonapp.data.model.AddressResponseModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.contracts.AddressContract;
import com.enrich.salonapp.ui.presenters.AddressPresenter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAddressActivity extends BaseActivity implements AddressContract.View {

    public static final int ADD_ADDRESS = 1840;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1484;
    public static final String ADDRESS_HOME = "S";
    public static final String ADDRESS_WORK = "W";
    public static final String ADDRESS_OTHER = "O";

    @BindView(R.id.drawer_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.location_container)
    LinearLayout locationContainer;

    @BindView(R.id.location_text)
    TextView locationText;

    @BindView(R.id.house_text)
    TextView houseText;

    @BindView(R.id.landmark_text)
    TextView landmarkText;

    @BindView(R.id.home_location_container)
    LinearLayout homeLocationContainer;

    @BindView(R.id.work_location_container)
    LinearLayout workLocationContainer;

    @BindView(R.id.other_location_container)
    LinearLayout otherLocationContainer;

    @BindView(R.id.home_image)
    ImageView homeImage;

    @BindView(R.id.work_image)
    ImageView workImage;

    @BindView(R.id.other_image)
    ImageView otherImage;

    @BindView(R.id.home_text)
    TextView homeText;

    @BindView(R.id.work_text)
    TextView workText;

    @BindView(R.id.other_text)
    TextView otherText;

    @BindView(R.id.save_address)
    Button saveAddress;

//    PlaceDetectionClient mPlaceDetectionClient;

    Place suggestedPlace;

    String addressType = "";
    String addressTypeStr = "";

    private ThreadExecutor threadExecutor;
    private MainUiThread mainUiThread;
    private DatabaseDefinition databaseDefinition;
    private DataRepository dataRepository;

    private AddressPresenter addressPresenter;

    private AddressModel addressModel;

    EnrichApplication application;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        application = (EnrichApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Add Address Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);

        addressType = getIntent().getStringExtra("AddressType") == null ? "" : getIntent().getStringExtra("AddressType");
        addressModel = getIntent().getParcelableExtra("Address");

        ButterKnife.bind(this);

        dataRepository = Injection.provideDataRepository(this, mainUiThread, threadExecutor, databaseDefinition);
        addressPresenter = new AddressPresenter(this, dataRepository);

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

        collapsingToolbarLayout.setTitle("ADD ADDRESS");

        locationContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setCountry("IN")
                            .build();

                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setFilter(typeFilter)
                                    .build(AddAddressActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    EnrichUtils.log(e.getLocalizedMessage());
                }

            }
        });

        if (addressModel != null) {
            locationText.setText(addressModel.Location);
            houseText.setText(addressModel.HouseNameFlatNo);
            landmarkText.setText(addressModel.Landmark);
            changeAddressTypeSelection(addressModel.AddressType);

            suggestedPlace = new Place() {
                @Override
                public String getId() {
                    return null;
                }

                @Override
                public List<Integer> getPlaceTypes() {
                    return null;
                }

                @Nullable
                @Override
                public CharSequence getAddress() {
                    return null;
                }

                @Override
                public Locale getLocale() {
                    return null;
                }

                @Override
                public CharSequence getName() {
                    return addressModel.Location;
                }

                @Override
                public LatLng getLatLng() {
                    return new LatLng(addressModel.Latitude, addressModel.Longitude);
                }

                @Nullable
                @Override
                public LatLngBounds getViewport() {
                    return null;
                }

                @Nullable
                @Override
                public Uri getWebsiteUri() {
                    return null;
                }

                @Nullable
                @Override
                public CharSequence getPhoneNumber() {
                    return null;
                }

                @Override
                public float getRating() {
                    return 0;
                }

                @Override
                public int getPriceLevel() {
                    return 0;
                }

                @Nullable
                @Override
                public CharSequence getAttributions() {
                    return null;
                }

                @Override
                public Place freeze() {
                    return null;
                }

                @Override
                public boolean isDataValid() {
                    return false;
                }
            };
        } else {
            locationText.setHint("Enter the closes landmark");

            changeAddressTypeSelection(addressType);
        }

        homeLocationContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAddressTypeSelection(ADDRESS_HOME);
            }
        });

        workLocationContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAddressTypeSelection(ADDRESS_WORK);
            }
        });

        otherLocationContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAddressTypeSelection(ADDRESS_OTHER);
            }
        });

        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locationStr = locationText.getText().toString();
                String houseStr = houseText.getText().toString();
                String landmarkStr = landmarkText.getText().toString();

                if (locationStr.isEmpty()) {
                    showToastMessage("Please fill all the fields");
                    return;
                }

                if (houseStr.isEmpty()) {
                    showToastMessage("Please fill all the fields");
                    return;
                }

                if (landmarkStr.isEmpty()) {
                    showToastMessage("Please fill all the fields");
                    return;
                }

                if (addressTypeStr.isEmpty()) {
                    showToastMessage("Please fill all the fields");
                    return;
                }

                if (suggestedPlace == null) {
                    showToastMessage("Please fill all the fields");
                    return;
                }

                AddressModel model = new AddressModel();
                model.GuestId = EnrichUtils.getUserData(AddAddressActivity.this).Id;
                model.Location = locationStr;
                model.HouseNameFlatNo = houseStr;
                model.Landmark = landmarkStr;
                model.Latitude = suggestedPlace.getLatLng().latitude;
                model.Longitude = suggestedPlace.getLatLng().longitude;
                model.AddressType = addressTypeStr;

                addressPresenter.addAddress(AddAddressActivity.this, model);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                suggestedPlace = PlaceAutocomplete.getPlace(this, data);
                locationText.setText(suggestedPlace.getName() + ", " + suggestedPlace.getAddress());

//                EnrichUtils.log("Place: " + suggestedPlace.getAddress());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                EnrichUtils.log(status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void changeAddressTypeSelection(String type) {
        switch (type) {
            case ADDRESS_HOME:
                homeImage.setColorFilter(ContextCompat.getColor(this, R.color.dark_gold), android.graphics.PorterDuff.Mode.SRC_IN);
                workImage.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
                otherImage.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);

                homeText.setTextColor(Color.parseColor("#d69e5c"));
                workText.setTextColor(Color.parseColor("#9E9E9E"));
                otherText.setTextColor(Color.parseColor("#9E9E9E"));

                addressTypeStr = "S";
                break;
            case ADDRESS_WORK:
                homeImage.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
                workImage.setColorFilter(ContextCompat.getColor(this, R.color.dark_gold), android.graphics.PorterDuff.Mode.SRC_IN);
                otherImage.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);

                homeText.setTextColor(Color.parseColor("#9E9E9E"));
                workText.setTextColor(Color.parseColor("#d69e5c"));
                otherText.setTextColor(Color.parseColor("#9E9E9E"));

                addressTypeStr = "W";
                break;
            case ADDRESS_OTHER:
                homeImage.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
                workImage.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
                otherImage.setColorFilter(ContextCompat.getColor(this, R.color.dark_gold), android.graphics.PorterDuff.Mode.SRC_IN);

                homeText.setTextColor(Color.parseColor("#9E9E9E"));
                workText.setTextColor(Color.parseColor("#9E9E9E"));
                otherText.setTextColor(Color.parseColor("#d69e5c"));

                addressTypeStr = "O";
                break;
            default:
                homeImage.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
                workImage.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
                otherImage.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);

                homeText.setTextColor(Color.parseColor("#9E9E9E"));
                workText.setTextColor(Color.parseColor("#9E9E9E"));
                otherText.setTextColor(Color.parseColor("#9E9E9E"));
                break;
        }
    }

    @Override
    public void addressAdded(AddressResponseModel model) {
        if (model != null) {
            GuestModel guestModel = EnrichUtils.getUserData(this);
            guestModel.GuestAddress = model.GuestAddress;
            EnrichUtils.saveUserData(this, guestModel);

            Intent returnIntent = new Intent();
            returnIntent.putExtra("GuestData", guestModel);
            returnIntent.putExtra("AddressType", addressType);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
