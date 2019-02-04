package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.ConfirmOrderRequestModel;
import com.enrich.salonapp.data.model.ConfirmReservationResponseModel;
import com.enrich.salonapp.data.model.InvoiceModel;
import com.enrich.salonapp.data.model.PaymentSummaryModel;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceiptActivity extends AppCompatActivity {

    @BindView(R.id.drawer_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.payment_confirm_order_purchase_amount)
    TextView paymentConfirmOrderPurchaseAmount;

    @BindView(R.id.payment_confirm_order_date)
    TextView paymentConfirmOrderDate;

    @BindView(R.id.order_time)
    TextView orderTime;

    @BindView(R.id.payment_confirm_order_payment_mode)
    TextView paymentConfirmOrderPaymentMode;

    @BindView(R.id.payment_confirm_order_salon_name)
    TextView paymentConfirmOrderSalonName;

    @BindView(R.id.view_transactions)
    Button viewTransactions;

    @BindView(R.id.drawer_toolbar)
    Toolbar toolbar;

    @BindView(R.id.order_date_layout)
    LinearLayout orderDateLayout;

    @BindView(R.id.order_time_layout)
    LinearLayout orderTimeLayout;

    @BindView(R.id.order_salon_number)
    TextView orderSalonNumber;

    @BindView(R.id.order_contact_details)
    LinearLayout orderContactDetails;

    ConfirmReservationResponseModel confirmReservationResponseModel;
    //    InvoiceModel invoiceModel;
    PaymentSummaryModel paymentSummaryModel;
    ConfirmOrderRequestModel confirmOrderRequestModel;

    EnrichApplication application;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        application = (EnrichApplication) getApplication();

        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Thank You");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);

        ButterKnife.bind(this);

        confirmReservationResponseModel = EnrichUtils.newGson().fromJson(getIntent().getStringExtra("ConfirmReservationResponseModel"), ConfirmReservationResponseModel.class);
        paymentSummaryModel = EnrichUtils.newGson().fromJson(getIntent().getStringExtra("InvoiceModel"), PaymentSummaryModel.class);
        confirmOrderRequestModel = EnrichUtils.newGson().fromJson(getIntent().getStringExtra("ConfirmOrderRequestModel"), ConfirmOrderRequestModel.class);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        assert toolbar != null;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiptActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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

        collapsingToolbarLayout.setTitle("RECEIPT");

        paymentConfirmOrderPurchaseAmount.setText(getResources().getString(R.string.Rs) + " " + paymentSummaryModel.getTotal());

        try {
            SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat stringToTime = new SimpleDateFormat("hh:mm:ss");

            Date date = stringToDate.parse(confirmReservationResponseModel.ConfirmedBookings.get(0).Services.get(0).StartTime);
            Date time = stringToTime.parse(confirmReservationResponseModel.ConfirmedBookings.get(0).Services.get(0).StartTime.substring(11));

            SimpleDateFormat dateToString = new SimpleDateFormat("dd MMM, yyyy");
            SimpleDateFormat timeToString = new SimpleDateFormat("hh:mm a");

            String dateStr = dateToString.format(date);
            String timeStr = timeToString.format(time);

            orderTimeLayout.setVisibility(View.VISIBLE);
            orderDateLayout.setVisibility(View.VISIBLE);

            paymentConfirmOrderDate.setText(dateStr);
            orderTime.setText(timeStr);
        } catch (Exception e) {
            e.printStackTrace();
            orderTimeLayout.setVisibility(View.GONE);
            orderDateLayout.setVisibility(View.GONE);
        }

        if (confirmOrderRequestModel.getConfirmOrder().getModeOfPayment() == Constants.PAYMENT_MODE_CASH) {
            paymentConfirmOrderPaymentMode.setText("Cash");
        } else if (confirmOrderRequestModel.getConfirmOrder().getModeOfPayment() == Constants.PAYMENT_MODE_ONLINE) {
            paymentConfirmOrderPaymentMode.setText("Online");
        }
        paymentConfirmOrderSalonName.setText("" + EnrichUtils.getHomeStore(this).Name);
        orderSalonNumber.setText("" + EnrichUtils.getHomeStore(this).Phone);
        Linkify.addLinks(orderSalonNumber, Linkify.ALL);
        orderSalonNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + orderSalonNumber.getText().toString()));
                startActivity(intent);
            }
        });

        viewTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiptActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
