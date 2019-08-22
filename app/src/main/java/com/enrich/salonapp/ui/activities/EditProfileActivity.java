package com.enrich.salonapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.AddressModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.data.model.GuestUpdateRequestModel;
import com.enrich.salonapp.data.model.GuestUpdateResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.contracts.UpdateGuestContract;
import com.enrich.salonapp.ui.presenters.UpdateGuestPresenter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.enrich.salonapp.ui.activities.AddAddressActivity.ADD_ADDRESS;

public class EditProfileActivity extends BaseActivity implements UpdateGuestContract.View {

    @BindView(R.id.drawer_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.edit_first_name)
    EditText firstName;

    @BindView(R.id.edit_last_name)
    EditText lastName;

    @BindView(R.id.edit_email)
    EditText email;

    @BindView(R.id.edit_phone)
    EditText phone;

    @BindView(R.id.edit_gender_male)
    RadioButton editGenderMale;

    @BindView(R.id.edit_gender_female)
    RadioButton editGenderFemale;

    @BindView(R.id.edit_profile_save)
    Button editProfileSave;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.edit_address_home)
    TextView editAddressHome;

    @BindView(R.id.edit_address_work)
    TextView editAddressWork;

    @BindView(R.id.edit_address_other)
    TextView editAddressOther;

    @BindView(R.id.address_other_container)
    RelativeLayout addressOtherContainer;

    @BindView(R.id.address_work_container)
    RelativeLayout addressWorkContainer;

    @BindView(R.id.address_home_container)
    RelativeLayout addressHomeContainer;

    String firstNameStr, lastNameStr, emailStr, phoneStr;

    int gender = -1;

    GuestUpdateRequestModel guestUpdateRequestModel;

    UpdateGuestPresenter updateGuestPresenter;
    DataRepository dataRepository;

    private ArrayList<AddressModel> list;

    GuestModel guestModel;

    EnrichApplication application;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        application = (EnrichApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Edit Profile Screen");
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

        collapsingToolbarLayout.setTitle("EDIT PROFILE");

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Light.otf");
        editGenderMale.setTypeface(custom_font);
        editGenderFemale.setTypeface(custom_font);

        guestModel = EnrichUtils.getUserData(this);
        firstName.setText(guestModel.FirstName);
        lastName.setText(guestModel.LastName);
        email.setText(guestModel.Email);
        phone.setText(guestModel.MobileNumber);

        if (guestModel.Gender == 1) {
            editGenderMale.setChecked(true);
            editGenderFemale.setChecked(false);
        } else if (guestModel.Gender == 2) {
            editGenderMale.setChecked(false);
            editGenderFemale.setChecked(true);
        } else {
            editGenderMale.setChecked(false);
            editGenderFemale.setChecked(false);
        }

        setAddressData(guestModel.GuestAddress);

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this, mainUiThread, threadExecutor, null);
        updateGuestPresenter = new UpdateGuestPresenter(this, dataRepository);

        editProfileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstNameStr = firstName.getText().toString();
                lastNameStr = lastName.getText().toString();
                emailStr = email.getText().toString();
                phoneStr = phone.getText().toString();

                if (!EnrichUtils.isValidEmailId(emailStr)) {
                    EnrichUtils.showMessage(EditProfileActivity.this, "Please enter valid email.");
                    return;
                }

                if (editGenderMale.isChecked()) {
                    gender = 1;
                } else if (editGenderFemale.isChecked()) {
                    gender = 2;
                }

                if (firstNameStr.isEmpty() || lastNameStr.isEmpty() || gender == -1) {
                    EnrichUtils.showMessage(EditProfileActivity.this, "All fields are mandatory.");
                    return;
                }

                GuestModel model = new GuestModel();
                model.FirstName = firstNameStr;
                model.LastName = lastNameStr;
                model.MobileNumber = phoneStr;
                model.Id = EnrichUtils.getUserData(EditProfileActivity.this).Id;
                model.Email = emailStr;
                model.Gender = gender;

//                GuestUpdateModel guestUpdateModel = new GuestUpdateModel();
//                guestUpdateModel.Email = emailStr;
//                guestUpdateModel.GuestFName = firstNameStr;
//                guestUpdateModel.GuestLName = lastNameStr;
//                guestUpdateModel.GuestPhone = phoneStr;
//                guestUpdateModel.id = EnrichUtils.getUserData(EditProfileActivity.this).Id;
//                if (editGenderMale.isChecked()) {
//                    guestUpdateModel.Gender = "Male";
//                } else if (editGenderFemale.isChecked()) {
//                    guestUpdateModel.Gender = "Female";
//                }

                guestUpdateRequestModel = new GuestUpdateRequestModel();
                guestUpdateRequestModel.CenterId = EnrichUtils.getUserData(EditProfileActivity.this).CenterId;
                guestUpdateRequestModel.Guest = model;

                EnrichUtils.log(EnrichUtils.newGson().toJson(guestUpdateRequestModel));

                updateGuestPresenter.updateGuest(EditProfileActivity.this, guestUpdateRequestModel);
            }
        });

        addressHomeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, AddAddressActivity.class);
                intent.putExtra("AddressType", AddAddressActivity.ADDRESS_HOME);
                intent.putExtra("Address", EnrichUtils.getHomeAddress(EditProfileActivity.this));
                startActivityForResult(intent, ADD_ADDRESS);
            }
        });

        addressWorkContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, AddAddressActivity.class);
                intent.putExtra("AddressType", AddAddressActivity.ADDRESS_WORK);
                intent.putExtra("Address", EnrichUtils.getWorkAddress(EditProfileActivity.this));
                startActivityForResult(intent, ADD_ADDRESS);
            }
        });

        addressOtherContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, AddAddressActivity.class);
                intent.putExtra("AddressType", AddAddressActivity.ADDRESS_OTHER);
                intent.putExtra("Address", EnrichUtils.getOtherAddress(EditProfileActivity.this));
                startActivityForResult(intent, ADD_ADDRESS);
            }
        });
    }

    private void setAddressData(ArrayList<AddressModel> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                switch (list.get(i).AddressType) {
                    case AddAddressActivity.ADDRESS_HOME:
                        String locationTextH=" ";
                        String cityTextH=" ";
                        if (list.get(i).Location.contains(" ")) {
                            locationTextH = list.get(i).Location.substring(0, list.get(i).Location.lastIndexOf(" "));
                            cityTextH = list.get(i).Location.substring(list.get(i).Location.lastIndexOf(" ") + 1);

                        }else
                        {
                            locationTextH=list.get(i).Location;

                        }
                        editAddressHome.setText(list.get(i).HouseNameFlatNo+","+locationTextH+","+list.get(i).Landmark+","+cityTextH);
                        break;
                    case AddAddressActivity.ADDRESS_WORK:
                        String locationTextW=" ";
                        String cityTextW=" ";
                        if (list.get(i).Location.contains(" ")) {
                            locationTextW = list.get(i).Location.substring(0, list.get(i).Location.lastIndexOf(" "));
                            cityTextW = list.get(i).Location.substring(list.get(i).Location.lastIndexOf(" ") + 1);

                        }else
                        {
                            locationTextW=list.get(i).Location;

                        }
                        editAddressWork.setText(list.get(i).HouseNameFlatNo+","+locationTextW+","+list.get(i).Landmark+","+cityTextW);
                        break;
                    case AddAddressActivity.ADDRESS_OTHER:
                        String locationTextO=" ";
                        String cityTextO=" ";
                        if (list.get(i).Location.contains(" ")) {
                            locationTextO = list.get(i).Location.substring(0, list.get(i).Location.lastIndexOf(" "));
                            cityTextO = list.get(i).Location.substring(list.get(i).Location.lastIndexOf(" ") + 1);

                        }else
                        {
                            locationTextO=list.get(i).Location;

                        }
                        editAddressOther.setText(list.get(i).HouseNameFlatNo+","+locationTextO+","+list.get(i).Landmark+","+cityTextO);
                        break;
                }
            }
        }
    }

    @Override
    public void guestUpdated(GuestUpdateResponseModel model) {
        if (model.Success)
            updateUser(guestUpdateRequestModel.Guest);
    }

    @Override
    public void updateFailed() {
        showToastMessage("Update failed. Please try again later.");
    }

    private void updateUser(GuestModel guestUpdateModel) {
        GuestModel model = EnrichUtils.getUserData(this);

        model.Id = Objects.requireNonNull(EnrichUtils.getUserData(this)).Id;
        model.CenterId = Objects.requireNonNull(EnrichUtils.getUserData(this)).CenterId;
        model.UserName = Objects.requireNonNull(EnrichUtils.getUserData(this)).UserName;
        model.Password = Objects.requireNonNull(EnrichUtils.getUserData(this)).Password;

        model.FirstName = guestUpdateModel.FirstName;
        model.LastName = guestUpdateModel.LastName;
        model.Email = guestUpdateModel.Email;
        model.MobileNumber = guestUpdateModel.MobileNumber;
        model.Gender = guestUpdateModel.Gender;

        EnrichUtils.saveUserData(this, model);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ADDRESS) {
            if (resultCode == Activity.RESULT_OK) {
                GuestModel guestModel = data.getParcelableExtra("GuestData");
                setAddressData(guestModel.GuestAddress);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
