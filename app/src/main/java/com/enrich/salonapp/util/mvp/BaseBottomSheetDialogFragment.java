package com.enrich.salonapp.util.mvp;

import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.enrich.salonapp.R;

import butterknife.BindView;

public class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment implements IBaseView {

    @BindView(R.id.progressBar)
    protected ProgressBar progressBar;

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
