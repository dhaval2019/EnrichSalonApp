package com.enrich.salonapp.util.handlers;

import com.enrich.salonapp.util.EnrichUtils;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

/**
 * Created by varunbarve on 21/02/18.
 */

public class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        EnrichUtils.log(result.toString());
    }
}