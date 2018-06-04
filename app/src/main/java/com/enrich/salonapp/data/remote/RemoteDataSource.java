package com.enrich.salonapp.data.remote;

import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.AppointmentModels.AppointmentRequestModel;
import com.enrich.salonapp.data.model.AppointmentResponseModel;
import com.enrich.salonapp.data.model.AuthenticationModel;
import com.enrich.salonapp.data.model.AuthenticationRequestModel;
import com.enrich.salonapp.data.model.AvailableTimeResponseModel;
import com.enrich.salonapp.data.model.CancelRequestModel;
import com.enrich.salonapp.data.model.CancelResponseModel;
import com.enrich.salonapp.data.model.CategoryResponseModel;
import com.enrich.salonapp.data.model.CenterResponseModel;
import com.enrich.salonapp.data.model.ChangePasswordRequestModel;
import com.enrich.salonapp.data.model.ChangePasswordResponseModel;
import com.enrich.salonapp.data.model.CheckUserNameResponseModel;
import com.enrich.salonapp.data.model.ConfirmOrderRequestModel;
import com.enrich.salonapp.data.model.ConfirmOrderResponseModel;
import com.enrich.salonapp.data.model.ConfirmReservationRequestModel;
import com.enrich.salonapp.data.model.ConfirmReservationResponseModel;
import com.enrich.salonapp.data.model.CreateOTPRequestModel;
import com.enrich.salonapp.data.model.CreateOTPResponseModel;
import com.enrich.salonapp.data.model.CreateOrderRequestModel;
import com.enrich.salonapp.data.model.CreateOrderResponseModel;
import com.enrich.salonapp.data.model.GuestResponseModel;
import com.enrich.salonapp.data.model.GuestUpdateRequestModel;
import com.enrich.salonapp.data.model.GuestUpdateResponseModel;
import com.enrich.salonapp.data.model.InvoiceResponseModel;
import com.enrich.salonapp.data.model.NewAndPopularResponseModel;
import com.enrich.salonapp.data.model.OfferResponseModel;
import com.enrich.salonapp.data.model.RegistrationRequestModel;
import com.enrich.salonapp.data.model.RegistrationResponseModel;
import com.enrich.salonapp.data.model.ReserveSlotRequestModel;
import com.enrich.salonapp.data.model.ReserveSlotResponseModel;
import com.enrich.salonapp.data.model.ServiceListResponseModel;
import com.enrich.salonapp.data.model.TherapistResponseModel;
import com.enrich.salonapp.data.model.UserExistsResponseModel;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteDataSource extends DataSource {

    public static final String HOST = "http://137.59.54.53/EnrichAPI/api/"; // STAGING
//    public static final String HOST = "http://13.71.113.69/EnrichAPI/api/"; // PROD

    public static final String IS_USER_REGISTERED = "Catalog/Guests/IsRegisteredUser";

    public static final String GET_INVOICE = "Catalog/Invoices/";

    public static final String GET_UPCOMING_APPOINTMENT = "Catalog/Guests/UpcomingAppointments?Page=0&Size=100";

    public static final String GET_PAST_APPOINTMENT = "Catalog/Guests/PastAppointments?Page=0&Size=100";

    private static RemoteDataSource remoteDataSource;

    private ApiService apiService;

    private RemoteDataSource(MainUiThread mainUiThread,
                             ThreadExecutor threadExecutor, ApiService apiService) {
        super(mainUiThread, threadExecutor);
        this.apiService = apiService;
    }

    public static synchronized RemoteDataSource getInstance(MainUiThread mainUiThread,
                                                            ThreadExecutor threadExecutor,
                                                            ApiService apiService) {
        if (remoteDataSource == null) {
            remoteDataSource = new RemoteDataSource(mainUiThread, threadExecutor, apiService);
        }
        return remoteDataSource;
    }

    @Override
    public void isUserRegistered(String phoneNumber, final GetIsUserRegisteredCallBack callBack) {
        Map<String, String> map = new HashMap<>();
        map.put("MobileNumber", phoneNumber);

        Call<UserExistsResponseModel> call = apiService.isUserRegistered(HOST + IS_USER_REGISTERED, map);
        call.enqueue(new Callback<UserExistsResponseModel>() {
            @Override
            public void onResponse(Call<UserExistsResponseModel> call, Response<UserExistsResponseModel> response) {
                callBack.onSuccess(response.code());
            }

            @Override
            public void onFailure(Call<UserExistsResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getAuthenticationToken(final AuthenticationRequestModel model, final GetAuthenticationCallBack callBack) {

        Call<AuthenticationModel> call = apiService.getAuthenticationToken(model);
        call.enqueue(new Callback<AuthenticationModel>() {
            @Override
            public void onResponse(Call<AuthenticationModel> call, Response<AuthenticationModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
                else
                    callBack.onFailure(new Throwable());
            }

            @Override
            public void onFailure(Call<AuthenticationModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getGuestData(String guestId, final GetGuestDataCallBack callBack) {

        Map<String, String> map = new HashMap<>();
        map.put("GuestId", guestId);

        Call<GuestResponseModel> call = apiService.getUserData(map);
        call.enqueue(new Callback<GuestResponseModel>() {
            @Override
            public void onResponse(Call<GuestResponseModel> call, Response<GuestResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body().Guest);
            }

            @Override
            public void onFailure(Call<GuestResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void checkUsername(String userName, final CheckUserNameCallBack callBack) {

        Map<String, String> map = new HashMap<>();
        map.put("userName", userName);

        Call<CheckUserNameResponseModel> call = apiService.checkForUserName(map);
        call.enqueue(new Callback<CheckUserNameResponseModel>() {
            @Override
            public void onResponse(Call<CheckUserNameResponseModel> call, Response<CheckUserNameResponseModel> response) {
                callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<CheckUserNameResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void createOTP(CreateOTPRequestModel model, final CreateOTPCallBack callBack) {

        Call<CreateOTPResponseModel> call = apiService.createOTP(model);
        call.enqueue(new Callback<CreateOTPResponseModel>() {
            @Override
            public void onResponse(Call<CreateOTPResponseModel> call, Response<CreateOTPResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<CreateOTPResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void registerUser(RegistrationRequestModel model, final RegisterUserCallBack callBack) {

        Call<RegistrationResponseModel> call = apiService.register(model);
        call.enqueue(new Callback<RegistrationResponseModel>() {
            @Override
            public void onResponse(Call<RegistrationResponseModel> call, Response<RegistrationResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<RegistrationResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getCenterList(Map<String, String> map, final GetCenterListCallBack callBack) {

        Call<CenterResponseModel> call = apiService.getCenter(map);
        call.enqueue(new Callback<CenterResponseModel>() {
            @Override
            public void onResponse(Call<CenterResponseModel> call, Response<CenterResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<CenterResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getOffers(final GetOfferListCallBack callBack) {

        Call<OfferResponseModel> call = apiService.getAllOffers();
        call.enqueue(new Callback<OfferResponseModel>() {
            @Override
            public void onResponse(Call<OfferResponseModel> call, Response<OfferResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<OfferResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getCategoryList(Map<String, String> map, final GetCategoryListCallBack callBack) {

        Call<CategoryResponseModel> call = apiService.getAllCategories(map);
        call.enqueue(new Callback<CategoryResponseModel>() {
            @Override
            public void onResponse(Call<CategoryResponseModel> call, Response<CategoryResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<CategoryResponseModel> call, Throwable t) {
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getAppointments(String url, final GetAppointmentsCallBack callBack) {

        Call<AppointmentResponseModel> call = apiService.getAppointmentsList(url);
        call.enqueue(new Callback<AppointmentResponseModel>() {
            @Override
            public void onResponse(Call<AppointmentResponseModel> call, Response<AppointmentResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<AppointmentResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void updateUser(GuestUpdateRequestModel model, final UpdateUserCallBack callBack) {

        Call<GuestUpdateResponseModel> call = apiService.updateGuest(model);
        call.enqueue(new Callback<GuestUpdateResponseModel>() {
            @Override
            public void onResponse(Call<GuestUpdateResponseModel> call, Response<GuestUpdateResponseModel> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<GuestUpdateResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });

    }

    @Override
    public void changePassword(ChangePasswordRequestModel model, final ChangePasswordCallBack callBack) {
        Call<ChangePasswordResponseModel> call = apiService.changePassword(model);
        call.enqueue(new Callback<ChangePasswordResponseModel>() {
            @Override
            public void onResponse(Call<ChangePasswordResponseModel> call, Response<ChangePasswordResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ChangePasswordResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getServiceList(Map<String, String> map, final GetServiceListCallBack callBack) {
        EnrichUtils.log(EnrichUtils.newGson().toJson(map));
        Call<ServiceListResponseModel> call = apiService.getServicesByCategories(map);
        call.enqueue(new Callback<ServiceListResponseModel>() {
            @Override
            public void onResponse(Call<ServiceListResponseModel> call, Response<ServiceListResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ServiceListResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getTherapist(final Map<String, String> map, final GetTherapistCallBack callBack) {
        Call<TherapistResponseModel> call = apiService.getTherapistList(map);
        call.enqueue(new Callback<TherapistResponseModel>() {
            @Override
            public void onResponse(Call<TherapistResponseModel> call, Response<TherapistResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<TherapistResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getTimeSlots(AppointmentRequestModel model, final GetTimeSlotsCallBack callBack) {
        EnrichUtils.log(EnrichUtils.newGson().toJson(model));

        Call<AvailableTimeResponseModel> call = apiService.getTimeSlots(model);
        call.enqueue(new Callback<AvailableTimeResponseModel>() {
            @Override
            public void onResponse(Call<AvailableTimeResponseModel> call, Response<AvailableTimeResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<AvailableTimeResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void reserveSlot(ReserveSlotRequestModel model, final ReserveSlotCallBack callBack) {
        Call<ReserveSlotResponseModel> call = apiService.reserveSlot(model);
        call.enqueue(new Callback<ReserveSlotResponseModel>() {
            @Override
            public void onResponse(Call<ReserveSlotResponseModel> call, Response<ReserveSlotResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ReserveSlotResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void createOrder(CreateOrderRequestModel model, final CreateOrderCallBack callBack) {
        Call<CreateOrderResponseModel> call = apiService.createOrder(model);
        call.enqueue(new Callback<CreateOrderResponseModel>() {
            @Override
            public void onResponse(Call<CreateOrderResponseModel> call, Response<CreateOrderResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<CreateOrderResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getInvoice(String url, final GetInvoiceCallBack callBack) {
        Call<InvoiceResponseModel> call = apiService.getInvoice(url);
        call.enqueue(new Callback<InvoiceResponseModel>() {
            @Override
            public void onResponse(Call<InvoiceResponseModel> call, Response<InvoiceResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<InvoiceResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void confirmReservation(final ConfirmReservationRequestModel model, final ConfirmReservationCallBack callBack) {
        Call<ConfirmReservationResponseModel> call = apiService.confirmReservation(model);
        call.enqueue(new Callback<ConfirmReservationResponseModel>() {
            @Override
            public void onResponse(Call<ConfirmReservationResponseModel> call, Response<ConfirmReservationResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ConfirmReservationResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void confirmOrder(final ConfirmOrderRequestModel model, final ConfirmOrderCallBack callBack) {
        Call<ConfirmOrderResponseModel> call = apiService.confirmOrder(model);
        call.enqueue(new Callback<ConfirmOrderResponseModel>() {
            @Override
            public void onResponse(Call<ConfirmOrderResponseModel> call, Response<ConfirmOrderResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ConfirmOrderResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getNewAndPopularServices(Map<String, String> map, final NewAndPopularServicesCallBack callBack) {
        Call<NewAndPopularResponseModel> call = apiService.getNewAndPopularService(map);
        call.enqueue(new Callback<NewAndPopularResponseModel>() {
            @Override
            public void onResponse(Call<NewAndPopularResponseModel> call, Response<NewAndPopularResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<NewAndPopularResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void cancelAppointment(String url, final CancelRequestModel model, final CancelAppointmentCallBack callBack) {
        Call<CancelResponseModel> call = apiService.cancelAppointment(url, model);
        call.enqueue(new Callback<CancelResponseModel>() {
            @Override
            public void onResponse(Call<CancelResponseModel> call, Response<CancelResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<CancelResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }
}
