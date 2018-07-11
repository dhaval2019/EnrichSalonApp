package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

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

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity implements RegisterContract.View {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        phoneNumberStr = getIntent().getStringExtra("PhoneNumber");
        model = getIntent().getParcelableExtra("GuestData");

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Light.otf");
        maleRadioBtn.setTypeface(tf);
        femaleRadioBtn.setTypeface(tf);

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this, mainUiThread, threadExecutor, null);

        registerPresenter = new RegisterPresenter(this, dataRepository);

        if (phoneNumberStr == null) {
            firstNameEdit.setText(model.FirstName);
            lastNameEdit.setText(model.LastName);
            emailEdit.setText(model.Email);
            phoneNumberEdit.setText("" + model.MobileNumber);
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
                    gender = 1;
                } else if (femaleRadioBtn.isChecked()) {
                    gender = 2;
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
            startActivity(intent);
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
