package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.GenericCartModel;
import com.enrich.salonapp.data.model.ReserveSlotRequestModel;
import com.enrich.salonapp.data.model.AppointmentModels.AppointmentServiceModel;
import com.enrich.salonapp.data.model.AppointmentModels.AppointmentServicesModel;
import com.enrich.salonapp.data.model.AppointmentModels.AppointmentSlotBookingsModel;
import com.enrich.salonapp.ui.adapters.CartAdapter;
import com.enrich.salonapp.util.DividerItemDecoration;
import com.enrich.salonapp.util.EnrichUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity {

    @BindView(R.id.cart_recycler_view)
    RecyclerView cartRecyclerView;

    @BindView(R.id.cart_total_items)
    TextView cartTotalItems;

    @BindView(R.id.cart_total_price)
    TextView cartTotalPrice;

    @BindView(R.id.cart_proceed)
    TextView cartProceed;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    EnrichApplication application;

    CartAdapter adapter;

    ArrayList<GenericCartModel> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ButterKnife.bind(this);

        application = (EnrichApplication) getApplication();
        cartList = application.getCartItems();

        setSupportActionBar(toolbar);
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
        getSupportActionBar().setTitle("Cart");

        if (application != null)
            updatePriceAndQuantityView();

        adapter = new CartAdapter(this, application.getCartItems(), this);
        cartRecyclerView.setAdapter(adapter);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cartRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        cartProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (doesCartContainServices()) {
                    ReserveSlotRequestModel reserveSlotModel = new ReserveSlotRequestModel();
                    reserveSlotModel.CenterId = EnrichUtils.getHomeStore(CartActivity.this).Id;
                    reserveSlotModel.CenterTime = cartList.get(0).SlotTime;
                    reserveSlotModel.CreateInvoice = true;
                    reserveSlotModel.ForceApplyAutomaticMembership = true;

                    ArrayList<AppointmentSlotBookingsModel> slotBookingsModelArrayList = new ArrayList<>();
                    for (int i = 0; i < cartList.size(); i++) {
                        ArrayList<AppointmentServicesModel> servicesModelArrayList = new ArrayList<>();

                        AppointmentServiceModel serviceModel = new AppointmentServiceModel();
                        serviceModel.Id = cartList.get(i).ServiceId;

                        AppointmentServicesModel servicesModel = new AppointmentServicesModel();
                        servicesModel.Service = serviceModel;

                        servicesModelArrayList.add(servicesModel);

                        AppointmentSlotBookingsModel slotBookingsModel = new AppointmentSlotBookingsModel();
                        slotBookingsModel.StartTime = cartList.get(i).SlotTime;
                        slotBookingsModel.GuestId = EnrichUtils.getUserData(CartActivity.this).Id;
                        slotBookingsModel.Services = servicesModelArrayList;
                        slotBookingsModel.TherapistId = cartList.get(i).getTherapistModel().Id;
                        slotBookingsModel.Quantity = 1;

                        slotBookingsModelArrayList.add(slotBookingsModel);
                    }
                    reserveSlotModel.SlotBookings = slotBookingsModelArrayList;

                    switchToNextScreen(reserveSlotModel);
                } else {
                    Intent intent = new Intent(CartActivity.this, BookingSummaryActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean doesCartContainServices() {
        boolean itDoes = false;
        for (int i = 0; i < application.getCartItems().size(); i++) {
            if (application.getCartItems().get(i).getCartItemType() == GenericCartModel.CART_TYPE_SERVICES) {
                itDoes = true;
            } else {
                itDoes = false;
            }
        }
        return itDoes;
    }

    public void updatePriceAndQuantityView() {
        cartTotalItems.setText("" + application.getCartItems().size());
        cartTotalPrice.setText(getResources().getString(R.string.Rs) + " " + application.getTotalPrice());
    }

    private void switchToNextScreen(ReserveSlotRequestModel reserveSlotModel) {
        Intent intent = new Intent(CartActivity.this, BookingSummaryActivity.class);
        intent.putExtra("ReserveSlotModel", EnrichUtils.newGson().toJson(reserveSlotModel));
        startActivity(intent);
    }
}
