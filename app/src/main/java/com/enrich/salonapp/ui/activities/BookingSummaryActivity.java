package com.enrich.salonapp.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.ConfirmOrderModel;
import com.enrich.salonapp.data.model.ConfirmOrderRequestModel;
import com.enrich.salonapp.data.model.ConfirmOrderResponseModel;
import com.enrich.salonapp.data.model.ConfirmReservationRequestModel;
import com.enrich.salonapp.data.model.ConfirmReservationResponseModel;
import com.enrich.salonapp.data.model.CreateOrderRequestModel;
import com.enrich.salonapp.data.model.CreateOrderResponseModel;
import com.enrich.salonapp.data.model.CreateOrderServiceModel;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.data.model.InvoiceModel;
import com.enrich.salonapp.data.model.InvoiceResponseModel;
import com.enrich.salonapp.data.model.ReserveSlotRequestModel;
import com.enrich.salonapp.data.model.ReserveSlotResponseModel;
import com.enrich.salonapp.data.remote.RemoteDataSource;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.BookingSummaryItemAdapter;
import com.enrich.salonapp.ui.contracts.BookingSummaryContract;
import com.enrich.salonapp.ui.presenters.BookingSummaryPresenter;
import com.enrich.salonapp.util.AppEnvironment;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.payumoney.core.SdkSession.paymentId;

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

    BookingSummaryItemAdapter adapter;

    EnrichApplication application;

    ReserveSlotResponseModel reserveSlotResponseModel;
    InvoiceModel invoiceModel;
    ReserveSlotRequestModel reserveSlotModel;
    ConfirmReservationResponseModel confirmReservationResponseModel;

    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

    CreateOrderResponseModel createOrderResponseModel;

    DataRepository dataRepository;
    BookingSummaryPresenter bookingSummaryPresenter;

    boolean isOnlinePayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        ButterKnife.bind(this);

        application = (EnrichApplication) getApplication();
        reserveSlotModel = EnrichUtils.newGson().fromJson(getIntent().getStringExtra("ReserveSlotModel"), ReserveSlotRequestModel.class);

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

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(this, mainUiThread, threadExecutor, null);
        bookingSummaryPresenter = new BookingSummaryPresenter(this, dataRepository);

        adapter = new BookingSummaryItemAdapter(this, application.getCartItems());
        bookingSummaryItemRecyclerView.setAdapter(adapter);
        bookingSummaryItemRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

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
                showCashDialog();
            }
        });

        makePaymentOfflineBtn.setEnabled(false);
        makePaymentOnlineBtn.setEnabled(false);

        bookingSummaryPresenter.reserveSlot(this, reserveSlotModel);
    }

    @Override
    public void slotReserved(ReserveSlotResponseModel model) {
        if (model.IsReserved) {
            reserveSlotResponseModel = model;

            ArrayList<CreateOrderServiceModel> createOrderServiceModels = new ArrayList<>();
            for (int i = 0; i < application.getCartItems().size(); i++) {
                CreateOrderServiceModel createOrderServiceModel = new CreateOrderServiceModel();
                createOrderServiceModel.setServiceId(application.getCartItems().get(i).Id);
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

            EnrichUtils.log(EnrichUtils.newGson().toJson(createOrderRequestModel));

            bookingSummaryPresenter.createOrder(BookingSummaryActivity.this, createOrderRequestModel);
        } else {
            EnrichUtils.showMessage(BookingSummaryActivity.this, "" + model.Error.Message);
        }
    }

    @Override
    public void orderCreated(CreateOrderResponseModel model) {
        if (model.getOrder() != null) {
            createOrderResponseModel = model;
            bookingSummaryPresenter.getInvoice(this, RemoteDataSource.HOST + RemoteDataSource.GET_INVOICE + reserveSlotResponseModel.InvoiceId);
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

            ConfirmOrderModel confirmOrderModel = new ConfirmOrderModel();
            confirmOrderModel.setAmount(createOrderResponseModel.getPaymentSummary().getPayableAmount());
            confirmOrderModel.setFirstName(EnrichUtils.getUserData(this).FirstName);
            confirmOrderModel.setLastName(EnrichUtils.getUserData(this).LastName);
            confirmOrderModel.setPhone(EnrichUtils.getUserData(this).MobileNumber);
            confirmOrderModel.setEmailAddress(EnrichUtils.getUserData(this).Email);
            confirmOrderModel.setReservationId(reserveSlotResponseModel.ReservationId);

            if (isOnlinePayment) { //ONLINE
                confirmOrderModel.setTransactionId(mPaymentParams.getParams().get(PayUmoneyConstants.TXNID));
                confirmOrderModel.setPaymentId(mPaymentParams.getParams().get(PayUmoneyConstants.TXNID));
                confirmOrderModel.setModeOfPayment(1);
                confirmOrderModel.setPaymentStatus(1);
            } else { // CASH
                confirmOrderModel.setTransactionId(System.currentTimeMillis() + "");
                confirmOrderModel.setPaymentId(System.currentTimeMillis() + "");
                confirmOrderModel.setModeOfPayment(2);
                confirmOrderModel.setPaymentStatus(1);
            }

            ConfirmOrderRequestModel confirmOrderRequestModel = new ConfirmOrderRequestModel();
            confirmOrderRequestModel.setOrderId(createOrderResponseModel.getOrder().getOrderId());
            confirmOrderRequestModel.setConfirmOrder(confirmOrderModel);

            confirmReservationResponseModel = model;

            bookingSummaryPresenter.confirmOrder(BookingSummaryActivity.this, confirmOrderRequestModel);
        } else {
            EnrichUtils.showMessage(BookingSummaryActivity.this, "" + model.Error.Message);
        }
    }

    @Override
    public void orderConfirmed(ConfirmOrderResponseModel model) {
        if (model.getConfirmOrder() != null) {
            Intent intent = new Intent(BookingSummaryActivity.this, ReceiptActivity.class);
            intent.putExtra("ConfirmReservationResponseModel", EnrichUtils.newGson().toJson(confirmReservationResponseModel));
            intent.putExtra("InvoiceModel", EnrichUtils.newGson().toJson(invoiceModel));
            startActivity(intent);
        } else {
            EnrichUtils.showMessage(BookingSummaryActivity.this, model.getError().Message);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EnrichUtils.log("request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction; Now confirm reservation
//                    createOrder();
                    confirmReservation();
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

    private void setData(InvoiceModel model) {
        totalPrice.setText(getResources().getString(R.string.Rs) + " " + application.getTotalPrice());
        grossTotalAmount.setText(getResources().getString(R.string.Rs) + " " + model.Price.sales);
        stylistLabel.setText("" + model.AppointmentServices.get(0).RequestedTherapist.FullName);
        payableAmount.setText(getResources().getString(R.string.Rs) + " " + model.Price._final);
        discountAmount.setText(getResources().getString(R.string.Rs) + " " + model.Price.discount);
        taxAmount.setText(getResources().getString(R.string.Rs) + " " + model.Price.tax);

        try {
            SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat stringToTime = new SimpleDateFormat("hh:mm:ss");

            Date date = stringToDate.parse(model.AppointmentServices.get(0).StartTimeInCenter);
            Date time = stringToTime.parse(model.AppointmentServices.get(0).StartTimeInCenter.substring(11, model.AppointmentServices.get(0).StartTime.length()));

            SimpleDateFormat dateToString = new SimpleDateFormat("dd MMM, yyyy");
            SimpleDateFormat timeToString = new SimpleDateFormat("hh:mm");

            String dateStr = dateToString.format(date);
            String timeStr = timeToString.format(time);

            dateTimeSlot.setText(dateStr + " @" + timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            dateTimeSlot.setText("-");
        }

        makePaymentOfflineBtn.setEnabled(true);
        makePaymentOnlineBtn.setEnabled(true);
    }

    private void showCashDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.payment_cash_dialog);
        dialog.setCancelable(true);

        TextView cashAmount = dialog.findViewById(R.id.cod_amount);
        Button cashPaymentProceedButton = dialog.findViewById(R.id.cash_payment_proceed_button);

        cashAmount.setText(getResources().getString(R.string.Rs) + " " + invoiceModel.Price._final);

        cashPaymentProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmReservation();
            }
        });

        dialog.show();
    }

    private void payOnline() {
        GuestModel guestModel = EnrichUtils.getUserData(this);

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();
        payUmoneyConfig.setPayUmoneyActivityTitle("Enrich");

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = invoiceModel.Price._final;
        String txnId = System.currentTimeMillis() + "";
        String phone = "" + guestModel.MobileNumber;
        String productName = "" + reserveSlotResponseModel.SlotBookings.get(0).Services.get(0).Service.name;
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
        builder.setAmount(amount)
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

            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, this, R.style.AppTheme_default, false);

        } catch (Exception e) {
            Log.e("PAY U", "ERROR: " + e.getMessage());
        }
    }

    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");

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
}
