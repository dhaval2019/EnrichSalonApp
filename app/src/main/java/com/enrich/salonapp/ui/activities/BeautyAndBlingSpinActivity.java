package com.enrich.salonapp.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.CampaignRewardModel;
import com.enrich.salonapp.data.model.CampaignRewardResponseModel;
import com.enrich.salonapp.data.model.SpinPriceModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.contracts.SpinPriceContract;
import com.enrich.salonapp.ui.presenters.SpinPricePresenter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

public class BeautyAndBlingSpinActivity extends BaseActivity implements SpinPriceContract.View {

    private static int ODD = 1;
    private static int EVEN = 2;

    List<LuckyItem> data = new ArrayList<>();
    List<LuckyItem> loadingData = new ArrayList<>();
    int[] array = {600, 700, 1250, 1750, 2000, 1000, 800, 1500, 900, 2500};
    int index;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long updatedTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    boolean stopBtnClicked = true;
    int[] rainbowColor;
    int count = 0;

    boolean isNewUser;

    @BindView(R.id.play)
    TextView playBtn;

    @BindView(R.id.stop)
    TextView stopButton;

    @BindView(R.id.wait)
    TextView waitButton;

    @BindView(R.id.showTimerId)
    TextView displayTimer;

    @BindView(R.id.luckyWheel)
    LuckyWheelView luckyWheelView;

    @BindView(R.id.loader_luckyWheel)
    LuckyWheelView loaderLuckyWheel;

    @BindView(R.id.loader_spin_container)
    FrameLayout loaderSpinContainer;

    @BindView(R.id.spin_container)
    RelativeLayout spinContainer;

    private DataRepository dataRepository;
    private SpinPricePresenter spinPricePresenter;

    private int price = -1;
    private int spinIndex;
    private String invoiceId;
    private String centerId;
    private String purchaseType;

    private ArrayList<CampaignRewardModel> priceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin);

        ButterKnife.bind(this);

        isNewUser = getIntent().getBooleanExtra("IsNewUser", false);
        spinIndex = getIntent().getIntExtra("SpinIndex", -1);
        invoiceId = getIntent().getStringExtra("InvoiceId");

        if (isNewUser) {
            centerId = EnrichUtils.getHomeStore(this).Id;
        } else {
            centerId = getIntent().getStringExtra("CenterId");
        }

        purchaseType = getIntent().getStringExtra("PurchaseType");
        if (purchaseType == null) {
            purchaseType = "Service";
        }

//        setLuckyLoad();

        dataRepository = Injection.provideDataRepository(this, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        spinPricePresenter = new SpinPricePresenter(this, dataRepository);

        playBtn.setTextColor(getResources().getColor(R.color.blueMedium));
        stopButton.setTextColor(getResources().getColor(R.color.blueMedium));
        waitButton.setTextColor(getResources().getColor(R.color.blueMedium));
        rainbowColor = getResources().getIntArray(R.array.rainbow);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPrice();

                stopButton.setEnabled(false);
                luckyWheelView.setRound(getRandomRound());
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 10000);
                stopButton.setGravity(Gravity.CENTER | Gravity.CENTER);

                stopBtnClicked = false;
                index = getRandomIndex();
//                Toast.makeText(getApplicationContext(), String.valueOf(index), Toast.LENGTH_SHORT).show();
                luckyWheelView.startLuckyWheelWithTargetIndex(price);
                playBtn.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
                waitButton.setVisibility(View.GONE);

            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EnrichApplication.getSpinList() != null)
                    EnrichApplication.getSpinList().get(spinIndex).isSpinTaken = true;

                luckyWheelView.setRound(6);
                stopBtnClicked = true;

//                if (price == -1) {
//                    index = getRandomIndex();
//                    price = priceList.get(index).RewardValue;
//                    luckyWheelView.stopLuckeyWheelWithTargetIndex(index);
//                } else {
                luckyWheelView.stopLuckeyWheelWithTargetIndex(getIndexOfPriceList(price));
                if (EnrichApplication.getSpinList() != null)
                    EnrichApplication.getSpinList().get(spinIndex).prizeWon = price;
//                }

                customHandler.removeCallbacks(updateTimerThread);
                displayTimer.setVisibility(View.GONE);

                playBtn.setVisibility(View.GONE);
                stopButton.setVisibility(View.GONE);
                waitButton.setVisibility(View.VISIBLE);

            }
        });

        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(final int index) {
                luckyWheelView.clearAnimation();
                stopButton.setVisibility(View.GONE);
                waitButton.setVisibility(View.GONE);
                customHandler.removeCallbacks(updateTimerThread);
                displayTimer.setVisibility(View.GONE);
                count++;

                if (!stopBtnClicked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            BeautyAndBlingSpinActivity.this);
                    builder.setTitle(getResources().getString(R.string.alert));
                    builder.setMessage(getResources().getString(R.string.alert_timeout));
                    builder.setPositiveButton(getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });
                    builder.show();
                } else {
                    if (count == 1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isNewUser) {
                                    Intent intent = new Intent(BeautyAndBlingSpinActivity.this, BeautyAndBlingWinningActivity.class);
                                    intent.putExtra("PointsWon", price);
                                    intent.putExtra("IsNewUser", isNewUser);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(BeautyAndBlingSpinActivity.this, BeautyAndBlingNewUserWinningActivity.class);
                                    intent.putExtra("PointsWon", price);
                                    intent.putExtra("IsNewUser", isNewUser);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }, 7500);
//                    Toast.makeText(getApplicationContext(), String.valueOf(index), Toast.LENGTH_SHORT).show();
                    }
                }
                playBtn.setVisibility(View.VISIBLE);

            }
        });

        Map<String, String> campaignMap = new HashMap<>();
        campaignMap.put("CenterId", centerId);
        spinPricePresenter.getCampaignRewards(this, campaignMap);
    }

    private void getPrice() {
        Map<String, String> map = new HashMap<>();
        map.put("GuestId", EnrichUtils.getUserData(this).Id);
        map.put("CenterId", centerId);
        if (invoiceId != null)
            map.put("InvoiceId", invoiceId);

        if (isNewUser)
            map.put("SpinType", "N");
        else
            map.put("SpinType", "E");

        spinPricePresenter.getSpinPrice(this, map);
    }

    private void setLuckyLoad() {
        for (int i = 0; i < array.length; i++) {
            LuckyItem luckyItem = new LuckyItem();
            luckyItem.text = array[i] + "";
            if (i % 2 != 0) {
                luckyItem.color = getResources().getColor(R.color.blueMedium);
            } else {
                luckyItem.color = getResources().getColor(R.color.blueLight);

            }
            loadingData.add(luckyItem);
        }

        luckyWheelView.setData(loadingData);
    }

    private int getRandomIndex() {
        return new Random().nextInt(priceList.size());
    }

    private int getRandomRound() {
        Random rand = new Random();
        return rand.nextInt(10) + 15;
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            //int milliseconds = (int) (updatedTime % 1000);
            displayTimer.setVisibility(View.VISIBLE);
            // Random rnd = new Random();
            // int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            displayTimer.setText("00:" + String.format("%02d", Math.abs((secs - 60))));
            displayTimer.setGravity(Gravity.CENTER | Gravity.TOP);
            stopButton.setGravity(Gravity.CENTER | Gravity.BOTTOM);
            //displayTimer.setText( "00:"+(secs-29)+"" );
            displayTimer.setTextColor(rainbowColor[new Random().nextInt(rainbowColor.length)]);

            Log.v("Timer--", String.format("%02d", secs));

            customHandler.postDelayed(this, 1000);
            if (mins == 1) {
                // Toast.makeText(getApplicationContext(), "Time out error", Toast.LENGTH_SHORT).show();
                displayTimer.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void setSpinPrice(SpinPriceModel model) {
        if (model.PriceWon != 0) {
            price = model.PriceWon;
            stopButton.setEnabled(true);
        }
    }

    @Override
    public void setCampaignRewards(CampaignRewardResponseModel model) {
        if (model.Error == null) {
            loaderSpinContainer.setVisibility(View.GONE);
            spinContainer.setVisibility(View.VISIBLE);

            if (purchaseType.equalsIgnoreCase("Service")) {
                priceList = model.ServiceCampaignReward;

                if (isOddEven(priceList.size()) == ODD) {
                    CampaignRewardModel campaignRewardModel = new CampaignRewardModel();
                    campaignRewardModel.Id = 0;
                    campaignRewardModel.CampaignId = 0;
                    campaignRewardModel.CampaignName = "Beauty N Bling";
                    campaignRewardModel.RewardName = "R00";
                    campaignRewardModel.RewardValue = 0;
                    campaignRewardModel.RewardWeightage = 0;
                    campaignRewardModel.CampaignType = "SERVICE";
                    campaignRewardModel.Count = 1;
                    priceList.add(campaignRewardModel);
                }

                for (int i = 0; i < model.ServiceCampaignReward.size(); i++) {
                    LuckyItem luckyItem = new LuckyItem();
                    luckyItem.text = model.ServiceCampaignReward.get(i).RewardValue + "";
                    if (i % 2 != 0) {
                        luckyItem.color = getResources().getColor(R.color.blueMedium);
                    } else {
                        luckyItem.color = getResources().getColor(R.color.blueLight);

                    }
                    data.add(luckyItem);
                }
            } else if (purchaseType.equalsIgnoreCase("Product")) {
                priceList = model.ProductCampaignReward;

                priceList = model.ServiceCampaignReward;

                if (isOddEven(priceList.size()) == ODD) {
                    CampaignRewardModel campaignRewardModel = new CampaignRewardModel();
                    campaignRewardModel.Id = 0;
                    campaignRewardModel.CampaignId = 0;
                    campaignRewardModel.CampaignName = "Beauty N Bling";
                    campaignRewardModel.RewardName = "R00";
                    campaignRewardModel.RewardValue = 0;
                    campaignRewardModel.RewardWeightage = 0;
                    campaignRewardModel.CampaignType = "PRODUCT";
                    campaignRewardModel.Count = 1;
                    priceList.add(campaignRewardModel);
                }

                for (int i = 0; i < model.ProductCampaignReward.size(); i++) {
                    LuckyItem luckyItem = new LuckyItem();
                    luckyItem.text = model.ProductCampaignReward.get(i).RewardValue + "";
                    if (i % 2 != 0) {
                        luckyItem.color = getResources().getColor(R.color.blueMedium);
                    } else {
                        luckyItem.color = getResources().getColor(R.color.blueLight);

                    }
                    data.add(luckyItem);
                }
            }

            luckyWheelView.setData(data);
        } else {
            loaderSpinContainer.setVisibility(View.VISIBLE);
            spinContainer.setVisibility(View.GONE);
        }
    }

    private int getIndexOfPriceList(int price) {
        for (int i = 0; i < priceList.size(); i++) {
            if (price == priceList.get(i).RewardValue) {
                return (i + 1);
            }
        }
        return 0;
    }

    private int isOddEven(int x) {
        if (x % 2 == 0)
            return EVEN;
        else
            return ODD;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
