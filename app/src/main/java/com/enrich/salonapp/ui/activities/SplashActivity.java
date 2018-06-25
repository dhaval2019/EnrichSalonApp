package com.enrich.salonapp.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.enrich.salonapp.BuildConfig;
import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.AuthenticationModel;
import com.enrich.salonapp.data.model.AuthenticationRequestModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.contracts.AuthenticationTokenContract;
import com.enrich.salonapp.ui.presenters.AuthenticationTokenPresenter;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.SharedPreferenceStore;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class SplashActivity extends BaseActivity implements AuthenticationTokenContract.View, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final int REQUEST_CHECK_SETTINGS = 7;

    AuthenticationTokenPresenter authenticationTokenPresenter;

    DataRepository dataRepository;

    GuestModel guestModel;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10 * 100).setFastestInterval(1 * 100);

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

    }

    private void switchToNextScreen() {
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
            ((EnrichApplication) getApplicationContext()).setAuthenticationModel(model);

            if (EnrichUtils.getHomeStore(this) != null) {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, StoreSelectorActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(SplashActivity.this, SliderActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            displayLocationSettingsRequest(this, REQUEST_CHECK_SETTINGS);
        }
    }

    public void displayLocationSettingsRequest(final Context context, final int REQUEST_CHECK_SETTINGS) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        EnrichUtils.log("All location settings are satisfied.");
                        switchToNextScreen();
//                        getLastLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        EnrichUtils.log("Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            EnrichUtils.log("PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        EnrichUtils.log("Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(SplashActivity.this,
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
                displayLocationSettingsRequest(this, REQUEST_CHECK_SETTINGS);
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
            switchToNextScreen();
            // location not switched on
        } else if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == Activity.RESULT_OK) {
            // location switched on
        }
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
