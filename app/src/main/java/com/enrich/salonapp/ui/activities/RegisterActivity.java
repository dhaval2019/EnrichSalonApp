package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.CheckUserNameResponseModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOTPRequestModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOTPResponseModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.data.model.PhoneModel;
import com.enrich.salonapp.data.model.RegistrationRequestModel;
import com.enrich.salonapp.data.model.SignIn.SignInGuestModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.contracts.RegisterContract;
import com.enrich.salonapp.ui.presenters.RegisterPresenter;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.segment.analytics.Analytics;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity implements RegisterContract.View {

    public static final int VERIFY_OTP = 6334;

    @BindView(R.id.first_name_edit)
    EditText firstNameEdit;

    @BindView(R.id.last_name_edit)
    EditText lastNameEdit;

    @BindView(R.id.email_edit)
    EditText emailEdit;

    @BindView(R.id.phone_number_edit)
    EditText phoneNumberEdit;

    @BindView(R.id.username_edit)
    EditText userNameEdit;

    @BindView(R.id.password_edit)
    EditText passwordEdit;

    @BindView(R.id.male_radio_btn)
    RadioButton maleRadioBtn;

    @BindView(R.id.female_radio_btn)
    RadioButton femaleRadioBtn;

    @BindView(R.id.sign_up_btn)
    Button signUpBtn;

    boolean isValidUserName;

    String phoneNumberStr;

    CreateOTPRequestModel createOTPRequestModel;
    RegistrationRequestModel registrationRequestModel;

    RegisterPresenter registerPresenter;

    DataRepository dataRepository;

    SignInGuestModel model;

    EnrichApplication application;
    Tracker mTracker;

    boolean isFromLoginLater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        application = (EnrichApplication) getApplication();

        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Registration");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);

        ButterKnife.bind(this);

        phoneNumberStr = getIntent().getStringExtra("PhoneNumber");
        model = getIntent().getParcelableExtra("GuestData");
        isFromLoginLater = getIntent().getBooleanExtra("IsFromLoginLater", false);
//        registrationRequestModel = getIntent().getParcelableExtra("RegisterModel");

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Light.otf");
        maleRadioBtn.setTypeface(tf);
        femaleRadioBtn.setTypeface(tf);

        dataRepository = Injection.provideDataRepository(this, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);

        registerPresenter = new RegisterPresenter(this, dataRepository);

//        if (registrationRequestModel != null) {
//            firstNameEdit.setText(registrationRequestModel.Guest.FirstName);
//            lastNameEdit.setText(registrationRequestModel.Guest.LastName);
//            emailEdit.setText(registrationRequestModel.Guest.Email);
//            phoneNumberEdit.setText(registrationRequestModel.Guest.MobileNumber);
//            userNameEdit.setText(registrationRequestModel.Guest.Email);
//            passwordEdit.setText(registrationRequestModel.Guest.Password);
//            if (registrationRequestModel.Guest.Gender == Constants.MALE) {
//                maleRadioBtn.setChecked(true);
//            } else if (registrationRequestModel.Guest.Gender == Constants.FEMALE) {
//                femaleRadioBtn.setChecked(true);
//            }
//        }

        if (phoneNumberStr == null) {
            firstNameEdit.setText(model.FirstName);
            lastNameEdit.setText(model.LastName);
            emailEdit.setText(model.Email);
            phoneNumberEdit.setText(String.format("%s", model.MobileNumber));
            phoneNumberEdit.setEnabled(false);
        } else {
            phoneNumberEdit.setText(phoneNumberStr);
            phoneNumberEdit.setEnabled(false);
        }

        userNameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!userNameEdit.getText().toString().isEmpty())
                        registerPresenter.checkUsername(RegisterActivity.this, userNameEdit.getText().toString());
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstNameStr = firstNameEdit.getText().toString();
                String lastNameStr = lastNameEdit.getText().toString();
                String emailStr = emailEdit.getText().toString();
                String phoneNumberStr = phoneNumberEdit.getText().toString();
                String userNameStr = userNameEdit.getText().toString();
                String passwordStr = passwordEdit.getText().toString();
                int gender = 0;

                if (maleRadioBtn.isChecked()) {
                    gender = Constants.MALE;
                } else if (femaleRadioBtn.isChecked()) {
                    gender = Constants.FEMALE;
                }

                if (!EnrichUtils.isValidEmailId(emailStr)) {
                    EnrichUtils.showMessage(RegisterActivity.this, "Please enter a valid email address.");
                    return;
                }

                if (firstNameStr.isEmpty() || lastNameStr.isEmpty() || emailStr.isEmpty() || phoneNumberStr.isEmpty() || gender == 0 || userNameStr.isEmpty() || passwordStr.isEmpty()) {
                    EnrichUtils.showMessage(RegisterActivity.this, "All fields are mandatory.");
                    return;
                }

                if (!isValidUserName) {
                    EnrichUtils.showMessage(RegisterActivity.this, "Username already exists.");
                    return;
                }

                registrationRequestModel = new RegistrationRequestModel();
                registrationRequestModel.CenterId = Constants.CENTER_ID;
                registrationRequestModel.Guest = new SignInGuestModel();
                registrationRequestModel.Guest.CenterId = Constants.CENTER_ID;
                registrationRequestModel.Guest.CreationDate = new Date().toString();
                registrationRequestModel.Guest.FirstName = firstNameStr;
                registrationRequestModel.Guest.LastName = lastNameStr;
                registrationRequestModel.Guest.Email = emailStr;
                registrationRequestModel.Guest.MobileNumber = phoneNumberStr;
                registrationRequestModel.Guest.Gender = gender;
                registrationRequestModel.Guest.Password = passwordStr;
                registrationRequestModel.Guest.UserName = userNameStr;

                createOTPRequestModel = new CreateOTPRequestModel();
                createOTPRequestModel.CenterId = Constants.CENTER_ID;
                createOTPRequestModel.Phone = new PhoneModel();
                createOTPRequestModel.Phone.CountryId = 1;
                createOTPRequestModel.Phone.Number = phoneNumberStr;
                createOTPRequestModel.Phone.DisplayNumber = phoneNumberStr;

                EnrichUtils.log(EnrichUtils.newGson().toJson(registrationRequestModel));
                EnrichUtils.log(EnrichUtils.newGson().toJson(createOTPRequestModel));

                registerPresenter.createOtp(RegisterActivity.this, createOTPRequestModel);
            }
        });
    }

    @Override
    public void otpSent(CreateOTPResponseModel model) {
        if (model.Error == null) {
            Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
            intent.putExtra("RegisterModel", registrationRequestModel);
            intent.putExtra("OTPResponseModel", model);
            intent.putExtra("OTPRequestModel", createOTPRequestModel);
            intent.putExtra("IsFromLoginLater", isFromLoginLater);
            startActivityForResult(intent, VERIFY_OTP);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VERIFY_OTP) {
            if (resultCode == RESULT_OK) {
                if (!isFromLoginLater) {
                    if (EnrichUtils.getHomeStore(this) != null) {
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(RegisterActivity.this, StoreSelectorActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    finish();
                }
            }
        }
    }

    @Override
    public void isValidUserName(CheckUserNameResponseModel model) {
        if (model.NumOfMatches == 0) {
            isValidUserName = true;
        } else {
            userNameEdit.setError("Username already exists.");
            isValidUserName = false;
        }
    }
}
