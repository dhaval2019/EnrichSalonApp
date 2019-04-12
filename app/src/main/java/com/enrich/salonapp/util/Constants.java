package com.enrich.salonapp.util;

public class Constants {

    public static String LOG_TAG = "EnrichSalonApp";

    public static int PLATFORM_ANDROID = 2;

    public static String BLACK_FONT = "Montserrat-Black.otf";
    public static String BOLD_FONT = "Montserrat-Bold.ttf";
    public static String EXTRA_BOLD_FONT = "Montserrat-ExtraBold.otf";
    public static String LIGHT_FONT = "Montserrat-Light.otf";
    public static String REGULAR_FONT = "Montserrat-Regular.ttf";
    public static String SEMI_BOLD_FONT = "Montserrat-SemiBold.otf";
    public static String ULTRA_LIGHT_FONT = "Montserrat-UltraLight.otf";
    public static String ITALIC_FONT = "ProximaNova-Italic.ttf";

    public static String CENTER_ID = "fe62cba7-f099-4f71-88f4-058c94e70655";
    public static String PARENT_CATEGORY_ID = "41AC7424-8BDE-461C-9C88-8E794C9EC77B";
    public static String HOME_CATEGORY_ID = "a980c048-71f3-45a7-90cd-fbf2edc9bb54";

    public static final String KEY_USER_DATA = "GuestsDataKey";
    public static final String KEY_USER_ADDRESS = "GuestsAddress";
    public static final String KEY_AUTHENTICATION = "AuthenticationKey";
    public static final String KEY_CURRENT_LATITUDE = "currentLatitude";
    public static final String KEY_CURRENT_LONGITUDE = "currentLongitude";
    public static final String KEY_TOAST_OFFERS = "ToastOffers";

    public static final int IST_EXTRA_MINS = (5 * 60) + 30; // UTC TIME IN MINUTES

    public static final String CURRENT_LATITUDE = "currentLatitude";
    public static final String CURRENT_LONGITUDE = "currentLongitude";

    public static final String HOME_STORE = "UserHomeStore";
    public static final String HOME_STORE_FOR_REBOOK = "UserHomeStoreForRebook";

    public static final int OFFER_COMMAND_LOOK = 0;
    public static final int OFFER_COMMAND_PARTICULAR_LOOK = 1;
    public static final int OFFER_COMMAND_EXCLUSIVE_LOOK = 2;
    public static final int OFFER_COMMAND_SERVICE_LIST = 3;
    public static final int OFFER_COMMAND_PRODUCTS_LIST = 4;
    public static final int OFFER_COMMAND_PRODUCTS_OF_SALON = 5;
    public static final int OFFER_COMMAND_PARTICULAR_PRODUCTS = 6;
    public static final int OFFER_COMMAND_PACKAGES_LIST = 7;
    public static final int OFFER_COMMAND_PARTICULAR_PACKAGES = 8;
    public static final int OFFER_COMMAND_STYLIST = 9;
    public static final int OFFER_COMMAND_PARTICULAR_STYLIST = 10;
    public static final int OFFER_COMMAND_NO_ACTION = 11;
    public static final int OFFER_COMMAND_PRODUCT_CATEGORY_LIST = 12;
    public static final int OFFER_COMMAND_PARTICULAR_PRODUCT_CATEGORY = 13;
    public static final int OFFER_COMMAND_PRODUCT_SUBCATEGORY_LIST = 14;
    public static final int OFFER_COMMAND_PARTICULAR_PRODUCT_SUBCATEGORY = 15;
    public static final int OFFER_COMMAND_PRODUCT_BRAND_LIST = 16;
    public static final int OFFER_COMMAND_PARTICULAR_PRODUCT_BRAND = 17;
    public static final int OFFER_COMMAND_WEB = 18;
    public static final int OFFER_COMMAND_BEAUTY_AND_BLING_SPIN = 19;

    public static final int PAYMENT_MODE_CASH = 0;
    public static final int PAYMENT_MODE_ONLINE = 1;
    public static final int PAYMENT_MODE_BOTH = 2;

    public static final int PAYMENT_SUCCESS = 1;

    public static final int CENTER_TYPE_SALON = 1;
    public static final int CENTER_TYPE_HOME = 2;
    public static final int CENTER_TYPE_BOTH = 3;

    public static final int SHOW_SPIN = 1;
    public static final int DONT_SHOW_SPIN = 0;

    public static final int IS_MEMBER = 1;
    public static final int IS_NOT_MEMBER = 0;

    public static final int SERVICE_CHECKOUT_STEP_SELECT_SERVICE = 1;
    public static final int SERVICE_CHECKOUT_STEP_SELECT_THERAPIST = 2;
    public static final int SERVICE_CHECKOUT_STEP_SELECT_TIME_SLOT = 3;
    public static final int SERVICE_CHECKOUT_STEP_INITIATION = 4;
    public static final int SERVICE_CHECKOUT_STEP_PAYMENT = 5;

    public static final int PRODUCT_CHECKOUT_STEP_ADD_TO_CART = 101;
    public static final int PRODUCT_CHECKOUT_STEP_INITIATION = 201;
    public static final int PRODUCT_CHECKOUT_STEP_PAYMENT = 301;

    public static final int PACKAGE_CHECKOUT_STEP_ADD_TO_CART = 1001;
    public static final int PACKAGE_CHECKOUT_STEP_INITIATION = 2001;
    public static final int PACKAGE_CHECKOUT_STEP_PAYMENT = 3001;

    public static final int MALE = 1;
    public static final int FEMALE = 2;

    public static final String TUTORIAL_SHOWN = "TutorialShown";

    public static final String SEGMENT_LOGIN = "Login";
    public static final String SEGMENT_REGISTRATION = "Registration";
    public static final String SEGMENT_SELECT_STORE = "SelectStore";
    public static final String SEGMENT_SELECT_SERVICE = "SelectService";
    public static final String SEGMENT_BOOK_APPOINTMENT = "BookAppointment";
    public static final String SEGMENT_BOOK_SUMMARY = "BookingSummary";
    public static final String SEGMENT_PAYMENT = "Payment";
    public static final String SEGMENT_BOOKING_STATUS = "BookingStatus";
    public static final String SEGMENT_ADD_TO_CART = "AddToCart";
    public static final String SEGMENT_CHECKOUT = "Checkout";
    public static final String SEGMENT_ADD_ADDRESS = "AddAddress";
    public static final String SEGMENT_SELECT_ADDRESS = "SelectAddress";
    public static final String SEGMENT_CANCEL_APPOINTMENT = "CancelAppointment";
}
