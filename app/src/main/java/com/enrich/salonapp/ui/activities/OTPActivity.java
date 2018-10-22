package com.enrich.salonapp.ui.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.AuthenticationModel;
import com.enrich.salonapp.data.model.AuthenticationRequestModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOTPRequestModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOTPResponseModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.data.model.RegisterFCMRequestModel;
import com.enrich.salonapp.data.model.RegisterFCMResponseModel;
import com.enrich.salonapp.data.model.RegistrationRequestModel;
import com.enrich.salonapp.data.model.RegistrationResponseModel;
import com.enrich.salonapp.data.model.VerifyOTPRequestModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.contracts.AuthenticationTokenContract;
import com.enrich.salonapp.ui.contracts.OTPContract;
import com.enrich.salonapp.ui.contracts.RegisterFCMContract;
import com.enrich.salonapp.ui.presenters.AuthenticationTokenPresenter;
import com.enrich.salonapp.ui.presenters.OTPPresenter;
import com.enrich.salonapp.ui.presenters.RegisterFCMPresenter;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OTPActivity extends BaseActivity implements OTPContract.RegisterView, AuthenticationTokenContract.View, RegisterFCMContract.View {

    @BindView(R.id.otp_container)
    LinearLayout otpContainer;

    @BindView(R.id.verify_otp_btn)
    Button verifyOtpBtn;

    @BindView(R.id.otp_number_edit)
    EditText otpNumberEdit;

    CreateOTPResponseModel createOTPResponseModel;
    CreateOTPRequestModel createOTPRequestModel;
    RegistrationRequestModel registrationRequestModel;

    OTPPresenter otpPresenter;
    AuthenticationTokenPresenter authenticationTokenPresenter;
    RegisterFCMPresenter registerFCMPresenter;
    DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        EnrichUtils.changeStatusBarColor(getWindow());

        registrationRequestModel = getIntent().getParcelableExtra("RegisterModel");
        createOTPResponseModel = getIntent().getParcelableExtra("OTPResponseModel");
        createOTPRequestModel = getIntent().getParcelableExtra("OTPRequestModel");

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this, mainUiThread, threadExecutor, null);

        otpPresenter = new OTPPresenter(this, dataRepository);
        authenticationTokenPresenter = new AuthenticationTokenPresenter(this, dataRepository);
        registerFCMPresenter = new RegisterFCMPresenter(this, dataRepository);

        verifyOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyOTPRequestModel model = new VerifyOTPRequestModel();
                model.CenterId = Constants.CENTER_ID;
                model.OTP = otpNumberEdit.getText().toString();
                model.VerificationId = createOTPResponseModel.VerificationId;

                registrationRequestModel.OTP = model.OTP;
                registrationRequestModel.VerificationId = model.VerificationId;

                otpPresenter.registerUser(OTPActivity.this, registrationRequestModel);
            }
        });
    }

    @Override
    public void userRegistered(RegistrationResponseModel model) {
        if (model.Success) {
            GuestModel guestModel = new GuestModel();
            guestModel.CenterId = registrationRequestModel.Guest.CenterId;
            guestModel.Id = registrationRequestModel.Guest.Id;
            guestModel.CreationDate = registrationRequestModel.Guest.CreationDate;
            guestModel.Email = registrationRequestModel.Guest.Email;
            guestModel.FirstName = registrationRequestModel.Guest.FirstName;
            guestModel.LastName = registrationRequestModel.Guest.LastName;
            guestModel.UserName = registrationRequestModel.Guest.UserName;
            guestModel.MobileNumber = registrationRequestModel.Guest.MobileNumber;
            guestModel.Password = registrationRequestModel.Guest.Password;
            guestModel.Gender = registrationRequestModel.Guest.Gender;

            EnrichUtils.saveUserData(OTPActivity.this, guestModel);

            AuthenticationRequestModel authenticationRequestModel = new AuthenticationRequestModel();
            authenticationRequestModel.username = registrationRequestModel.Guest.UserName;
            authenticationRequestModel.password = registrationRequestModel.Guest.Password;
            authenticationTokenPresenter.getAuthenticationToken(OTPActivity.this, authenticationRequestModel, true);

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(OTPActivity.this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    RegisterFCMRequestModel fcmModel = new RegisterFCMRequestModel();
                    fcmModel.GuestId = registrationRequestModel.Guest.Id;
                    fcmModel.Platform = Constants.PLATFORM_ANDROID;
                    fcmModel.Token = instanceIdResult.getToken();

                    registerFCMPresenter.registerFCM(OTPActivity.this, fcmModel);
                }
            });
        }
    }

    @Override
    public void registerFailed() {
        showToastMessage("Something went wrong. Please try again later.");
    }

    @Override
    public void saveAuthenticationToken(AuthenticationModel model) {
        if (model.accessToken != null) {
            ((EnrichApplication) getApplicationContext()).setAuthenticationModel(model);
        }
    }

    @Override
    public void createTokenError() {

    }

    @Override
    public void FCMRegistered(RegisterFCMResponseModel model) {
        if (model.Error != null)
            EnrichUtils.log("FCM REGISTER: " + model.Error.StatusCode);
    }
}
