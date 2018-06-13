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
import com.enrich.salonapp.data.model.CreateOTPRequestModel;
import com.enrich.salonapp.data.model.CreateOTPResponseModel;
import com.enrich.salonapp.data.model.RegistrationRequestModel;
import com.enrich.salonapp.data.model.RegistrationResponseModel;
import com.enrich.salonapp.data.model.VerifyOTPRequestModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.contracts.AuthenticationTokenContract;
import com.enrich.salonapp.ui.contracts.OTPContract;
import com.enrich.salonapp.ui.presenters.AuthenticationTokenPresenter;
import com.enrich.salonapp.ui.presenters.OTPPresenter;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OTPActivity extends BaseActivity implements OTPContract.RegisterView, AuthenticationTokenContract.View {

    @BindView(R.id.otp_container)
    LinearLayout otpContainer;

    @BindView(R.id.phone_number_container)
    LinearLayout phoneNumberContainer;

    @BindView(R.id.send_otp_btn)
    Button sendOtpBtn;

    @BindView(R.id.verify_otp_btn)
    Button verifyOtpBtn;

    @BindView(R.id.phone_number_edit)
    EditText phoneNumberEdit;

    @BindView(R.id.otp_number_edit)
    EditText otpNumberEdit;

    CreateOTPResponseModel createOTPResponseModel;
    CreateOTPRequestModel createOTPRequestModel;
    RegistrationRequestModel registrationRequestModel;

    OTPPresenter otpPresenter;
    AuthenticationTokenPresenter authenticationTokenPresenter;
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
            EnrichUtils.saveUserData(OTPActivity.this, registrationRequestModel.Guest);

            AuthenticationRequestModel authenticationRequestModel = new AuthenticationRequestModel();
            authenticationRequestModel.username = registrationRequestModel.Guest.UserName;
            authenticationRequestModel.password = registrationRequestModel.Guest.Password;
            authenticationTokenPresenter.getAuthenticationToken(OTPActivity.this, authenticationRequestModel, true);
        }
    }

    @Override
    public void saveAuthenticationToken(AuthenticationModel model) {
        if (model.accessToken != null) {
            ((EnrichApplication)getApplicationContext()).setAuthenticationModel(model);
//            EnrichUtils.saveAuthenticationModel(OTPActivity.this, model);
        }
    }
}
