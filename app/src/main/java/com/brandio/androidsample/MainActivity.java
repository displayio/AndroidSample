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
import com.brandio.androidsample.plc.PlacementListAdapter;
import com.brandio.androidsample.plc.PlacementListItem;
import com.brandio.androidsample.plc.RecyclerTouchListener;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    public static final String PLACEMENT_ID = "placementId";
    public static final String NAME = "placementName";

    //app id required to initialize SDK
    private static final String APP_ID = "6494";

    private static final PlacementListItem[] data = {
            new PlacementListItem("3231", AdUnitType.INTERSTITIAL, "Interstitial Video"),
            new PlacementListItem("4654", AdUnitType.INTERSTITIAL, "Interstitial Display"),
            new PlacementListItem("4655", AdUnitType.INFEED, "Infeed Video"),
            new PlacementListItem("5369", AdUnitType.INFEED, "Infeed Display"),
            new PlacementListItem("6428", AdUnitType.BANNER, "Banner"),
            new PlacementListItem("6429", AdUnitType.MEDIUMRECTANGLE, "Medium Rectangle"),
            new PlacementListItem("6735", AdUnitType.INTERSCROLLER, "Interscroller Video"),
            new PlacementListItem("6430", AdUnitType.INTERSCROLLER, "Interscroller Display"),
            new PlacementListItem("6430", AdUnitType.INTERSCROLLER, "Interscroller (ViewPager)"),
            new PlacementListItem("6430", AdUnitType.INTERSCROLLER, "Interscroller ORTB (ViewPager)"),
            new PlacementListItem("4655", AdUnitType.INFEED, "Infeed Video (Compose)"),
            new PlacementListItem("6430", AdUnitType.INTERSCROLLER, "Interscroller Display (Compose)"),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Controller ctrl = Controller.getInstance();
        //call init method with your app id at the beginning of your app
        ctrl.init(this, APP_ID, new SdkInitListener() {
            @Override
            public void onInit() {
                Toast.makeText(MainActivity.this,
                                "DIO SDK initialized",
                                Toast.LENGTH_LONG)
                        .show();
                postInit();
            }

            @Override
            public void onInitError(DIOError error) {
                Toast.makeText(MainActivity.this,
                                "Error initialize SDK, check your internet connection",
                                Toast.LENGTH_LONG)
                        .show();
                Log.e(TAG, Objects.requireNonNull(error.getMessage()));
            }
        });

    }

    private void postInit() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PlacementListAdapter(data));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                switch (data[position].type) {
                    case INFEED:
                    case INTERSCROLLER: {
                        String name = data[position].name;
                        Class clazz;
                        if (name.contains("Compose")) {
                            clazz = ComposeFeedActivity.class;
                        } else {
                            clazz = name.contains("ViewPager") ? ViewPagerActivity.class : FeedActivity.class;
                        }

                        Intent intent = new Intent(MainActivity.this, clazz);
                        intent.putExtra(PLACEMENT_ID, data[position].id);
                        intent.putExtra(NAME, name);
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
