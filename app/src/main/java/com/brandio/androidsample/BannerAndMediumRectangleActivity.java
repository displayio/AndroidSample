package com.brandio.androidsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.brandio.ads.AdProvider;
import com.brandio.ads.AdRequest;
import com.brandio.ads.BannerPlacement;
import com.brandio.ads.Controller;
import com.brandio.ads.MediumRectanglePlacement;
import com.brandio.ads.Placement;
import com.brandio.ads.ads.Ad;
import com.brandio.ads.exceptions.DIOError;
import com.brandio.ads.exceptions.DioSdkException;
import com.brandio.ads.listeners.AdLoadListener;
import com.brandio.ads.listeners.AdRequestListener;

public class BannerAndMediumRectangleActivity extends AppCompatActivity {

    private static String TAG = "BannerAndMediumRectangleActivity";
    private String placementId;
    private String adUnitType;
    RelativeLayout adLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_and_medium_rectangle);

        placementId = getIntent().getStringExtra(MainActivity.PLACEMENT_ID);
        adUnitType = getIntent().getStringExtra(MainActivity.AD_UNIT_TYPE);

        adLayout = findViewById(R.id.reserved_for_ad);
        loadAd();


    }

    private void loadAd() {

        Placement placement;
        try {
            placement = Controller.getInstance().getPlacement(placementId);
        } catch (DioSdkException e) {
            Log.e(TAG, e.getLocalizedMessage());
            return;
        }

        final AdRequest adRequest = placement.newAdRequest();
        adRequest.setAdRequestListener(new AdRequestListener() {
            @Override
            public void onAdReceived(AdProvider adProvider) {

                adProvider.setAdLoadListener(new AdLoadListener() {
                    @Override
                    public void onLoaded(Ad ad) {
                        switch (adUnitType){
                            case "BANNER" :{
                                try {
                                    BannerPlacement bannerPlacement = (BannerPlacement) Controller.getInstance().getPlacement(placementId);
                                    View bannerView = bannerPlacement.getBanner(BannerAndMediumRectangleActivity.this, adRequest.getId());
                                    adLayout.addView(bannerView);
                                    adLayout.setVisibility(View.VISIBLE);
                                } catch (DioSdkException e) {
                                    Log.e(TAG, e.getLocalizedMessage());
                                }
                                break;
                            }
                            case "MEDIUM_RECTABGLE" :{
                                try {
                                    MediumRectanglePlacement mediumRectanglePlacement = (MediumRectanglePlacement) Controller.getInstance().getPlacement(placementId);
                                    View mediumRectView = mediumRectanglePlacement.getMediumRectangle(BannerAndMediumRectangleActivity.this, adRequest.getId());
                                    adLayout.addView(mediumRectView);
                                    adLayout.setVisibility(View.VISIBLE);
                                } catch (DioSdkException e) {
                                    Log.e(TAG, e.getLocalizedMessage());
                                }
                                break;
                            }
                        }

                    }

                    @Override
                    public void onFailedToLoad(DIOError error) {
                        Toast.makeText(BannerAndMediumRectangleActivity.this, "Ad for placement " + placementId + " failed to load", Toast.LENGTH_LONG).show();
                    }
                });

                try {
                    adProvider.loadAd();
                } catch(DioSdkException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }

            @Override
            public void onNoAds(DIOError error) {
                Toast.makeText(BannerAndMediumRectangleActivity.this, "No Ads placement " + placementId, Toast.LENGTH_LONG).show();
            }
        });

        adRequest.requestAd();
    }
}
