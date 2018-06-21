package com.enrich.salonapp.data.local;

import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.AppointmentModels.AppointmentRequestModel;
import com.enrich.salonapp.data.model.AuthenticationRequestModel;
import com.enrich.salonapp.data.model.CancelRequestModel;
import com.enrich.salonapp.data.model.ChangePasswordRequestModel;
import com.enrich.salonapp.data.model.ConfirmOrderRequestModel;
import com.enrich.salonapp.data.model.ConfirmReservationRequestModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOTPRequestModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOrderRequestModel;
import com.enrich.salonapp.data.model.ForgotPasswordRequestModel;
import com.enrich.salonapp.data.model.GuestUpdateRequestModel;
import com.enrich.salonapp.data.model.Product.ProductRequestModel;
import com.enrich.salonapp.data.model.RegistrationRequestModel;
import com.enrich.salonapp.data.model.ReserveSlotRequestModel;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;

import java.util.Map;

public class LocalDataSource extends DataSource {

    private static LocalDataSource localDataSource;

    private DatabaseDefinition databaseDefinition;

    public LocalDataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor, DatabaseDefinition databaseDefinition) {
        super(mainUiThread, threadExecutor);
        this.databaseDefinition = databaseDefinition;
    }

    public static synchronized LocalDataSource getInstance(MainUiThread mainUiThread,
                                                           ThreadExecutor threadExecutor, DatabaseDefinition databaseDefinition) {
        if (localDataSource == null) {
            localDataSource = new LocalDataSource(mainUiThread, threadExecutor, databaseDefinition);
        }
        return localDataSource;
    }

    @Override
    public void isUserRegistered(String phoneNumber, GetIsUserRegisteredCallBack callBack) {

    }

    @Override
    public void getAuthenticationToken(AuthenticationRequestModel model, GetAuthenticationCallBack callBack) {

    }

    @Override
    public void getGuestData(String guestId, GetGuestDataCallBack callBack) {

    }

    @Override
    public void checkUsername(String userName, CheckUserNameCallBack callBack) {

    }

    @Override
    public void createOTP(CreateOTPRequestModel model, CreateOTPCallBack callBack) {

    }

    @Override
    public void registerUser(RegistrationRequestModel model, RegisterUserCallBack callBack) {

    }

    @Override
    public void getCenterList(Map<String, String> map, GetCenterListCallBack callBack) {

    }

    @Override
    public void getOffers(GetOfferListCallBack callBack) {

    }

    @Override
    public void getCategoryList(Map<String, String> map, GetCategoryListCallBack callBack) {

    }

    @Override
    public void getAppointments(String url, GetAppointmentsCallBack callBack) {

    }

    @Override
    public void updateUser(GuestUpdateRequestModel model, UpdateUserCallBack callBack) {

    }

    @Override
    public void changePassword(ChangePasswordRequestModel model, ChangePasswordCallBack callBack) {

    }

    @Override
    public void getServiceList(Map<String, String> map, GetServiceListCallBack callBack) {

    }

    @Override
    public void getTherapist(Map<String, String> map, GetTherapistCallBack callBack) {

    }

    @Override
    public void getTimeSlots(AppointmentRequestModel model, GetTimeSlotsCallBack callBack) {

    }

    @Override
    public void reserveSlot(ReserveSlotRequestModel model, ReserveSlotCallBack callBack) {

    }

    @Override
    public void createOrder(CreateOrderRequestModel model, CreateOrderCallBack callBack) {

    }

    @Override
    public void getInvoice(String url, GetInvoiceCallBack callBack) {

    }

    @Override
    public void confirmReservation(ConfirmReservationRequestModel model, ConfirmReservationCallBack callBack) {

    }

    @Override
    public void confirmOrder(ConfirmOrderRequestModel model, ConfirmOrderCallBack callBack) {

    }

    @Override
    public void getNewAndPopularServices(Map<String, String> map, NewAndPopularServicesCallBack callBack) {

    }

    @Override
    public void cancelAppointment(String url, CancelRequestModel model, CancelAppointmentCallBack callBack) {

    }

    @Override
    public void forgotPassword(ForgotPasswordRequestModel model, ForgotPasswordCallBack callBack) {

    }

    @Override
    public void getAllPackages(PackageListCallBack callBack) {

    }

    @Override
    public void getPackagesDetails(Map<String, String> map, GetPackageDetailsCallback callBack) {

    }

    @Override
    public void getWallet(Map<String, String> string, GetWalletCallback callback) {

    }

    @Override
    public void getWalletHistory(Map<String, String> string, GetWalletHistoryCallback callback) {

    }

    @Override
    public void getProductList(ProductRequestModel model, GetProductListCallback callback) {

    }

    @Override
    public void getProductDetails(Map<String, String> map, GetProductDetailsCallback callback) {

    }

    @Override
    public void getMyPackages(Map<String, String> map, GetMyPackagesCallback callback) {

    }
}
