package com.enrich.salonapp.ui.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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
    @BindView(R.id.backarrow)
    ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_afriend);
        ButterKnife.bind(this);
        popupLayout.setVisibility(View.GONE);
        btnInvite.setVisibility(View.VISIBLE);
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReferAFriendActivity.this, SelectFriendActivity.class);
                startActivity(intent);
                ReferAFriendActivity.this.finish();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ReferAFriendActivity.this.finish();
            }
        });
        txtFriendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.setVisibility(View.VISIBLE);
               /* RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) popupLayout.getLayoutParams();
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                popupLayout.setLayoutParams(lp);*/
                Animation animation;
                animation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.bottom_to_original);
                popupLayout.setAnimation(animation);
                popupLayout.bringToFront();
                btnInvite.setVisibility(View.GONE);
            }
        });
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLayout.setVisibility(View.GONE);
                btnInvite.setVisibility(View.VISIBLE);
            }
        });

    }
    @Override
    public void onBackPressed() {


        ReferAFriendActivity.this.finish();
    }
}
