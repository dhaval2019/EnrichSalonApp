package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.ConfirmOrderRequestModel;
import com.enrich.salonapp.data.model.ConfirmOrderResponseModel;
import com.enrich.salonapp.data.model.ConfirmReservationRequestModel;
import com.enrich.salonapp.data.model.ConfirmReservationResponseModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOrderRequestModel;
import com.enrich.salonapp.data.model.CreateOrder.CreateOrderResponseModel;
import com.enrich.salonapp.data.model.InvoiceResponseModel;
import com.enrich.salonapp.data.model.ReserveSlotRequestModel;
import com.enrich.salonapp.data.model.ReserveSlotResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

public interface BookingSummaryContract {

    interface View extends IBaseView {
        void slotReserved(ReserveSlotResponseModel model);

        void orderCreated(CreateOrderResponseModel model);

        void invoiceCreated(InvoiceResponseModel model);

        void reservationConfirmed(ConfirmReservationResponseModel model);

        void orderConfirmed(ConfirmOrderResponseModel model);

        void orderNotConfirmed();
    }

    interface Presenter extends IBasePresenter<BookingSummaryContract.View> {
        void reserveSlot(Context context, ReserveSlotRequestModel model);

        void createOrder(Context context, CreateOrderRequestModel model);

        void getInvoice(Context context, String url);

        void confirmReservation(Context context, ConfirmReservationRequestModel model);

        void confirmOrder(Context context, ConfirmOrderRequestModel model);
    }
}
