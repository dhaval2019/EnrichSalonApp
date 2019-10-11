package com.enrich.salonapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.util.CustomTypefaceSpan;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class BeautyAndBlingNewUserWinningActivity extends AppCompatActivity {

    @BindView(R.id.bandb_text_1)
    TextView bandbText1;

    @BindView(R.id.bandb_text_2)
    TextView bandbText2;

    @BindView(R.id.bandb_text_3)
    TextView bandbText3;

    @BindView(R.id.book_now)
    Button bookNow;

    @BindView(R.id.beauty_and_bling_skip)
    TextView beautyAndBlingSkip;

    @BindView(R.id.points_expiry_date)
    TextView pointsExpiryDate;

    private int pointsWon;
    private boolean spinTaken;
    private String validityDateStr;
    Tracker mTracker;
    EnrichApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_and_bling_new_user_winning);

        ButterKnife.bind(this);
        application = (EnrichApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Beauty and Bling new user winning screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);
        pointsWon = getIntent().getIntExtra("PointsWon", 0);
        spinTaken = getIntent().getBooleanExtra("SpinTaken", false);
        validityDateStr = getIntent().getStringExtra("ValidityDateStr");

        if (spinTaken) {
            bandbText1.setVisibility(View.GONE);
            String pointsWonStr = "" + pointsWon;
            String bandText2Str = "You have " + pointsWonStr + " points in your account!";
            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(bandText2Str);
            spannableStringBuilder2.setSpan(new CustomTypefaceSpan("", Typeface.createFromAsset(getAssets(),
                    "fonts/Quicksand-Bold.ttf")), 9, 9 + pointsWonStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder2.setSpan(new UnderlineSpan(), 9, 9 + pointsWonStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            bandbText2.setText(spannableStringBuilder2);
            bandbText3.setText(String.format("You can redeem these points on a minimum spend on %s2500 on services at any of our salons", getResources().getString(R.string.Rs)));

            try {
                SimpleDateFormat stringToDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date expiryDate = stringToDateFormat.parse(validityDateStr);

                String daysRemaining = "" + TimeUnit.DAYS.convert(expiryDate.getTime() - new Date().getTime(), TimeUnit.MILLISECONDS);
                String pointsExpiryDateStr = "Points expiring in " + daysRemaining + " days";
                SpannableStringBuilder pointsExpiryDateSpannableStringBuilder = new SpannableStringBuilder(pointsExpiryDateStr);
                pointsExpiryDateSpannableStringBuilder.setSpan(new CustomTypefaceSpan("", Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Bold.ttf")), 19, 19 + daysRemaining.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                pointsExpiryDateSpannableStringBuilder.setSpan(new UnderlineSpan(), 19, 19 + daysRemaining.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                pointsExpiryDate.setText(pointsExpiryDateSpannableStringBuilder);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            bandbText1.setVisibility(View.VISIBLE);
            bandbText2.setVisibility(View.GONE);
            bandbText1.setText(String.format("You have won %d points", pointsWon));
        }

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BeautyAndBlingNewUserWinningActivity.this, ServiceListActivity.class);
                intent.putExtra("isHomeSelected", false);
                startActivity(intent);
                finish();
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
