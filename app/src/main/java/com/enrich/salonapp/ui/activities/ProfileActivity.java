package com.enrich.salonapp.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.AddressModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.data.model.ImageModel;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.drawer_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.email)
    TextView email;

    @BindView(R.id.phone)
    TextView phone;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.gender)
    TextView gender;

    @BindView(R.id.address_home)
    TextView addressHome;

    @BindView(R.id.address_work)
    TextView addressWork;

    @BindView(R.id.address_other)
    TextView addressOther;

    @BindView(R.id.membership_cardview)
    CardView membershipCardview;

    @BindView(R.id.membership_name)
    TextView membershipName;

    @BindView(R.id.membership_expiry_date)
    TextView membershipExpiryDate;

    ImageModel model;

    EnrichApplication application;
    Tracker mTracker;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        application = (EnrichApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Profile Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        assert toolbar != null;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitleTextColor(Color.parseColor("#000000"));

        assert collapsingToolbarLayout != null;
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);
        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTypeface(tf);

        collapsingToolbarLayout.setTitle("PROFILE");

        GuestModel guestModel = EnrichUtils.getUserData(this);
        name.setText(String.format("%s %s", guestModel.FirstName, guestModel.LastName));
        email.setText(guestModel.Email);
        phone.setText(String.format("%s", guestModel.MobileNumber));
        gender.setText(guestModel.Gender == 1 ? "Male" : "Female");
        setAddressData(guestModel.GuestAddress);
        if (guestModel.IsMember == Constants.IS_MEMBER) {
            membershipCardview.setVisibility(View.VISIBLE);
            membershipName.setText(String.format("%s", guestModel.MembershipModel.get(0).MembershipName));
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date stringToDate = sdf.parse(guestModel.MembershipModel.get(0).ExpiryDate.substring(0, 10));

                SimpleDateFormat dateToStringFormat = new SimpleDateFormat("dd/MM/yyyy");
                String expiryDate = dateToStringFormat.format(stringToDate);

                membershipExpiryDate.setText(String.format("Expires on %s", expiryDate));

            } catch (ParseException e) {
                e.printStackTrace();
                membershipExpiryDate.setVisibility(View.GONE);
            }
        } else {
            membershipCardview.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (name != null || email != null || phone != null || gender != null) {
            GuestModel guestModel = EnrichUtils.getUserData(this);
            name.setText(String.format("%s %s", guestModel.FirstName, guestModel.LastName));
            email.setText(guestModel.Email);
            phone.setText(String.format("%s", guestModel.MobileNumber));
            gender.setText(guestModel.Gender == 1 ? "Male" : "Female");
            setAddressData(guestModel.GuestAddress);
        }
    }

    private void setAddressData(ArrayList<AddressModel> list) {
        if (list != null) {
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    switch (list.get(i).AddressType) {
                        case "S":
                            addressHome.setText(list.get(i).Location);
                            break;
                        case "W":
                            addressWork.setText(list.get(i).Location);
                            break;
                        case "O":
                            addressOther.setText(list.get(i).Location);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("ImageModel", model);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
