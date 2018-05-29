package com.enrich.salonapp.util.handlers;

import com.enrich.salonapp.util.EnrichUtils;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

/**
 * Created by varunbarve on 21/02/18.
 */

public class NotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
    @Override
    public void notificationReceived(OSNotification notification) {
        EnrichUtils.log(notification.toString());
    }
}
