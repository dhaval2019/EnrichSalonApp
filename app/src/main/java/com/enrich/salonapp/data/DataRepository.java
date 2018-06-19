package com.enrich.salonapp.data;

import android.content.Context;

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
import com.enrich.salonapp.data.model.GuestModel;
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
import com.enrich.salonapp.data.model.Wallet.WalletModel;
import com.enrich.salonapp.util.NetworkHelper;

import java.util.Map;

public class DataRepository {

    private DataSource remoteDataSource;
    private DataSource localDataSource;
    private NetworkHelper networkHelper;
    private static DataRepository dataRepository;

    public DataRepository(DataSource remoteDataSource, DataSource localDataSource,
                          NetworkHelper networkHelper) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.networkHelper = networkHelper;
    }

    // Singleton instance
    public static synchronized DataRepository getInstance(DataSource remoteDataSource,
                                                          DataSource localDataSource,
                                                          NetworkHelper networkHelper) {
        if (dataRepository == null) {
            dataRepository = new DataRepository(remoteDataSource, localDataSource, networkHelper);
        }
        return dataRepository;
    }

    public void isUserRegistered(Context context, String phoneNumber, final DataSource.GetIsUserRegisteredCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.isUserRegistered(phoneNumber, new DataSource.GetIsUserRegisteredCallBack() {
                @Override
                public void onSuccess(int responseCode) {
                    callBack.onSuccess(responseCode);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void getAuthentication(Context context, AuthenticationRequestModel model, final DataSource.GetAuthenticationCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getAuthenticationToken(model, new DataSource.GetAuthenticationCallBack() {
                @Override
                public void onSuccess(AuthenticationModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void getUserData(Context context, String guestId, final DataSource.GetGuestDataCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getGuestData(guestId, new DataSource.GetGuestDataCallBack() {
                @Override
                public void onSuccess(GuestModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void checkUserName(Context context, String username, final DataSource.CheckUserNameCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.checkUsername(username, new DataSource.CheckUserNameCallBack() {
                @Override
                public void onSuccess(CheckUserNameResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }

    }

    public void createOTP(Context context, CreateOTPRequestModel model, final DataSource.CreateOTPCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.createOTP(model, new DataSource.CreateOTPCallBack() {
                @Override
                public void onSuccess(CreateOTPResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void registerUser(Context context, RegistrationRequestModel model, final DataSource.RegisterUserCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.registerUser(model, new DataSource.RegisterUserCallBack() {
                @Override
                public void onSuccess(RegistrationResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void getCenterList(Context context, Map<String, String> map, final DataSource.GetCenterListCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getCenterList(map, new DataSource.GetCenterListCallBack() {
                @Override
                public void onSuccess(CenterResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void getOffersList(Context context, final DataSource.GetOfferListCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getOffers(new DataSource.GetOfferListCallBack() {
                @Override
                public void onSuccess(OfferResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void getCategories(Context context, Map<String, String> map, final DataSource.GetCategoryListCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getCategoryList(map, new DataSource.GetCategoryListCallBack() {
                @Override
                public void onSuccess(CategoryResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void getAppointment(Context context, String url, final DataSource.GetAppointmentsCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getAppointments(url, new DataSource.GetAppointmentsCallBack() {
                @Override
                public void onSuccess(AppointmentResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void updateUser(Context context, GuestUpdateRequestModel model, final DataSource.UpdateUserCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.updateUser(model, new DataSource.UpdateUserCallBack() {
                @Override
                public void onSuccess(GuestUpdateResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void changePassword(Context context, ChangePasswordRequestModel model, final DataSource.ChangePasswordCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.changePassword(model, new DataSource.ChangePasswordCallBack() {
                @Override
                public void onSuccess(ChangePasswordResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void getServiceList(Context context, Map<String, String> map, final DataSource.GetServiceListCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getServiceList(map, new DataSource.GetServiceListCallBack() {
                @Override
                public void onSuccess(ServiceListResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void getTherapist(Context context, Map<String, String> map, final DataSource.GetTherapistCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getTherapist(map, new DataSource.GetTherapistCallBack() {
                @Override
                public void onSuccess(TherapistResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void getTimeSlot(Context context, AppointmentRequestModel model, final DataSource.GetTimeSlotsCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getTimeSlots(model, new DataSource.GetTimeSlotsCallBack() {
                @Override
                public void onSuccess(AvailableTimeResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void reserveSlot(Context context, ReserveSlotRequestModel model, final DataSource.ReserveSlotCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.reserveSlot(model, new DataSource.ReserveSlotCallBack() {
                @Override
                public void onSuccess(ReserveSlotResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void createOrder(Context context, CreateOrderRequestModel model, final DataSource.CreateOrderCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.createOrder(model, new DataSource.CreateOrderCallBack() {
                @Override
                public void onSuccess(CreateOrderResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void getInvoice(Context context, String url, final DataSource.GetInvoiceCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getInvoice(url, new DataSource.GetInvoiceCallBack() {
                @Override
                public void onSuccess(InvoiceResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void confirmReservation(Context context, ConfirmReservationRequestModel model, final DataSource.ConfirmReservationCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.confirmReservation(model, new DataSource.ConfirmReservationCallBack() {
                @Override
                public void onSuccess(ConfirmReservationResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    onNetworkFailure();
                }
            });
        }
    }

    public void confirmOrder(Context context, ConfirmOrderRequestModel model, final DataSource.ConfirmOrderCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.confirmOrder(model, new DataSource.ConfirmOrderCallBack() {
                @Override
                public void onSuccess(ConfirmOrderResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    onNetworkFailure();
                }
            });
        }
    }

    public void getNewAndPopularServices(Context context, Map<String, String> map, final DataSource.NewAndPopularServicesCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getNewAndPopularServices(map, new DataSource.NewAndPopularServicesCallBack() {
                @Override
                public void onSuccess(NewAndPopularResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void cancelAppointment(Context context, String url, CancelRequestModel model, final DataSource.CancelAppointmentCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.cancelAppointment(url, model, new DataSource.CancelAppointmentCallBack() {
                @Override
                public void onSuccess(CancelResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void forgotPassword(Context context, ForgotPasswordRequestModel model, final DataSource.ForgotPasswordCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.forgotPassword(model, new DataSource.ForgotPasswordCallBack() {
                @Override
                public void onSuccess(ForgotPasswordResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void getAllPackages(Context context, final DataSource.PackageListCallBack callBack) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getAllPackages(new DataSource.PackageListCallBack() {
                @Override
                public void onSuccess(PackageResponseModel model) {
                    callBack.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callBack.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callBack.onNetworkFailure();
                }
            });
        }
    }

    public void getWallet(Context context, Map<String, String> map, final DataSource.GetWalletCallback callback) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getWallet(map, new DataSource.GetWalletCallback() {
                @Override
                public void onSuccess(WalletModel model) {
                    callback.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callback.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callback.onNetworkFailure();
                }
            });
        }
    }

    public void getProducts(Context context, ProductRequestModel model, final DataSource.GetProductListCallback callback) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getProductList(model, new DataSource.GetProductListCallback() {
                @Override
                public void onSuccess(ProductResponseModel model) {
                    callback.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callback.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callback.onNetworkFailure();
                }
            });
        }
    }

    public void getMyPackages(Context context, Map<String, String> map, final DataSource.GetMyPackagesCallback callback) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getMyPackages(map, new DataSource.GetMyPackagesCallback() {
                @Override
                public void onSuccess(MyPackageResponseModel model) {
                    callback.onSuccess(model);
                }

                @Override
                public void onFailure(Throwable t) {
                    callback.onFailure(t);
                }

                @Override
                public void onNetworkFailure() {
                    callback.onNetworkFailure();
                }
            });
        }
    }
}
