package com.enrich.salonapp.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.enrich.salonapp.ui.activities.RegisterActivity;
import com.enrich.salonapp.ui.activities.SignInActivity;
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
import com.enrich.salonapp.util.LoginListener;
import com.enrich.salonapp.util.mvp.BaseBottomSheetDialogFragment;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginBottomSheetFragment extends BaseBottomSheetDialogFragment implements SignInContract.View, AuthenticationTokenContract.View, GuestsContact.View, RegisterFCMContract.View, ForgotPasswordContract.View {

    @BindView(R.id.user_name_edit)
    EditText userNameEdit;

    @BindView(R.id.password_edit)
    EditText passwordEdit;

    @BindView(R.id.password_edit_container)
    TextInputLayout passwordEditContainer;

    @BindView(R.id.should_login_cancel)
    TextView shouldLoginCancel;

    @BindView(R.id.should_login_ok)
    TextView shouldLoginOk;

    @BindView(R.id.password_container)
    LinearLayout passwordContainer;

    @BindView(R.id.forgot_password_btn)
    TextView forgotPasswordBtn;

    SignInPresenter signInPresenter;
    AuthenticationTokenPresenter authenticationTokenPresenter;
    GuestPresenter guestPresenter;
    RegisterFCMPresenter registerFCMPresenter;
    ForgotPasswordPresenter forgotPasswordPresenter;

    DataRepository dataRepository;

    EnrichApplication application;
    Tracker mTracker;
    boolean isUserNameAccepted = false;
    Context context;

    static LoginListener loginListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (EnrichApplication) getActivity().getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Login Dialog");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);
    }

    public static LoginBottomSheetFragment getInstance() {
        return new LoginBottomSheetFragment();
    }

    public static LoginBottomSheetFragment getInstance(LoginListener login) {
        loginListener = login;
        return new LoginBottomSheetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_bottom_dialog, container, false);
        ButterKnife.bind(this, view);
        isUserNameAccepted = false;
        dataRepository = Injection.provideDataRepository(context, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);

        signInPresenter = new SignInPresenter(this, dataRepository);
        authenticationTokenPresenter = new AuthenticationTokenPresenter(this, dataRepository);
        guestPresenter = new GuestPresenter(this, dataRepository);
        registerFCMPresenter = new RegisterFCMPresenter(this, dataRepository);
        forgotPasswordPresenter = new ForgotPasswordPresenter(this, dataRepository);

        userNameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    isUserNameAccepted = true;
                    signInPresenter.isUserRegistered(context, userNameEdit.getText().toString());
                }
                return false;
            }
        });

        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String userNameStr = userNameEdit.getText().toString();
                    String passwordStr = passwordEdit.getText().toString();

                    if (userNameStr.isEmpty() || passwordStr.isEmpty()) {
                        EnrichUtils.showMessage(context, "Please enter all the fields");
                        return false;
                    }

                    setProgressBar(true);
                    AuthenticationRequestModel model = new AuthenticationRequestModel();
                    model.username = userNameStr;
                    model.password = passwordStr;

                    authenticationTokenPresenter.getAuthenticationToken(context, model, true);
                }
                return false;
            }
        });

        shouldLoginOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userNameStr = userNameEdit.getText().toString();
                String passwordStr = passwordEdit.getText().toString();
                if (!isUserNameAccepted) {
                    if ((!userNameStr.isEmpty()) && passwordStr.isEmpty()) {

                        signInPresenter.isUserRegistered(context, userNameEdit.getText().toString());
                        return;
                    }
                } else if (userNameStr.isEmpty() || passwordStr.isEmpty()) {
                    EnrichUtils.showMessage(context, "Please enter all the fields");
                    return;
                }

                setProgressBar(true);
                AuthenticationRequestModel model = new AuthenticationRequestModel();
                model.username = userNameStr;
                model.password = passwordStr;

                authenticationTokenPresenter.getAuthenticationToken(context, model, true);
            }
        });

        shouldLoginCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginBottomSheetFragment.this.dismiss();
            }
        });

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = userNameEdit.getText().toString();

                if (phoneNumber.isEmpty()) {
                    EnrichUtils.showMessage(context, "Please enter your phone number");
                } else {
                    ForgotPasswordRequestModel forgotPasswordRequestModel = new ForgotPasswordRequestModel();
                    forgotPasswordRequestModel.UserName = "" + phoneNumber;

                    forgotPasswordPresenter.forgotPassword(context, forgotPasswordRequestModel);
                }
            }
        });

        return view;
    }

    @Override
    public void showPasswordField(IsUserRegisteredResponseModel model) {

        Crashlytics.setString("PhoneNumber", userNameEdit.getText().toString());
        if (model != null) {

            if (model.Error != null) {


                if (model.Error.StatusCode == 200) {
                    passwordEditContainer.setVisibility(View.VISIBLE);
                    passwordContainer.setVisibility(View.VISIBLE);
                } else if (model.Error.StatusCode == 403) {
                    EnrichUtils.showMessage(context, "Invalid Number");
                } else if (model.Error.StatusCode == 404) {
//             Redirect to Registration screen
                    if (context != null) {
                        isUserNameAccepted = false;
                        Intent intent = new Intent(context, RegisterActivity.class);
                        intent.putExtra("PhoneNumber", userNameEdit.getText().toString());
                        intent.putExtra("IsFromLoginLater", true);
                        startActivity(intent);
                    }
                } else if (model.Error.StatusCode == 410) {// NO EMAIL
                    passwordEditContainer.setVisibility(View.VISIBLE);
                    passwordContainer.setVisibility(View.VISIBLE);
                } else if (model.Error.StatusCode == 409) {// NO USERNAME
                    if (context != null) {
                        isUserNameAccepted = false;
                        Intent intent = new Intent(context, RegisterActivity.class);

                        intent.putExtra("GuestData", model.Guest);

                        startActivity(intent);
                    }

                }
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (EnrichUtils.getUserData(context) != null) {
            Toast.makeText(getContext(), "Welcome " + EnrichUtils.getUserData(context).FirstName + "!", Toast.LENGTH_SHORT).show();
            LoginBottomSheetFragment.this.dismiss();
        }
    }

    @Override
    public void saveAuthenticationToken(AuthenticationModel model) {
        if (model.accessToken != null) {
            EnrichUtils.saveAuthenticationModel(context, model);
            guestPresenter.getUserData(context, model.userId, true);
        } else {
            showToastMessage(context, "Invalid Credentials");
        }
    }

    @Override
    public void createTokenError() {
        Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveUserDetails(final GuestModel model) {
        if (model != null) {
            model.UserName = userNameEdit.getText().toString();
            model.Password = passwordEdit.getText().toString();

            EnrichUtils.saveUserData(context, model);
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) context, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    RegisterFCMRequestModel fcmModel = new RegisterFCMRequestModel();
                    fcmModel.GuestId = model.Id;
                    fcmModel.Platform = Constants.PLATFORM_ANDROID;
                    fcmModel.Token = instanceIdResult.getToken();

                    registerFCMPresenter.registerFCM(context, fcmModel);
                }
            });
            // try {
            Toast.makeText(context, "Welcome " + model.FirstName + "!", Toast.LENGTH_SHORT).show();
       /* } catch (Exception e) {

        }*/
            Analytics.with(context).track(Constants.SEGMENT_LOGIN, new Properties()
                    .putValue("user_id", model.Id)
                    .putValue("first_name", model.FirstName)
                    .putValue("last_name", model.LastName)
                    .putValue("email", model.Email)
                    .putValue("mobile", model.MobileNumber));

            LoginBottomSheetFragment.this.dismiss();
        }
    }

    @Override
    public void dataNotFound() {

    }

    @Override
    public void FCMRegistered(RegisterFCMResponseModel model) {
        if (model.Error != null)
            EnrichUtils.log("FCM REGISTER: " + model.Error.StatusCode);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (loginListener != null)
            loginListener.onLoginSuccessful();
    }

    @Override
    public void passwordSent(ForgotPasswordResponseModel model) {
        if (model.Success) {
            showToastMessage(context, "Password sent to SMS and Email");
        } else {
            showToastMessage(context, "" + model.Error.Message);
        }
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
