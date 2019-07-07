package com.enrich.salonapp.ui.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.enrich.salonapp.R;
import com.enrich.salonapp.util.mvp.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReferAFriendActivity extends BaseActivity {
    @BindView(R.id.invite_button)
    Button btnInvite;//by dhaval shah 7/7/19

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_afriend);
        ButterKnife.bind(this);
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReferAFriendActivity.this, SelectFriendActivity.class);
                startActivity(intent);
            }
        });

    }
}
