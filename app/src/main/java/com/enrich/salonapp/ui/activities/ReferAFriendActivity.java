package com.enrich.salonapp.ui.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.util.mvp.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReferAFriendActivity extends BaseActivity {
    @BindView(R.id.invite_button)
    Button btnInvite;//by dhaval shah 7/7/19
    @BindView(R.id.tvfrndlink)
    TextView txtFriendLink;
    @BindView(R.id.popuplayout)
    RelativeLayout popupLayout;
    @BindView(R.id.tvclose)
    TextView txtClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_afriend);
        ButterKnife.bind(this);
        popupLayout.setVisibility(View.GONE);
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReferAFriendActivity.this, SelectFriendActivity.class);
                startActivity(intent);
            }
        });
        txtFriendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.setVisibility(View.VISIBLE);
                Animation animation;
                animation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.bottom_to_original);
                popupLayout.setAnimation(animation);
            }
        });
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.setVisibility(View.GONE);
            }
        });

    }
}
