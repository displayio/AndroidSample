package com.brandio.androidsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.brandio.ads.AdProvider;
import com.brandio.ads.AdRequest;
import com.brandio.ads.Controller;
import com.brandio.ads.FeedInterstitialPlacement;
import com.brandio.ads.Placement;
import com.brandio.ads.ads.Ad;
import com.brandio.ads.ads.supers.FeedInterstittialAdInterface;
import com.brandio.ads.containers.FeedInterstitialContainer;
import com.brandio.ads.exceptions.DIOError;
import com.brandio.ads.exceptions.DioSdkException;
import com.brandio.ads.listeners.AdLoadListener;
import com.brandio.ads.listeners.AdRequestListener;


public class LoadInfeedActivity extends AppCompatActivity {

    public final static int AD_POSITION = 12;
    private static String TAG = "LoadInfeedActivity";

    private Button loadButton;
    private Button showButton;

    private String placementId;
    private String requestId;
    private String adUnitType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infeed);

        placementId = getIntent().getStringExtra(MainActivity.PLACEMENT_ID);
        adUnitType = getIntent().getStringExtra(MainActivity.AD_UNIT_TYPE);

        loadButton = findViewById(R.id.button_load_infeed);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd();
            }
        });

        showButton = findViewById(R.id.button_show_infeed);
        showButton.setEnabled(true);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoadInfeedActivity.this, ShowListWithInfeedActivity.class);
                intent.putExtra(MainActivity.PLACEMENT_ID, placementId);
                intent.putExtra(MainActivity.REQUEST_ID, requestId);
                intent.putExtra(MainActivity.AD_UNIT_TYPE, adUnitType);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadButton.setEnabled(true);
    }

    private void loadAd() {
        loadButton.setEnabled(false);

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
                        requestId = adRequest.getId();
                        showButton.setEnabled(true);
                        if (ad instanceof FeedInterstittialAdInterface){
                            try {
                                FeedInterstitialPlacement placement = (FeedInterstitialPlacement) Controller.getInstance().getPlacement(placementId);
                                FeedInterstitialContainer container = placement.getContainer(getApplicationContext(), requestId, AD_POSITION);
                                Controller.getInstance().setStoredContainer(container);
                            } catch (DioSdkException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailedToLoad(DIOError error) {
                        Toast.makeText(LoadInfeedActivity.this, "Ad for placement " + placementId + " failed to load", Toast.LENGTH_LONG).show();
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
                Toast.makeText(LoadInfeedActivity.this, "No Ads placement " + placementId, Toast.LENGTH_LONG).show();
            }
        });

        adRequest.requestAd();
    }

}
