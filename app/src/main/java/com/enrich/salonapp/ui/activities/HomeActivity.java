package com.enrich.salonapp.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.enrich.salonapp.BuildConfig;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.CenterDetailModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.ui.fragments.HomeFragment;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.SharedPreferenceStore;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final int REQUEST_CHECK_SETTINGS = 7;

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

    CenterDetailModel centerDetailModel;

    GuestModel guestModel;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

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
                EnrichUtils.logout(HomeActivity.this);
                Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        settingsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        addFragment(HomeFragment.getInstance(drawer));
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.home_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            EnrichUtils.displayLocationSettingsRequest(this, REQUEST_CHECK_SETTINGS);
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(HomeActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            EnrichUtils.log("Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            EnrichUtils.log("Requesting permission");
            startLocationPermissionRequest();
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        EnrichUtils.log("onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                EnrichUtils.log("User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                EnrichUtils.displayLocationSettingsRequest(this, REQUEST_CHECK_SETTINGS);
            } else {
                // Permission denied.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == Activity.RESULT_CANCELED) {
            // location not switched on
        } else if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == Activity.RESULT_OK) {
            // location switched on
        }
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
}
