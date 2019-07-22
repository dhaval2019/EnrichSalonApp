package com.enrich.salonapp.data;

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
import com.enrich.salonapp.data.model.ForgotPasswordRequestModel;
import com.enrich.salonapp.data.model.ForgotPasswordResponseModel;
import com.enrich.salonapp.data.model.FriendResponseModel;
import com.enrich.salonapp.data.model.GuestModel;
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
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;

import java.util.ArrayList;
import java.util.Map;

public abstract class DataSource {

    protected MainUiThread mainUiThread;
    protected ThreadExecutor threadExecutor;

    public DataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        this.mainUiThread = mainUiThread;
        this.threadExecutor = threadExecutor;
    }

    public interface GetIsUserRegisteredCallBack {
        void onSuccess(IsUserRegisteredResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void isUserRegistered(String phoneNumber, GetIsUserRegisteredCallBack callBack);


    public interface GetAuthenticationCallBack {
        void onSuccess(AuthenticationModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getAuthenticationToken(AuthenticationRequestModel model, GetAuthenticationCallBack callBack);


    public interface GetGuestDataCallBack {
        void onSuccess(GuestModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getGuestData(String guestId, GetGuestDataCallBack callBack);


    public interface CheckUserNameCallBack {
        void onSuccess(CheckUserNameResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void checkUsername(String userName, CheckUserNameCallBack callBack);


    public interface CreateOTPCallBack {
        void onSuccess(CreateOTPResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void createOTP(CreateOTPRequestModel model, CreateOTPCallBack callBack);


    public interface RegisterUserCallBack {
        void onSuccess(RegistrationResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void registerUser(RegistrationRequestModel model, RegisterUserCallBack callBacks);


    public interface GetCenterListCallBack {
        void onSuccess(CenterResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getCenterList(Map<String, String> map, GetCenterListCallBack callBack);


    public interface GetOfferListCallBack {
        void onSuccess(OfferResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getOffers(Map<String, String> map, GetOfferListCallBack callBack);


    public interface GetCategoryListCallBack {
        void onSuccess(CategoryResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getCategoryList(Map<String, String> map, GetCategoryListCallBack callBack);


    public interface GetAppointmentsCallBack {
        void onSuccess(AppointmentResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getAppointments(String url, Map<String, String> map, GetAppointmentsCallBack callBack);


    public interface UpdateUserCallBack {
        void onSuccess(GuestUpdateResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void updateUser(GuestUpdateRequestModel model, UpdateUserCallBack callBack);


    public interface ChangePasswordCallBack {
        void onSuccess(ChangePasswordResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void changePassword(ChangePasswordRequestModel model, ChangePasswordCallBack callBack);


    public interface GetServiceListCallBack {
        void onSuccess(ServiceListResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getServiceList(Map<String, String> map, GetServiceListCallBack callBack);


    public interface GetTherapistCallBack {
        void onSuccess(TherapistResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getTherapist(Map<String, String> map, GetTherapistCallBack callBack);


    public interface GetTimeSlotsCallBack {
        void onSuccess(AvailableTimeResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getTimeSlots(String url, AppointmentRequestModel model, GetTimeSlotsCallBack callBack);


    public interface ReserveSlotCallBack {
        void onSuccess(ReserveSlotResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void reserveSlot(ReserveSlotRequestModel model, ReserveSlotCallBack callBack);


    public interface CreateOrderCallBack {
        void onSuccess(CreateOrderResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void createOrder(CreateOrderRequestModel model, CreateOrderCallBack callBack);


    public interface GetInvoiceCallBack {
        void onSuccess(InvoiceResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getInvoice(String url, GetInvoiceCallBack callBack);


    public interface ConfirmReservationCallBack {
        void onSuccess(ConfirmReservationResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void confirmReservation(ConfirmReservationRequestModel model, ConfirmReservationCallBack callBack);


    public interface ConfirmOrderCallBack {
        void onSuccess(ConfirmOrderResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void confirmOrder(ConfirmOrderRequestModel model, ConfirmOrderCallBack callBack);


    public interface NewAndPopularServicesCallBack {
        void onSuccess(NewAndPopularResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getNewAndPopularServices(Map<String, String> map, NewAndPopularServicesCallBack callBack);


    public interface CancelAppointmentCallBack {
        void onSuccess(CancelResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void cancelAppointment(String url, CancelRequestModel model, CancelAppointmentCallBack callBack);


    public interface ForgotPasswordCallBack {
        void onSuccess(ForgotPasswordResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void forgotPassword(ForgotPasswordRequestModel model, ForgotPasswordCallBack callBack);


    public interface PackageListCallBack {
        void onSuccess(PackageResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getAllPackages(PackageListCallBack callBack);


    public interface GetPackageDetailsCallback {
        void onSuccess(PackageDetailsResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getPackagesDetails(Map<String, String> map, GetPackageDetailsCallback callBack);

    public interface GetWalletCallback {
        void onSuccess(WalletResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getWallet(Map<String, String> string, GetWalletCallback callback);


    public interface GetWalletHistoryCallback {
        void onSuccess(WalletHistoryResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getWalletHistory(Map<String, String> string, GetWalletHistoryCallback callback);


    public interface GetProductListCallback {
        void onSuccess(ProductResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getProductList(ProductRequestModel model, GetProductListCallback callback);


    public interface GetProductDetailsCallback {
        void onSuccess(ProductDetailResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getProductDetails(Map<String, String> map, GetProductDetailsCallback callback);


    public interface GetMyPackagesCallback {
        void onSuccess(MyPackageResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getMyPackages(Map<String, String> map, GetMyPackagesCallback callback);


    public interface GetBrandsListCallback {
        void onSuccess(BrandResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getBrandsList(GetBrandsListCallback callback);


    public interface GetProductsCategoryListCallback {
        void onSuccess(ProductCategoryResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getProductCategoryList(GetProductsCategoryListCallback callback);


    public interface GetProductsSubCategoryListCallback {
        void onSuccess(ProductSubCategoryResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getProductSubCategoryList(GetProductsSubCategoryListCallback callback);


    public interface GetServiceSubCategoryCallback {
        void onSuccess(SubCategoryResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getServiceSubCategories(Map<String, String> map, GetServiceSubCategoryCallback callback);


    public interface GetParentAndNormalServiceListCallback {
        void onSuccess(ParentAndNormalServiceListResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getParentAndNormalServiceList(Map<String, String> map, GetParentAndNormalServiceListCallback callback);


    public interface GetServiceVariantsCallback {
        void onSuccess(ServiceVariantResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getServiceVariantsList(Map<String, String> map, GetServiceVariantsCallback callback);


    public interface GetProductOffersCallback {
        void onSuccess(OfferResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getProductOffers(GetProductOffersCallback callback);


    public interface AddAddressCallback {
        void onSuccess(AddressResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }



    public abstract void addAddress(AddressModel model, AddAddressCallback callback);

    public abstract void referFriend(ReferFriendModel model, ReferFriendCallback callback);


    public interface ReferFriendCallback {
        void onSuccess(FriendResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public interface GetAppUpdateCallback {
        void onSuccess(AppUpdateResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getAppUpdate(Map<String, String> map, GetAppUpdateCallback callback);


    public interface RegisterFCMCallback {
        void onSuccess(RegisterFCMResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void registerFCM(RegisterFCMRequestModel model, RegisterFCMCallback callback);


    public interface GetBeautyAndBlingCallback {
        void onSuccess(BeautyAndBlingResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getBeautyAndBling(GetBeautyAndBlingCallback callback);


    public interface GetGuestSpinCountCallback {
        void onSuccess(GuestSpinCountResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getGuestSpinCount(Map<String, String> map, GetGuestSpinCountCallback callback);


    public interface GetSpinPriceCallback {
        void onSuccess(SpinPriceModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getSpinPrice(Map<String, String> map, GetSpinPriceCallback callback);


    public interface GetCampaignRewards {
        void onSuccess(CampaignRewardResponseModel model);

        void onFailure(Throwable t);

        void onNetworkFailure();
    }

    public abstract void getCampaignRewards(Map<String, String> map, GetCampaignRewards callback);
}
