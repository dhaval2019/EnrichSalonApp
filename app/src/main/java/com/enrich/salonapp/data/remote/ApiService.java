package com.enrich.salonapp.data.remote;

import com.enrich.salonapp.data.model.AddressModel;
import com.enrich.salonapp.data.model.AddressResponseModel;
import com.enrich.salonapp.data.model.AppUpdateResponseModel;
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
import com.enrich.salonapp.data.model.PackageDetailsResponseModel;
import com.enrich.salonapp.data.model.Product.BrandResponseModel;
import com.enrich.salonapp.data.model.Product.ProductCategoryResponseModel;
import com.enrich.salonapp.data.model.Product.ProductDetailResponseModel;
import com.enrich.salonapp.data.model.Product.ProductRequestModel;
import com.enrich.salonapp.data.model.Product.ProductResponseModel;
import com.enrich.salonapp.data.model.Product.ProductSubCategoryResponseModel;
import com.enrich.salonapp.data.model.RegisterFCMRequestModel;
import com.enrich.salonapp.data.model.RegisterFCMResponseModel;
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

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {

    @GET()
    Call<IsUserRegisteredResponseModel> isUserRegistered(@Url String url, @QueryMap Map<String, String> map);

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
    Call<OfferResponseModel> getAllOffers(@QueryMap Map<String, String> map);

    @GET("Catalog/Services/GetCategories")
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

    @GET("Package/GetPackageByID")
    Call<PackageDetailsResponseModel> getPackageDetails(@QueryMap Map<String, String> map);

    @GET("Catalog/Payments/GetWalletByGuestId")
    Call<WalletResponseModel> getWallet(@QueryMap Map<String, String> map);

    @GET("Catalog/Payments/GetWalletHistory")
    Call<WalletHistoryResponseModel> getWalletHistory(@QueryMap Map<String, String> map);

    @POST("Catalog/Products/GetAllProducts")
    Call<ProductResponseModel> getProduct(@Body ProductRequestModel model);

    @GET("Catalog/Products/GetProductByID")
    Call<ProductDetailResponseModel> getProductDetails(@QueryMap Map<String, String> map);

    @GET("GetGuestPackage")
    Call<MyPackageResponseModel> getMyPackages(@QueryMap Map<String, String> map);

    @GET("Catalog/Products/GetBrands")
    Call<BrandResponseModel> getBrandsList();

    @GET("Catalog/Products/GetProductCategories")
    Call<ProductCategoryResponseModel> getProductCategoryList();

    @GET("Catalog/Products/GetProductSubCategories")
    Call<ProductSubCategoryResponseModel> getProductSubCategoryList();

    @GET("Catalog/Services/GetSubCategories")
    Call<SubCategoryResponseModel> getSubCategoryList(@QueryMap Map<String, String> map);

    @GET("Catalog/Services/GetParentAndNormalServiceList")
    Call<ParentAndNormalServiceListResponseModel> getParentAndNormalServiceList(@QueryMap Map<String, String> map);

    @GET("Catalog/Services/ServiceVariants")
    Call<ServiceVariantResponseModel> getServiceVariantList(@QueryMap Map<String, String> map);

    @GET("Catalog/Products/GetProductOffers")
    Call<OfferResponseModel> getProductOffers();

    @POST("Catalog/Guests/AddGuestAddress")
    Call<AddressResponseModel> addAddress(@Body AddressModel model);

    @GET("Catalog/Guests/GetAppUpdate")
    Call<AppUpdateResponseModel> getAppUpdate(@QueryMap Map<String, String> map);

    @POST("Catalog/Guests/RegisterGuestDevice")
    Call<RegisterFCMResponseModel> registerFCM(@Body RegisterFCMRequestModel model);
}
