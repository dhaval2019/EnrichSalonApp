package com.enrich.salonapp.ui.activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.enrich.salonapp.R;

import java.util.ArrayList;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class WalletActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    RecyclerView walletRecyclerView;
    ShimmerLayout walletLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
    }
}
