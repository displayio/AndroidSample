package com.brandio.androidsample;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.brandio.androidsample.DIOAdConfig.APP_ID;
import static com.brandio.androidsample.DIOAdConfig.BANNER_ID;
import static com.brandio.androidsample.DIOAdConfig.INFEED_DISPLAY_ID;
import static com.brandio.androidsample.DIOAdConfig.INFEED_VIDEO_ID;
import static com.brandio.androidsample.DIOAdConfig.INTERSCROLLER_DISPLAY_ID;
import static com.brandio.androidsample.DIOAdConfig.INTERSCROLLER_VIDEO_ID;
import static com.brandio.androidsample.DIOAdConfig.INTERSTITIAL_DISPLAY_ID;
import static com.brandio.androidsample.DIOAdConfig.INTERSTITIAL_VIDEO_ID;
import static com.brandio.androidsample.DIOAdConfig.MEDIUM_RECTANGLE_ID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brandio.ads.Controller;
import com.brandio.ads.ads.AdUnitType;
import com.brandio.ads.exceptions.DIOError;
import com.brandio.ads.listeners.SdkInitListener;
import com.brandio.androidsample.plc.PlacementListAdapter;
import com.brandio.androidsample.plc.PlacementListItem;
import com.brandio.androidsample.plc.RecyclerTouchListener;

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
//            new PlacementListItem(INTERSCROLLER_DISPLAY_ID, AdUnitType.INTERSCROLLER, "Interscroller Display (Compose)"),
    };

    private Button btnInitialize;
    private ProgressBar loader;

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
                    case INTERSCROLLER: {
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
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
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
