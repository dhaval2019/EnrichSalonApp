package com.enrich.salonapp;

import android.app.Application;

import com.enrich.salonapp.util.handlers.NotificationOpenedHandler;
import com.enrich.salonapp.util.handlers.NotificationReceivedHandler;
import com.onesignal.OneSignal;

public class EnrichApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new NotificationReceivedHandler())
                .setNotificationOpenedHandler(new NotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}
