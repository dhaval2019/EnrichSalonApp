package com.enrich.salonapp.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.OfferModel;
import com.enrich.salonapp.data.model.Product.OfferProductBrandTagsModel;
import com.enrich.salonapp.data.model.Product.OfferProductCategoryTagsModel;
import com.enrich.salonapp.data.model.Product.OfferProductSubCategoryTagsModel;
import com.enrich.salonapp.ui.activities.HomeActivity;
import com.enrich.salonapp.ui.activities.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class EnrichFireBaseMessagingService extends FirebaseMessagingService {

    private int callToAction;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        EnrichUtils.log("onNewToken: " + s);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        EnrichUtils.log("From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            EnrichUtils.log("Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            EnrichUtils.log("Notification Data: " + EnrichUtils.newGson().toJson(remoteMessage.getNotification()));
            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), remoteMessage.getData());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(String body, String title, Map<String, String> data) {
        OfferModel model = new OfferModel();
        if (data.size() != 0) {
            callToAction = Integer.parseInt(data.get("CallToAction"));
        }else{
            callToAction = 11;
        }

        switch (callToAction) {
            case Constants.OFFER_COMMAND_PACKAGES_LIST:
                model.CallToAction = callToAction;
                break;
            case Constants.OFFER_COMMAND_SERVICE_LIST:
                model.CallToAction = callToAction;
                break;
            case Constants.OFFER_COMMAND_PARTICULAR_PACKAGES:
                model.CallToAction = callToAction;
                model.PackageId = Integer.parseInt(data.get("PackageId"));
                break;
            case Constants.OFFER_COMMAND_PRODUCTS_LIST:
                model.CallToAction = callToAction;
                break;
            case Constants.OFFER_COMMAND_PARTICULAR_PRODUCTS:
                model.CallToAction = callToAction;
                model.ProductId = Integer.parseInt(data.get("ProductId"));
                break;
            case Constants.OFFER_COMMAND_PRODUCT_CATEGORY_LIST:
                model.CallToAction = callToAction;
                break;
            case Constants.OFFER_COMMAND_PARTICULAR_PRODUCT_CATEGORY:
                model.CallToAction = callToAction;
                ArrayList<OfferProductCategoryTagsModel> productCategoryList = new ArrayList<>();

                String[] productCategorySplit = data.get("ProductCategoryId").split(",");
                for (int i = 0; i < productCategorySplit.length; i++) {
                    productCategoryList.add(new OfferProductCategoryTagsModel(0, 0, Integer.parseInt(productCategorySplit[i].trim())));
                }
                model.OfferProductCategoryTags = productCategoryList;
                break;
            case Constants.OFFER_COMMAND_PRODUCT_SUBCATEGORY_LIST:
                model.CallToAction = callToAction;
                break;
            case Constants.OFFER_COMMAND_PARTICULAR_PRODUCT_SUBCATEGORY:
                model.CallToAction = callToAction;
                ArrayList<OfferProductSubCategoryTagsModel> productSubCategoryList = new ArrayList<>();

                String[] productSubCategorySplit = data.get("ProductSubcategoryId").split(",");
                for (int i = 0; i < productSubCategorySplit.length; i++) {
                    productSubCategoryList.add(new OfferProductSubCategoryTagsModel(0, 0, Integer.parseInt(productSubCategorySplit[i].trim())));
                }
                model.OfferProductSubCategoryTags = productSubCategoryList;
                break;
            case Constants.OFFER_COMMAND_PRODUCT_BRAND_LIST:
                model.CallToAction = callToAction;
                break;
            case Constants.OFFER_COMMAND_PARTICULAR_PRODUCT_BRAND:
                model.CallToAction = callToAction;
                ArrayList<OfferProductBrandTagsModel> productBrandList = new ArrayList<>();

                String[] productBrandSplit = data.get("ProductBrandId").split(",");
                for (int i = 0; i < productBrandSplit.length; i++) {
                    productBrandList.add(new OfferProductBrandTagsModel(0, 0, Integer.parseInt(productBrandSplit[i].trim())));
                }
                model.OfferProductBrandTags = productBrandList;
                break;
            case Constants.OFFER_COMMAND_NO_ACTION:
                break;
        }


        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("CallToAction", data.get("CallToAction"));
        intent.putExtra("OfferModelFromNotification", model);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Bitmap notificationIconBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.placeholder, null)).getBitmap();
        Bitmap notificationImageBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.placeholder_ext, null)).getBitmap();

        try {
            URL notificationIconImageUrl = new URL(data.get("PushNotificationIconImageUrl"));
            notificationIconBitmap = BitmapFactory.decodeStream(notificationIconImageUrl.openConnection().getInputStream());

            URL notificationImageUrl = new URL(data.get("PushNotificationImageUrl"));
            notificationImageBitmap = BitmapFactory.decodeStream(notificationImageUrl.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Notification Layout
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
        contentView.setImageViewBitmap(R.id.image_news, notificationIconBitmap);
        contentView.setTextViewText(R.id.tv_news_title, title);

        // Big Notification Layout
        RemoteViews bigContentView = new RemoteViews(getPackageName(), R.layout.big_notification_layout);
        bigContentView.setImageViewBitmap(R.id.image_news, notificationImageBitmap);
        bigContentView.setTextViewText(R.id.tv_news_title, title);

        int notifyId = 1;
        String channelId = "some_channel_id";
        Notification notification = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Enrich";
            String description = "";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notification = new Notification.Builder(this)
                    .setCustomBigContentView(bigContentView)
                    .setSmallIcon(Icon.createWithBitmap(notificationIconBitmap))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setChannelId(channelId)
                    .build();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notification = new Notification.Builder(this)
                    .setCustomBigContentView(bigContentView)
                    .setCustomHeadsUpContentView(bigContentView)
                    .setSmallIcon(Icon.createWithBitmap(notificationIconBitmap))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .build();
        } else {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notification = new Notification.Builder(this)
                    .setSmallIcon(Icon.createWithBitmap(notificationIconBitmap))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .build();
            notification.bigContentView = bigContentView;
        }

        notificationManager.notify(notifyId, notification);
    }
}
