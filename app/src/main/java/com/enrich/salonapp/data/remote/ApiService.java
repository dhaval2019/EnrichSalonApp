package com.enrich.salonapp.data.remote;

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
import com.enrich.salonapp.data.model.Product.ProductRequestModel;
import com.enrich.salonapp.data.model.Product.ProductResponseModel;
import com.enrich.salonapp.data.model.RegistrationRequestModel;
import com.enrich.salonapp.data.model.RegistrationResponseModel;
import com.enrich.salonapp.data.model.ReserveSlotRequestModel;
import com.enrich.salonapp.data.model.ReserveSlotResponseModel;
import com.enrich.salonapp.data.model.ServiceListResponseModel;
import com.enrich.salonapp.data.model.TherapistResponseModel;
import com.enrich.salonapp.data.model.UserExistsResponseModel;
import com.enrich.salonapp.data.model.Wallet.WalletHistoryResponseModel;
import com.enrich.salonapp.data.model.Wallet.WalletModel;
import com.enrich.salonapp.data.model.Wallet.WalletResponseModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {

    @GET()
    Call<UserExistsResponseModel> isUserRegistered(@Url String url, @QueryMap Map<String, String> map);

    @POST("Catalog/Guests/CreateToken")
    Call<AuthenticationModel> getAuthenticationToken(@Body AuthenticationRequestModel model);

    @GET("Catalog/Guests")
    Call<GuestResponseModel> getUserData(@QueryMap Map<String, String> map);

    @POST("Catalog/Guests/CreateOTP")
    Call<CreateOTPResponseModel> createOTP(@Body CreateOTPRequestModel model);

    @GET("Catalog/Guests/CheckUserName")
    Call<CheckUserNameResponseModel> checkForUserName(@QueryMap Map<String, String> map);

    @POST("Catalog/Guests/Register")
    Call<RegistrationResponseModel> register(@Body RegistrationRequestModel model);

    @GET("Catalog/Centers")
    Call<CenterResponseModel> getCenter(@QueryMap Map<String, String> map);

    @GET("Catalog/Payments/GetAllOffers")
    Call<OfferResponseModel> getAllOffers();

    @GET("Catalog/Services/Categories")
    Call<CategoryResponseModel> getAllCategories(@QueryMap Map<String, String> map);

    @GET
    Call<AppointmentResponseModel> getAppointmentsList(@Url String url);

    @POST("Catalog/Guests/Update")
    Call<GuestUpdateResponseModel> updateGuest(@Body GuestUpdateRequestModel body);

    @POST("Catalog/Guests/ChangePassword")
    Call<ChangePasswordResponseModel> changePassword(@Body ChangePasswordRequestModel body);

    @GET("Catalog/Services/BasedOnMembership")
    Call<ServiceListResponseModel> getServicesByCategories(@QueryMap Map<String, String> map);

    @GET("Catalog/Therapists")
    Call<TherapistResponseModel> getTherapistList(@QueryMap Map<String, String> map);

    @POST("Catalog/Appointments/AvailableTimes")
    Call<AvailableTimeResponseModel> getTimeSlots(@Body AppointmentRequestModel body);

    @POST("Catalog/Appointments/ReserveSlots")
    Call<ReserveSlotResponseModel> reserveSlot(@Body ReserveSlotRequestModel model);

    @POST("Catalog/Payments/CreateOrder")
    Call<CreateOrderResponseModel> createOrder(@Body CreateOrderRequestModel model);

    @GET()
    Call<InvoiceResponseModel> getInvoice(@Url String url);

    @POST("Catalog/Appointments/ConfirmReservation")
    Call<ConfirmReservationResponseModel> confirmReservation(@Body ConfirmReservationRequestModel model);

    @POST("Catalog/Payments/ConfirmOrder")
    Call<ConfirmOrderResponseModel> confirmOrder(@Body ConfirmOrderRequestModel body);

    @GET("Catalog/Services/NewAndPopularService")
    Call<NewAndPopularResponseModel> getNewAndPopularService(@QueryMap Map<String, String> map);

    @POST()
    Call<CancelResponseModel> cancelAppointment(@Url String url, @Body CancelRequestModel body);

    @POST("Catalog/Guests/ForgottenPassword")
    Call<ForgotPasswordResponseModel> forgotPassword(@Body ForgotPasswordRequestModel model);

    @GET("Package/GetAllPackages")
    Call<PackageResponseModel> getAllPackages();

    @GET("Catalog/Payments/GetWalletByGuestId")
    Call<WalletResponseModel> getWallet(@QueryMap Map<String, String> map);

    @GET("Catalog/Payments/GetWalletHistory")
    Call<WalletHistoryResponseModel> getWalletHistory(@QueryMap Map<String, String> map);

    @POST("Catalog/Products/GetAllProducts")
    Call<ProductResponseModel> getProduct(@Body ProductRequestModel model);

    @GET("GetGuestPackage")
    Call<MyPackageResponseModel> getMyPackages(@QueryMap Map<String, String> map);
}
