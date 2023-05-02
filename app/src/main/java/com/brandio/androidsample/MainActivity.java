package com.brandio.androidsample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brandio.ads.Controller;
import com.brandio.ads.ads.AdUnitType;
import com.brandio.ads.exceptions.DIOError;
import com.brandio.ads.listeners.SdkInitListener;
import com.brandio.androidsample.utils.RecyclerTouchListener;


public class MainActivity extends AppCompatActivity {

    public static final String PLACEMENT_ID = "placementId";
    public static final String REQUEST_ID = "requestId";
    public static final String AD_UNIT_TYPE = "adUnitType";
    public static final String NAME = "placementName";
    private static String TAG = "MainActivity";

    private static final String APP_ID = "6494";

    private static final PlacementListItem[] data = {
            new PlacementListItem("4654", AdUnitType.INTERSTITIAL, "Interstitial Display"),
            new PlacementListItem("3231", AdUnitType.INTERSTITIAL, "Interstitial Video"),
            new PlacementListItem("4655", AdUnitType.INFEED, "Infeed Video"),
            new PlacementListItem("5369", AdUnitType.INFEED, "Infeed Display"),
            new PlacementListItem("6428", AdUnitType.BANNER, "Banner"),
            new PlacementListItem("6429", AdUnitType.MEDIUMRECTANGLE, "Medium Rectangle"),
            new PlacementListItem("6430", AdUnitType.INTERSCROLLER, "Interscroller"),
            new PlacementListItem("6735", AdUnitType.HEADLINE, "Headline Video"),
            new PlacementListItem("6955", AdUnitType.HEADLINE, "Headline Display"),
            new PlacementListItem("6430", AdUnitType.INTERSCROLLER, "Interscroller (ViewPager)"),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Controller ctrl = Controller.getInstance();
        if (!ctrl.isInitialized()) {
            ctrl.init(this, null, APP_ID, new SdkInitListener() {
                @Override
                public void onInit() {
                    Log.i(TAG, "Controller initialized");
                    postInit();
                }

                @Override
                public void onInitError(DIOError error) {
                    Toast.makeText(MainActivity.this,
                                    "Error initialize SDK, check your internet connection",
                                    Toast.LENGTH_LONG)
                            .show();
                    Log.e(TAG, error.getMessage());
                }
            });
        } else {
            postInit();
        }
    }

    private void postInit() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        PlacementListAdapter adapter = new PlacementListAdapter(data);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                switch (data[position].type) {
                    case INFEED:
                    case INTERSCROLLER:
                    case HEADLINE: {
                        Intent intent = new Intent(MainActivity.this, LoadInfeedActivity.class);
                        intent.putExtra(PLACEMENT_ID, data[position].id);
                        intent.putExtra(AD_UNIT_TYPE, data[position].type.name());
                        intent.putExtra(NAME, data[position].name);
                        startActivity(intent);
                        break;
                    }

                    case INTERSTITIAL:
                    case REWARDEDVIDEO: {
                        Intent intent = new Intent(MainActivity.this, InterstitialActivity.class);
                        intent.putExtra(PLACEMENT_ID, data[position].id);
                        startActivity(intent);
                        break;
                    }

                    case BANNER:
                    case MEDIUMRECTANGLE: {
                        Intent intent = new Intent(MainActivity.this, BannerAndMediumRectangleActivity.class);
                        intent.putExtra(PLACEMENT_ID, data[position].id);
                        intent.putExtra(AD_UNIT_TYPE, data[position].type.name());
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
}
