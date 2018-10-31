package com.enrich.salonapp.util;

import android.app.Activity;
import android.content.Intent;

import com.enrich.salonapp.data.model.OfferModel;
import com.enrich.salonapp.data.model.Product.ProductRequestModel;
import com.enrich.salonapp.ui.activities.PackageDetailActivity;
import com.enrich.salonapp.ui.activities.PackagesActivity;
import com.enrich.salonapp.ui.activities.ProductActivity;
import com.enrich.salonapp.ui.activities.ProductDetailActivity;
import com.enrich.salonapp.ui.activities.ServiceListActivity;
import com.enrich.salonapp.ui.activities.WebActivity;

import java.util.ArrayList;

import static com.enrich.salonapp.util.Constants.OFFER_COMMAND_WEB;

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
            case Constants.OFFER_COMMAND_PRODUCT_CATEGORY_LIST:
                launchProductList(activity, model);
                break;
            case Constants.OFFER_COMMAND_PARTICULAR_PRODUCT_CATEGORY:
                launchProductList(activity, model);
                break;
            case Constants.OFFER_COMMAND_PRODUCT_SUBCATEGORY_LIST:
                launchProductList(activity, model);
                break;
            case Constants.OFFER_COMMAND_PARTICULAR_PRODUCT_SUBCATEGORY:
                launchProductList(activity, model);
                break;
            case Constants.OFFER_COMMAND_PRODUCT_BRAND_LIST:
                launchProductList(activity, model);
                break;
            case Constants.OFFER_COMMAND_PARTICULAR_PRODUCT_BRAND:
                launchProductList(activity, model);
                break;
            case OFFER_COMMAND_WEB:
                launchWebPage(activity, model.BlogRedirectURL);
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

        ProductRequestModel productRequestModel = new ProductRequestModel();
        if (model.OfferProductBrandTags != null) {
            ArrayList<Integer> tempList = new ArrayList<>();
            for (int i = 0; i < model.OfferProductBrandTags.size(); i++) {
                tempList.add(model.OfferProductBrandTags.get(i).ProductBrandId);
            }
            productRequestModel.BrandIds = tempList;
        }

        if (model.OfferProductCategoryTags != null) {
            ArrayList<Integer> tempList = new ArrayList<>();
            for (int i = 0; i < model.OfferProductCategoryTags.size(); i++) {
                tempList.add(model.OfferProductCategoryTags.get(i).ProductCategoryId);
            }
            productRequestModel.ProductCategoryIds = tempList;
        }

        if (model.OfferProductSubCategoryTags != null) {
            ArrayList<Integer> tempList = new ArrayList<>();
            for (int i = 0; i < model.OfferProductSubCategoryTags.size(); i++) {
                tempList.add(model.OfferProductSubCategoryTags.get(i).ProductSubCategoryId);
            }
            productRequestModel.ProductSubCategoryIds = tempList;
        }

        Intent intent = new Intent(activity, ProductActivity.class);
        intent.putExtra("ProductRequestModel", productRequestModel);
        activity.startActivity(intent);
    }

    private static void launchParticularProduct(Activity activity, OfferModel model) {
        Intent intent = new Intent(activity, ProductDetailActivity.class);
        intent.putExtra("ProductId", model.ProductId);
        activity.startActivity(intent);
    }

    private static void launchWebPage(Activity activity, String url) {
        Intent intent = new Intent(activity, WebActivity.class);
        intent.putExtra("URL", url);
        activity.startActivity(intent);
    }
}
