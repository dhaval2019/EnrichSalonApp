package com.enrich.salonapp.util.mvp;

import android.content.Context;

public interface IBaseBottomSheetDialogFragment {
    void showToastMessage(Context context, String message);

    void setProgressBar(boolean show);

    Context getContext();
}
