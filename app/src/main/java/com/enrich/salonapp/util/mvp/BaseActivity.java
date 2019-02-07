package com.enrich.salonapp.util.mvp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.OfferModel;
import com.enrich.salonapp.ui.activities.BookingSummaryActivity;
import com.enrich.salonapp.ui.activities.CartActivity;
import com.enrich.salonapp.ui.activities.MyPackageActivity;
import com.enrich.salonapp.ui.activities.OTPActivity;
import com.enrich.salonapp.ui.activities.PackageDetailActivity;
import com.enrich.salonapp.ui.activities.PackagesActivity;
import com.enrich.salonapp.ui.activities.RegisterActivity;
import com.enrich.salonapp.ui.activities.SignInActivity;
import com.enrich.salonapp.ui.activities.SplashActivity;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.OfferHandler;
import com.enrich.salonapp.util.SharedPreferenceStore;
import com.enrich.salonapp.util.supertoast.Style;
import com.enrich.salonapp.util.supertoast.SuperActivityToast;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class BaseActivity extends AppCompatActivity implements IBaseView {

    public static final String SHOW_SOCIAL_PROOF = "ShowSocialProof";
    Timer timer;

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setProgressBar(boolean show) {
        if (show) {
            EnrichUtils.showProgressDialog(this);
        } else {
            EnrichUtils.cancelCurrentDialog(this);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BaseActivity.this instanceof SplashActivity)
            return;

        if (BaseActivity.this instanceof BookingSummaryActivity)
            return;

        if (BaseActivity.this instanceof SignInActivity)
            return;

        if (BaseActivity.this instanceof RegisterActivity)
            return;

        if (BaseActivity.this instanceof OTPActivity)
            return;

        if (BaseActivity.this instanceof PackageDetailActivity)
            return;

        if (BaseActivity.this instanceof PackagesActivity)
            return;

        if (BaseActivity.this instanceof MyPackageActivity)
            return;

        if (BaseActivity.this instanceof CartActivity)
            return;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final ArrayList<OfferModel> list = EnrichUtils.newGson().fromJson(SharedPreferenceStore.getValue(BaseActivity.this, Constants.KEY_TOAST_OFFERS, ""), new TypeToken<List<OfferModel>>() {
                        }.getType());

                        if (list != null) {
                            if (!list.isEmpty()) {
                                final int position = new Random().nextInt(list.size());
                                SuperActivityToast.create(BaseActivity.this, new Style(), Style.TYPE_IMAGE_AND_BUTTON)
                                        .setOnButtonClickListener("tag", null, new SuperActivityToast.OnButtonClickListener() {
                                            @Override
                                            public void onClick(View view, Parcelable token) {
                                                OfferHandler.handleOfferRedirection(BaseActivity.this, list.get(position));
                                            }
                                        }).setText(list.get(position).ToastMessage)
                                        .setDuration(Style.DURATION_VERY_LONG)
                                        .setFrame(Style.FRAME_STANDARD)
                                        .setColor(Color.parseColor("#d69e5c"))
                                        .setImageURL(list.get(position).ToastImageURL)
                                        .setAnimations(Style.ANIMATIONS_POP).show();
                            }
                        }
                    }
                });
            }
        }, 3000, 10000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SuperActivityToast.cancelAllSuperToasts();
//        new Timer().cancel();
//        new Timer().purge();

//        if (timer != null) {
//            timer.scheduleAtFixedRate(new TimerTask() {
//                @Override
//                public void run() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            final ArrayList<OfferModel> list = EnrichUtils.newGson().fromJson(SharedPreferenceStore.getValue(BaseActivity.this, Constants.KEY_TOAST_OFFERS, ""), new TypeToken<List<OfferModel>>() {
//                            }.getType());
//
//                            if (list != null) {
//                                if (!list.isEmpty()) {
//                                    final int position = new Random().nextInt(list.size());
//                                    SuperActivityToast.create(BaseActivity.this, new Style(), Style.TYPE_IMAGE_AND_BUTTON)
//                                            .setOnButtonClickListener("tag", null, new SuperActivityToast.OnButtonClickListener() {
//                                                @Override
//                                                public void onClick(View view, Parcelable token) {
//                                                    OfferHandler.handleOfferRedirection(BaseActivity.this, list.get(position));
//                                                }
//                                            }).setText(list.get(position).ToastMessage)
//                                            .setDuration(Style.DURATION_VERY_LONG)
//                                            .setFrame(Style.FRAME_STANDARD)
//                                            .setColor(Color.parseColor("#d69e5c"))
//                                            .setImageURL(list.get(position).ToastImageURL)
//                                            .setAnimations(Style.ANIMATIONS_POP).show();
//                                }
//                            }
//                        }
//                    });
//                }
//            }, 3000, 10000);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
