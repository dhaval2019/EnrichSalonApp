package com.enrich.salonapp.util;

import android.app.Activity;
import android.content.Intent;

import com.enrich.salonapp.data.model.OfferModel;
import com.enrich.salonapp.ui.activities.PackagesActivity;

public class OfferHandler {

    public static void handleOfferRedirection(Activity activity, OfferModel model) {
        switch (model.CallToAction) {
            case Constants.OFFER_COMMAND_PACKAGES:
                launchPackageListPage(activity);
                break;
            case Constants.OFFER_COMMAND_STORE_RATE_CARD:
                break;
        }
    }

    private static void launchPackageListPage(Activity activity) {
        Intent intent = new Intent(activity, PackagesActivity.class);
        activity.startActivity(intent);
    }
}
