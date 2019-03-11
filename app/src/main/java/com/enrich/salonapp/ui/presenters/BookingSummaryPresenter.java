package com.enrich.salonapp.ui.presenters;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.DataSource;
import com.enrich.salonapp.data.model.ConfirmOrderRequestModel;
import com.enrich.salonapp.data.model.ConfirmOrderResponseModel;
import com.enrich.salonapp.data.model.ConfirmReservationRequestModel;
import com.enrich.salonapp.data.model.ConfirmReservationResponseModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOrderRequestModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOrderResponseModel;
import com.enrich.salonapp.data.model.InvoiceResponseModel;
import com.enrich.salonapp.data.model.ReserveSlotRequestModel;
import com.enrich.salonapp.data.model.ReserveSlotResponseModel;
import com.enrich.salonapp.ui.contracts.BookingSummaryContract;
import com.enrich.salonapp.util.mvp.BasePresenter;

public class BookingSummaryPresenter extends BasePresenter<BookingSummaryContract.View> implements BookingSummaryContract.Presenter {

    private DataRepository dataRepository;

    public BookingSummaryPresenter(BookingSummaryContract.View view, DataRepository dataRepository) {
        this.view = view;
        this.dataRepository = dataRepository;
    }

    @Override
    public void reserveSlot(Context context, ReserveSlotRequestModel model) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.reserveSlot(context, model, new DataSource.ReserveSlotCallBack() {
            @Override
            public void onSuccess(ReserveSlotResponseModel model) {
                if (view != null) {
                    view.slotReserved(model);
//                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("Something went wrong. Please try again later.");
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("No Network. Please try again later.");
                }
            }
        });
    }

    @Override
    public void createOrder(Context context, CreateOrderRequestModel model) {
        if (view == null)
            return;

//        view.setProgressBar(true);

        dataRepository.createOrder(context, model, new DataSource.CreateOrderCallBack() {
            @Override
            public void onSuccess(CreateOrderResponseModel model) {
                if (view != null) {
                    view.orderCreated(model);
//                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("Something went wrong. Please try again later.");
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("No Network. Please try again later.");
                }
            }
        });
    }

    @Override
    public void getInvoice(Context context, String url) {
        if (view == null)
            return;

//        view.setProgressBar(true);

        dataRepository.getInvoice(context, url, new DataSource.GetInvoiceCallBack() {
            @Override
            public void onSuccess(InvoiceResponseModel model) {
                if (view != null) {
                    view.invoiceCreated(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("Something went wrong. Please try again later.");
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("No Network. Please try again later.");
                }
            }
        });
    }

    @Override
    public void confirmReservation(Context context, ConfirmReservationRequestModel model) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.confirmReservation(context, model, new DataSource.ConfirmReservationCallBack() {
            @Override
            public void onSuccess(ConfirmReservationResponseModel model) {
                if (view != null) {
                    view.reservationConfirmed(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("Something went wrong. Please try again later.");
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("No Network. Please try again later.");
                }
            }
        });
    }

    @Override
    public void confirmOrder(Context context, ConfirmOrderRequestModel model) {
        if (view == null)
            return;

        view.setProgressBar(true);

        dataRepository.confirmOrder(context, model, new DataSource.ConfirmOrderCallBack() {
            @Override
            public void onSuccess(ConfirmOrderResponseModel model) {
                if (view != null) {
                    view.orderConfirmed(model);
                    view.setProgressBar(false);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (view != null) {
                    view.orderNotConfirmed();
                    view.setProgressBar(false);
                    view.showToastMessage("Something went wrong. Please try again later.");
                }
            }

            @Override
            public void onNetworkFailure() {
                if (view != null) {
                    view.setProgressBar(false);
                    view.showToastMessage("No Network. Please try again later.");
                }
            }
        });
    }
}
