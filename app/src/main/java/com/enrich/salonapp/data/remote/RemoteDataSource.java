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
import com.enrich.salonapp.data.model.CreateOrder.CreateOTPRequestModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOTPResponseModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOrderRequestModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOrderResponseModel;
import com.enrich.salonapp.data.model.ErrorModel;
import com.enrich.salonapp.data.model.ForgotPasswordRequestModel;
import com.enrich.salonapp.data.model.ForgotPasswordResponseModel;
import com.enrich.salonapp.data.model.GuestResponseModel;
import com.enrich.salonapp.data.model.GuestUpdateRequestModel;
import com.enrich.salonapp.data.model.GuestUpdateResponseModel;
import com.enrich.salonapp.data.model.InvoiceResponseModel;
import com.enrich.salonapp.data.model.NewAndPopularResponseModel;
import com.enrich.salonapp.data.model.OfferResponseModel;
import com.enrich.salonapp.data.model.Package.MyPackageResponseModel;
import com.enrich.salonapp.data.model.Package.PackageResponseModel;
import com.enrich.salonapp.data.model.PackageDetailsResponseModel;
import com.enrich.salonapp.data.model.Product.BrandResponseModel;
import com.enrich.salonapp.data.model.Product.ProductCategoryResponseModel;
import com.enrich.salonapp.data.model.Product.ProductDetailResponseModel;
import com.enrich.salonapp.data.model.Product.ProductRequestModel;
import com.enrich.salonapp.data.model.Product.ProductResponseModel;
import com.enrich.salonapp.data.model.Product.ProductSubCategoryResponseModel;
import com.enrich.salonapp.data.model.RegistrationRequestModel;
import com.enrich.salonapp.data.model.RegistrationResponseModel;
import com.enrich.salonapp.data.model.ReserveSlotRequestModel;
import com.enrich.salonapp.data.model.ReserveSlotResponseModel;
import com.enrich.salonapp.data.model.ServiceList.ParentAndNormalServiceListResponseModel;
import com.enrich.salonapp.data.model.ServiceList.ServiceVariantResponseModel;
import com.enrich.salonapp.data.model.ServiceList.SubCategoryResponseModel;
import com.enrich.salonapp.data.model.ServiceListResponseModel;
import com.enrich.salonapp.data.model.SignIn.IsUserRegisteredResponseModel;
import com.enrich.salonapp.data.model.TherapistResponseModel;
import com.enrich.salonapp.data.model.Wallet.WalletHistoryResponseModel;
import com.enrich.salonapp.data.model.Wallet.WalletResponseModel;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteDataSource extends DataSource {

    //    public static final String HOST = "http://137.59.54.53/EnrichAPI/api/"; // STAGING
    public static final String HOST = "http://13.71.113.69/EnrichAPI/api/"; // PROD

    public static final String IS_USER_REGISTERED = "Catalog/Guests/IsRegisteredUser_New";

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

        Call<IsUserRegisteredResponseModel> call = apiService.isUserRegistered(HOST + IS_USER_REGISTERED, map);
        call.enqueue(new Callback<IsUserRegisteredResponseModel>() {
            @Override
            public void onResponse(Call<IsUserRegisteredResponseModel> call, Response<IsUserRegisteredResponseModel> response) {
                if (response.isSuccessful()) {
                    ErrorModel errorModel = new ErrorModel();
                    errorModel.StatusCode = response.code();

                    IsUserRegisteredResponseModel isUserRegisteredResponseModel = new IsUserRegisteredResponseModel();
                    isUserRegisteredResponseModel.Error = errorModel;

                    callBack.onSuccess(isUserRegisteredResponseModel);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                        EnrichUtils.log(EnrichUtils.newGson().toJson(response.errorBody().toString()));

                        IsUserRegisteredResponseModel model = EnrichUtils.newGson().fromJson(jObjError.toString(), IsUserRegisteredResponseModel.class);

                        callBack.onSuccess(model);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<IsUserRegisteredResponseModel> call, Throwable t) {
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

        EnrichUtils.log(EnrichUtils.newGson().toJson(model));

        Call<RegistrationResponseModel> call = apiService.register(model);
        call.enqueue(new Callback<RegistrationResponseModel>() {
            @Override
            public void onResponse(Call<RegistrationResponseModel> call, Response<RegistrationResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
                else
                    callBack.onFailure(new Throwable());
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
                else
                    callBack.onFailure(new Throwable());
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
        EnrichUtils.log(EnrichUtils.newGson().toJson(model));
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
        EnrichUtils.log("CreateOrder: " + EnrichUtils.newGson().toJson(model));

        Call<CreateOrderResponseModel> call = apiService.createOrder(model);
        call.enqueue(new Callback<CreateOrderResponseModel>() {
            @Override
            public void onResponse(Call<CreateOrderResponseModel> call, Response<CreateOrderResponseModel> response) {
                if (response.isSuccessful()) {
                    EnrichUtils.log(EnrichUtils.newGson().toJson(response.body()));
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure(new Throwable());
                }
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
        EnrichUtils.log("GetInvoice: " + url);

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
        EnrichUtils.log("ConfirmReservation: " + EnrichUtils.newGson().toJson(model));

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
        EnrichUtils.log(EnrichUtils.newGson().toJson(model));
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

    @Override
    public void forgotPassword(ForgotPasswordRequestModel model, final ForgotPasswordCallBack callBack) {
        Call<ForgotPasswordResponseModel> call = apiService.forgotPassword(model);
        call.enqueue(new Callback<ForgotPasswordResponseModel>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponseModel> call, Response<ForgotPasswordResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponseModel> call, Throwable t) {
                callBack.onFailure(t);
            }
        });
    }

    @Override
    public void getAllPackages(final PackageListCallBack callBack) {
        Call<PackageResponseModel> call = apiService.getAllPackages();
        call.enqueue(new Callback<PackageResponseModel>() {
            @Override
            public void onResponse(Call<PackageResponseModel> call, Response<PackageResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<PackageResponseModel> call, Throwable t) {
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
            public void onResponse(Call<PackageDetailsResponseModel> call, Response<PackageDetailsResponseModel> response) {
                if (response.isSuccessful())
                    callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<PackageDetailsResponseModel> call, Throwable t) {
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
            public void onResponse(Call<WalletResponseModel> call, Response<WalletResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<WalletResponseModel> call, Throwable t) {
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
            public void onResponse(Call<WalletHistoryResponseModel> call, Response<WalletHistoryResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<WalletHistoryResponseModel> call, Throwable t) {
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
            public void onResponse(Call<ProductResponseModel> call, Response<ProductResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProductResponseModel> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getProductDetails(Map<String, String> map, final GetProductDetailsCallback callback) {
        Call<ProductDetailResponseModel> call = apiService.getProductDetails(map);
        call.enqueue(new Callback<ProductDetailResponseModel>() {
            @Override
            public void onResponse(Call<ProductDetailResponseModel> call, Response<ProductDetailResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProductDetailResponseModel> call, Throwable t) {
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
            public void onResponse(Call<MyPackageResponseModel> call, Response<MyPackageResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
                else
                    callback.onFailure(new Throwable());
            }

            @Override
            public void onFailure(Call<MyPackageResponseModel> call, Throwable t) {
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
            public void onResponse(Call<BrandResponseModel> call, Response<BrandResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<BrandResponseModel> call, Throwable t) {
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
            public void onResponse(Call<ProductCategoryResponseModel> call, Response<ProductCategoryResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProductCategoryResponseModel> call, Throwable t) {
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
            public void onResponse(Call<ProductSubCategoryResponseModel> call, Response<ProductSubCategoryResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProductSubCategoryResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getServiceSubCategories(Map<String, String> map, final GetServiceSubCategoryCallback callback) {
        Call<SubCategoryResponseModel> call = apiService.getSubCategoryList(map);
        call.enqueue(new Callback<SubCategoryResponseModel>() {
            @Override
            public void onResponse(Call<SubCategoryResponseModel> call, Response<SubCategoryResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<SubCategoryResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getParentAndNormalServiceList(Map<String, String> map, final GetParentAndNormalServiceListCallback callback) {
        Call<ParentAndNormalServiceListResponseModel> call = apiService.getParentAndNormalServiceList(map);
        call.enqueue(new Callback<ParentAndNormalServiceListResponseModel>() {
            @Override
            public void onResponse(Call<ParentAndNormalServiceListResponseModel> call, Response<ParentAndNormalServiceListResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ParentAndNormalServiceListResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void getServiceVariantsList(Map<String, String> map, final GetServiceVariantsCallback callback) {
        Call<ServiceVariantResponseModel> call = apiService.getServiceVariantList(map);
        call.enqueue(new Callback<ServiceVariantResponseModel>() {
            @Override
            public void onResponse(Call<ServiceVariantResponseModel> call, Response<ServiceVariantResponseModel> response) {
                if (response.isSuccessful())
                    callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ServiceVariantResponseModel> call, Throwable t) {
                EnrichUtils.log(t.getLocalizedMessage());
                callback.onFailure(t);
            }
        });
    }
}
