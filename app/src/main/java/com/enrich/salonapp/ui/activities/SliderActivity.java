package com.enrich.salonapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.enrich.salonapp.R;
import com.enrich.salonapp.ui.fragments.TutorialFragmentFour;
import com.enrich.salonapp.ui.fragments.TutorialFragmentOne;
import com.enrich.salonapp.ui.fragments.TutorialFragmentThree;
import com.enrich.salonapp.ui.fragments.TutorialFragmentTwo;
import com.enrich.salonapp.util.EnrichUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SliderActivity extends AppCompatActivity {

    @BindView(R.id.image_slider)
    ViewPager imageSlider;

    @BindView(R.id.proceed_button)
    Button proceedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        EnrichUtils.changeStatusBarColor(getWindow());

        ButterKnife.bind(this);

        imageSlider.setAdapter(new CustomFragmentPagerAdapter(getSupportFragmentManager()));

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SliderActivity.this, SignInActivity.class);
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
