package com.brandio.androidsample;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.brandio.androidsample.DIOAdConfig.APP_ID;
import static com.brandio.androidsample.DIOAdConfig.BANNER_ID;
import static com.brandio.androidsample.DIOAdConfig.INFEED_DISPLAY_ID;
import static com.brandio.androidsample.DIOAdConfig.INFEED_VIDEO_ID;
import static com.brandio.androidsample.DIOAdConfig.INGAME_AUDIO_ID;
import static com.brandio.androidsample.DIOAdConfig.INTERSCROLLER_DISPLAY_ID;
import static com.brandio.androidsample.DIOAdConfig.INTERSCROLLER_VIDEO_ID;
import static com.brandio.androidsample.DIOAdConfig.INTERSTITIAL_DISPLAY_ID;
import static com.brandio.androidsample.DIOAdConfig.INTERSTITIAL_VIDEO_ID;
import static com.brandio.androidsample.DIOAdConfig.MEDIUM_RECTANGLE_ID;
import static com.brandio.androidsample.DIOAdConfig.NATIVE_ID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brandio.ads.Controller;
import com.brandio.ads.ads.Ad;
import com.brandio.ads.ads.AdUnit;
import com.brandio.ads.ads.AdUnitType;
import com.brandio.ads.containers.InGameAudioContainer;
import com.brandio.ads.exceptions.DIOError;
import com.brandio.ads.listeners.AdEventListener;
import com.brandio.ads.listeners.AdRequestListener;
import com.brandio.ads.listeners.SdkInitListener;
import com.brandio.ads.placements.InGameAudioPlacement;
import com.brandio.ads.request.AdRequest;
import com.brandio.androidsample.plc.PlacementListAdapter;
import com.brandio.androidsample.plc.PlacementListItem;
import com.brandio.androidsample.plc.RecyclerTouchListener;
import com.brandio.androidsample.tools.DIOAdRequestHelper;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    public static final String PLACEMENT_ID = "placementId";
    public static final String NAME = "placementName";

    private static final PlacementListItem[] data = {
            new PlacementListItem(INTERSTITIAL_VIDEO_ID, AdUnitType.INTERSTITIAL, "Interstitial Video"),
            new PlacementListItem(INTERSTITIAL_DISPLAY_ID, AdUnitType.INTERSTITIAL, "Interstitial Display"),
            new PlacementListItem(INFEED_VIDEO_ID, AdUnitType.INFEED, "Infeed Video"),
            new PlacementListItem(INFEED_DISPLAY_ID, AdUnitType.INFEED, "Infeed Display"),
            new PlacementListItem(BANNER_ID, AdUnitType.BANNER, "Banner"),
            new PlacementListItem(MEDIUM_RECTANGLE_ID, AdUnitType.MEDIUMRECTANGLE, "Medium Rectangle"),
            new PlacementListItem(INTERSCROLLER_VIDEO_ID, AdUnitType.INTERSCROLLER, "Interscroller Video"),
            new PlacementListItem(INTERSCROLLER_DISPLAY_ID, AdUnitType.INTERSCROLLER, "Interscroller Display"),
            new PlacementListItem(INTERSCROLLER_DISPLAY_ID, AdUnitType.INTERSCROLLER, "Interscroller (ViewPager)"),
            new PlacementListItem(INTERSCROLLER_DISPLAY_ID, AdUnitType.INTERSCROLLER, "Interscroller ORTB (ViewPager)"),
            new PlacementListItem(INFEED_VIDEO_ID, AdUnitType.INFEED, "Infeed Video (Compose)"),
            new PlacementListItem(INTERSCROLLER_DISPLAY_ID, AdUnitType.INTERSCROLLER, "Interscroller Display (Compose)"),
            new PlacementListItem(NATIVE_ID, AdUnitType.NATIVE, "Native (Feed)"),
            new PlacementListItem(INGAME_AUDIO_ID, AdUnitType.INGAMEAUDIO, "In-Game Audio"),
    };

    private Button btnInitialize;
    private ProgressBar loader;
    private Ad inGameAudioAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();

        Controller ctrl = Controller.getInstance();
        if (ctrl.isInitialized()) {
            hideLoader();
            btnInitialize.setVisibility(GONE);
            postInit();
            return;
        }

        btnInitialize.setOnClickListener(v -> {
            showLoader();

            //call init method with your app id at the beginning of your app
            ctrl.init(MainActivity.this, APP_ID, new SdkInitListener() {
                @Override
                public void onInit() {
                    hideLoader();
                    btnInitialize.setVisibility(GONE);
                    Toast.makeText(MainActivity.this,
                                    "DIO SDK initialized, version : " + ctrl.getVer(),
                                    Toast.LENGTH_LONG)
                            .show();
                    postInit();
                }

                @Override
                public void onInitError(DIOError error) {
                    hideLoader();
                    Toast.makeText(MainActivity.this,
                                    "Error initialize SDK, check your internet connection",
                                    Toast.LENGTH_LONG)
                            .show();
                    Log.e(TAG, Objects.requireNonNull(error.getMessage()));
                }
            });
        });


    }


    private void postInit() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_main);
        recyclerView.setVisibility(VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PlacementListAdapter(data));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                switch (data[position].type) {
                    case INFEED:
                    case INTERSCROLLER:
                    case NATIVE: {
                        String name = data[position].name;
                        Class<? extends Activity> clazz;
                        if (name.contains("Compose")) {
                            clazz = ComposeFeedActivity.class;
                        } else if (name.contains("ViewPager")) {
                            clazz = ViewPagerActivity.class;
                        } else {
                            clazz = FeedActivity.class;
                        }

                        Intent intent = new Intent(MainActivity.this, clazz);
                        intent.putExtra(PLACEMENT_ID, data[position].id);
                        intent.putExtra(NAME, name);
                        startActivity(intent);
                        break;
                    }

                    case INTERSTITIAL: {
                        Intent intent = new Intent(MainActivity.this, InterstitialActivity.class);
                        intent.putExtra(PLACEMENT_ID, data[position].id);
                        startActivity(intent);
                        break;
                    }

                    case BANNER:
                    case MEDIUMRECTANGLE: {
                        Intent intent = new Intent(MainActivity.this, BannerAndMediumRectangleActivity.class);
                        intent.putExtra(PLACEMENT_ID, data[position].id);
                        startActivity(intent);
                        break;
                    }

                    // in-game audio card is bound right into this screen, on top of the list
                    case INGAMEAUDIO: {
                        loadInGameAudioAd(data[position].id);
                        break;
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void loadInGameAudioAd(String placementId) {
        final InGameAudioPlacement placement;
        try {
            placement = (InGameAudioPlacement) Controller.getInstance().getPlacement(placementId);
        } catch (Exception e) {
            Toast.makeText(this, "DioSdkException: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
//        placement.setCustomWidth(200); // uncomment to test a custom card size

        // only one card at a time: close() also removes the card view from the hierarchy
        if (inGameAudioAd != null) {
            inGameAudioAd.close();
        }

//        final AdRequest adRequest = placement.newAdRequest(); // use default ad request
        final AdRequest adRequest = DIOAdRequestHelper.createAndPopulateAdRequest(placement);
        adRequest.setAdRequestListener(new AdRequestListener() {
            @Override
            public void onAdReceived(Ad ad) {
                ad.setEventListener(new AdEventListener() {
                    @Override
                    public void onShown(Ad ad) {
                        Log.d(TAG, "in-game audio onShown");
                    }

                    @Override
                    public void onFailedToShow(Ad ad, DIOError error) {
                        Log.e(TAG, "in-game audio onFailedToShow");
                    }

                    @Override
                    public void onClicked(Ad ad) {
                        Log.d(TAG, "in-game audio onClicked");
                    }

                    @Override
                    public void onClosed(Ad ad) {
                        Log.d(TAG, "in-game audio onClosed");
                        if (inGameAudioAd == ad) {
                            inGameAudioAd = null;
                        }
                    }

                    @Override
                    public void onAdCompleted(Ad ad) {
                    }
                });

                inGameAudioAd = ad;

                // top right corner of the screen, over the placement list
                ConstraintLayout root = findViewById(R.id.main_root);
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topToBottom = R.id.app_bar_layout;
                params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
                params.topMargin = AdUnit.dpToPx(16);
                params.setMarginEnd(AdUnit.dpToPx(16));

                InGameAudioContainer container = placement.getContainer(ad.getRequestId());
                container.bindTo(root, params);
            }

            @Override
            public void onNoAds(DIOError error) {
                Toast.makeText(MainActivity.this, "No ads", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailedToLoad(DIOError error) {
                Toast.makeText(MainActivity.this, "Failed to load ad", Toast.LENGTH_SHORT).show();
            }
        });
        adRequest.requestAd();
    }

    private void setupView() {
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorLightGrey));
        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Toolbar toolbar = findViewById(R.id.app_bar_layout);
        setSupportActionBar(toolbar);
        btnInitialize = findViewById(R.id.btnInitialize);
        loader = findViewById(R.id.initLoader);
    }

    private void showLoader() {
        loader.setVisibility(View.VISIBLE);
        btnInitialize.setVisibility(GONE);
    }
    private void hideLoader() {
        loader.setVisibility(GONE);
        if (!Controller.getInstance().isInitialized()) {
            btnInitialize.setVisibility(VISIBLE);
        }
    }
}
