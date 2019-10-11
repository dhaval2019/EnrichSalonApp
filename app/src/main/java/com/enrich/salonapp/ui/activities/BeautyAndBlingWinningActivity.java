package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.SpinModel;
import com.enrich.salonapp.ui.adapters.WinningSpinRecyclerView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BeautyAndBlingWinningActivity extends AppCompatActivity {

    @BindView(R.id.try_a_spin)
    Button tryASpinAgain;

    @BindView(R.id.winning_points_list)
    RecyclerView winningPointsList;

    @BindView(R.id.total_winnings)
    TextView totalWinnings;

    @BindView(R.id.move_ahead)
    TextView moveAhead;

    boolean isNewUser;

    boolean areAllSpinsDone = false;
    Tracker mTracker;
    EnrichApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_and_bling_winning);

        ButterKnife.bind(this);
        application = (EnrichApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Beauty and Bling winning screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);
        isNewUser = getIntent().getBooleanExtra("IsNewUser", false);

        int winnings = getIntent().getIntExtra("PointsWon", 0);

        totalWinnings.setText("" + getTotalPriceWon(EnrichApplication.getSpinList()));

        if (EnrichApplication.getSpinList() != null) {
            for (int i = 0; i < EnrichApplication.getSpinList().size(); i++) {
                if (EnrichApplication.getSpinList().get(i).isSpinTaken) {
                    areAllSpinsDone = true;
                } else {
                    areAllSpinsDone = false;
                }
            }
        } else {
            areAllSpinsDone = true;
        }

        if (isNewUser) {
            tryASpinAgain.setText("Book Now");
        }

        if (areAllSpinsDone) {
            tryASpinAgain.setVisibility(View.GONE);
            moveAhead.setVisibility(View.VISIBLE);
        } else {
            tryASpinAgain.setVisibility(View.VISIBLE);
            moveAhead.setVisibility(View.GONE);
        }

        tryASpinAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        moveAhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BeautyAndBlingWinningActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        WinningSpinRecyclerView adapter = new WinningSpinRecyclerView(this, getTotalSpinsTakenList(EnrichApplication.getSpinList()));
        winningPointsList.setAdapter(adapter);
        winningPointsList.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private int getTotalPriceWon(ArrayList<SpinModel> list) {
        int totalPrice = 0;
        for (int i = 0; i < list.size(); i++) {
            totalPrice = totalPrice + list.get(i).prizeWon;
        }
        return totalPrice;
    }

    private ArrayList<SpinModel> getTotalSpinsTakenList(ArrayList<SpinModel> completeList) {
        ArrayList<SpinModel> list = new ArrayList<>();
        if (!completeList.isEmpty()) {
            for (int i = 0; i < completeList.size(); i++) {
                if (completeList.get(i).isSpinTaken) {
                    list.add(completeList.get(i));
                }
            }
        }
        return list;
    }
}
