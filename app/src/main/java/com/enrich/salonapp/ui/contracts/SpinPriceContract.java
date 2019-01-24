package com.enrich.salonapp.ui.contracts;

import android.content.Context;

import com.enrich.salonapp.data.model.CampaignRewardResponseModel;
import com.enrich.salonapp.data.model.SpinPriceModel;
import com.enrich.salonapp.util.mvp.IBasePresenter;
import com.enrich.salonapp.util.mvp.IBaseView;

import java.util.Map;

public interface SpinPriceContract {

    interface View extends IBaseView {
        void setSpinPrice(SpinPriceModel model);

        void setCampaignRewards(CampaignRewardResponseModel model);
    }

    interface Presenter extends IBasePresenter<View> {
        void getSpinPrice(Context context, Map<String, String> map);

        void getCampaignRewards(Context context, Map<String, String> map);
    }
}
