package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.ui.fragments.TutorialFragmentFour;
import com.enrich.salonapp.ui.fragments.TutorialFragmentOne;
import com.enrich.salonapp.ui.fragments.TutorialFragmentThree;
import com.enrich.salonapp.ui.fragments.TutorialFragmentTwo;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.SharedPreferenceStore;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SliderActivity extends AppCompatActivity {

    @BindView(R.id.image_slider)
    ViewPager imageSlider;

    @BindView(R.id.proceed_button)
    Button proceedButton;

    EnrichApplication application;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        SharedPreferenceStore.storeValue(this, Constants.TUTORIAL_SHOWN, true);

        application = (EnrichApplication) getApplication();

        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Tutorial");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        EnrichUtils.changeStatusBarColor(getWindow());

        ButterKnife.bind(this);

        imageSlider.setAdapter(new CustomFragmentPagerAdapter(getSupportFragmentManager()));

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SliderActivity.this, SignInActivity.class); //StoreSelectorActivity
                startActivity(intent);
                finish();
            }
        });
    }

    class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

        public CustomFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TutorialFragmentOne.getInstance();
                case 1:
                    return TutorialFragmentTwo.getInstance();
                case 2:
                    return TutorialFragmentThree.getInstance();
                case 3:
                    return TutorialFragmentFour.getInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
