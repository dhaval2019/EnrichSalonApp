package com.enrich.salonapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.AddressModel;
import com.enrich.salonapp.data.model.AppointmentModels.AppointmentServiceModel;
import com.enrich.salonapp.data.model.AppointmentModels.AppointmentServicesModel;
import com.enrich.salonapp.data.model.AppointmentModels.AppointmentSlotBookingsModel;
import com.enrich.salonapp.data.model.GenericCartModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.data.model.ReserveSlotRequestModel;
import com.enrich.salonapp.ui.adapters.CartAdapter;
import com.enrich.salonapp.util.DividerItemDecoration;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.enrich.salonapp.ui.activities.AddAddressActivity.ADD_ADDRESS;
import static com.enrich.salonapp.ui.activities.AddressSelectorActivity.SELECT_ADDRESS;

public class CartActivity extends BaseActivity {

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

    @BindView(R.id.empty_cart_container)
    LinearLayout emptyCartContainer;

    EnrichApplication application;
    Tracker mTracker;

    CartAdapter adapter;

    ArrayList<GenericCartModel> cartList;

    boolean isHomeSelected;

    ReserveSlotRequestModel reserveSlotModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ButterKnife.bind(this);

        isHomeSelected = getIntent().getBooleanExtra("isHomeSelected", false);

        application = (EnrichApplication) getApplication();
        cartList = application.getCartItems();

        // SEND ANALYTICS
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Cart Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);

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
        getSupportActionBar().setTitle("CART");

        if (application != null)
            updatePriceAndQuantityView();

        if (application.isCartEmpty()) {
            cartRecyclerView.setVisibility(View.GONE);
            emptyCartContainer.setVisibility(View.VISIBLE);

            cartProceed.setEnabled(false);

            cartProceed.setBackground(getResources().getDrawable(R.drawable.grey_gradient_curve_bg));
        } else {
            cartRecyclerView.setVisibility(View.VISIBLE);
            emptyCartContainer.setVisibility(View.GONE);

            cartProceed.setEnabled(true);

            adapter = new CartAdapter(this, application.getCartItems(), this);
            cartRecyclerView.setAdapter(adapter);
            cartRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            cartRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        }

        cartProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveSlotModel = new ReserveSlotRequestModel();

                if (doesCartContainServices()) {
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

                        if (cartList.get(i).getTherapistModel() != null) {
                            slotBookingsModel.TherapistId = cartList.get(i).getTherapistModel().Id;
                        }
                        slotBookingsModel.Quantity = 1;

                        slotBookingsModelArrayList.add(slotBookingsModel);
                    }
                    reserveSlotModel.SlotBookings = slotBookingsModelArrayList;

                    switchToNextScreen(reserveSlotModel);

                } else if (doesCartContainProducts()) {
                    if (EnrichUtils.doesUserHasAddresses(CartActivity.this)) {
                        Intent intent = new Intent(CartActivity.this, AddressSelectorActivity.class);
                        startActivityForResult(intent, SELECT_ADDRESS);
                    } else {
                        Intent intent = new Intent(CartActivity.this, AddAddressActivity.class);
                        startActivity(intent);
                    }

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

    private boolean doesCartContainProducts() {
        boolean itDoes = false;
        for (int i = 0; i < application.getCartItems().size(); i++) {
            if (application.getCartItems().get(i).getCartItemType() == GenericCartModel.CART_TYPE_PRODUCTS) {
                itDoes = true;
            } else {
                itDoes = false;
            }
        }
        return itDoes;
    }

    public void updatePriceAndQuantityView() {
        cartTotalItems.setText("" + application.getCartItems().size());
        cartTotalPrice.setText(getResources().getString(R.string.Rs) + " " +  (int) Math.round(application.getTotalPrice()));
    }

    private void switchToNextScreen(ReserveSlotRequestModel reserveSlotModel) {
        if (isHomeSelected) {
            if (EnrichUtils.doesUserHasAddresses(CartActivity.this)) {
                Intent intent = new Intent(CartActivity.this, AddressSelectorActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
            } else {
                Intent intent = new Intent(CartActivity.this, AddAddressActivity.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(CartActivity.this, BookingSummaryActivity.class);
            intent.putExtra("ReserveSlotModel", EnrichUtils.newGson().toJson(reserveSlotModel));
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ADDRESS) {
            if (resultCode == Activity.RESULT_OK) {
                GuestModel guestModel = data.getParcelableExtra("GuestData");
                String addressType = data.getStringExtra("AddressType");
                AddressModel model = null;
                for (int i = 0; i < guestModel.GuestAddress.size(); i++) {
                    if (guestModel.GuestAddress.get(i).AddressType.equalsIgnoreCase(addressType)) {
                        model = guestModel.GuestAddress.get(i);
                    }
                }
                redirectScreen(model);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == SELECT_ADDRESS) {
            if (resultCode == Activity.RESULT_OK) {
                AddressModel model = data.getParcelableExtra("SelectedAddress");

                redirectScreen(model);
            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private void redirectScreen(AddressModel model) {
        Intent intent = new Intent(CartActivity.this, BookingSummaryActivity.class);
        intent.putExtra("SelectedAddress", model);
        intent.putExtra("ReserveSlotModel", EnrichUtils.newGson().toJson(reserveSlotModel));
        startActivity(intent);
    }
}
