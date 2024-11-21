package com.brandio.androidsample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.brandio.ads.Controller;
import com.brandio.ads.ads.Ad;
import com.brandio.ads.ads.AdUnitType;
import com.brandio.ads.containers.BannerContainer;
import com.brandio.ads.containers.InlineContainer;
import com.brandio.ads.containers.MediumRectangleContainer;
import com.brandio.ads.exceptions.DIOError;
import com.brandio.ads.listeners.AdEventListener;
import com.brandio.ads.listeners.AdRequestListener;
import com.brandio.ads.placements.BannerPlacement;
import com.brandio.ads.placements.MediumRectanglePlacement;
import com.brandio.ads.placements.Placement;
import com.brandio.ads.request.AdRequest;
import com.brandio.androidsample.tools.DIOAdRequestHelper;
import com.brandio.androidsample.tools.DIOAdViewBinder;

import java.util.ArrayDeque;

public class BannerAndMediumRectangleActivity extends AppCompatActivity {

    private static final String TAG = "BannerAndMRectActivity";
    private RelativeLayout reservedForAd;
    private Button loadNewAd;
    private Button showAdFromQueue;
    private Placement placement;
    private Ad displayedAd;
    private final ArrayDeque<Ad> receivedAds = new ArrayDeque<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_and_medium_rectangle);
        reservedForAd = findViewById(R.id.reserved_for_ad);
        loadNewAd = findViewById(R.id.load_new_ad);
        showAdFromQueue = findViewById(R.id.show_ad_from_queue);

        String placementId = getIntent().getStringExtra(MainActivity.PLACEMENT_ID);

        try {
            placement = Controller.getInstance().getPlacement(placementId);
            setupButtons();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(BannerAndMediumRectangleActivity.this,
                    "DioSdkException: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void setupButtons() {
        loadNewAd.setOnClickListener(v -> {

//                final AdRequest adRequest = placement.newAdRequest(); // use default ad request
            final AdRequest adRequest = DIOAdRequestHelper.createAndPopulateAdRequest(placement); // use customised ad request
            adRequest.setAdRequestListener(new AdRequestListener() {

                @Override
                public void onAdReceived(Ad ad) {
                    ad.setEventListener(new AdEventListener() {
                        @Override
                        public void onShown(Ad Ad) {
                            Log.e(TAG, "onShown");
                        }

                        @Override
                        public void onFailedToShow(Ad ad) {

                        }

                        @Override
                        public void onClicked(Ad ad) {
                            Log.e(TAG, "onClicked");
                        }

                        @Override
                        public void onClosed(Ad ad) {
                        }

                        @Override
                        public void onAdCompleted(Ad ad) {
                        }
                    });

                    receivedAds.addLast(ad);

                    Toast.makeText(BannerAndMediumRectangleActivity.this,
                            "Preloaded ads in queue = " + receivedAds.size(),
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNoAds(DIOError error) {
                    Toast.makeText(BannerAndMediumRectangleActivity.this,
                            "No ads",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailedToLoad(DIOError dioError) {
                    Toast.makeText(BannerAndMediumRectangleActivity.this,
                            "Failed to load ad ",
                            Toast.LENGTH_SHORT).show();
                }
            });
            adRequest.requestAd();
        });

        showAdFromQueue.setOnClickListener(v -> {
            reservedForAd.removeAllViews();

            if (displayedAd != null) {
                displayedAd.close();
            }
            displayedAd = receivedAds.poll();
            View adView = DIOAdViewBinder.getAdView(displayedAd, BannerAndMediumRectangleActivity.this);
            if (adView != null) {
                reservedForAd.addView(adView);
                reservedForAd.setVisibility(View.VISIBLE);
            } else {
                reservedForAd.setVisibility(View.GONE);
                Toast.makeText(BannerAndMediumRectangleActivity.this,
                        "Load ad first",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    //in case you do not need ads always call close on ad object to avoid memory leaks
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (displayedAd != null) {
            displayedAd.close();
        }
        if (!receivedAds.isEmpty()) {
            for (Ad ad : receivedAds) {
                ad.close();
            }
        }
    }
}
