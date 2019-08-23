package com.enrich.salonapp.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.AppUpdateModel;
import com.enrich.salonapp.data.model.AppUpdateResponseModel;
import com.enrich.salonapp.data.model.AuthenticationModel;
import com.enrich.salonapp.data.model.AuthenticationRequestModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.data.model.RegisterFCMRequestModel;
import com.enrich.salonapp.data.model.RegisterFCMResponseModel;
import com.enrich.salonapp.data.model.SelectFriendModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.contracts.AppUpdateContract;
import com.enrich.salonapp.ui.contracts.AuthenticationTokenContract;
import com.enrich.salonapp.ui.contracts.GuestsContact;
import com.enrich.salonapp.ui.contracts.RegisterFCMContract;
import com.enrich.salonapp.ui.presenters.AppUpdatePresenter;
import com.enrich.salonapp.ui.presenters.AuthenticationTokenPresenter;
import com.enrich.salonapp.ui.presenters.GuestPresenter;
import com.enrich.salonapp.ui.presenters.RegisterFCMPresenter;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.GpsTracker;
import com.enrich.salonapp.util.NetworkHelper;
import com.enrich.salonapp.util.SharedPreferenceStore;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.supertoast.utils.HelloService;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.facebook.appevents.FacebookUninstallTracker;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.enrich.salonapp.util.EnrichUtils.getUserData;
import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;

public class SplashActivity extends BaseActivity implements AuthenticationTokenContract.View, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, EasyPermissions.PermissionCallbacks, GuestsContact.View, AppUpdateContract.View, RegisterFCMContract.View {

    @BindView(R.id.progress_status)
    TextView progressStatus;
    private NetworkHelper networkHelper = NetworkHelper.getInstance();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final int REQUEST_CHECK_SETTINGS = 7;
    private static final int RC_LOCATION_PERMISSION = 123;
    private static final int RC_PHONE_STATE_PERMISSION = 1223;

    AuthenticationTokenPresenter authenticationTokenPresenter;
    GuestPresenter guestPresenter;
    AppUpdatePresenter appUpdatePresenter;
    RegisterFCMPresenter registerFCMPresenter;

    DataRepository dataRepository;

    GuestModel guestModel;

    private GoogleApiClient googleApiClient;
    double latitude;
    double longitude;

    private GpsTracker gpsTracker;

    EnrichApplication application;
    Tracker mTracker;

    private PlaceDetectionClient mPlaceDetectionClient;

    LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;
    public static ArrayList<SelectFriendModel> albumList = new ArrayList<>();

    //    private int callToAction;
//    private OfferModel offerModel;
    private static final int PERMISSION_REQUEST_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setAutoLogAppEventsEnabled(true);

        ButterKnife.bind(this);

//        callToAction = Integer.parseInt(getIntent().getStringExtra("CallToAction") == null ? "-1" : getIntent().getStringExtra("CallToAction"));
//        offerModel = getIntent().getParcelableExtra("OfferModelFromNotification");

        // SEND ANALYTICS
        checkNetWork();
    }

    public void checkNetWork() {
        if (networkHelper.isNetworkAvailable(this)) {
            application = (EnrichApplication) getApplication();
            mTracker = application.getDefaultTracker();

            mTracker.setScreenName("" + this.getClass().getSimpleName());
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            mTracker.enableAdvertisingIdCollection(true);

            application.getFirebaseInstance().setUserProperty("platform", "Android");

            // Making notification bar transparent
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }

            EnrichUtils.changeStatusBarColor(getWindow());

            dataRepository = Injection.provideDataRepository(this, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
            authenticationTokenPresenter = new AuthenticationTokenPresenter(this, dataRepository);
            guestPresenter = new GuestPresenter(this, dataRepository);
            appUpdatePresenter = new AppUpdatePresenter(this, dataRepository);
            registerFCMPresenter = new RegisterFCMPresenter(this, dataRepository);

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            // Construct a PlaceDetectionClient.
            mPlaceDetectionClient = Places.getPlaceDetectionClient(this);

            googleApiClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
            googleApiClient.connect();
            askForContactPermission();


            updateProgressStatus("", false);

            // Facebook Uninstall Tracker
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(
                    new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String deviceToken = instanceIdResult.getToken();
                            FacebookUninstallTracker.updateDeviceToken(deviceToken);
                        }
                    }
            );
        } else {
            try {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Closing the App")
                        .setMessage("No Internet Connection,check your settings")
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        })
                        .show();
            } catch (Exception e) {

            }
        }
    }

    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {

                startService(new Intent(this, HelloService.class));

                displayLocationSettingsRequest();
            }
        } else {

            startService(new Intent(this, HelloService.class));
            displayLocationSettingsRequest();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {

            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    startService(new Intent(this, HelloService.class));
                    displayLocationSettingsRequest();

                } else {
                    Toast.makeText(this, "No permission for contacts", Toast.LENGTH_LONG).show();
                    displayLocationSettingsRequest();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            default:
                EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
                break;


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void getAppUpdate() {
        try {
            updateProgressStatus("Checking for App Update...", true);

            Map<String, String> map = new HashMap<>();
            map.put("CurrentVersion", "" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            map.put("Platform", "" + Constants.PLATFORM_ANDROID);
            appUpdatePresenter.getAppUpdate(this, map);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getAuthenticationToken() {
        guestModel = getUserData(this);
        if (guestModel != null) {
            if (application != null) {
                AuthenticationModel model = application.getAuthenticationModel();
                if (model != null) {
                    try {
                        SimpleDateFormat stringToDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
                        Date expiryDate = stringToDate.parse(model.expires);
                        Date today = new Date();

                        if (today.after(expiryDate)) {
                            updateProgressStatus("Contacting Server...", true);
                            AuthenticationRequestModel authenticationRequestModel = new AuthenticationRequestModel();
                            authenticationRequestModel.username = getUserData(SplashActivity.this).UserName;
                            authenticationRequestModel.password = getUserData(SplashActivity.this).Password;
                            authenticationTokenPresenter.getAuthenticationToken(SplashActivity.this, authenticationRequestModel, false);
                            return;
                        } else {
                            switchToNextScreen();
//                        getBeautyAndBling();
//                        updateProgressStatus("Getting Your Data...", true);
//                        guestPresenter.getUserData(this, application.getAuthenticationModel().userId, false);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    updateProgressStatus("Contacting Server...", true);
                    AuthenticationRequestModel authenticationRequestModel = new AuthenticationRequestModel();
                    authenticationRequestModel.username = getUserData(SplashActivity.this).UserName;
                    authenticationRequestModel.password = getUserData(SplashActivity.this).Password;
                    authenticationTokenPresenter.getAuthenticationToken(SplashActivity.this, authenticationRequestModel, false);
                }
            }
        } else {
            boolean isTutorialShown = SharedPreferenceStore.getValue(this, Constants.TUTORIAL_SHOWN, false);
            if (!isTutorialShown) {
                Intent intent = new Intent(SplashActivity.this, SliderActivity.class);
                startActivity(intent);
                finish();
            } else {
                if (EnrichUtils.getHomeStore(this) != null) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, StoreSelectorActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    @Override
    public void saveAuthenticationToken(AuthenticationModel model) {
        if (model.accessToken != null) {
//            ((EnrichApplication) getApplicationContext()).setAuthenticationModel(model);
            EnrichUtils.saveAuthenticationModel(this, model);
            guestPresenter.getUserData(this, model.userId, false);
        } else {
            Intent intent = new Intent(SplashActivity.this, SliderActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void createTokenError() {
        // Cant create Authentication Token
        showToastMessage("Please restart the app.");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @AfterPermissionGranted(RC_LOCATION_PERMISSION)
    public void displayLocationSettingsRequest() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(10000 / 2);

//            locationCallback = new LocationCallback() {
//                @Override
//                public void onLocationResult(LocationResult locationResult) {
//                    Location currentLocation = locationResult.getLocations().get(0);
//                    saveLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
//                }
//            };

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
//            builder.setAlwaysShow(true);

            Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        getCurrentPlace();
                    } catch (ApiException e) {
                        switch (e.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException resolvable = (ResolvableApiException) e;
                                    resolvable.startResolutionForResult(SplashActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sendIntentException) {
                                    // IGNORE
                                } catch (ClassCastException classCastException) {
                                    // IGNORE, ALMOST IMPOSSIBLE
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                break;
                        }
                    }
                }
            });
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale), RC_LOCATION_PERMISSION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    int count = 0;

    private void getCurrentPlace() {
//        if (count < 1) {
        updateProgressStatus("Getting Your Location...", true);
//            LocationRequest locationRequest = new LocationRequest();
//            locationRequest.setInterval(2000);
//            locationRequest.setFastestInterval(1000);
//            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//
//            ++count;
//        }

        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(new PlaceFilter());
        placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                try {
                    PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        EnrichUtils.log(String.format("Place '%s' has likelihood: %g", placeLikelihood.getPlace().getName(), placeLikelihood.getLikelihood()));
                        saveLocation(placeLikelihood.getPlace().getLatLng().latitude, placeLikelihood.getPlace().getLatLng().longitude);
                        return;
                    }
                    likelyPlaces.release();
                } catch (Exception e) {
                    count++;
                    final double latitude = SharedPreferenceStore.getValue(SplashActivity.this, Constants.CURRENT_LATITUDE, 0.0);
                    final double longitude = SharedPreferenceStore.getValue(SplashActivity.this, Constants.CURRENT_LONGITUDE, 0.0);

                    if (latitude == 0.0 && longitude == 0.0) {
                        if (count == 1) {
                            updateProgressStatus("Couldn't get your location. We'll try again...", true);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    saveLocation(latitude, longitude);
                                }
                            }, 1500);
                        } else
                            getCurrentPlace();
                    } else {
                        saveLocation(latitude, longitude);
                    }
                    Log.e("ERROR TAG: ", e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHECK_SETTINGS) {
                getCurrentPlace();
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == REQUEST_CHECK_SETTINGS) {
                displayLocationSettingsRequest();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        EnrichUtils.log("GoogleApiClient Connected");
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

    private void saveLocation(double latitude, double longitude) {
        SharedPreferenceStore.storeValue(this, Constants.CURRENT_LATITUDE, latitude);
        SharedPreferenceStore.storeValue(this, Constants.CURRENT_LONGITUDE, longitude);
//        getAppUpdate();
        getAuthenticationToken();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
//        if (requestCode == RC_PHONE_STATE_PERMISSION) {
//            sendAnalyticsData();
//            return;
//        }

        if (requestCode == RC_LOCATION_PERMISSION) {
            displayLocationSettingsRequest();
            return;
        }
    }

    @Override
    public void saveUserDetails(final GuestModel model) {
        /*GuestModel guestModel = EnrichUtils.getUserData(this);
        model.Password = guestModel.Password;//dhaval shah 22/08/2019
*/
        // EnrichUtils.saveUserData(this, guestModel);
        EnrichUtils.saveUserData(this, model);//dhaval shah 22/08/2019
        switchToNextScreen();
    }

    private void switchToNextScreen() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                RegisterFCMRequestModel fcmModel = new RegisterFCMRequestModel();
                fcmModel.GuestId = application.getAuthenticationModel().userId;
                fcmModel.Platform = Constants.PLATFORM_ANDROID;
                fcmModel.Token = instanceIdResult.getToken();

                EnrichUtils.log(EnrichUtils.newGson().toJson(fcmModel));
                registerFCMPresenter.registerFCM(SplashActivity.this, fcmModel);
            }
        });

//        EnrichUtils.saveUserData(this, model);
//        if (callToAction == -1) {
        if (EnrichUtils.getHomeStore(this) != null) {
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, StoreSelectorActivity.class);
            startActivity(intent);
            finish();
        }
//        } else {
//            OfferHandler.handleOfferRedirection(this, offerModel);
//        }
    }

    @Override
    public void dataNotFound() {

    }

//    @SuppressLint("HardwareIds")
//    @AfterPermissionGranted(RC_PHONE_STATE_PERMISSION)
//    private void sendAnalyticsData() {
//        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_PHONE_STATE)) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//            Answers.getInstance().logContentView(new ContentViewEvent()
//                    .putContentName("Install")
//                    .putContentType("Application")
//                    .putContentId("" + telephonyManager.getDeviceId()));
//        } else {
//            EasyPermissions.requestPermissions(this, getString(R.string.phone_state_rationale), RC_PHONE_STATE_PERMISSION, Manifest.permission.READ_PHONE_STATE);
//        }
//    }

    @Override
    public void showAppUpdate(AppUpdateResponseModel model) {
        AppUpdateModel appUpdateModel = model.AppUpdate;

        if (appUpdateModel.ShouldUpdate) {
            if (appUpdateModel.ForceUpdate) {
                showForceUpdateDialog();
            } else {
                showShouldUpdateDialog();
            }
        } else {
            getAuthenticationToken();
        }
    }

    private void showShouldUpdateDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.should_update_dialog);

        dialog.setCancelable(true);

        TextView ok = dialog.findViewById(R.id.should_update_ok);
        TextView cancel = dialog.findViewById(R.id.should_update_cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAuthenticationToken();
                dialog.dismiss();
            }
        });
        if (!(SplashActivity.this.isFinishing())) {
            dialog.show();
        }
    }

    private void showForceUpdateDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.force_update_dialog);

        dialog.setCancelable(true);

        TextView ok = dialog.findViewById(R.id.force_update_ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void updateProgressStatus(String message, boolean isVisible) {
        if (isVisible) {
            progressStatus.setVisibility(View.VISIBLE);
            progressStatus.setText(message);
        } else {
            progressStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public void FCMRegistered(RegisterFCMResponseModel model) {
        if (model.Error != null)
            EnrichUtils.log("FCM REGISTER: " + model.Error.StatusCode);
    }
}
