package com.brandio.androidsample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.brandio.ads.Controller;
import com.brandio.ads.ads.Ad;
import com.brandio.ads.containers.BannerContainer;
import com.brandio.ads.containers.InlineContainer;
import com.brandio.ads.containers.MediumRectangleContainer;
import com.brandio.ads.exceptions.DIOError;
import com.brandio.ads.exceptions.DioSdkException;
import com.brandio.ads.listeners.AdEventListener;
import com.brandio.ads.listeners.AdRequestListener;
import com.brandio.ads.placements.BannerPlacement;
import com.brandio.ads.placements.MediumRectanglePlacement;
import com.brandio.ads.placements.Placement;
import com.brandio.ads.request.AdRequest;
import com.brandio.androidsample.utils.DIOAdrequestHelper;

import java.util.ArrayDeque;

public class BannerAndMediumRectangleActivity extends AppCompatActivity {

    private static final String TAG = "BannerAndMRectActivity";
    private RelativeLayout reservedForAd;
    private Button loadNewAd;
    private Button showAdFromQueue;
    private final ArrayDeque<View> receivedAds = new ArrayDeque<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_and_medium_rectangle);
        reservedForAd = findViewById(R.id.reserved_for_ad);
        loadNewAd = findViewById(R.id.load_new_ad);
        showAdFromQueue = findViewById(R.id.show_ad_from_queue);

        String placementId = getIntent().getStringExtra(MainActivity.PLACEMENT_ID);
        String adUnitType = getIntent().getStringExtra(MainActivity.AD_UNIT_TYPE);

        setupButtons(placementId, adUnitType);
    }


    private View getBannerView(String placementId, String requestId) {
        ViewGroup bannerView = null;
        BannerPlacement bannerPlacement = null;
        try {
            bannerPlacement = ((BannerPlacement) Controller.getInstance().getPlacement(placementId));
        } catch (DioSdkException e) {
            e.printStackTrace();
        }
        if (bannerPlacement != null) {
            BannerContainer container = bannerPlacement.getContainer(this, requestId);
            bannerView = InlineContainer.getAdView(this);
            container.bindTo(bannerView);
        }
        return bannerView;
    }

    private View getMrectView(String placementId, String requestId) {
        ViewGroup mrectView = null;
        MediumRectanglePlacement mediumRectanglePlacement = null;
        try {
            mediumRectanglePlacement = ((MediumRectanglePlacement) Controller.getInstance().getPlacement(placementId));
        } catch (DioSdkException e) {
            e.printStackTrace();
        }
        if (mediumRectanglePlacement != null) {
            MediumRectangleContainer container = mediumRectanglePlacement.getContainer(this, requestId);
            mrectView = InlineContainer.getAdView(this);
            container.bindTo(mrectView);
        }
        return mrectView;
    }

    private void setupButtons(final String placementId, final String placementType) {

        loadNewAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Placement placement;
                try {
                    placement = Controller.getInstance().getPlacement(placementId);
                } catch (DioSdkException e) {
                    e.printStackTrace();
                    Toast.makeText(BannerAndMediumRectangleActivity.this,
                            "DioSdkException: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

//                final AdRequest adRequest = placement.newAdRequest(); // use default ad request
                final AdRequest adRequest = DIOAdrequestHelper.createAndPopulateAdRequest(placement); // use customised ad request
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

                        if (placementType.equals("BANNER")) {
                            receivedAds.addLast(getBannerView(placementId, adRequest.getId()));
                        } else {
                            receivedAds.addLast(getMrectView(placementId, adRequest.getId()));
                        }
                        Log.e(TAG, "adView added to queue, ads in queue = " + receivedAds.size());
                        Toast.makeText(BannerAndMediumRectangleActivity.this,
                                "adView added to queue, ads in queue = " + receivedAds.size(),
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
            }
        });

        showAdFromQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View adView = receivedAds.poll();
                if (adView != null) {
                    reservedForAd.removeView(adView);
                    reservedForAd.addView(adView);
                    reservedForAd.setVisibility(View.VISIBLE);
                } else {
                    reservedForAd.removeView(adView);
                    reservedForAd.setVisibility(View.GONE);

                    Log.e(TAG, "adView == null, ads in queue = " + receivedAds.size());
                    Toast.makeText(BannerAndMediumRectangleActivity.this,
                            "adView == null, ads in queue = " + receivedAds.size(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
