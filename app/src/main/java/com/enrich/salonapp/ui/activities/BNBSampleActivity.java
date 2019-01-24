package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.enrich.salonapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BNBSampleActivity extends AppCompatActivity {

    @BindView(R.id.new_user)
    Button newUser;

    @BindView(R.id.existing_user)
    Button existingUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bnbsample);

        ButterKnife.bind(this);

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BNBSampleActivity.this, BeautyAndBlingLandingActivity.class);
                intent.putExtra("IsNewUser", true);
                startActivity(intent);
            }
        });

        existingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BNBSampleActivity.this, BeautyAndBlingEligibilityActivity.class);
                intent.putExtra("IsNewUser", false);
                startActivity(intent);
            }
        });
    }
}
