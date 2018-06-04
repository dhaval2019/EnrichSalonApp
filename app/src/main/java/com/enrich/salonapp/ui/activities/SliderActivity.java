package com.enrich.salonapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.enrich.salonapp.util.EnrichUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SliderActivity extends AppCompatActivity {

    @BindView(R.id.image_slider)
    ViewPager imageSlider;

    @BindView(R.id.proceed_button)
    Button proceedButton;

    int[] images = {R.drawable.tutorial_banner_1, R.drawable.tutorial_banner_2, R.drawable.tutorial_banner_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        EnrichUtils.changeStatusBarColor(getWindow());

        ButterKnife.bind(this);

        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(this);
        imageSlider.setAdapter(customPagerAdapter);

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SliderActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = itemView.findViewById(R.id.imageView);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            imageView.setImageBitmap(EnrichUtils.decodeSampledBitmapFromResource(getResources(), images[position], width, height));

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
