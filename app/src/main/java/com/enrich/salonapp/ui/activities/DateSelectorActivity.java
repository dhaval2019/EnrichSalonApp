package com.enrich.salonapp.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.AppointmentModels.AppointmentRequestModel;
import com.enrich.salonapp.data.model.AppointmentModels.AppointmentRequestedTherapistModel;
import com.enrich.salonapp.data.model.AppointmentModels.AppointmentServiceModel;
import com.enrich.salonapp.data.model.AppointmentModels.AppointmentServicesModel;
import com.enrich.salonapp.data.model.AppointmentModels.AppointmentSlotBookingsModel;
import com.enrich.salonapp.data.model.AvailableTimeResponseModel;
import com.enrich.salonapp.data.model.CenterDetailModel;
import com.enrich.salonapp.data.model.GenericCartModel;
import com.enrich.salonapp.data.model.SlotModel;
import com.enrich.salonapp.data.remote.RemoteDataSource;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.SlotsAdapter;
import com.enrich.salonapp.ui.contracts.TimeSlotContract;
import com.enrich.salonapp.ui.presenters.TimeSlotPresenter;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DateSelectorActivity extends BaseActivity implements DatePickerListener, TimeSlotContract.View {

    @BindView(R.id.date_time_slot_recycler_view)
    RecyclerView dateTimeSlotRecyclerView;

    @BindView(R.id.stylist_recycler_view_container)
    LinearLayout stylistRecyclerViewContainer;

    @BindView(R.id.datePicker)
    HorizontalPicker picker;

    @BindView(R.id.time_slot_proceed)
    Button timeSlotProceed;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

//    @BindView(R.id.no_slots_available)
//    TextView noSlotsAvailable;

    @BindView(R.id.store_phone_number)
    TextView storePhoneNumber;

    @BindView(R.id.no_slots_available_container)
    LinearLayout noSlotsAvailableContainer;

    @BindView(R.id.no_slots_phone_number_container)
    LinearLayout noSlotsPhoneNumberContainer;

    ArrayList<GenericCartModel> cartList;

    EnrichApplication application;

    DateTime selectedDate;

    AppointmentRequestModel appointmentRequestModel;

    DataRepository dataRepository;
    TimeSlotPresenter timeSlotPresenter;

    ArrayList<AppointmentSlotBookingsModel> slotBookingsModels;
    ArrayList<AppointmentServicesModel> servicesModelArrayList;

    CenterDetailModel centerDetailModel;

    Tracker mTracker;

    BottomSheetDialog dialog;

    boolean isHomeSelected;
    boolean isRebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selector);

        ButterKnife.bind(this);

        isHomeSelected = getIntent().getBooleanExtra("isHomeSelected", false);
        isRebook = getIntent().getBooleanExtra("isRebook", false);

        application = (EnrichApplication) getApplication();
        cartList = application.getCartItems();

        if (isRebook) {
            centerDetailModel = EnrichUtils.getHomeStoreForRebook(this);
        } else {
            centerDetailModel = EnrichUtils.getHomeStore(this);
        }

        // SEND ANALYTICS
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Date Time Slot List Screen");
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

        picker = findViewById(R.id.datePicker);
        picker.setListener(this)
                .setMonthAndYearTextColor(Color.parseColor("#424242"))
                .setDateSelectedColor(Color.parseColor("#d69e5c"))
                .setDateSelectedTextColor(Color.parseColor("#ffffff"))
                .setUnselectedDayTextColor(Color.parseColor("#9e9e9e"))
                .setUnselectedDayTextColor(Color.parseColor("#BDBDBD"))
                .setTodayButtonTextColor(Color.parseColor("#9e9e9e"))
                .setOffset(3)
                .showTodayButton(true)
                .init();

        picker.setDate(new DateTime());

        timeSlotProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DateSelectorActivity.this, CartActivity.class);
                intent.putExtra("isHomeSelected", isHomeSelected);
                startActivity(intent);
            }
        });

        dataRepository = Injection.provideDataRepository(this, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        timeSlotPresenter = new TimeSlotPresenter(this, dataRepository);

        storePhoneNumber.setText(String.format("Call %s on %s to check and book your appointment", centerDetailModel.Name, centerDetailModel.Phone));
        Linkify.addLinks(storePhoneNumber, Linkify.ALL);

        storePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = centerDetailModel.Phone;

                //call phone
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDateSelected(DateTime dateSelected) {
        if (dateSelected.isBefore(new DateTime()))
            picker.setDate(new DateTime());

        selectedDate = dateSelected;

        slotBookingsModels = new ArrayList<>();
        AppointmentSlotBookingsModel slotBookingsModel = new AppointmentSlotBookingsModel();

        servicesModelArrayList = new ArrayList<>();
        AppointmentServicesModel servicesModel = new AppointmentServicesModel();

        for (int i = 0; i < cartList.size(); i++) {

            AppointmentServiceModel serviceModel = new AppointmentServiceModel();
            serviceModel.Id = cartList.get(i).ServiceId;

            servicesModel.Service = serviceModel;

            if (cartList.get(i).therapistModel != null) {
                AppointmentRequestedTherapistModel requestedTherapistModel = new AppointmentRequestedTherapistModel();
                requestedTherapistModel.Id = cartList.get(i).therapistModel.Id;
                servicesModel.RequestedTherapist = requestedTherapistModel;
            }

            servicesModelArrayList.add(servicesModel);

            slotBookingsModel.Services = servicesModelArrayList;
            slotBookingsModel.Quantity = 1;
//            slotBookingsModel.TherapistId = cartList.get(i).therapistModel.Id;
        }

        slotBookingsModels.add(slotBookingsModel);

        appointmentRequestModel = new AppointmentRequestModel();
        appointmentRequestModel.CenterDate = selectedDate.toString();
        appointmentRequestModel.CenterId = centerDetailModel.Id;
        appointmentRequestModel.RequiredSlotsCount = 100;
        appointmentRequestModel.SlotBookings = slotBookingsModels;

        EnrichUtils.log(EnrichUtils.newGson().toJson(appointmentRequestModel));

        if (isHomeSelected) {
            timeSlotPresenter.getTimeSlots(this, RemoteDataSource.HOST + RemoteDataSource.GET_HOME_DATE_TME_SLOTS, appointmentRequestModel);
        } else {
            timeSlotPresenter.getTimeSlots(this, RemoteDataSource.HOST + RemoteDataSource.GET_SALON_DATE_TME_SLOTS, appointmentRequestModel);
        }
    }

    @Override
    public void setTimeSlot(AvailableTimeResponseModel model) {
        if (model.Error == null) {
            if (model.OpenSlots.size() != 0) {
                setSlotAdapter(model.OpenSlots);
            } else {
                noSlotsAvailableContainer.setVisibility(View.VISIBLE);
                dateTimeSlotRecyclerView.setVisibility(View.GONE);
                EnrichUtils.showMessage(DateSelectorActivity.this, "No slots available");
            }
        } else {
            showErrorDialog(model.Error.InternalMessage);
        }
    }

    @Override
    public void noTimeSlot() {
        noSlotsAvailableContainer.setVisibility(View.VISIBLE);
        dateTimeSlotRecyclerView.setVisibility(View.GONE);
        EnrichUtils.showMessage(DateSelectorActivity.this, "No slots available");
    }

    private void setSlotAdapter(ArrayList<SlotModel> list) {
        ArrayList<SlotModel> tempList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            try {
                SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd");
                Date date = stringToDate.parse(list.get(i).Time);

                if (date.equals(selectedDate.toDate())) {
                    tempList.add(list.get(i));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (tempList.size() != 0) {
            noSlotsAvailableContainer.setVisibility(View.GONE);
            dateTimeSlotRecyclerView.setVisibility(View.VISIBLE);
            SlotsAdapter slotsAdapter = new SlotsAdapter(DateSelectorActivity.this, tempList, DateSelectorActivity.this);
            dateTimeSlotRecyclerView.setAdapter(slotsAdapter);
            dateTimeSlotRecyclerView.setLayoutManager(new GridLayoutManager(DateSelectorActivity.this, 4));
        } else {
            noSlotsAvailableContainer.setVisibility(View.VISIBLE);
            dateTimeSlotRecyclerView.setVisibility(View.GONE);
            EnrichUtils.showMessage(DateSelectorActivity.this, "No slots available");
        }
    }

    public void setDateToReserveSlot(String date) {
        for (int i = 0; i < cartList.size(); i++) {
            cartList.get(i).SlotTime = date;
            logFirebaseCheckoutProgress(cartList.get(i));
        }

        timeSlotProceed.setEnabled(true);
        timeSlotProceed.setBackgroundResource(R.drawable.gold_bg_gradient_curved);


    }

    private void logFirebaseCheckoutProgress(GenericCartModel model) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(model.ServiceId));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, model.Name);
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Service");
        bundle.putString(FirebaseAnalytics.Param.PRICE, String.valueOf(model.Price));
        bundle.putString(FirebaseAnalytics.Param.QUANTITY, String.valueOf(model.getQuantity()));
        bundle.putString(FirebaseAnalytics.Param.CHECKOUT_STEP, String.valueOf(Constants.SERVICE_CHECKOUT_STEP_SELECT_TIME_SLOT));
        application.getFirebaseInstance().logEvent(FirebaseAnalytics.Event.CHECKOUT_PROGRESS, bundle);
    }

    private void showErrorDialog(String message) {
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.forgot_password_success);

        dialog.setCancelable(true);

        TextView therapistDialogTitle = dialog.findViewById(R.id.therapist_dialog_title);
        TextView dialogText = dialog.findViewById(R.id.dialog_text);
        TextView openMailApp = dialog.findViewById(R.id.open_mail_app);

        therapistDialogTitle.setText("Error");
        dialogText.setText(message);
        openMailApp.setText("OK");

        openMailApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
