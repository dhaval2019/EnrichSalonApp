package com.enrich.salonapp.util.supertoast.utils;

import android.content.Context;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

public class AccessibilityUtils {

    /**
     * Try to send an {@link AccessibilityEvent}
     * for a {@link View}.
     *
     * @param view The View that will dispatch the AccessibilityEvent
     * @return true if the AccessibilityEvent was dispatched
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean sendAccessibilityEvent(View view) {
        final AccessibilityManager accessibilityManager = (AccessibilityManager)
                view.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);

        if (!accessibilityManager.isEnabled()) return false;

        final AccessibilityEvent accessibilityEvent = AccessibilityEvent
                .obtain(AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
        accessibilityEvent.setClassName(view.getClass().getName());
        accessibilityEvent.setPackageName(view.getContext().getPackageName());

        view.dispatchPopulateAccessibilityEvent(accessibilityEvent);
        accessibilityManager.sendAccessibilityEvent(accessibilityEvent);

        return true;
    }
}
