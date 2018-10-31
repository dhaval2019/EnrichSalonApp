package com.enrich.salonapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.AddressModel;
import com.enrich.salonapp.data.model.AddressResponseModel;
import com.enrich.salonapp.data.model.AuthenticationModel;
import com.enrich.salonapp.data.model.CenterDetailModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.ui.activities.AddressSelectorActivity;
import com.enrich.salonapp.ui.views.BadgeDrawable;
import com.enrich.salonapp.ui.views.LoadingDialogBox;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnrichUtils {

    public static DialogDisplay showDialog = new DialogDisplay();
    public static LoadingDialogBox pDialog;
    public static Runnable cancelDialog = new Runnable() {
        @Override
        public void run() {
            try {
                if (pDialog != null)
                    pDialog.cancel();
                pDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static class DialogDisplay implements Runnable {
        Activity activity;
        String message;

        public DialogDisplay updateActivity(Activity activity, String message) {
            this.activity = activity;
            this.message = message;
            return this;
        }

        @Override
        public void run() {
            try {
                if (pDialog != null) {
                    pDialog.cancel();
                }
                pDialog = new LoadingDialogBox(activity);
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.setCancelable(false);
                pDialog.setMessage(message);
                pDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showProgressDialog(final Activity activity) {
        activity.runOnUiThread(showDialog.updateActivity(activity, null));
    }

    public static void cancelCurrentDialog(Activity activity) {
        if (pDialog != null && activity != null) {
            new Handler().postDelayed(cancelDialog, 0);
        }
    }

    /**
     * Log String data in Error
     *
     * @param logData
     */
    public static void log(String logData) {
        Log.e(Constants.LOG_TAG, logData + "");
    }

    /**
     * Check if the email address entered is Valid or Not.
     *
     * @param email
     * @return
     */
    public static boolean isValidEmailId(String email) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern p = Pattern.compile(emailPattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * Return new Gson object
     *
     * @return Gson Object
     */
    public static Gson newGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new GsonDateSerializer());
        return builder.create();
    }

    /**
     * Show Message in Toast
     *
     * @param context
     * @param message
     */
    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Encode Bitmap to Base64 String
     *
     * @param image Bitmap Image
     * @return Base64 String
     */
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    /**
     * Decode Base64 String to Bitmap
     *
     * @param input String Base64
     * @return Bitmap
     */
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    /**
     * Display dialog for location settings.
     *
     * @param context                Application Context
     * @param REQUEST_CHECK_SETTINGS Request Constant
     */
    public static void displayLocationSettingsRequest(final Context context, final int REQUEST_CHECK_SETTINGS) {
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

    /**
     * Image resizing function
     *
     * @param res       Resources
     * @param resId     Resource Id
     * @param reqWidth  Required Width
     * @param reqHeight Required Height
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * Calculate InSample Size
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 2;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Log user out
     *
     * @param context Application Context
     */
    public static void logout(Context context) {
        SharedPreferenceStore.deleteValue(context, Constants.KEY_USER_DATA);
    }

    /**
     * Get Time in UTC Format
     *
     * @param isUTC      Check if time is already in UTC format
     * @param selectTime Time to convert to UTC format
     * @return
     */
    static public String getUTCFormat(boolean isUTC, Date selectTime) {
        String dateAsString = selectTime.toString();
        try {
            /*DateTimeZone zoneIndia = DateTimeZone.getDefault();;
                DateTime now;

            if (isUTC){
                now = DateTime.now( DateTimeZone.UTC );
            }else {
                now = DateTime.now( zoneIndia );
            }

            System.out.println( "dateTime: " + now.toString() );
            // System.out.println( "dateTimeUtc: " + now1.toString() );*/

            Log.e("selectTime", " " + selectTime.toString());
            Date requiredUTCTime = new Date(selectTime.getTime() - new Date(Constants.IST_EXTRA_MINS * 60 * 1000).getTime());// Remove Extra
            // DateTime now = DateTime.now( DateTimeZone.UTC );
            Log.e("requiredUTCTime", " " + requiredUTCTime.toString());
            DateTime requestedTime;
            if (isUTC) {
                requestedTime = new LocalDateTime(requiredUTCTime.getTime()).toDateTime(DateTimeZone.UTC);

                Log.e("requestedTime in UTC ", " with UTC Time " + requestedTime.toString());
            } else {
                requestedTime = new DateTime(requiredUTCTime.getTime())
                        .withZoneRetainFields(DateTimeZone.UTC)
                        .withZone(DateTimeZone.getDefault());
                Log.e("requestedTime in UTC ", " with Indian Time " + requestedTime.toString());
            }
            dateAsString = requestedTime.toString();
            Log.e("requestedTime", " " + requestedTime.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateAsString;
    }

    public static void saveAuthenticationModel(Context context, AuthenticationModel model) {
        if (model != null) {
            SharedPreferenceStore.storeValue(context, Constants.KEY_AUTHENTICATION, newGson().toJson(model));
        }
    }

    public static AuthenticationModel getAuthenticationModel(Context context) {
        AuthenticationModel model = newGson().fromJson(SharedPreferenceStore.getValue(context, Constants.KEY_AUTHENTICATION, ""), AuthenticationModel.class);
        return model;
    }

    /**
     * Change Status color to transparent
     *
     * @param window
     */
    public static void changeStatusBarColor(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * Save Guest/User Data
     *
     * @param context
     * @param model
     */
    public static void saveUserData(Context context, GuestModel model) {
        if (model != null) {
            SharedPreferenceStore.storeValue(context, Constants.KEY_USER_DATA, newGson().toJson(model));
        }
    }

    /**
     * Get Guest/User Data
     *
     * @param context
     * @return
     */
    public static GuestModel getUserData(Context context) {
        GuestModel model = newGson().fromJson(SharedPreferenceStore.getValue(context, Constants.KEY_USER_DATA, ""), GuestModel.class);
        return model;
    }

    public static boolean doesUserHasAddresses(Context context) {
        return getHomeAddress(context) != null || getWorkAddress(context) != null || getOtherAddress(context) != null;
    }

    public static AddressModel getHomeAddress(Context context) {
        AddressModel model = null;
        if (getUserData(context).GuestAddress == null)
            return model;

        for (int i = 0; i < getUserData(context).GuestAddress.size(); i++) {
            if (getUserData(context).GuestAddress.get(i).AddressType.equals("S")) {
                model = getUserData(context).GuestAddress.get(i);
            }
        }
        return model;
    }

    public static AddressModel getWorkAddress(Context context) {
        AddressModel model = null;
        if (getUserData(context).GuestAddress == null)
            return model;

        for (int i = 0; i < getUserData(context).GuestAddress.size(); i++) {
            if (getUserData(context).GuestAddress.get(i).AddressType.equals("W")) {
                model = getUserData(context).GuestAddress.get(i);
            }
        }
        return model;
    }

    public static AddressModel getOtherAddress(Context context) {
        AddressModel model = null;
        if (getUserData(context).GuestAddress == null)
            return model;

        for (int i = 0; i < getUserData(context).GuestAddress.size(); i++) {
            if (getUserData(context).GuestAddress.get(i).AddressType.equals("O")) {
                model = getUserData(context).GuestAddress.get(i);
            }
        }
        return model;
    }

    public static void saveHomeStore(Context context, String modelStr) {
        if (modelStr != null) {
            SharedPreferenceStore.storeValue(context, Constants.HOME_STORE, modelStr);
        }
    }

    public static CenterDetailModel getHomeStore(Context context) {
        return newGson().fromJson(SharedPreferenceStore.getValue(context, Constants.HOME_STORE, ""), CenterDetailModel.class);
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    public static void removeBadge(Context context, LayerDrawable icon) {
        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.removeBadge(context);
    }
}
