package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.AuthenticationModel;
import com.enrich.salonapp.data.model.AuthenticationRequestModel;
import com.enrich.salonapp.data.model.ForgotPasswordRequestModel;
import com.enrich.salonapp.data.model.ForgotPasswordResponseModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.contracts.AuthenticationTokenContract;
import com.enrich.salonapp.ui.contracts.SignInContract;
import com.enrich.salonapp.ui.presenters.AuthenticationTokenPresenter;
import com.enrich.salonapp.ui.presenters.SignInPresenter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends BaseActivity implements SignInContract.View, AuthenticationTokenContract.View {

    @BindView(R.id.user_name_edit)
    EditText userNameEdit;

    @BindView(R.id.password_edit)
    EditText passwordEdit;

    @BindView(R.id.sign_in_btn)
    Button signInBtn;

    @BindView(R.id.login_sign_up_btn)
    TextView signUpBtn;

    @BindView(R.id.forgot_password_btn)
    TextView forgotPasswordBtn;

    @BindView(R.id.password_container)
    LinearLayout passwordContainer;

    SignInPresenter signInPresenter;
    AuthenticationTokenPresenter authenticationTokenPresenter;

    DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        EnrichUtils.changeStatusBarColor(getWindow());

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        forgotPasswordBtn.setTypeface(custom_font);

        // INIT DataRepository
        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this, mainUiThread, threadExecutor, null);

        signInPresenter = new SignInPresenter(this, dataRepository);
        authenticationTokenPresenter = new AuthenticationTokenPresenter(this, dataRepository);

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
                setProgressBar(true);

                String userNameStr = userNameEdit.getText().toString();
                String passwordStr = passwordEdit.getText().toString();

                if (userNameStr.isEmpty() || passwordStr.isEmpty()) {
                    EnrichUtils.showMessage(SignInActivity.this, "Please enter all the fields");
                    return;
                }

                AuthenticationRequestModel model = new AuthenticationRequestModel();
                model.username = userNameStr;
                model.password = passwordStr;

                authenticationTokenPresenter.getAuthenticationToken(SignInActivity.this, model, false);
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

                    signInPresenter.forgotPassword(SignInActivity.this, forgotPasswordRequestModel);
                }
            }
        });
    }

    @Override
    public void showPasswordField(int responseCode) {
        if (responseCode == 200) {
            passwordContainer.setVisibility(View.VISIBLE);
        } else if (responseCode == 403) {
            EnrichUtils.showMessage(SignInActivity.this, "Invalid Number");
        } else if (responseCode == 404) {
//             Redirect to Registration screen
            Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
            intent.putExtra("PhoneNumber", userNameEdit.getText().toString());
            startActivity(intent);
        }
    }

    @Override
    public void saveUserDetails(GuestModel model) {
        model.UserName = userNameEdit.getText().toString();
        model.Password = passwordEdit.getText().toString();

        EnrichUtils.saveUserData(SignInActivity.this, model);
        setProgressBar(false);

        Intent intent = new Intent(SignInActivity.this, StoreSelectorActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void passwordSent(ForgotPasswordResponseModel model) {
        if (model.Success) {
            EnrichUtils.showLongMessage(SignInActivity.this, "We have sent you an Email and SMS with your new password. Please login with the new password.");
        }
    }

    @Override
    public void saveAuthenticationToken(AuthenticationModel model) {
        if (model != null) {
            ((EnrichApplication) getApplicationContext()).setAuthenticationModel(model);
//            EnrichUtils.saveAuthenticationModel(this, model);
            setProgressBar(true);
            signInPresenter.getUserData(SignInActivity.this, model.userId);

        }
    }
}
