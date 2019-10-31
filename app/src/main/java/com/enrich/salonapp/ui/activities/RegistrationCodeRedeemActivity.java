/*

package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.enrich.salonapp.R;
import com.enrich.salonapp.util.EnrichUtils;

public class RegistrationCodeRedeemActivity extends AppCompatActivity {
    boolean isFromLoginLater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_code_redeem);
        isFromLoginLater = getIntent().getBooleanExtra("IsFromLoginLater", false);
        if (!isFromLoginLater) {
            if (EnrichUtils.getHomeStore(this) != null) {
                Intent intent = new Intent(RegistrationCodeRedeemActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(RegistrationCodeRedeemActivity.this, StoreSelectorActivity.class);
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
*/
