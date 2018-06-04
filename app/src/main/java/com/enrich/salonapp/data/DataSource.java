package com.enrich.salonapp.data;

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
import com.enrich.salonapp.data.model.GuestModel;
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
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;

import java.util.Map;

public abstract class DataSource {

    protected MainUiThread mainUiThread;
    protected ThreadExecutor threadExecutor;

    public DataSource(MainUiThread mainUiThread, ThreadExecutor threadExecutor) {
        this.mainUiThread = mainUiThread;
        this.threadExecutor = threadExecutor;
    }

    public interface GetIsUserRegisteredCallBack {
        void onSuccess(int responseCode);

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

    public abstract void getOffers(GetOfferListCallBack callBack);


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

    public abstract void getAppointments(String url, GetAppointmentsCallBack callBack);

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

    public abstract void getTimeSlots(AppointmentRequestModel model, GetTimeSlotsCallBack callBack);


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
}
