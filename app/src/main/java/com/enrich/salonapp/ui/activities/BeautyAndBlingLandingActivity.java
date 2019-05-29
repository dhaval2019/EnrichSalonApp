package com.enrich.salonapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.util.CustomTypefaceSpan;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class BeautyAndBlingLandingActivity extends AppCompatActivity {

    @BindView(R.id.bandb_text_1)
    TextView bandbText1;

    @BindView(R.id.bandb_text_2)
    TextView bandbText2;

    @BindView(R.id.bandb_text_3)
    TextView bandbText3;

    @BindView(R.id.bandb_text_4)
    TextView bandbText4;

    @BindView(R.id.try_a_spin)
    TextView tryASpin;

    @BindView(R.id.beauty_and_bling_skip)
    TextView beautyAndBlingSkip;

    boolean isNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_and_bling_landing);

        ButterKnife.bind(this);

        isNewUser = getIntent().getBooleanExtra("IsNewUser", false);

        String bandText2Str = "Spend " + getResources().getString(R.string.rs_symbol) + "2500 on services or products and spin the wheel for assured reward points of up to 2500.";

        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(bandText2Str);
        spannableStringBuilder2.setSpan(new CustomTypefaceSpan("", Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.otf")), 6, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder2.setSpan(new CustomTypefaceSpan("", Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.otf")), bandText2Str.length() - 5, bandText2Str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        bandbText2.setText(spannableStringBuilder2);

        String bandText3Str = "What's more, there are prizes worth " + getResources().getString(R.string.rs_symbol) + "40 lacs up for grabs in 55 lucky draws.";

        SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(bandText3Str);
        spannableStringBuilder3.setSpan(new CustomTypefaceSpan("", Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.otf")), 36, 39, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder3.setSpan(new CustomTypefaceSpan("", Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.otf")), bandText3Str.length() - 16, bandText3Str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        bandbText3.setText(spannableStringBuilder3);

        tryASpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNewUser) {
                    Intent intent = new Intent(BeautyAndBlingLandingActivity.this, BeautyAndBlingSpinActivity.class);
                    intent.putExtra("IsNewUser", isNewUser);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(BeautyAndBlingLandingActivity.this, BeautyAndBlingEligibilityActivity.class);
                    intent.putExtra("IsNewUser", isNewUser);
                    startActivity(intent);
                    finish();
                }
            }
        });

        beautyAndBlingSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
