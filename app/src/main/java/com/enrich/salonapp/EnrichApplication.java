package com.enrich.salonapp;

import android.app.Application;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.enrich.salonapp.data.model.AuthenticationModel;
import com.enrich.salonapp.data.model.CartItem;
import com.enrich.salonapp.data.model.GenericCartModel;
import com.enrich.salonapp.data.model.SpinModel;
import com.enrich.salonapp.ui.activities.BookingSummaryActivity;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.ObjectSerializer;
import com.enrich.salonapp.util.SharedPreferenceStore;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
//import com.onesignal.OneSignal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import io.fabric.sdk.android.Fabric;

public class EnrichApplication extends Application {

    ArrayList<GenericCartModel> cartList;

    int maxQuantityAllowed = 9;
    int maxItemsAllowed = 10;

    AuthenticationModel authenticationModel = null;

    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    static ArrayList<SpinModel> spinList;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        cartList = getStoredCartItem();
//        OneSignal.startInit(this)
//                .setNotificationReceivedHandler(new NotificationReceivedHandler())
//                .setNotificationOpenedHandler(new NotificationOpenedHandler())
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .unsubscribeWhenNotificationsAreDisabled(true)
//                .init();
        sAnalytics = GoogleAnalytics.getInstance(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers());
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }
        return sTracker;
    }

    public FirebaseAnalytics getFirebaseInstance() {
        if (mFirebaseAnalytics != null)
            return mFirebaseAnalytics;
        else
            return FirebaseAnalytics.getInstance(this);
    }

    public static ArrayList<SpinModel> getSpinList() {
        return spinList;
    }

    public static void setSpinList(ArrayList<SpinModel> spinListTemp) {
        spinList = spinListTemp;
    }

    public static void clearSpinList() {
        spinList = null;
    }

    public AuthenticationModel getAuthenticationModel() {
        return EnrichUtils.getAuthenticationModel(getApplicationContext());
    }

    public void setAuthenticationModel(AuthenticationModel authenticationModel) {
        this.authenticationModel = authenticationModel;
    }

    public void addToCart(CartItem cartItem) {
        if (isCartFull())
            return;

        if (cartItemTypeAlreadyExists(cartItem)) {
            if (alreadyExist(cartItem)) {
                updateCartItem(cartItem);
                return;
            }
        }

        if (cartItem.getCartItemType() == CartItem.CART_TYPE_SERVICES)
            if (cartHasLooks()) {
                EnrichUtils.showMessage(getApplicationContext(), getApplicationContext().getString(R.string.services_looks_error));
                return;
            }

        if (cartItem.getCartItemType() == CartItem.CART_TYPE_LOOKS)
            if (cartHasServices()) {
                EnrichUtils.showMessage(getApplicationContext(), getApplicationContext().getString(R.string.services_looks_error));
                return;
            } else {
                if (cartHasLooks()) {
                    EnrichUtils.showMessage(getApplicationContext(), getApplicationContext().getString(R.string.more_than_one_looks_error));
                    return;
                }
            }

        if (isCartEmpty()) {
            cartItem.setQuantity(1);
            cartItem.added();

            GenericCartModel genericCartModel = new GenericCartModel();
            genericCartModel.Id = cartItem.getId();
            genericCartModel.ServiceId = cartItem.getServiceId();
            genericCartModel.Quantity = cartItem.getQuantity();
            genericCartModel.Name = cartItem.getName();
            genericCartModel.Price = cartItem.getPrice();
            genericCartModel.CartItemType = cartItem.getCartItemType();
            genericCartModel.StoreId = cartItem.getStoreId();
            genericCartModel.Latitude = cartItem.getLatitude();
            genericCartModel.Longitude = cartItem.getLongitude();
            genericCartModel.DeliveryPeriod = cartItem.getDeliveryPeriod();
            genericCartModel.DeliveryInformation = cartItem.getDeliveryInformation();
            genericCartModel.isMyPackage = cartItem.isMyPackage();
            genericCartModel.packageBundleId = cartItem.getPackageBundleId();
            genericCartModel.PaymentMode = cartItem.getPaymentMode();
            genericCartModel.therapistModel = cartItem.getTherapistModel();
            genericCartModel.PackageBundleItemType = cartItem.getPackageBundleItemType();
            genericCartModel.PackageBundleItemCount = cartItem.getPackageBundleItemCount();

            cartList.add(genericCartModel);
            logAddedToCartEvent(genericCartModel);

            EnrichUtils.showMessage(getApplicationContext(), "Added to Cart!");
            return;
        }

        cartItem.setQuantity(1);
        cartItem.added();

        GenericCartModel genericCartModel = new GenericCartModel();
        genericCartModel.Id = cartItem.getId();
        genericCartModel.ServiceId = cartItem.getServiceId();
        genericCartModel.Quantity = cartItem.getQuantity();
        genericCartModel.Name = cartItem.getName();
        genericCartModel.Price = cartItem.getPrice();
        genericCartModel.CartItemType = cartItem.getCartItemType();
        genericCartModel.StoreId = cartItem.getStoreId();
        genericCartModel.Latitude = cartItem.getLatitude();
        genericCartModel.Longitude = cartItem.getLongitude();
        genericCartModel.DeliveryPeriod = cartItem.getDeliveryPeriod();
        genericCartModel.DeliveryInformation = cartItem.getDeliveryInformation();
        genericCartModel.isMyPackage = cartItem.isMyPackage();
        genericCartModel.packageBundleId = cartItem.getPackageBundleId();
        genericCartModel.PaymentMode = cartItem.getPaymentMode();
        genericCartModel.therapistModel = cartItem.getTherapistModel();
        genericCartModel.PackageBundleItemType = cartItem.getPackageBundleItemType();
        genericCartModel.PackageBundleItemCount = cartItem.getPackageBundleItemCount();

        cartList.add(genericCartModel);
        logAddedToCartEvent(genericCartModel);

        EnrichUtils.showMessage(getApplicationContext(), "Added to Cart!");
//        commitCart();
    }

    public void logAddedToCartEvent(GenericCartModel cartItem) {
        // Facebook - AppEvents
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, cartItem.Name);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, "" + cartItem.Id);
        if (cartItem.CartItemType == CartItem.CART_TYPE_SERVICES) {
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "Service");
        } else if (cartItem.CartItemType == CartItem.CART_TYPE_PRODUCTS) {
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "Products");
        } else if (cartItem.CartItemType == CartItem.CART_TYPE_SUB_PACKAGE) {
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "Package");
        }
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");
        params.putString(AppEventsConstants.EVENT_PARAM_DESCRIPTION, EnrichUtils.getHomeStore(this).Name);
        params.putString(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, "" + cartItem.getQuantity());
        AppEventsLogger.newLogger(this).logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, cartItem.Price, params);

        // Firebase
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(cartItem.Id));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, cartItem.Name);
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "INR");

        if (cartItem.CartItemType == CartItem.CART_TYPE_SERVICES) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Service");
        } else if (cartItem.CartItemType == CartItem.CART_TYPE_PRODUCTS) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Products");
        } else if (cartItem.CartItemType == CartItem.CART_TYPE_SUB_PACKAGE) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Package");
        }

        bundle.putString(FirebaseAnalytics.Param.PRICE, String.valueOf(cartItem.Price));
        bundle.putString(FirebaseAnalytics.Param.QUANTITY, String.valueOf(cartItem.getQuantity()));
        bundle.putString(FirebaseAnalytics.Param.CHECKOUT_STEP, String.valueOf(Constants.SERVICE_CHECKOUT_STEP_SELECT_THERAPIST));
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, bundle);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.CHECKOUT_PROGRESS, bundle);
    }

    public void updateCartItem(CartItem cartItem) {
        removeFromCart(cartItem);
        addToCart(cartItem);
    }

    public boolean cartHasServices() {
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getCartItemType() == GenericCartModel.CART_TYPE_SERVICES) {
                return true;
            }
        }
        return false;
    }

    public boolean cartHasProducts() {
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getCartItemType() == GenericCartModel.CART_TYPE_PRODUCTS) {
                return true;
            }
        }
        return false;
    }

    public boolean cartHasLooks() {
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getCartItemType() == GenericCartModel.CART_TYPE_LOOKS) {
                return true;
            }
        }
        return false;
    }

    public boolean cartHasPackages() {
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getCartItemType() == GenericCartModel.CART_TYPE_SUB_PACKAGE) {
                return true;
            }
        }
        return false;
    }

    public int getQuantity(CartItem cartItem) {
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getId() == cartItem.getId()) {
                return cartList.get(i).getQuantity();
            }
        }
        return 0;
    }

    public int increaseQuantity(CartItem cartItem) {
        for (int i = 0; i < cartList.size(); i++) {
            if (cartItemTypeAlreadyExists(cartItem)) {
                if (cartList.get(i).getCartItemType() == GenericCartModel.CART_TYPE_PRODUCTS) {
                    if (cartList.get(i).getId() == cartItem.getId()) {
                        if (getQuantity(cartItem) == getMaxQuantityAllowed()) {
                            EnrichUtils.showMessage(this, "You have already added maximum allowed quantity!");
                            return getQuantity(cartItem);
                        } else {
                            cartItem.setQuantity(cartItem.getQuantity() + 1);
                            cartList.get(i).setQuantity(cartItem.getQuantity());
//                    cartUpdated();
                            return cartItem.getQuantity();
                        }
                    }
                } else if (cartList.get(i).getCartItemType() == GenericCartModel.CART_TYPE_SUB_PACKAGE) {
                    if (cartList.get(i).getId() == cartItem.getId()) {
                        if (getQuantity(cartItem) == getMaxQuantityAllowed()) {
                            EnrichUtils.showMessage(this, "You have already added maximum allowed quantity!");
                            return getQuantity(cartItem);
                        } else {
                            cartItem.setQuantity(cartItem.getQuantity() + 1);
                            cartList.get(i).setQuantity(cartItem.getQuantity());
//                    cartUpdated();
                            return cartItem.getQuantity();
                        }
                    }
                }
            }
        }
        cartItem.setQuantity(1);
        addToCart(cartItem);
        return cartItem.getQuantity();
    }

    public int decreaseQuantity(CartItem cartItem) {
        for (int i = 0; i < cartList.size(); i++) {
            if (cartItemTypeAlreadyExists(cartItem)) {
                if (cartList.get(i).getCartItemType() == GenericCartModel.CART_TYPE_PRODUCTS) {
                    if (cartList.get(i).getId() == cartItem.getId()) {
                        cartItem.setQuantity(cartItem.getQuantity() - 1);
                        cartList.get(i).setQuantity(cartItem.getQuantity());
                        if (cartItem.getQuantity() == 0)
                            removeFromCart(cartItem);
                        else
                            return cartItem.getQuantity();
                    }
                } else if (cartList.get(i).getCartItemType() == GenericCartModel.CART_TYPE_SUB_PACKAGE) {
                    if (cartList.get(i).getId() == cartItem.getId()) {
                        cartItem.setQuantity(cartItem.getQuantity() - 1);
                        cartList.get(i).setQuantity(cartItem.getQuantity());
                        if (cartItem.getQuantity() == 0)
                            removeFromCart(cartItem);
                        else
                            return cartItem.getQuantity();
                    }
                }
            }
        }
        return 0;
    }

    public int getItemQuantity(CartItem item) {
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).Id == item.getId()) {
                return cartList.get(i).getQuantity();
            }
        }
        return 0;
    }

    private boolean cartItemTypeAlreadyExists(CartItem cartItem) {
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getCartItemType() == cartItem.getCartItemType()) {
                return true;
            }
        }
        return false;
    }

    public boolean alreadyExist(CartItem cartItem) {
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getId() == cartItem.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean isCartFull() {
        if (cartList.size() >= getMaxAllowed()) {
            EnrichUtils.showMessage(getApplicationContext(), "Cart is full!");
            return true;
        }
        return false;
    }

    public boolean isCartEmpty() {
        if (cartList.size() == 0) {
            return true;
        } else
            return false;

    }

    public int getMaxAllowed() {
        return maxItemsAllowed;
    }

    public void setMaxItemsAllowed(int maxItemsAllowed) {
        this.maxItemsAllowed = maxItemsAllowed;
    }

    public int getMaxQuantityAllowed() {
        return maxQuantityAllowed;
    }

    public void setMaxQuantityAllowed(int maxQuantityAllowed) {
        this.maxQuantityAllowed = maxQuantityAllowed;
    }

    // 1:ADDED ; 0:REMOVED ; -1:COULD N0T ADD ; -2:CART FULL
    public int toggleItem(CartItem cartItem) {
        int toggleStatus = 0;

        if (hasThisItem(cartItem)) {
            removeFromCart(cartItem);
            toggleStatus = 0;
        } else {
            if (cartItem.getCartItemType() == CartItem.CART_TYPE_SERVICES) {
                if (isSameStore(cartItem.getStoreId())) {
                    addToCart(cartItem);
                    toggleStatus = 1;
                } else {
                    toggleStatus = -1;
                }
            } else {
                addToCart(cartItem);
                toggleStatus = 1;
            }
        }

        EnrichUtils.log("TOGGLE STATUS: " + toggleStatus);
        return toggleStatus;
    }

    public boolean isSameStore(int storeId) {
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getStoreId() != storeId)
                return cartList.get(i).getStoreId() == -1 ? true : false;
        }
        return true;
    }

    public boolean hasThisItem(CartItem cartItem) {
        if (cartItemTypeAlreadyExists(cartItem)) {
            for (int i = 0; i < cartList.size(); i++)
                if (cartList.get(i).getId() == cartItem.getId())
                    return true;
        }
        return false;
    }

    public void removeFromCart(CartItem cartItem) {
        Iterator<GenericCartModel> itemIterator = cartList.iterator();
        while (itemIterator.hasNext()) {
            GenericCartModel genericCartItem = itemIterator.next();
            if (genericCartItem.getId() == cartItem.getId()) {
                itemIterator.remove();
                genericCartItem.removed();
            }
        }
    }

    public void removeFromCartBasedOnId(int id) {
        Iterator<GenericCartModel> itemIterator = cartList.iterator();
        while (itemIterator.hasNext()) {
            GenericCartModel genericCartItem = itemIterator.next();
            if (genericCartItem.getId() == id) {
                genericCartItem.setTherapistModel(null);
                itemIterator.remove();
                genericCartItem.removed();
            }
        }
    }

    public GenericCartModel getCartItem(int position) {
        return cartList.get(position);
    }

    public int getItemCountWithQuantity() {
        int itemCount = 0;
        for (int i = 0; i < cartList.size(); i++) {
            itemCount += cartList.get(i).getQuantity();
        }
        return itemCount;
    }

    public int getItemCount() {
        return cartList.size();
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (int i = 0; i < cartList.size(); i++)
            totalPrice += (cartList.get(i).getPrice() * cartList.get(i).getQuantity());
        return totalPrice;
    }

    public ArrayList<GenericCartModel> getCartItems() {
        return cartList;
    }

    public static GenericCartModel getGenericCartModelForServices(ArrayList<GenericCartModel> cartList) {
        GenericCartModel genericCartModel = new GenericCartModel();

        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getCartItemType() == GenericCartModel.CART_TYPE_SERVICES) {
                genericCartModel = cartList.get(i);
            }
        }
        return genericCartModel;
    }

    public ArrayList<GenericCartModel> getProductsInCart() {
        ArrayList<GenericCartModel> tempProducts = new ArrayList<>();
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getCartItemType() == GenericCartModel.CART_TYPE_PRODUCTS) {
                tempProducts.add(cartList.get(i));
            }
        }
        return tempProducts;
    }

    public ArrayList<GenericCartModel> getServicesInCart() {
        ArrayList<GenericCartModel> tempProducts = new ArrayList<>();
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getCartItemType() == GenericCartModel.CART_TYPE_SERVICES) {
                tempProducts.add(cartList.get(i));
            }
        }
        return tempProducts;
    }

    public ArrayList<GenericCartModel> getLooksInCart() {
        ArrayList<GenericCartModel> tempProducts = new ArrayList<>();
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getCartItemType() == GenericCartModel.CART_TYPE_LOOKS) {
                tempProducts.add(cartList.get(i));
            }
        }
        return tempProducts;
    }

    public ArrayList<GenericCartModel> getPackagesInCart() {
        ArrayList<GenericCartModel> tempProducts = new ArrayList<>();
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getCartItemType() == GenericCartModel.CART_TYPE_SUB_PACKAGE) {
                tempProducts.add(cartList.get(i));
            }
        }
        return tempProducts;
    }

    public void commitCart() {
        try {
            SharedPreferenceStore.storeValue(getApplicationContext(), "CART", ObjectSerializer.serialize(cartList));

            ArrayList<GenericCartModel> storedCartItems = (ArrayList<GenericCartModel>) ObjectSerializer.deserialize(SharedPreferenceStore.getValue(getApplicationContext(), "CART", ObjectSerializer.serialize(new ArrayList<GenericCartModel>())));

            EnrichUtils.log("" + storedCartItems.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<GenericCartModel> getStoredCartItem() {
        try {
            ArrayList<GenericCartModel> storedCartItems = (ArrayList<GenericCartModel>) ObjectSerializer.deserialize(SharedPreferenceStore.getValue(getApplicationContext(), "CART", ObjectSerializer.serialize(new ArrayList<GenericCartModel>())));

            EnrichUtils.log("" + storedCartItems.size());

            return storedCartItems;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void clearCart() {
        cartList.clear();
//        SharedPreferenceStore.deleteValue(getApplicationContext(), "CART");
    }

    public boolean isBackAllowed() {
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getCartItemType() == GenericCartModel.CART_TYPE_SERVICES || cartList.get(i).getCartItemType() == GenericCartModel.CART_TYPE_LOOKS) {
                return false;
            }
        }
        return true;
    }

    public String getCartItemNames() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < cartList.size(); i++) {
            sb.append(cartList.get(i) + ", ");
        }

        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    public String getDeliveryPeriod() {
        if (cartHasProducts()) {
            for (int i = 0; i < cartList.size(); i++) {
                if (cartList.get(i).CartItemType == CartItem.CART_TYPE_PRODUCTS) {
                    return cartList.get(i).getDeliveryPeriod();
                }
            }
        }

        return "-";
    }

    public String getDeliveryInformation() {
        if (cartHasProducts()) {
            for (int i = 0; i < cartList.size(); i++) {
                if (cartList.get(i).CartItemType == CartItem.CART_TYPE_PRODUCTS) {
                    return cartList.get(i).getDeliveryInformation();
                }
            }
        }

        return "-";
    }

    public GenericCartModel getServiceById(int id) {
        if (cartHasServices()) {
            for (int i = 0; i < cartList.size(); i++) {
                if (id == cartList.get(i).Id) {
                    return cartList.get(i);
                }
            }
        }
        return null;
    }
}

