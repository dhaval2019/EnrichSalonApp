package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.FriendResponseModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThankyouActivity extends AppCompatActivity {
    @BindView(R.id.tvthankyou)
    TextView tvThankYou;
    @BindView(R.id.continue_shopping_button)
    Button btnContinueShopping;//by dhaval shah 7/7/19
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        FriendResponseModel model = bundle.getParcelable("friendResponseModel");

        btnContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThankyouActivity.this,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });

        String eg = "";
        if (model.ExistingGuests.size() == 1) {
            for (int i = 0; i < model.ExistingGuests.size(); i++) {
                eg = model.ExistingGuests.get(i).getMobileNo() + "";
            }
        } else if (model.ExistingGuests.size() > 1) {
            eg = model.ExistingGuests.get(0).getMobileNo() + "";
            for (int i = 1; i < model.ExistingGuests.size(); i++) {
                eg = eg + " , " + model.ExistingGuests.get(i).getMobileNo();
            }
        }
        String er = "";
        if (model.ExistingReferrals.size() == 1) {
            for (int i = 0; i < model.ExistingReferrals.size(); i++) {
                er = model.ExistingReferrals.get(i).getMobileNo() + "";
            }
        } else if (model.ExistingReferrals.size() > 1) {
            er = model.ExistingReferrals.get(0).getMobileNo() + "";
            for (int i = 1; i < model.ExistingReferrals.size(); i++) {
                er = er + " , " + model.ExistingReferrals.get(i).getMobileNo();
            }
        }
        String vr = "";
        if (model.ValidReferrals.size() == 1) {
            for (int i = 0; i < model.ValidReferrals.size(); i++) {
                vr = model.ValidReferrals.get(i).getMobileNo() + "";
            }
        } else if (model.ValidReferrals.size() > 1) {
            vr = model.ValidReferrals.get(0).getMobileNo() + "";
            for (int i = 1; i < model.ValidReferrals.size(); i++) {
                vr = vr + " , " + model.ValidReferrals.get(i).getMobileNo();
            }
        }
        String rftExistingGuestSingle = "";
        String rftExistingGuestMulti = "";
        if (model.ExistingGuests.size() == 1) {
            rftExistingGuestSingle = "The guest -" + eg + " you have referred is already an existing Enrich customer. Please select another guest to invite. ";
        } else if (model.ExistingGuests.size() >= 1) {
            rftExistingGuestMulti = "The guests -" + eg + " you have referred are already an existing Enrich customer. Please select another guests to invite. ";
        }
        String rftExistingReferralSingle="";
        String rftExistingReferralMulti="";
        if (model.ExistingReferrals.size() == 1) {
            rftExistingReferralSingle = "The guest -" + er + " you have referred has already been invited by another. Please select another guest to invite. ";
        } else if (model.ExistingReferrals.size() >= 1) {
            rftExistingReferralMulti = "The guests - " + er + " you have referred have already been invited by another. Please select another guests to invite. ";
        }

        String rftValidReferralSingle="";
        String rftValidReferralMulti="";

        if (model.ValidReferrals.size() == 1) {
            rftValidReferralSingle = "The guest -" + vr + " you have referred has been invited. Thank you! ";
        } else if (model.ValidReferrals.size() >= 1) {
            rftValidReferralMulti = "The guests -" + vr + " you have referred have been invited. Thank you! ";
        }
        boolean bvr = false, beg = false;
        if (model.ValidReferrals.size() == 1) {
            tvThankYou.setText(rftValidReferralSingle.toString());
            bvr = true;
        }else
        if (model.ValidReferrals.size() > 1) {
            tvThankYou.setText(rftValidReferralMulti.toString());
            bvr = true;
        }

        if (model.ExistingGuests.size() == 1) {
            if (bvr) {
                tvThankYou.append(" "+rftExistingGuestSingle);
                beg = true;
            } else {
                tvThankYou.setText(rftExistingGuestSingle);
                beg = true;
            }
        }else
        if (model.ExistingGuests.size() > 1) {
            if (bvr) {
                tvThankYou.append(" "+rftExistingGuestMulti);
                beg = true;
            } else {
                tvThankYou.setText(rftExistingGuestMulti);
                beg = true;
            }
        }

        if (model.ExistingReferrals.size() == 1) {
            if ((bvr) || (beg)) {
                tvThankYou.append(" "+rftExistingReferralSingle);
            } else {
                tvThankYou.setText(rftExistingReferralSingle);
            }
        }else
        if (model.ExistingReferrals.size() > 1) {
            if ((bvr) || (beg)) {
                tvThankYou.append(" "+rftExistingReferralMulti);
            } else {
                tvThankYou.setText(rftExistingReferralMulti);
            }
        }


    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ThankyouActivity.this, SelectFriendActivity.class);
        startActivity(intent);
        ThankyouActivity.this.finish();
    }
}
