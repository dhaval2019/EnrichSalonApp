package com.enrich.salonapp.ui.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.BeautyAndBlingResponseModel;
import com.enrich.salonapp.data.model.CenterDetailModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.data.model.GuestSpinCountResponseModel;
import com.enrich.salonapp.data.model.OfferModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.contracts.BeautyAndBlingContract;
import com.enrich.salonapp.ui.contracts.BeautyAndBlingEligibilityContract;
import com.enrich.salonapp.ui.fragments.HomeFragment;
import com.enrich.salonapp.ui.presenters.BeautyAndBlingEligibilityPresenter;
import com.enrich.salonapp.ui.presenters.BeautyAndBlingPresenter;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.OfferHandler;
import com.enrich.salonapp.util.SharedPreferenceStore;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.supertoast.SuperActivityToast;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.enrich.salonapp.util.Constants.SHOW_SPIN;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, BeautyAndBlingContract.View, BeautyAndBlingEligibilityContract.View {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public final static String BEAUTY_AND_BLING_SPIN = "com.enrich.salonapp.Beauty.And.Bling";

    @BindView(R.id.profile_container)
    RelativeLayout profileContainer;

    @BindView(R.id.wallet_container)
    RelativeLayout walletContainer;

    @BindView(R.id.appointment_container)
    RelativeLayout appointmentContainer;

    @BindView(R.id.contact_us_container)
    RelativeLayout contactUsContainer;

    @BindView(R.id.share_container)
    RelativeLayout shareContainer;

    @BindView(R.id.rate_us_container)
    RelativeLayout rateUsContainer;

    @BindView(R.id.logout_container)
    RelativeLayout logoutContainer;

    @BindView(R.id.settings_container)
    RelativeLayout settingsContainer;

    @BindView(R.id.location_container)
    LinearLayout locationContainer;

    @BindView(R.id.home_center_name)
    TextView homeCenterName;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.my_package_container)
    RelativeLayout myPackageContainer;



    @BindView(R.id.refer_a_friend_container)
    RelativeLayout referAFriendContainer;//by dhaval shah 6/7/19

    CenterDetailModel centerDetailModel;

    GuestModel guestModel;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    double latitude;
    double longitude;

    BottomSheetDialog dialog;

    private int callToAction;
    private OfferModel offerModel;

    private BeautyAndBlingPresenter beautyAndBlingPresenter;
    private BeautyAndBlingEligibilityPresenter beautyAndBlingEligibilityPresenter;

    private boolean isFromOfferClick = false;

    private BroadcastReceiver beautyAndBling = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isFromOfferClick = true;
            beautyAndBlingPresenter.getBeautyAndBling(HomeActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        callToAction = Integer.parseInt(getIntent().getStringExtra("CallToAction") == null ? "-1" : getIntent().getStringExtra("CallToAction"));
        offerModel = getIntent().getParcelableExtra("OfferModelFromNotification");

        if (callToAction != -1 && offerModel != null) {
            OfferHandler.handleOfferRedirection(this, offerModel);
        }

        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10 * 100).setFastestInterval(1 * 100);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView navHeaderUserName = navigationView.findViewById(R.id.nav_header_user_name);

        guestModel = EnrichUtils.getUserData(this);

        if (guestModel != null)
            navHeaderUserName.setText(guestModel.FirstName + " " + guestModel.LastName);

        centerDetailModel = EnrichUtils.getHomeStore(this);

        if (centerDetailModel != null) {
            homeCenterName.setText(centerDetailModel.Name);
        }

        locationContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, StoreSelectorActivity.class);
                startActivity(intent);
            }
        });

        profileContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        myPackageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MyPackageActivity.class);
                startActivity(intent);
            }
        });

        walletContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, WalletActivity.class);
                startActivity(intent);
            }
        });

        appointmentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AppointmentsActivity.class);
                startActivity(intent);
            }
        });

        contactUsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
                startActivity(intent);
            }
        });

        shareContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "It’s easier than ever to book your appointments with us. Intall the Enrich app and get done in a jiffy! https://play.google.com/store/apps/details?id=com.enrich.salonapp");
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

        rateUsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.enrich.salonapp")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.enrich.salonapp")));
                }
            }
        });

        logoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
//                EnrichUtils.logout(HomeActivity.this);
//                SharedPreferenceStore.clearSharedPreferences(HomeActivity.this);
//                Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
            }
        });

        settingsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        referAFriendContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ReferAFriendActivity.class);
                startActivity(intent);
            }
        });
        addFragment(HomeFragment.getInstance(drawer));

        DataRepository dataRepository = Injection.provideDataRepository(this, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        beautyAndBlingPresenter = new BeautyAndBlingPresenter(this, dataRepository);
        beautyAndBlingEligibilityPresenter = new BeautyAndBlingEligibilityPresenter(this, dataRepository);

        beautyAndBlingPresenter.getBeautyAndBling(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(beautyAndBling, new IntentFilter(BEAUTY_AND_BLING_SPIN));
    }

    private void logoutDialog() {
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.logout_dialog);

        dialog.setCancelable(true);

        TextView logoutOk = dialog.findViewById(R.id.logout_ok);
        TextView logoutCancel = dialog.findViewById(R.id.logout_cancel);

        logoutOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EnrichUtils.logout(HomeActivity.this);
                EnrichApplication.clearSpinList();
                SharedPreferenceStore.clearSharedPreferences(HomeActivity.this);

                Crashlytics.setUserIdentifier("");
                logoutContainer.setVisibility(View.GONE);
                drawer.closeDrawers();
                dialog.dismiss();

//                Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
            }
        });

        logoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.home_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (drawer != null) {
            drawer.closeDrawers();
        }

        googleApiClient.connect();

        if (homeCenterName != null) {
            centerDetailModel = EnrichUtils.getHomeStore(this);

            if (centerDetailModel != null) {
                homeCenterName.setText(centerDetailModel.Name);

            }
        }

        if (logoutContainer != null) {
            if (EnrichUtils.getUserData(this) != null) {
                logoutContainer.setVisibility(View.VISIBLE);
            } else {
                logoutContainer.setVisibility(View.GONE);
            }
        }

        SuperActivityToast.cancelAllSuperToasts();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(beautyAndBling);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//        }
//            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } else {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            saveLocation(latitude, longitude);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        EnrichUtils.log("Connection Suspended: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (Exception e) {
                Log.d("GPSLocation failed", e.toString());
                e.printStackTrace();
            }
        } else {
            Log.e("GPSLocation Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        saveLocation(latitude, longitude);
    }

    private void saveLocation(double latitude, double longitude) {
        SharedPreferenceStore.storeValue(this, Constants.CURRENT_LATITUDE, latitude);
        SharedPreferenceStore.storeValue(this, Constants.CURRENT_LONGITUDE, longitude);
    }

    @Override
    public void checkBeautyAndBling(BeautyAndBlingResponseModel model) {
        if (EnrichUtils.getUserData(this) != null) {
            if (!model.BeautyAndBling.isEmpty()) {
                for (int i = 0; i < model.BeautyAndBling.size(); i++) {
                    if (model.BeautyAndBling.get(i).IsActive) {
                        Map<String, String> map = new HashMap<>();
                        map.put("GuestId", EnrichUtils.getUserData(this).Id);
                        beautyAndBlingEligibilityPresenter.getSpinCount(this, map);
                    }
                }
            }
        }
    }

    @Override
    public void showSpinCount(GuestSpinCountResponseModel model) {
        if (model != null) {
            if (model.GuestSpinWheel != null) {
                if (model.GuestSpinWheel.isEmpty()) {
                    if (model.SpinsWithoutInvoices == SHOW_SPIN) {
                        Intent intent = new Intent(HomeActivity.this, BeautyAndBlingLandingActivity.class);
                        intent.putExtra("IsNewUser", true);
                        startActivity(intent);
                    } else {
                        if (model.WonPrice > 0) {
                            try {
                                SimpleDateFormat stringToDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date expiryDate = stringToDateFormat.parse(model.ValidityDate.substring(0, 10));
                                if (new Date().after(expiryDate)) {
                                    Intent intent = new Intent(HomeActivity.this, BeautyAndBlingLandingActivity.class);
                                    intent.putExtra("IsNewUser", true);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(HomeActivity.this, BeautyAndBlingNewUserWinningActivity.class);
                                    intent.putExtra("IsNewUser", true);
                                    intent.putExtra("SpinTaken", true);
                                    intent.putExtra("PointsWon", model.WonPrice);
                                    intent.putExtra("ValidityDateStr", model.ValidityDate.substring(0, 10));
                                    startActivity(intent);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            isFromOfferClick = false;
                        }
                    }
                } else {
                    Intent intent = new Intent(HomeActivity.this, BeautyAndBlingLandingActivity.class);
                    intent.putExtra("IsNewUser", false);
                    startActivity(intent);
                }
            }
        }
    }
}
