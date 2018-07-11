package com.enrich.salonapp.ui.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.data.model.GuestUpdateModel;
import com.enrich.salonapp.data.model.GuestUpdateRequestModel;
import com.enrich.salonapp.data.model.GuestUpdateResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.contracts.UpdateGuestContract;
import com.enrich.salonapp.ui.presenters.UpdateGuestPresenter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    String firstNameStr, lastNameStr, emailStr, phoneStr, genderStr;

    GuestUpdateRequestModel guestUpdateRequestModel;

    UpdateGuestPresenter updateGuestPresenter;
    DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

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

        GuestModel model = EnrichUtils.getUserData(this);
        firstName.setText(model.FirstName);
        lastName.setText(model.LastName);
        email.setText(model.Email);
        phone.setText(model.MobileNumber);

        if (model.Gender.equals("Male")) {
            editGenderMale.setChecked(true);
            editGenderFemale.setChecked(false);
        } else {
            editGenderMale.setChecked(false);
            editGenderFemale.setChecked(true);
        }

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

                if (editGenderMale.isChecked()) {
                    genderStr = "Male";
                } else if (editGenderFemale.isChecked()) {
                    genderStr = "Female";
                }

                GuestUpdateModel guestUpdateModel = new GuestUpdateModel();
                guestUpdateModel.Email = emailStr;
                guestUpdateModel.GuestFName = firstNameStr;
                guestUpdateModel.GuestLName = lastNameStr;
                guestUpdateModel.GuestPhone = phoneStr;
                guestUpdateModel.id = EnrichUtils.getUserData(EditProfileActivity.this).Id;
                if (editGenderMale.isChecked()) {
                    guestUpdateModel.Gender = "Male";
                } else if (editGenderFemale.isChecked()) {
                    guestUpdateModel.Gender = "Female";
                }

                guestUpdateRequestModel = new GuestUpdateRequestModel();
                guestUpdateRequestModel.CenterId = EnrichUtils.getUserData(EditProfileActivity.this).CenterId;
                guestUpdateRequestModel.Guest = guestUpdateModel;

                EnrichUtils.log(EnrichUtils.newGson().toJson(guestUpdateRequestModel));

                if (firstNameStr.isEmpty() || lastNameStr.isEmpty() || genderStr.isEmpty()) {
                    EnrichUtils.showMessage(EditProfileActivity.this, "All fields are mandatory.");
                    return;
                }

                updateGuestPresenter.updateGuest(EditProfileActivity.this, guestUpdateRequestModel);
            }
        });
    }

    @Override
    public void guestUpdated(GuestUpdateResponseModel model) {
        if (model.Success)
            updateUser(guestUpdateRequestModel.Guest);
    }

    private void updateUser(GuestUpdateModel guestUpdateModel) {
        GuestModel model = EnrichUtils.getUserData(this);

        model.Id = Objects.requireNonNull(EnrichUtils.getUserData(this)).Id;
        model.CenterId = Objects.requireNonNull(EnrichUtils.getUserData(this)).CenterId;
        model.UserName = Objects.requireNonNull(EnrichUtils.getUserData(this)).UserName;
        model.Password = Objects.requireNonNull(EnrichUtils.getUserData(this)).Password;

        model.FirstName = guestUpdateModel.GuestFName;
        model.LastName = guestUpdateModel.GuestLName;
        model.Email = guestUpdateModel.Email;
        model.MobileNumber = guestUpdateModel.GuestPhone;
        model.Gender = guestUpdateModel.Gender;

        EnrichUtils.saveUserData(this, model);
    }
}
