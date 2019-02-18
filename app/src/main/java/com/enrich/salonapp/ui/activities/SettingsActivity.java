package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.ChangePasswordRequestModel;
import com.enrich.salonapp.data.model.ChangePasswordResponseModel;
import com.enrich.salonapp.data.model.ForgotPasswordRequestModel;
import com.enrich.salonapp.data.model.ForgotPasswordResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.contracts.ForgotPasswordContract;
import com.enrich.salonapp.ui.contracts.SettingsContract;
import com.enrich.salonapp.ui.fragments.LoginBottomSheetFragment;
import com.enrich.salonapp.ui.presenters.ForgotPasswordPresenter;
import com.enrich.salonapp.ui.presenters.SettingsPresenter;
import com.enrich.salonapp.util.Animations;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.LoginListener;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity implements SettingsContract.View, ForgotPasswordContract.View, LoginListener {

    @BindView(R.id.drawer_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.change_password_edit_container)
    LinearLayout changePasswordEditContainer;

    @BindView(R.id.change_password_label_container)
    LinearLayout changePasswordLabelContainer;

    @BindView(R.id.change_password_more)
    ImageView changePasswordMore;

    @BindView(R.id.change_password_old_password)
    EditText changePasswordOldPassword;

    @BindView(R.id.change_password_new_password)
    EditText changePasswordNewPassword;

    @BindView(R.id.change_password_confirm_password)
    EditText changePasswordConfirmPassword;

    @BindView(R.id.change_password_cancel)
    Button changePasswordCancel;

    @BindView(R.id.change_password_save)
    Button changePasswordSave;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.forgot_password_click_here)
    TextView forgotPasswordClickHere;

    @BindView(R.id.forgot_password_more)
    ImageView forgotPasswordMore;

    @BindView(R.id.version_number)
    TextView versionNumber;

    @BindView(R.id.user_settings)
    LinearLayout userSettings;

    @BindView(R.id.sign_in_container)
    LinearLayout signInContainer;

    @BindView(R.id.settings_login_button)
    Button settingsLoginButton;

    String oldPasswordStr, newPasswordStr, confirmPasswordStr;

    SettingsPresenter changePassword;
    DataRepository dataRepository;
    ForgotPasswordPresenter forgotPasswordPresenter;

    BottomSheetDialog dialog;

    EnrichApplication application;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        application = (EnrichApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Settings Screen");
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

        collapsingToolbarLayout.setTitle("SETTINGS");

        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            versionNumber.setText(String.format("%s", info.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        dataRepository = Injection.provideDataRepository(this, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        changePassword = new SettingsPresenter(this, dataRepository);
        forgotPasswordPresenter = new ForgotPasswordPresenter(this, dataRepository);

        changePasswordMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changePasswordEditContainer.getVisibility() == View.VISIBLE) {
                    Animations.collapse(changePasswordEditContainer, 400);
                } else {
                    changePasswordOldPassword.setText("");
                    changePasswordNewPassword.setText("");
                    changePasswordConfirmPassword.setText("");
                    Animations.expand(changePasswordEditContainer, 400);
                }
            }
        });

        changePasswordCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changePasswordEditContainer.getVisibility() == View.VISIBLE) {
                    Animations.collapse(changePasswordEditContainer, 400);
                }
            }
        });

        changePasswordSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPasswordStr = changePasswordOldPassword.getText().toString();
                newPasswordStr = changePasswordNewPassword.getText().toString();
                confirmPasswordStr = changePasswordConfirmPassword.getText().toString();

                if (oldPasswordStr.isEmpty() || newPasswordStr.isEmpty() || confirmPasswordStr.isEmpty()) {
                    EnrichUtils.showMessage(SettingsActivity.this, "All fields are mandatory");
                    return;
                }

                if (!confirmPasswordStr.equals(newPasswordStr)) {
                    EnrichUtils.showMessage(SettingsActivity.this, "Passwords do not match");
                    return;
                }

                ChangePasswordRequestModel body = new ChangePasswordRequestModel();
                body.NewPassword = newPasswordStr;
                body.OldPassword = oldPasswordStr;
                body.UserId = EnrichUtils.getUserData(SettingsActivity.this).Id;
                body.Username = EnrichUtils.getUserData(SettingsActivity.this).UserName;

                changePassword.changePassword(SettingsActivity.this, body);
            }
        });

        forgotPasswordMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (forgotPasswordClickHere.getVisibility() == View.GONE) {
                    Animations.expand(forgotPasswordClickHere, 400);
                } else {
                    Animations.collapse(forgotPasswordClickHere, 400);
                }
            }
        });

        forgotPasswordClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPasswordRequestModel forgotPasswordRequestModel = new ForgotPasswordRequestModel();
                forgotPasswordRequestModel.UserName = "" + EnrichUtils.getHomeStore(SettingsActivity.this).Phone;

                forgotPasswordPresenter.forgotPassword(SettingsActivity.this, forgotPasswordRequestModel);
            }
        });

        if (EnrichUtils.getUserData(SettingsActivity.this) != null) {
            userSettings.setVisibility(View.VISIBLE);
            signInContainer.setVisibility(View.GONE);
        } else {
            userSettings.setVisibility(View.GONE);
            signInContainer.setVisibility(View.VISIBLE);

            settingsLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginBottomSheetFragment.getInstance(SettingsActivity.this).show(getSupportFragmentManager(), "login_bottomsheet_fragment");
                }
            });
        }
    }

    @Override
    public void passwordChanged(ChangePasswordResponseModel model) {
        if (changePasswordEditContainer.getVisibility() == View.VISIBLE) {
            Animations.collapse(changePasswordEditContainer, 400);
        }
        EnrichUtils.showMessage(SettingsActivity.this, "Password changed successfully");
    }

    @Override
    public void passwordSent(ForgotPasswordResponseModel model) {
        if (model.Success) {
            showForgotPasswordDialog();
        } else {
            EnrichUtils.showMessage(SettingsActivity.this, "" + model.Error.Message);
        }
    }

    private void showForgotPasswordDialog() {
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.forgot_password_success);

        dialog.setCancelable(false);

        TextView openMailApp = dialog.findViewById(R.id.open_mail_app);

        openMailApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mailClient = new Intent(Intent.ACTION_VIEW);
                mailClient.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivityGmail");
                startActivity(mailClient);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onLoginSuccessful() {
        if (EnrichUtils.getUserData(this) != null) {
            userSettings.setVisibility(View.VISIBLE);
            signInContainer.setVisibility(View.GONE);
        }
    }
}
