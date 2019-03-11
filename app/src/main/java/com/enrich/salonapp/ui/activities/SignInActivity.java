package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.AuthenticationModel;
import com.enrich.salonapp.data.model.AuthenticationRequestModel;
import com.enrich.salonapp.data.model.ForgotPasswordRequestModel;
import com.enrich.salonapp.data.model.ForgotPasswordResponseModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.data.model.RegisterFCMRequestModel;
import com.enrich.salonapp.data.model.RegisterFCMResponseModel;
import com.enrich.salonapp.data.model.SignIn.IsUserRegisteredResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.contracts.AuthenticationTokenContract;
import com.enrich.salonapp.ui.contracts.ForgotPasswordContract;
import com.enrich.salonapp.ui.contracts.GuestsContact;
import com.enrich.salonapp.ui.contracts.RegisterFCMContract;
import com.enrich.salonapp.ui.contracts.SignInContract;
import com.enrich.salonapp.ui.presenters.AuthenticationTokenPresenter;
import com.enrich.salonapp.ui.presenters.ForgotPasswordPresenter;
import com.enrich.salonapp.ui.presenters.GuestPresenter;
import com.enrich.salonapp.ui.presenters.RegisterFCMPresenter;
import com.enrich.salonapp.ui.presenters.SignInPresenter;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.segment.analytics.Analytics;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends BaseActivity implements SignInContract.View, AuthenticationTokenContract.View, ForgotPasswordContract.View, GuestsContact.View, RegisterFCMContract.View {

    @BindView(R.id.user_name_edit)
    EditText userNameEdit;

    @BindView(R.id.password_edit)
    EditText passwordEdit;

    @BindView(R.id.sign_in_btn)
    Button signInBtn;

    @BindView(R.id.forgot_password_btn)
    TextView forgotPasswordBtn;

    @BindView(R.id.password_container)
    LinearLayout passwordContainer;

    @BindView(R.id.progress_dialog)
    LinearLayout progressDialog;

    BottomSheetDialog dialog;

    SignInPresenter signInPresenter;
    GuestPresenter guestPresenter;
    ForgotPasswordPresenter forgotPasswordPresenter;
    AuthenticationTokenPresenter authenticationTokenPresenter;
    RegisterFCMPresenter registerFCMPresenter;

    DataRepository dataRepository;

    EnrichApplication application;
    Tracker mTracker;

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        application = (EnrichApplication) getApplication();

        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Login Activity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        EnrichUtils.changeStatusBarColor(getWindow());

        // INIT DataRepository
        dataRepository = Injection.provideDataRepository(this, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);

        signInPresenter = new SignInPresenter(this, dataRepository);
        forgotPasswordPresenter = new ForgotPasswordPresenter(this, dataRepository);
        authenticationTokenPresenter = new AuthenticationTokenPresenter(this, dataRepository);
        guestPresenter = new GuestPresenter(this, dataRepository);
        registerFCMPresenter = new RegisterFCMPresenter(this, dataRepository);

        userNameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    signInPresenter.isUserRegistered(SignInActivity.this, userNameEdit.getText().toString());
                }
                return false;
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userNameStr = userNameEdit.getText().toString();
                String passwordStr = passwordEdit.getText().toString();

                if (userNameStr.isEmpty() || passwordStr.isEmpty()) {
                    EnrichUtils.showMessage(SignInActivity.this, "Please enter all the fields");
                    return;
                }

                setProgressBar(true);
                AuthenticationRequestModel model = new AuthenticationRequestModel();
                model.username = userNameStr;
                model.password = passwordStr;

                authenticationTokenPresenter.getAuthenticationToken(SignInActivity.this, model, true);
            }
        });

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = userNameEdit.getText().toString();

                if (phoneNumber.isEmpty()) {
                    EnrichUtils.showMessage(SignInActivity.this, "Please enter your phone number");
                } else {
                    ForgotPasswordRequestModel forgotPasswordRequestModel = new ForgotPasswordRequestModel();
                    forgotPasswordRequestModel.UserName = "" + phoneNumber;

                    forgotPasswordPresenter.forgotPassword(SignInActivity.this, forgotPasswordRequestModel);
                }
            }
        });
    }

    @Override
    public void showPasswordField(IsUserRegisteredResponseModel model) {
        Crashlytics.setString("PhoneNumber", userNameEdit.getText().toString());
        if (model.Error.StatusCode == 200) {
            passwordContainer.setVisibility(View.VISIBLE);
        } else if (model.Error.StatusCode == 403) {
            EnrichUtils.showMessage(SignInActivity.this, "Invalid Number");
        } else if (model.Error.StatusCode == 404) {
//             Redirect to Registration screen
            Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
            intent.putExtra("PhoneNumber", userNameEdit.getText().toString());
            startActivity(intent);
            finish();
        } else if (model.Error.StatusCode == 410) {// NO EMAIL
            passwordContainer.setVisibility(View.VISIBLE);
        } else if (model.Error.StatusCode == 409) {// NO USERNAME
            Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
            intent.putExtra("GuestData", model.Guest);
            startActivity(intent);
        }
    }

    @Override
    public void createTokenError() {
        progressDialog.setVisibility(View.GONE);
        showToastMessage("Invalid Credentials. Please try again or Sign up.");
    }

    @Override
    public void saveUserDetails(final GuestModel model) {
        model.UserName = userNameEdit.getText().toString();
        model.Password = passwordEdit.getText().toString();

        EnrichUtils.saveUserData(SignInActivity.this, model);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SignInActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                RegisterFCMRequestModel fcmModel = new RegisterFCMRequestModel();
                fcmModel.GuestId = model.Id;
                fcmModel.Platform = Constants.PLATFORM_ANDROID;
                fcmModel.Token = instanceIdResult.getToken();

                registerFCMPresenter.registerFCM(SignInActivity.this, fcmModel);
            }
        });

        Intent intent = new Intent(SignInActivity.this, StoreSelectorActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void dataNotFound() {
        progressDialog.setVisibility(View.GONE);
    }

    @Override
    public void passwordSent(ForgotPasswordResponseModel model) {
        if (model.Success) {
            showForgotPasswordDialog();
        } else {
            EnrichUtils.showMessage(SignInActivity.this, "" + model.Error.Message);
        }
    }

    @Override
    public void saveAuthenticationToken(AuthenticationModel model) {
        if (model.accessToken != null) {
            EnrichUtils.saveAuthenticationModel(this, model);
//            ((EnrichApplication) getApplicationContext()).setAuthenticationModel(model);
            progressDialog.setVisibility(View.VISIBLE);
            guestPresenter.getUserData(SignInActivity.this, model.userId, true);
        } else {
            progressDialog.setVisibility(View.GONE);
            showToastMessage("Invalid Credentials");
        }
    }

    private void showForgotPasswordDialog() {
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.forgot_password_success);

        dialog.setCancelable(true);

        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Forgot Password");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        TextView openMailApp = dialog.findViewById(R.id.open_mail_app);

        openMailApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mailClient = new Intent(Intent.ACTION_VIEW);
//                mailClient.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivityGmail");
                mailClient.setType("message/rfc822");
                startActivity(mailClient);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void FCMRegistered(RegisterFCMResponseModel model) {
        if (model.Error != null)
            EnrichUtils.log("FCM REGISTER: " + model.Error.StatusCode);
    }
}
