package com.enrich.salonapp.util;

import android.app.Activity;
import android.content.Intent;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.OfferModel;
import com.enrich.salonapp.data.remote.RemoteDataSource;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.activities.PackageDetailActivity;
import com.enrich.salonapp.ui.activities.PackagesActivity;
import com.enrich.salonapp.ui.activities.ProductActivity;
import com.enrich.salonapp.ui.activities.ProductDetailActivity;
import com.enrich.salonapp.ui.activities.ServiceListActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;

public class OfferHandler {

    public static void handleOfferRedirection(Activity activity, OfferModel model) {
        switch (model.CallToAction) {
            case Constants.OFFER_COMMAND_PACKAGES_LIST:
                launchPackageListPage(activity);
                break;
            case Constants.OFFER_COMMAND_SERVICE_LIST:
                launchServiceListPage(activity, model);
                break;
            case Constants.OFFER_COMMAND_PARTICULAR_PACKAGES:
                launchParticularPackage(activity, model);
                break;
            case Constants.OFFER_COMMAND_PRODUCTS_LIST:
                launchProductList(activity, model);
                break;
            case Constants.OFFER_COMMAND_PARTICULAR_PRODUCTS:
                launchParticularProduct(activity, model);
                break;
        }
    }

    private static void launchPackageListPage(Activity activity) {
        Intent intent = new Intent(activity, PackagesActivity.class);
        activity.startActivity(intent);
    }

    private static void launchServiceListPage(Activity activity, OfferModel model) {
        Intent intent = new Intent(activity, ServiceListActivity.class);
        activity.startActivity(intent);
    }

    private static void launchParticularPackage(Activity activity, OfferModel model) {
        Intent intent = new Intent(activity, PackageDetailActivity.class);
        intent.putExtra("CreateOrderPackageBundleModel", model.PackageId);
        activity.startActivity(intent);
    }

    private static void launchProductList(Activity activity, OfferModel model) {
        Intent intent = new Intent(activity, ProductActivity.class);
        activity.startActivity(intent);
    }

    private static void launchParticularProduct(Activity activity, OfferModel model) {
        Intent intent = new Intent(activity, ProductDetailActivity.class);
        intent.putExtra("ProductId", model.ProductId);
        activity.startActivity(intent);
    }
}
