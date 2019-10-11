package com.enrich.salonapp.data.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.AddressModel;
import com.enrich.salonapp.data.model.AddressResponseModel;
import com.enrich.salonapp.data.model.AppUpdateResponseModel;
import com.enrich.salonapp.data.model.AppointmentModels.AppointmentRequestModel;
import com.enrich.salonapp.data.model.AppointmentResponseModel;
import com.enrich.salonapp.data.model.AuthenticationModel;
import com.enrich.salonapp.data.model.AuthenticationRequestModel;
import com.enrich.salonapp.data.model.AvailableTimeResponseModel;
import com.enrich.salonapp.data.model.BeautyAndBlingResponseModel;
import com.enrich.salonapp.data.model.CampaignRewardResponseModel;
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
import com.enrich.salonapp.data.model.CreateOrder.CreateOTPRequestModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOTPResponseModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOrderRequestModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOrderResponseModel;
import com.enrich.salonapp.data.model.ErrorModel;
import com.enrich.salonapp.data.model.ForgotPasswordRequestModel;
import com.enrich.salonapp.data.model.ForgotPasswordResponseModel;
import com.enrich.salonapp.data.model.FriendResponseModel;
import com.enrich.salonapp.data.model.GuestResponseModel;
import com.enrich.salonapp.data.model.GuestSpinCountResponseModel;
import com.enrich.salonapp.data.model.GuestUpdateRequestModel;
import com.enrich.salonapp.data.model.GuestUpdateResponseModel;
import com.enrich.salonapp.data.model.InvoiceResponseModel;
import com.enrich.salonapp.data.model.NewAndPopularResponseModel;
import com.enrich.salonapp.data.model.OfferResponseModel;
import com.enrich.salonapp.data.model.Package.MyPackageResponseModel;
import com.enrich.salonapp.data.model.Package.PackageResponseModel;
import com.enrich.salonapp.data.model.PackageDetailsResponseModel;
import com.enrich.salonapp.data.model.Product.BrandResponseModel;
import com.enrich.salonapp.data.model.Product.ProductCategoryModel;
import com.enrich.salonapp.data.model.Product.ProductCategoryResponseModel;
import com.enrich.salonapp.data.model.Product.ProductDetailResponseModel;
import com.enrich.salonapp.data.model.Product.ProductRequestModel;
import com.enrich.salonapp.data.model.Product.ProductResponseModel;
import com.enrich.salonapp.data.model.Product.ProductSubCategoryResponseModel;
import com.enrich.salonapp.data.model.ReferFriendModel;
import com.enrich.salonapp.data.model.RegisterFCMRequestModel;
import com.enrich.salonapp.data.model.RegisterFCMResponseModel;
import com.enrich.salonapp.data.model.RegistrationRequestModel;
import com.enrich.salonapp.data.model.RegistrationResponseModel;
import com.enrich.salonapp.data.model.ReserveSlotRequestModel;
import com.enrich.salonapp.data.model.ReserveSlotResponseModel;
import com.enrich.salonapp.data.model.SelectFriendModel;
import com.enrich.salonapp.data.model.ServiceList.ParentAndNormalServiceListResponseModel;
import com.enrich.salonapp.data.model.ServiceList.ServiceVariantResponseModel;
import com.enrich.salonapp.data.model.ServiceList.SubCategoryResponseModel;
import com.enrich.salonapp.data.model.ServiceListResponseModel;
import com.enrich.salonapp.data.model.SignIn.IsUserRegisteredResponseModel;
import com.enrich.salonapp.data.model.SpinPriceModel;
import com.enrich.salonapp.data.model.TherapistResponseModel;
import com.enrich.salonapp.data.model.Wallet.WalletHistoryResponseModel;
import com.enrich.salonapp.data.model.Wallet.WalletResponseModel;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.ProductCategoryComparator;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteDataSource extends DataSource {

 //  public static final String HOST = "http://137.59.54.53/EnrichAPI/api/"; // STAGING 53
//    public static final String HOST = "http://137.59.54.51/EnrichAPI/api/"; // STAGING 51
 // public static final String HOST = "http://13.71.113.69/EnrichAPI/api/"; // PROD
    public static final String HOST = "http://enrichsalon.com/EnrichAPI/api/"; // PROD
    private static final String IS_USER_REGISTERED = "Catalog/Guests/IsRegisteredUser_New";

    public static final String GET_INVOICE = "Catalog/Invoices/";

    public static final String GET_UPCOMING_APPOINTMENT = "Catalog/Guests/UpcomingAppointments";

    public static final String GET_PAST_APPOINTMENT = "Catalog/Guests/PastAppointments";

    public static final String GET_SALON_DATE_TME_SLOTS = "Catalog/Appointments/AvailableTimes";
    public static final String GET_HOME_DATE_TME_SLOTS = "Catalog/Appointments/AvailableTimesForHomeService";

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

        Call<IsUserRegisteredResponseModel> call = apiService.isUserRegistered(HOST + IS_USER_REGISTERED, map);
        call.enqueue(new Callback<IsUserRegisteredResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<IsUserRegisteredResponseModel> call, @NonNull Response<IsUserRegisteredResponseModel> response) {
                if (response.isSuccessful()) {
                    ErrorModel errorModel = new ErrorModel();
                    errorModel.StatusCode = response.code();

                    IsUserRegisteredResponseModel isUserRegisteredResponseModel = new IsUserRegisteredResponseModel();
                    isUserRegisteredResponseModel.Error = errorModel;

                    callBack.onSuccess(isUserRegisteredResponseModel);
                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                        EnrichUtils.log(EnrichUtils.newGson().toJson(response.errorBody().toString()));

                        IsUserRegisteredResponseModel model = EnrichUtils.newGson().fromJson(jObjError.toString(), IsUserRegisteredResponseModel.class);

                        callBack.onSuccess(model);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<IsUserRegisteredResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
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
            public void onResponse(@NonNull Call<AuthenticationModel> call, @NonNull Response<AuthenticationModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
                else
                    callBack.onFailure(new Throwable());
            }

            @Override
            public void onFailure(@NonNull Call<AuthenticationModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getGuestData(String guestId, final GetGuestDataCallBack callBack) {

        Map<String, String> map = new HashMap<>();
        map.put("GuestId", guestId);
        EnrichUtils.log(EnrichUtils.newGson().toJson(map));

        Call<GuestResponseModel> call = apiService.getUserData(map);
        call.enqueue(new Callback<GuestResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<GuestResponseModel> call, @NonNull Response<GuestResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().Guest != null) {
                        callBack.onSuccess(response.body().Guest);
                    } else {
                        callBack.onFailure(new Throwable());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GuestResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
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
            public void onResponse(@NonNull Call<CheckUserNameResponseModel> call, @NonNull Response<CheckUserNameResponseModel> response) {
                callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<CheckUserNameResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
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
            public void onResponse(@NonNull Call<CreateOTPResponseModel> call, @NonNull Response<CreateOTPResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<CreateOTPResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void registerUser(RegistrationRequestModel model, final RegisterUserCallBack callBack) {

        EnrichUtils.log(EnrichUtils.newGson().toJson(model));

        Call<RegistrationResponseModel> call = apiService.register(model);
        call.enqueue(new Callback<RegistrationResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponseModel> call, @NonNull Response<RegistrationResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
                else
                    callBack.onFailure(new Throwable());
            }

            @Override
            public void onFailure(@NonNull Call<RegistrationResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
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
            public void onResponse(@NonNull Call<CenterResponseModel> call, @NonNull Response<CenterResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<CenterResponseModel> call, @NonNull Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getOffers(Map<String, String> map, final GetOfferListCallBack callBack) {

        EnrichUtils.log(EnrichUtils.newGson().toJson(map));

        Call<OfferResponseModel> call = apiService.getAllOffers(map);
        call.enqueue(new Callback<OfferResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<OfferResponseModel> call, @NonNull Response<OfferResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<OfferResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getCategoryList(Map<String, String> map, final GetCategoryListCallBack callBack) {

        EnrichUtils.log(EnrichUtils.newGson().toJson(map));

        Call<CategoryResponseModel> call = apiService.getAllCategories(map);
        call.enqueue(new Callback<CategoryResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponseModel> call, @NonNull Response<CategoryResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getAppointments(String url, Map<String, String> map, final GetAppointmentsCallBack callBack) {

        EnrichUtils.log(url + EnrichUtils.newGson().toJson(map));

        Call<AppointmentResponseModel> call = apiService.getAppointmentsList(url, map);
        call.enqueue(new Callback<AppointmentResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<AppointmentResponseModel> call, @NonNull Response<AppointmentResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<AppointmentResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
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
            public void onResponse(@NonNull Call<GuestUpdateResponseModel> call, @NonNull Response<GuestUpdateResponseModel> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure(new Throwable());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GuestUpdateResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
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
            public void onResponse(@NonNull Call<ChangePasswordResponseModel> call, @NonNull Response<ChangePasswordResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ChangePasswordResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
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
            public void onResponse(@NonNull Call<ServiceListResponseModel> call, @NonNull Response<ServiceListResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ServiceListResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getTherapist(final Map<String, String> map, final GetTherapistCallBack callBack) {
        EnrichUtils.log(EnrichUtils.newGson().toJson(map));

        Call<TherapistResponseModel> call = apiService.getTherapistList(map);
        call.enqueue(new Callback<TherapistResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<TherapistResponseModel> call, @NonNull Response<TherapistResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TherapistResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getTimeSlots(String url, AppointmentRequestModel model, final GetTimeSlotsCallBack callBack) {
        EnrichUtils.log(EnrichUtils.newGson().toJson(model));

        Call<AvailableTimeResponseModel> call = apiService.getTimeSlots(url, model);
        call.enqueue(new Callback<AvailableTimeResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<AvailableTimeResponseModel> call, @NonNull Response<AvailableTimeResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
                else
                    callBack.onFailure(new Throwable());
            }

            @Override
            public void onFailure(@NonNull Call<AvailableTimeResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void reserveSlot(ReserveSlotRequestModel model, final ReserveSlotCallBack callBack) {
        EnrichUtils.log(EnrichUtils.newGson().toJson(model));
        Call<ReserveSlotResponseModel> call = apiService.reserveSlot(model);
        call.enqueue(new Callback<ReserveSlotResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ReserveSlotResponseModel> call, @NonNull Response<ReserveSlotResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ReserveSlotResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void createOrder(CreateOrderRequestModel model, final CreateOrderCallBack callBack) {
        EnrichUtils.log("CreateOrder: " + EnrichUtils.newGson().toJson(model));

        Call<CreateOrderResponseModel> call = apiService.createOrder(model);
        call.enqueue(new Callback<CreateOrderResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<CreateOrderResponseModel> call, @NonNull Response<CreateOrderResponseModel> response) {
                if (response.isSuccessful()) {
                    EnrichUtils.log(EnrichUtils.newGson().toJson(response.body()));
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure(new Throwable());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreateOrderResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getInvoice(String url, final GetInvoiceCallBack callBack) {
        EnrichUtils.log("GetInvoice: " + url);

        Call<InvoiceResponseModel> call = apiService.getInvoice(url);
        call.enqueue(new Callback<InvoiceResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<InvoiceResponseModel> call, @NonNull Response<InvoiceResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<InvoiceResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void confirmReservation(final ConfirmReservationRequestModel model, final ConfirmReservationCallBack callBack) {
        EnrichUtils.log("ConfirmReservation: " + EnrichUtils.newGson().toJson(model));

        Call<ConfirmReservationResponseModel> call = apiService.confirmReservation(model);
        call.enqueue(new Callback<ConfirmReservationResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ConfirmReservationResponseModel> call, @NonNull Response<ConfirmReservationResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ConfirmReservationResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void confirmOrder(final ConfirmOrderRequestModel model, final ConfirmOrderCallBack callBack) {
        EnrichUtils.log(EnrichUtils.newGson().toJson(model));
        Call<ConfirmOrderResponseModel> call = apiService.confirmOrder(model);
        call.enqueue(new Callback<ConfirmOrderResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ConfirmOrderResponseModel> call, @NonNull Response<ConfirmOrderResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
                else
                    callBack.onFailure(new Throwable());
            }

            @Override
            public void onFailure(@NonNull Call<ConfirmOrderResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
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
            public void onResponse(@NonNull Call<NewAndPopularResponseModel> call, @NonNull Response<NewAndPopularResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<NewAndPopularResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
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
            public void onResponse(@NonNull Call<CancelResponseModel> call, @NonNull Response<CancelResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<CancelResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void forgotPassword(ForgotPasswordRequestModel model, final ForgotPasswordCallBack callBack) {
        Call<ForgotPasswordResponseModel> call = apiService.forgotPassword(model);
        call.enqueue(new Callback<ForgotPasswordResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ForgotPasswordResponseModel> call, @NonNull Response<ForgotPasswordResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ForgotPasswordResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getAllPackages(final PackageListCallBack callBack) {
        Call<PackageResponseModel> call = apiService.getAllPackages();
        call.enqueue(new Callback<PackageResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<PackageResponseModel> call, @NonNull Response<PackageResponseModel> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure(new Throwable());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PackageResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getPackagesDetails(Map<String, String> map, final GetPackageDetailsCallback callBack) {
        Call<PackageDetailsResponseModel> call = apiService.getPackageDetails(map);
        call.enqueue(new Callback<PackageDetailsResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<PackageDetailsResponseModel> call, @NonNull Response<PackageDetailsResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<PackageDetailsResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getWallet(Map<String, String> map, final GetWalletCallback callback) {
        EnrichUtils.log(EnrichUtils.newGson().toJson(map));
        Call<WalletResponseModel> call = apiService.getWallet(map);
        call.enqueue(new Callback<WalletResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<WalletResponseModel> call, @NonNull Response<WalletResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<WalletResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getWalletHistory(Map<String, String> map, final GetWalletHistoryCallback callback) {
        Call<WalletHistoryResponseModel> call = apiService.getWalletHistory(map);
        call.enqueue(new Callback<WalletHistoryResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<WalletHistoryResponseModel> call, @NonNull Response<WalletHistoryResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<WalletHistoryResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getProductList(ProductRequestModel model, final GetProductListCallback callback) {
        EnrichUtils.log(EnrichUtils.newGson().toJson(model));

        Call<ProductResponseModel> call = apiService.getProduct(model);
        call.enqueue(new Callback<ProductResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponseModel> call, @NonNull Response<ProductResponseModel> response) {
                if (response.isSuccessful()) {
                    Log.e("enrich",response.toString());
                    callback.onSuccess(response.body());
                }

            }

            @Override
            public void onFailure(@NonNull Call<ProductResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                Log.e("enrich",t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getProductDetails(Map<String, String> map, final GetProductDetailsCallback callback) {
        Call<ProductDetailResponseModel> call = apiService.getProductDetails(map);
        call.enqueue(new Callback<ProductDetailResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ProductDetailResponseModel> call, @NonNull Response<ProductDetailResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ProductDetailResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });

    }

    @Override
    public void getMyPackages(Map<String, String> map, final GetMyPackagesCallback callback) {
        EnrichUtils.log(EnrichUtils.newGson().toJson(map));
        Call<MyPackageResponseModel> call = apiService.getMyPackages(map);
        call.enqueue(new Callback<MyPackageResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<MyPackageResponseModel> call, @NonNull Response<MyPackageResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
                else
                    callback.onFailure(new Throwable());
            }

            @Override
            public void onFailure(@NonNull Call<MyPackageResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getBrandsList(final GetBrandsListCallback callback) {
        Call<BrandResponseModel> call = apiService.getBrandsList();
        call.enqueue(new Callback<BrandResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<BrandResponseModel> call, @NonNull Response<BrandResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<BrandResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getProductCategoryList(final GetProductsCategoryListCallback callback) {
        Call<ProductCategoryResponseModel> call = apiService.getProductCategoryList();
        call.enqueue(new Callback<ProductCategoryResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ProductCategoryResponseModel> call, @NonNull Response<ProductCategoryResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    ArrayList<ProductCategoryModel> list = response.body().ProductCategory;
                    Collections.sort(list, new ProductCategoryComparator());
                    ProductCategoryResponseModel productCategoryResponseModel = new ProductCategoryResponseModel();
                    productCategoryResponseModel.ProductCategory = list;
                    callback.onSuccess(productCategoryResponseModel);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductCategoryResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getProductSubCategoryList(final GetProductsSubCategoryListCallback callback) {
        Call<ProductSubCategoryResponseModel> call = apiService.getProductSubCategoryList();
        call.enqueue(new Callback<ProductSubCategoryResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ProductSubCategoryResponseModel> call, @NonNull Response<ProductSubCategoryResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ProductSubCategoryResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getServiceSubCategories(Map<String, String> map, final GetServiceSubCategoryCallback callback) {
        EnrichUtils.log(EnrichUtils.newGson().toJson(map));
        Call<SubCategoryResponseModel> call = apiService.getSubCategoryList(map);
        call.enqueue(new Callback<SubCategoryResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SubCategoryResponseModel> call, @NonNull Response<SubCategoryResponseModel> response) {
                if (response.isSuccessful()) {

                    callback.onSuccess(response.body());
                }
                else
                    callback.onFailure(new Throwable());
            }

            @Override
            public void onFailure(@NonNull Call<SubCategoryResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getParentAndNormalServiceList(Map<String, String> map, final GetParentAndNormalServiceListCallback callback) {
        EnrichUtils.log("P.A.N. REQ: " + EnrichUtils.newGson().toJson(map));
        Call<ParentAndNormalServiceListResponseModel> call = apiService.getParentAndNormalServiceList(map);
        call.enqueue(new Callback<ParentAndNormalServiceListResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ParentAndNormalServiceListResponseModel> call, @NonNull Response<ParentAndNormalServiceListResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ParentAndNormalServiceListResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getServiceVariantsList(Map<String, String> map, final GetServiceVariantsCallback callback) {
        EnrichUtils.log(EnrichUtils.newGson().toJson(map));
        Call<ServiceVariantResponseModel> call = apiService.getServiceVariantList(map);
        call.enqueue(new Callback<ServiceVariantResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ServiceVariantResponseModel> call, @NonNull Response<ServiceVariantResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ServiceVariantResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getProductOffers(final GetProductOffersCallback callback) {
        Call<OfferResponseModel> call = apiService.getProductOffers();
        call.enqueue(new Callback<OfferResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<OfferResponseModel> call, @NonNull Response<OfferResponseModel> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {

                    callback.onFailure(new Throwable());
                }
            }

            @Override
            public void onFailure(@NonNull Call<OfferResponseModel> call, @NonNull Throwable t) {

                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void addAddress(AddressModel model, final AddAddressCallback callback) {
        EnrichUtils.log(EnrichUtils.newGson().toJson(model));
        Call<AddressResponseModel> call = apiService.addAddress(model);
        call.enqueue(new Callback<AddressResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<AddressResponseModel> call, @NonNull Response<AddressResponseModel> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Throwable());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddressResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });

    }
    @Override
    public void referFriend(ReferFriendModel model, final ReferFriendCallback callback) {
        EnrichUtils.log(EnrichUtils.newGson().toJson(model));
        Call<FriendResponseModel> call = apiService.referFriend(model);
        call.enqueue(new Callback<FriendResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<FriendResponseModel> call, @NonNull Response<FriendResponseModel> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Throwable());
                }
            }

            @Override
            public void onFailure(@NonNull Call<FriendResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });

    }
    @Override
    public void getAppUpdate(final Map<String, String> map, final GetAppUpdateCallback callback) {
        EnrichUtils.log(EnrichUtils.newGson().toJson(map));
        Call<AppUpdateResponseModel> call = apiService.getAppUpdate(map);
        call.enqueue(new Callback<AppUpdateResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<AppUpdateResponseModel> call, @NonNull Response<AppUpdateResponseModel> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppUpdateResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void registerFCM(RegisterFCMRequestModel model, final RegisterFCMCallback callback) {
        Call<RegisterFCMResponseModel> call = apiService.registerFCM(model);
        call.enqueue(new Callback<RegisterFCMResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<RegisterFCMResponseModel> call, @NonNull Response<RegisterFCMResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());

            }

            @Override
            public void onFailure(@NonNull Call<RegisterFCMResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getBeautyAndBling(final GetBeautyAndBlingCallback callback) {
        Call<BeautyAndBlingResponseModel> call = apiService.getBeautyAndBling();
        call.enqueue(new Callback<BeautyAndBlingResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<BeautyAndBlingResponseModel> call, @NonNull Response<BeautyAndBlingResponseModel> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<BeautyAndBlingResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getGuestSpinCount(Map<String, String> map, final GetGuestSpinCountCallback callback) {
        Call<GuestSpinCountResponseModel> call = apiService.getGuestSpinCount(map);
        call.enqueue(new Callback<GuestSpinCountResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<GuestSpinCountResponseModel> call, @NonNull Response<GuestSpinCountResponseModel> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<GuestSpinCountResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getSpinPrice(Map<String, String> map, final GetSpinPriceCallback callback) {
        Call<SpinPriceModel> call = apiService.getSpinPrice(map);
        call.enqueue(new Callback<SpinPriceModel>() {
            @Override
            public void onResponse(@NonNull Call<SpinPriceModel> call, @NonNull Response<SpinPriceModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
                else
                    callback.onFailure(new Throwable());
            }

            @Override
            public void onFailure(@NonNull Call<SpinPriceModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getCampaignRewards(Map<String, String> map, final GetCampaignRewards callback) {
        Call<CampaignRewardResponseModel> call = apiService.getCampaignRewards(map);
        call.enqueue(new Callback<CampaignRewardResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<CampaignRewardResponseModel> call, @NonNull Response<CampaignRewardResponseModel> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<CampaignRewardResponseModel> call, @NonNull Throwable t) {
                Crashlytics.logException(t);
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onNetworkFailure();
            }
        });
    }
}
