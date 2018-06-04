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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.ChangePasswordRequestModel;
import com.enrich.salonapp.data.model.ChangePasswordResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.contracts.SettingsContract;
import com.enrich.salonapp.ui.presenters.SettingsPresenter;
import com.enrich.salonapp.util.Animations;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity implements SettingsContract.View {

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

    String oldPasswordStr, newPasswordStr, confirmPasswordStr;

    SettingsPresenter changePassword;
    DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.otf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTypeface(tf);

        collapsingToolbarLayout.setTitle("Settings");

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this, mainUiThread, threadExecutor, null);
        changePassword = new SettingsPresenter(this, dataRepository);

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
    }

    @Override
    public void passwordChanged(ChangePasswordResponseModel model) {
        if (changePasswordEditContainer.getVisibility() == View.VISIBLE) {
            Animations.collapse(changePasswordEditContainer, 400);
        }
        EnrichUtils.showMessage(SettingsActivity.this, "Password changed successfully");
    }
}
