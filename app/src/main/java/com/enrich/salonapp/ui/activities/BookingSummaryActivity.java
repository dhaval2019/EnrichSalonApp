package com.enrich.salonapp.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.AddressModel;
import com.enrich.salonapp.data.model.ConfirmOrderModel;
import com.enrich.salonapp.data.model.ConfirmOrderRequestModel;
import com.enrich.salonapp.data.model.ConfirmOrderResponseModel;
import com.enrich.salonapp.data.model.ConfirmReservationRequestModel;
import com.enrich.salonapp.data.model.ConfirmReservationResponseModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOrderPackageBundleModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOrderProductModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOrderRequestModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOrderResponseModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOrderServiceModel;
import com.enrich.salonapp.data.model.GenericCartModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.data.model.InvoiceModel;
import com.enrich.salonapp.data.model.InvoiceResponseModel;
import com.enrich.salonapp.data.model.PaymentSummaryModel;
import com.enrich.salonapp.data.model.ReserveSlotRequestModel;
import com.enrich.salonapp.data.model.ReserveSlotResponseModel;
import com.enrich.salonapp.data.model.SegmentLoggingProducts;
import com.enrich.salonapp.data.model.SegmentLoggingServiceModel;
import com.enrich.salonapp.data.model.SegmentLoggingServiceModelParent;
import com.enrich.salonapp.data.remote.RemoteDataSource;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.BookingSummaryItemAdapter;
import com.enrich.salonapp.ui.contracts.BookingSummaryContract;
import com.enrich.salonapp.ui.presenters.BookingSummaryPresenter;
import com.enrich.salonapp.util.AppEnvironment;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.enrich.salonapp.util.Constants.PAYMENT_MODE_CASH;
import static com.enrich.salonapp.util.Constants.PAYMENT_MODE_ONLINE;
import static com.enrich.salonapp.util.Constants.PAYMENT_SUCCESS;
import static com.enrich.salonapp.util.Constants.PLATFORM_ANDROID;

public class BookingSummaryActivity extends BaseActivity implements BookingSummaryContract.View {

    @BindView(R.id.total_price)
    TextView totalPrice;

    @BindView(R.id.gross_total_amount)
    TextView grossTotalAmount;

    @BindView(R.id.tax_amount)
    TextView taxAmount;

    @BindView(R.id.discount_amount)
    TextView discountAmount;

    @BindView(R.id.payable_amount)
    TextView payableAmount;

    @BindView(R.id.cashback_amount)
    TextView cashbackAmount;

    @BindView(R.id.address_label)
    TextView addressLabel;

    @BindView(R.id.date_time_slot)
    TextView dateTimeSlot;

    @BindView(R.id.stylist_label)
    TextView stylistLabel;

    @BindView(R.id.booking_summary_item_recycler_view)
    RecyclerView bookingSummaryItemRecyclerView;

    @BindView(R.id.make_payment_btn)
    Button makePaymentBtn;

    @BindView(R.id.make_payment_online_btn)
    Button makePaymentOnlineBtn;

    @BindView(R.id.make_payment_offline_btn)
    Button makePaymentOfflineBtn;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.service_info_container)
    LinearLayout serviceInfoContainer;

    @BindView(R.id.product_info_container)
    LinearLayout productInfoContainer;

    @BindView(R.id.delivery_period)
    TextView deliveryPeriod;

    @BindView(R.id.delivery_information)
    TextView deliveryInformation;

    BookingSummaryItemAdapter adapter;

    EnrichApplication application;
    Tracker mTracker;

    ReserveSlotResponseModel reserveSlotResponseModel;
    InvoiceModel invoiceModel;
    ReserveSlotRequestModel reserveSlotModel;
    ConfirmReservationResponseModel confirmReservationResponseModel;
    ConfirmOrderRequestModel confirmOrderRequestModel;

    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

    CreateOrderResponseModel createOrderResponseModel;

    DataRepository dataRepository;
    BookingSummaryPresenter bookingSummaryPresenter;

    boolean isOnlinePayment;

    AddressModel selectedAddress;

    ConfirmOrderModel confirmOrderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        ButterKnife.bind(this);

        application = (EnrichApplication) getApplication();
        reserveSlotModel = EnrichUtils.newGson().fromJson(getIntent().getStringExtra("ReserveSlotModel"), ReserveSlotRequestModel.class);
        selectedAddress = getIntent().getParcelableExtra("SelectedAddress");

        // SEND ANALYTICS
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Checkout Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
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

        dataRepository = Injection.provideDataRepository(this, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        bookingSummaryPresenter = new BookingSummaryPresenter(this, dataRepository);


        if (application.cartHasServices()) {
            if (reserveSlotModel.SlotBookings != null) {
                bookingSummaryPresenter.reserveSlot(this, reserveSlotModel);
            }
        } else if (application.cartHasPackages()) {
            makePaymentOfflineBtn.setVisibility(View.VISIBLE);

            CreateOrderRequestModel createOrderRequestModel = new CreateOrderRequestModel();
            createOrderRequestModel.setApplyCredits(false);
            createOrderRequestModel.setGuestId(EnrichUtils.getUserData(this).Id);
            createOrderRequestModel.setGuestName(EnrichUtils.getUserData(this).FirstName + " " + EnrichUtils.getUserData(this).LastName);
            createOrderRequestModel.setPlatform(0);
            createOrderRequestModel.setPromoCode("");

            ArrayList<CreateOrderPackageBundleModel> createOrderPackageList = new ArrayList<>();
            for (int i = 0; i < application.getCartItems().size(); i++) {
                if (application.getCartItems().get(i).getCartItemType() == GenericCartModel.CART_TYPE_SUB_PACKAGE) {
                    CreateOrderPackageBundleModel createOrderPackageBundleModel = new CreateOrderPackageBundleModel();
                    createOrderPackageBundleModel.setPackageBundleId(application.getCartItem(i).packageBundleId);
                    createOrderPackageBundleModel.setCount(application.getCartItem(i).Quantity);

                    createOrderPackageList.add(createOrderPackageBundleModel);
                }
            }

            createOrderRequestModel.setPackageIds(createOrderPackageList);

            EnrichUtils.log(EnrichUtils.newGson().toJson(createOrderRequestModel));

            bookingSummaryPresenter.createOrder(this, createOrderRequestModel);

            adapter = new BookingSummaryItemAdapter(this, application.getCartItems());
            bookingSummaryItemRecyclerView.setAdapter(adapter);
            bookingSummaryItemRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        } else if (application.cartHasProducts()) {
            if (application.getCartItem(0).getPaymentMode() == Constants.PAYMENT_MODE_CASH) {
                makePaymentOfflineBtn.setVisibility(View.VISIBLE);
                makePaymentOnlineBtn.setVisibility(View.GONE);
            } else if (application.getCartItem(0).getPaymentMode() == PAYMENT_MODE_ONLINE) {
                makePaymentOfflineBtn.setVisibility(View.GONE);
                makePaymentOnlineBtn.setVisibility(View.VISIBLE);
            }

            CreateOrderRequestModel createOrderRequestModel = new CreateOrderRequestModel();
            createOrderRequestModel.setApplyCredits(false);
            createOrderRequestModel.setGuestId(EnrichUtils.getUserData(this).Id);
            createOrderRequestModel.setGuestName(EnrichUtils.getUserData(this).FirstName + " " + EnrichUtils.getUserData(this).LastName);
            createOrderRequestModel.setPlatform(0);
            createOrderRequestModel.setPromoCode("");
            createOrderRequestModel.setGuestAddress(selectedAddress);

            ArrayList<CreateOrderProductModel> createOrderProductModels = new ArrayList<>();
            for (int i = 0; i < application.getCartItems().size(); i++) {
                if (application.getCartItems().get(i).getCartItemType() == GenericCartModel.CART_TYPE_PRODUCTS) {
                    CreateOrderProductModel createOrderProductModel = new CreateOrderProductModel();
                    createOrderProductModel.setProductId(application.getCartItem(i).getId());
                    createOrderProductModel.setQuantity(application.getCartItem(i).Quantity);

                    createOrderProductModels.add(createOrderProductModel);
                }
            }

            createOrderRequestModel.setProductIds(createOrderProductModels);

            bookingSummaryPresenter.createOrder(this, createOrderRequestModel);

            adapter = new BookingSummaryItemAdapter(this, application.getCartItems());
            bookingSummaryItemRecyclerView.setAdapter(adapter);
            bookingSummaryItemRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }

        makePaymentOnlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOnlinePayment = true;
                payOnline();
            }
        });

        makePaymentOfflineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOnlinePayment = false;
                if (makePaymentOfflineBtn.getText().toString().equalsIgnoreCase(getString(R.string.txt_confirm_order))) {
                    if (application.cartHasServices()) {
                        confirmReservation();
                    } else if (application.cartHasPackages()) {
                        ConfirmReservationResponseModel confirmReservationResponseModel = new ConfirmReservationResponseModel();
                        confirmReservationResponseModel.IsConfirmed = true;
                        reservationConfirmed(confirmReservationResponseModel);
                    } else if (application.cartHasProducts()) {
                        ConfirmReservationResponseModel confirmReservationResponseModel = new ConfirmReservationResponseModel();
                        confirmReservationResponseModel.IsConfirmed = true;
                        reservationConfirmed(confirmReservationResponseModel);
                    }
                } else {
                    showCashDialog();
                }

            }
        });

        makePaymentOfflineBtn.setEnabled(false);
        makePaymentOnlineBtn.setEnabled(false);


    }

    @Override
    public void slotReserved(ReserveSlotResponseModel model) {
        if (model.IsReserved) {
            reserveSlotResponseModel = model;

            ArrayList<CreateOrderServiceModel> createOrderServiceModels = new ArrayList<>();
            for (int i = 0; i < application.getCartItems().size(); i++) {
                CreateOrderServiceModel createOrderServiceModel = new CreateOrderServiceModel();
                createOrderServiceModel.setServiceId(application.getCartItems().get(i).Id);

                if (application.getCartItems().get(i).therapistModel != null)
                    createOrderServiceModel.setStylistId(application.getCartItems().get(i).therapistModel.Id);

                createOrderServiceModels.add(createOrderServiceModel);
            }

            CreateOrderRequestModel createOrderRequestModel = new CreateOrderRequestModel();
            createOrderRequestModel.setServiceIds(createOrderServiceModels);
            createOrderRequestModel.setApplyCredits(false);
            createOrderRequestModel.setPromoCode("");
            createOrderRequestModel.setGuestId(EnrichUtils.getUserData(this).Id);
            createOrderRequestModel.setGuestName(EnrichUtils.getUserData(this).FirstName + " " + EnrichUtils.getUserData(this).LastName);
            createOrderRequestModel.setSlotBookdate(reserveSlotResponseModel.SlotBookings.get(0).Services.get(0).StartTime);
            createOrderRequestModel.setPlatform(PLATFORM_ANDROID);

            EnrichUtils.log(EnrichUtils.newGson().toJson(createOrderRequestModel));

            bookingSummaryPresenter.createOrder(BookingSummaryActivity.this, createOrderRequestModel);
        } else {
            EnrichUtils.showMessage(BookingSummaryActivity.this, "" + model.Error.Message);
            setProgressBar(false);
            onBackPressed();
        }
    }

    @Override
    public void orderCreated(CreateOrderResponseModel model) {
        if (model.getOrder() != null) {
            createOrderResponseModel = model;
            if (application.cartHasServices()) {
                bookingSummaryPresenter.getInvoice(this, RemoteDataSource.HOST + RemoteDataSource.GET_INVOICE + reserveSlotResponseModel.InvoiceId);
                Log.e("invoiceid", reserveSlotResponseModel.InvoiceId + "");
                setData(model.getPaymentSummary());
            } else if (application.cartHasProducts()) {

                setData(model.getPaymentSummary());
                logSegmentProducts();
            } else {
                setData(model.getPaymentSummary());
            }
        } else {
            EnrichUtils.showMessage(BookingSummaryActivity.this, "Something went wrong. Please try again.");
        }
    }

    @Override
    public void invoiceCreated(InvoiceResponseModel model) {
        if (model.Error == null) {
            invoiceModel = model.AppointmentGroup;
            setData(model.AppointmentGroup);
        } else {
            EnrichUtils.showMessage(BookingSummaryActivity.this, "" + model.Error.Message);
        }
    }

    @Override
    public void reservationConfirmed(ConfirmReservationResponseModel model) {
        if (model.IsConfirmed) {
            if (application.cartHasServices()) {
                logSegmentBookAppointment();//changed by dhaval shah on 12june2019because invoice null for other condition
            }

            ConfirmOrderModel confirmOrderModel = new ConfirmOrderModel();
            confirmOrderModel.setAmount(createOrderResponseModel.getPaymentSummary().getPayableAmount());
            confirmOrderModel.setFirstName(EnrichUtils.getUserData(this).FirstName);
            confirmOrderModel.setLastName(EnrichUtils.getUserData(this).LastName);
            confirmOrderModel.setPhone(EnrichUtils.getUserData(this).MobileNumber);
            confirmOrderModel.setEmailAddress(EnrichUtils.getUserData(this).Email);

            if (invoiceModel != null)
                confirmOrderModel.setReservationId(invoiceModel.AppointmentGroupId);

            if (isOnlinePayment) { //ONLINE
                confirmOrderModel.setTransactionId(mPaymentParams.getParams().get(PayUmoneyConstants.TXNID));
                confirmOrderModel.setPaymentId(mPaymentParams.getParams().get(PayUmoneyConstants.TXNID));
                confirmOrderModel.setModeOfPayment(PAYMENT_MODE_ONLINE);
                confirmOrderModel.setPaymentStatus(PAYMENT_SUCCESS);
                if (application.cartHasServices()) {
                    logSegmentPayment("Card", "");//changed by dhaval shah on 12june2019because invoice null for other condition
                }

            } else { // CASH
                confirmOrderModel.setTransactionId(System.currentTimeMillis() + "");
                confirmOrderModel.setPaymentId(System.currentTimeMillis() + "");
                confirmOrderModel.setModeOfPayment(PAYMENT_MODE_CASH);
                confirmOrderModel.setPaymentStatus(PAYMENT_SUCCESS);
                if (application.cartHasServices()) {
                    logSegmentPayment("Cash", "");//changed by dhaval shah on 12june2019because invoice null for other condition
                }

            }

            confirmOrderRequestModel = new ConfirmOrderRequestModel();
            confirmOrderRequestModel.setOrderId(createOrderResponseModel.getOrder().getOrderId());
            confirmOrderRequestModel.setConfirmOrder(confirmOrderModel);

            confirmReservationResponseModel = model;

            EnrichUtils.log(EnrichUtils.newGson().toJson(confirmOrderRequestModel));

            bookingSummaryPresenter.confirmOrder(BookingSummaryActivity.this, confirmOrderRequestModel);
        } else {

            EnrichUtils.showMessage(BookingSummaryActivity.this, "" + model.Error.Message);
        }

    }

    @Override
    public void orderConfirmed(ConfirmOrderResponseModel model) {
        if (model.getConfirmOrder() != null) {
            EnrichUtils.log(EnrichUtils.newGson().toJson(model));
            confirmOrderModel = model.getConfirmOrder().getConfirmOrder();

            if (application.cartHasServices()) {//changed by dhaval shah on 12june2019because invoice null for other condition
                logAppointmentBooked(model.getConfirmOrder().getConfirmOrder());
                logFirebasePurchaseEvent(model.getConfirmOrder().getConfirmOrder());
                logSegmentBookSummary();
                logSegmentBookingStatus("Successful");

                if (model.getConfirmOrder().getConfirmOrder().getModeOfPayment() == Constants.PAYMENT_MODE_ONLINE) {
                    logPurchaseOnlineEvent(model.getConfirmOrder().getConfirmOrder());
                } else if (model.getConfirmOrder().getConfirmOrder().getModeOfPayment() == Constants.PAYMENT_MODE_CASH) {
                    logPurchaseOfflineEvent(model.getConfirmOrder().getConfirmOrder());
                }
            }


            Intent intent = new Intent(BookingSummaryActivity.this, ReceiptActivity.class);
            intent.putExtra("ConfirmReservationResponseModel", EnrichUtils.newGson().toJson(confirmReservationResponseModel));
            intent.putExtra("InvoiceModel", EnrichUtils.newGson().toJson(createOrderResponseModel.getPaymentSummary()));
            intent.putExtra("ConfirmOrderRequestModel", EnrichUtils.newGson().toJson(confirmOrderRequestModel));

            startActivity(intent);
        } else {
            EnrichUtils.showMessage(BookingSummaryActivity.this, model.getError().Message);
            logSegmentBookingStatus("Failed");
        }
    }

    @Override
    public void orderNotConfirmed() {
        logSegmentBookingStatus("Failed");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EnrichUtils.log("request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);
            if (transactionResponse.getPayuResponse() != null) {

                try {
                    JSONObject transactionResponseJSONObject = new JSONObject(transactionResponse.getPayuResponse());
                    JSONObject payUResponseJSONObject = transactionResponseJSONObject.getJSONObject("result");
                    String cardType = payUResponseJSONObject.getString("card_type");
                    String amount = payUResponseJSONObject.getString("amount");

                    EnrichUtils.log(cardType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    if (application.cartHasServices()) {
                        confirmReservation();
                    } else if (application.cartHasPackages()) {
                        ConfirmReservationResponseModel confirmReservationResponseModel = new ConfirmReservationResponseModel();
                        confirmReservationResponseModel.IsConfirmed = true;
                        reservationConfirmed(confirmReservationResponseModel);
                    } else if (application.cartHasProducts()) {
                        ConfirmReservationResponseModel confirmReservationResponseModel = new ConfirmReservationResponseModel();
                        confirmReservationResponseModel.IsConfirmed = true;
                        reservationConfirmed(confirmReservationResponseModel);
                    }
                } else {
                    //Failure Transaction;
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                EnrichUtils.log("PayU Resp: " + payuResponse);

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();

                EnrichUtils.log("Merchant Resp: " + merchantResponse);

            } else if (resultModel != null && resultModel.getError() != null) {
                EnrichUtils.log("" + resultModel.getError().getTransactionResponse());
            } else {
                EnrichUtils.log("Both objects are null!");
            }


        }
    }

    private void confirmReservation() {
        ConfirmReservationRequestModel confirmReservationRequestModel = new ConfirmReservationRequestModel();
        confirmReservationRequestModel.CenterId = reserveSlotResponseModel.CenterId;
        confirmReservationRequestModel.ReservationId = reserveSlotResponseModel.ReservationId;
        confirmReservationRequestModel.BookingSource = 0;
        confirmReservationRequestModel.ReservationNotes = "";

        for (int i = 0; i < reserveSlotResponseModel.SlotBookings.size(); i++) {
            reserveSlotResponseModel.SlotBookings.get(i).Services.get(0).RequestedTherapist.Id = reserveSlotResponseModel.SlotBookings.get(i).TherapistId;
        }
        confirmReservationRequestModel.SlotBookings = reserveSlotResponseModel.SlotBookings;

        bookingSummaryPresenter.confirmReservation(BookingSummaryActivity.this, confirmReservationRequestModel);
    }

    private void setData(PaymentSummaryModel model) {//redefiend in integer by dhaval shah
        makePaymentOfflineBtn.setEnabled(true);
        makePaymentOnlineBtn.setEnabled(true);

        if (application.cartHasServices()) {
            serviceInfoContainer.setVisibility(View.VISIBLE);
            productInfoContainer.setVisibility(View.GONE);
            int i1 = (int) Math.round(model.getActualPrice());
//            totalPrice.setText(String.format("%s %s", getResources().getString(R.string.Rs), new DecimalFormat(".##").format(model.getTotal())));
            grossTotalAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), i1));
            int i2 = (int) Math.round(model.getTotal());
            payableAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), i2));
            int i3 = (int) Math.round(model.getDiscount());
            discountAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), i3));
            int i4 = (int) Math.round(model.getTax());
            taxAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), i4));
            int i5 = (int) Math.round(model.getCashBackApplied());
            cashbackAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), i5));

        } else if (application.cartHasPackages()) {
            totalPrice.setText(String.format("%s %s", getResources().getString(R.string.Rs), (int) Math.round(application.getTotalPrice())));
            grossTotalAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), (int) Math.round(model.getActualPrice())));
            payableAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), (int) Math.round(model.getTotal())));
            discountAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), (int) Math.round(model.getDiscount())));
            taxAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), (int) Math.round(model.getTax())));

            dateTimeSlot.setText("-");
            stylistLabel.setText("-");

            serviceInfoContainer.setVisibility(View.GONE);
            productInfoContainer.setVisibility(View.GONE);
        } else if (application.cartHasProducts()) {
            totalPrice.setText(String.format("%s %s", getResources().getString(R.string.Rs), (int) Math.round(application.getTotalPrice())));
            grossTotalAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), (int) Math.round(model.getActualPrice())));
            payableAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), (int) Math.round(model.getTotal())));
            discountAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), (int) Math.round(model.getDiscount())));
            taxAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), (int) Math.round(model.getTax())));

            dateTimeSlot.setText("-");
            stylistLabel.setText("-");

            serviceInfoContainer.setVisibility(View.GONE);
            productInfoContainer.setVisibility(View.VISIBLE);
            deliveryPeriod.setText(application.getDeliveryPeriod());
            deliveryInformation.setText(application.getDeliveryInformation());
            makePaymentOfflineBtn.setText("PAY CASH");
        }
        if (model.getTotal() == 0.0) {
            makePaymentOnlineBtn.setVisibility(View.GONE);
            makePaymentOfflineBtn.setText(getString(R.string.txt_confirm_order));
        } else {
            makePaymentOnlineBtn.setVisibility(View.VISIBLE);
            makePaymentOfflineBtn.setText(getString(R.string.txt_pay_at_salon));
        }
    }
   /* private void setData(PaymentSummaryModel model) {
        makePaymentOfflineBtn.setEnabled(true);
        makePaymentOnlineBtn.setEnabled(true);

        if (application.cartHasServices()) {
            serviceInfoContainer.setVisibility(View.VISIBLE);
            productInfoContainer.setVisibility(View.GONE);

//            totalPrice.setText(String.format("%s %s", getResources().getString(R.string.Rs), new DecimalFormat(".##").format(model.getTotal())));
            grossTotalAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), new DecimalFormat(".##").format(model.getActualPrice())));
            payableAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), new DecimalFormat(".##").format(model.getTotal())));
            discountAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), new DecimalFormat(".##").format(model.getDiscount())));
            taxAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), new DecimalFormat(".##").format(model.getTax())));
            cashbackAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), new DecimalFormat(".##").format(model.getCashBackApplied())));

        } else if (application.cartHasPackages()) {
            totalPrice.setText(String.format("%s %s", getResources().getString(R.string.Rs), application.getTotalPrice()));
            grossTotalAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), model.getActualPrice()));
            payableAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), model.getTotal()));
            discountAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), model.getDiscount()));
            taxAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), model.getTax()));

            dateTimeSlot.setText("-");
            stylistLabel.setText("-");

            serviceInfoContainer.setVisibility(View.GONE);
            productInfoContainer.setVisibility(View.GONE);
        } else if (application.cartHasProducts()) {
            totalPrice.setText(String.format("%s %s", getResources().getString(R.string.Rs), application.getTotalPrice()));
            grossTotalAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), model.getActualPrice()));
            payableAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), model.getTotal()));
            discountAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), model.getDiscount()));
            taxAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), model.getTax()));

            dateTimeSlot.setText("-");
            stylistLabel.setText("-");

            serviceInfoContainer.setVisibility(View.GONE);
            productInfoContainer.setVisibility(View.VISIBLE);
            deliveryPeriod.setText(application.getDeliveryPeriod());
            deliveryInformation.setText(application.getDeliveryInformation());
            makePaymentOfflineBtn.setText("PAY CASH");
        }
          if (model.getTotal()==0.0) {
            makePaymentOnlineBtn.setVisibility(View.GONE);
            makePaymentOfflineBtn.setText(getString(R.string.txt_confirm_order));
        } else {
            makePaymentOnlineBtn.setVisibility(View.VISIBLE);
            makePaymentOfflineBtn.setText(getString(R.string.txt_pay_at_salon));
        }
    }*/

    private void setData(InvoiceModel model) {

        if (application.cartHasServices()) {
            ArrayList<GenericCartModel> genericCartList = new ArrayList<>();

            for (int i = 0; i < model.AppointmentServices.size(); i++) {
                GenericCartModel genericCartModel = new GenericCartModel();
                genericCartModel.Name = model.AppointmentServices.get(i).Service.name;
                // genericCartModel.Price = model.AppointmentServices.get(i).Service.price._final;
                genericCartModel.Price = model.AppointmentServices.get(i).Service.price._final;

//                if (EnrichUtils.getUserData(BookingSummaryActivity.this).IsMember == Constants.IS_MEMBER) {
//                    genericCartModel.Price = model.AppointmentServices.get(i).Service.price._final;
//                } else {
//                    genericCartModel.Price = model.AppointmentServices.get(i).Service.price.sales;
//                }

                genericCartList.add(genericCartModel);
                logFirebaseCheckoutProgress(genericCartModel);
            }

            //adapter = new BookingSummaryItemAdapter(this, genericCartList,"service");
            adapter = new BookingSummaryItemAdapter(this, application.getCartItems());//changed by dhaval since data was not correct in genericcartlist
            bookingSummaryItemRecyclerView.setAdapter(adapter);
            bookingSummaryItemRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            /*  totalPrice.setText(String.format("%s %s", getResources().getString(R.string.Rs), new DecimalFormat(".##").format(getTotalPrice(genericCartList))));*/
            /* totalPrice.setText(String.format("%s %s", getResources().getString(R.string.Rs),  (int) Math.round(getTotalPrice(genericCartList))));*/
            totalPrice.setText(String.format("%s %s", getResources().getString(R.string.Rs), (int) Math.round(application.getTotalPrice())));//changed by dhaval
        }

        stylistLabel.setText(String.format("%s", model.AppointmentServices.get(0).RequestedTherapist.FullName));

//        totalPrice.setText(String.format("%s %s", getResources().getString(R.string.Rs), new DecimalFormat(".##").format(model.Price.sales)));
//        grossTotalAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), new DecimalFormat(".##").format(model.Price.sales)));
//        payableAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), new DecimalFormat(".##").format(model.Price._final)));
//        discountAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), model.Price.discount));
//        taxAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), new DecimalFormat(".##").format(model.Price.tax)));
//
//        serviceInfoContainer.setVisibility(View.VISIBLE);

        try {
            SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat stringToTime = new SimpleDateFormat("hh:mm:ss");

            Date date = stringToDate.parse(model.AppointmentServices.get(0).StartTimeInCenter);
            Date time = stringToTime.parse(model.AppointmentServices.get(0).StartTimeInCenter.substring(11, model.AppointmentServices.get(0).StartTime.length()));

            SimpleDateFormat dateToString = new SimpleDateFormat("dd MMM, yyyy");
            SimpleDateFormat timeToString = new SimpleDateFormat("hh:mm");

            String dateStr = dateToString.format(date);
            String timeStr = timeToString.format(time);

            dateTimeSlot.setText(String.format("%s @%s", dateStr, timeStr));
        } catch (ParseException e) {
            e.printStackTrace();
            dateTimeSlot.setText("-");
        }

        makePaymentOfflineBtn.setEnabled(true);
        makePaymentOnlineBtn.setEnabled(true);

        logInitiatedCheckoutEvent(model);
    }

    public double getTotalPrice(ArrayList<GenericCartModel> list) {
        double totalPrice = 0;
        for (int i = 0; i < list.size(); i++)
            totalPrice += (list.get(i).getPrice() * 1);
        return totalPrice;
    }

    private void showCashDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.payment_cash_dialog);
        dialog.setCancelable(true);

        TextView cashAmount = dialog.findViewById(R.id.cod_amount);
        Button cashPaymentProceedButton = dialog.findViewById(R.id.cash_payment_proceed_button);

//        if (application.cartHasServices()) {
//            cashAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), new DecimalFormat(".##").format(invoiceModel.Price._final)));
//        } else {
        cashAmount.setText(String.format("%s %s", getResources().getString(R.string.Rs), (int) Math.round(createOrderResponseModel.getPaymentSummary().getTotal())));
//        }

        cashPaymentProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (application.cartHasServices()) {
                    confirmReservation();
                } else {
                    ConfirmReservationResponseModel confirmReservationResponseModel = new ConfirmReservationResponseModel();
                    confirmReservationResponseModel.IsConfirmed = true;
                    reservationConfirmed(confirmReservationResponseModel);
                }
            }
        });

        dialog.show();
    }

    double amount;

    private void payOnline() {
        GuestModel guestModel = EnrichUtils.getUserData(this);

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();
        payUmoneyConfig.setPayUmoneyActivityTitle("Enrich");

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        if (application.cartHasServices()) {
            amount = invoiceModel.Price._final;
        } else {
            amount = createOrderResponseModel.getPaymentSummary().getTotal();
        }

        String txnId = System.currentTimeMillis() + "";
        String phone = "" + guestModel.MobileNumber;
//        String productName = "" + reserveSlotResponseModel.SlotBookings.get(0).Services.get(0).Service.name;
        String productName = "" + application.getCartItemNames();
        String firstName = "" + guestModel.FirstName;
        String lastName = "" + guestModel.LastName;
        String email = "" + guestModel.Email;
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = AppEnvironment.SANDBOX;
        builder.setAmount("" + new DecimalFormat(".##").format(amount))
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName + " " + lastName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();

            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, this, R.style.PayU_AppTheme_default, false);

        } catch (Exception e) {
            Log.e("PAY U", "ERROR: " + e.getMessage());
        }
    }

    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(
            final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5)).append("||||||");

        AppEnvironment appEnvironment = AppEnvironment.SANDBOX;
        stringBuilder.append(appEnvironment.salt());

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
    }

    public String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    /*
     * Logging events for Analytics
     */
    private void logAppointmentBooked(ConfirmOrderModel model) {
        // FACEBOOK - AppEvents
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, EnrichUtils.newGson().toJson(model));
        AppEventsLogger.newLogger(this).logPurchase(BigDecimal.valueOf(model.getAmount()), Currency.getInstance("INR"), params);
    }

    private void logPurchaseOnlineEvent(ConfirmOrderModel model) {
        Bundle params = new Bundle();
        params.putString("Amount", "" + model.getAmount());
        params.putString("StoreName", EnrichUtils.getHomeStore(this).Name);
        params.putString("UserPhoneNumber", EnrichUtils.getUserData(this).MobileNumber);
        params.putString("UserName", EnrichUtils.getUserData(this).FirstName + " " + EnrichUtils.getUserData(this).LastName);
        AppEventsLogger.newLogger(this).logEvent("PurchaseOnline", model.getAmount(), params);
    }

    private void logPurchaseOfflineEvent(ConfirmOrderModel model) {
        Bundle params = new Bundle();
        params.putString("Amount", "" + model.getAmount());
        params.putString("StoreName", EnrichUtils.getHomeStore(this).Name);
        params.putString("UserPhoneNumber", EnrichUtils.getUserData(this).MobileNumber);
        params.putString("UserName", EnrichUtils.getUserData(this).FirstName + " " + EnrichUtils.getUserData(this).LastName);
        AppEventsLogger.newLogger(this).logEvent("PurchaseOffline", model.getAmount(), params);
    }

    private void logFirebaseCheckoutProgress(GenericCartModel model) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(model.ServiceId));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, model.Name);
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Service");
        bundle.putString(FirebaseAnalytics.Param.PRICE, String.valueOf(model.Price));
        bundle.putString(FirebaseAnalytics.Param.QUANTITY, String.valueOf(model.getQuantity()));
        bundle.putString(FirebaseAnalytics.Param.CHECKOUT_STEP, String.valueOf(Constants.SERVICE_CHECKOUT_STEP_INITIATION));
        application.getFirebaseInstance().logEvent(FirebaseAnalytics.Event.CHECKOUT_PROGRESS, bundle);
    }

    private void logInitiatedCheckoutEvent(InvoiceModel model) {
        // Facebook - AppEvents
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, model.AppointmentGroupId);
        if (application.cartHasServices()) {
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "Service");
        } else if (application.cartHasProducts()) {
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "Products");
        } else if (application.cartHasPackages()) {
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "Package");
        }
        params.putString(AppEventsConstants.EVENT_PARAM_DESCRIPTION, EnrichUtils.getHomeStore(this).Name);
        params.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, application.getItemCount());
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");
        AppEventsLogger.newLogger(this).logEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT, model.Price._final, params);

        // Firebase
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, model.AppointmentGroupId);
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
        if (application.cartHasServices()) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Service");
        } else if (application.cartHasProducts()) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Products");
        } else if (application.cartHasPackages()) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Packages");
        }
        bundle.putString(FirebaseAnalytics.Param.PRICE, String.valueOf(model.Price));
        application.getFirebaseInstance().logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, bundle);
    }

    private void logFirebasePurchaseEvent(ConfirmOrderModel model) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
        bundle.putString(FirebaseAnalytics.Param.VALUE, String.valueOf(model.getAmount()));
        bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, model.getTransactionId());
        application.getFirebaseInstance().logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle);
    }

    private void logSegmentPayment(String paymentType, String cardType) {
        List<Properties> propertiesArrayList = new ArrayList<>();
        String st_amount = null;
        for (int i = 0; i < invoiceModel.AppointmentServices.size(); i++) {
            if (EnrichUtils.getUserData(this).IsMember == 1) {
                st_amount = application.getCartItems().get(i).MembershipPrice + "";
            } else {
                st_amount = application.getCartItems().get(i).getPrice() + "";
            }
            Properties properties = new Properties();
            properties.putValue("service", invoiceModel.AppointmentServices.get(i).Service.name);
            //  properties.putValue("category", invoiceModel.AppointmentServices.get(i).Service.CategoryName);
            properties.putValue("category", application.getCartItems().get(i).CategoryName);
            properties.putValue("stylist", invoiceModel.AppointmentServices.get(i).RequestedTherapist.FullName);
            properties.putValue("amount_st", st_amount);//changed by dhaval shah 28/6/19
            propertiesArrayList.add(properties);

        }

        Properties properties = new Properties()
                .putValue("user_id", EnrichUtils.getUserData(this).Id)
                .putValue("mobile", EnrichUtils.getUserData(this).MobileNumber)
                .putValue("salonid", EnrichUtils.getHomeStore(this).Id)
                .putValue("salon_name", EnrichUtils.getHomeStore(this).Name)
                .putValue("location", EnrichUtils.getHomeStore(this).Address)
                .putValue("area", EnrichUtils.getHomeStore(this).Area)
                .putValue("city", EnrichUtils.getHomeStore(this).City)
                .putValue("state", EnrichUtils.getHomeStore(this).State == null ? "" : EnrichUtils.getHomeStore(this).State.Name)
                .putValue("zipcode", EnrichUtils.getHomeStore(this).ZipCode)
                // .putValue("amount", invoiceModel.Price._final)
                .putValue("amount", (int) Math.round(application.getTotalPrice()) + "")//changed by dhaval shah 28/6/19
                .putValue("paymentType", paymentType)
                .putValue("cardType", cardType)
                .putValue("payAtSalon", paymentType.equalsIgnoreCase("Cash"))
                .putValue("services", propertiesArrayList);

        Analytics.with(this).track(Constants.SEGMENT_PAYMENT, properties);
    }

    private void logSegmentBookAppointment() {

        List<Properties> propertiesArrayList = new ArrayList<>();

        for (int i = 0; i < invoiceModel.AppointmentServices.size(); i++) {
            Properties properties = new Properties();
            properties.putValue("service", invoiceModel.AppointmentServices.get(i).Service.name);
            //properties.putValue("category", invoiceModel.AppointmentServices.get(i).Service.CategoryName);
            properties.putValue("category", application.getCartItems().get(i).CategoryName);//changed by dhaval shah28/6/19
            properties.putValue("stylist", invoiceModel.AppointmentServices.get(i).RequestedTherapist.FullName);
            //properties.putValue("amount", "" + invoiceModel.Price._final);
            properties.putValue("amount", "" + (int) Math.round(application.getTotalPrice()) + "");//changed by dhaval shah 28/6/19
            properties.putValue("salonid", EnrichUtils.getHomeStore(this).Id);
            properties.putValue("salon_name", EnrichUtils.getHomeStore(this).Name);
            properties.putValue("location", EnrichUtils.getHomeStore(this).Address);
            properties.putValue("area", EnrichUtils.getHomeStore(this).Area);//Area instead of city by dhaval shah24/6/19
            properties.putValue("city", EnrichUtils.getHomeStore(this).City);
            properties.putValue("state", EnrichUtils.getHomeStore(this).State == null ? "" : EnrichUtils.getHomeStore(this).State.Name);
            properties.putValue("zipcode", EnrichUtils.getHomeStore(this).ZipCode);

            propertiesArrayList.add(properties);

        }

        Properties properties = new Properties()
                .putValue("user_id", EnrichUtils.getUserData(this).Id)
                .putValue("mobile", EnrichUtils.getUserData(this).MobileNumber)
                .putValue("appointmentDateTime", invoiceModel.AppointmentServices.get(0).StartTime)
                .putValue("services", propertiesArrayList);

        Analytics.with(this).track(Constants.SEGMENT_BOOK_APPOINTMENT, properties);
    }

    private void logSegmentBookSummary() {
        List<Properties> propertiesArrayList = new ArrayList<>();

        for (int i = 0; i < invoiceModel.AppointmentServices.size(); i++) {

            Properties properties = new Properties();
            properties.putValue("service", invoiceModel.AppointmentServices.get(i).Service.name);
            // properties.putValue("category", invoiceModel.AppointmentServices.get(i).Service.CategoryName);
            properties.putValue("category", application.getCartItems().get(i).CategoryName);//changed by dhaval shah 28/6/19
            //application.getCartItems().get(i).CategoryName
            properties.putValue("stylist", invoiceModel.AppointmentServices.get(i).RequestedTherapist.FullName);
            // properties.putValue("amount", "" + invoiceModel.Price._final);
            properties.putValue("amount", "" + (int) Math.round(application.getTotalPrice()) + "");//changed by dhaval shah 28/6/19
            propertiesArrayList.add(properties);
        }

        Properties properties = new Properties()
                .putValue("user_id", EnrichUtils.getUserData(this).Id)
                .putValue("mobile", EnrichUtils.getUserData(this).MobileNumber)
                .putValue("salonid", EnrichUtils.getHomeStore(this).Id)
                .putValue("salon_name", EnrichUtils.getHomeStore(this).Name)
                .putValue("location", EnrichUtils.getHomeStore(this).Address)
                .putValue("area", EnrichUtils.getHomeStore(this).Area)//area instead of empty string by dhaval shah24/6/19
                .putValue("city", EnrichUtils.getHomeStore(this).City)
                .putValue("state", EnrichUtils.getHomeStore(this).State == null ? "" : EnrichUtils.getHomeStore(this).State.Name)
                .putValue("zipcode", EnrichUtils.getHomeStore(this).ZipCode)
                .putValue("gross_amount", invoiceModel.Price.sales)
                .putValue("tax_amount", invoiceModel.Price.tax)
                .putValue("total_amount", invoiceModel.Price._final)
                .putValue("payAtSalon", isOnlinePayment)
                .putValue("services", propertiesArrayList);

        Analytics.with(this).track(Constants.SEGMENT_BOOK_SUMMARY, properties);
    }

    private void logSegmentBookingStatus(String status) {
        List<Properties> propertiesArrayList = new ArrayList<>();
        if (invoiceModel != null) {
            if (invoiceModel.AppointmentServices != null) {
                for (int i = 0; i < invoiceModel.AppointmentServices.size(); i++) {
                    Properties properties = new Properties();
                    properties.putValue("service", invoiceModel.AppointmentServices.get(i).Service.name);
                    //properties.putValue("category", invoiceModel.AppointmentServices.get(i).Service.CategoryName);
                    if( application.getCartItems().get(i).CategoryName != null) {
                        properties.putValue("category", application.getCartItems().get(i).CategoryName);//changed by dhaval shah 28/6/19
                    }
                    properties.putValue("stylist", invoiceModel.AppointmentServices.get(i).RequestedTherapist.FullName);
                    // properties.putValue("amount", "" + invoiceModel.Price._final);
                    properties.putValue("amount", "" + (int) Math.round(application.getTotalPrice()));//changed by dhaval shah 28/6/19
                    propertiesArrayList.add(properties);
                }
            }
        }

        Properties properties = new Properties()
                .putValue("user_id", EnrichUtils.getUserData(this).Id)
                .putValue("mobile", EnrichUtils.getUserData(this).MobileNumber)
                .putValue("salonid", EnrichUtils.getHomeStore(this).Id)
                .putValue("salon_name", EnrichUtils.getHomeStore(this).Name)
                .putValue("location", EnrichUtils.getHomeStore(this).Address)
                .putValue("area", EnrichUtils.getHomeStore(this).Area)//area instead of empty string by dhaval shah24/6/19
                .putValue("city", EnrichUtils.getHomeStore(this).City)
                .putValue("state", EnrichUtils.getHomeStore(this).State == null ? "" : EnrichUtils.getHomeStore(this).State.Name)
                .putValue("zipcode", EnrichUtils.getHomeStore(this).ZipCode)
                //  .putValue("total_amount", invoiceModel.Price._final)
                .putValue("total_amount", "" + (int) Math.round(application.getTotalPrice()))//changed by dhaval shah 28/6/19
                .putValue("status", status)
                .putValue("services", propertiesArrayList);

        Analytics.with(this).track(Constants.SEGMENT_BOOKING_STATUS, properties);
    }

    private void logSegmentProducts() {
        List<Properties> propertiesArrayList = new ArrayList<>();

        for (int i = 0; i < application.getCartItems().size(); i++) {
            Properties properties = new Properties();

            properties.putValue("product_name", application.getCartItems().get(i).Name);
            properties.putValue("description", application.getCartItems().get(i).getDescription());
            properties.putValue("quantity", "" + application.getCartItems().get(i).getQuantity());
            properties.putValue("amount", "" + application.getCartItems().get(i).getPrice());

            propertiesArrayList.add(properties);
        }

        Properties properties = new Properties();
        properties.putValue("user_id", EnrichUtils.getUserData(this).Id)
                .putValue("mobile", EnrichUtils.getUserData(this).MobileNumber);
        properties.putValue("products", propertiesArrayList);

        Analytics.with(this).track(Constants.SEGMENT_CHECKOUT, properties);
    }
}
