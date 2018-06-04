package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.TherapistResponseModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface TherapistContract {

    interface View extends IBaseView {
        void showTherapist(TherapistResponseModel model);
    }

    interface Presenter extends IBasePresenter<TherapistContract.View>{
        void getTherapist(Context context, Map<String, String> map);
    }
}
