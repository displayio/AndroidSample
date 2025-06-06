package com.brandio.androidsample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.brandio.ads.Controller;
import com.brandio.ads.ads.Ad;
import com.brandio.ads.exceptions.DIOError;
import com.brandio.ads.exceptions.DioSdkException;
import com.brandio.ads.listeners.AdEventListener;
import com.brandio.ads.listeners.AdRequestListener;
import com.brandio.ads.placements.Placement;
import com.brandio.ads.request.AdRequest;
import com.brandio.androidsample.tools.DIOAdRequestHelper;


public class InterstitialActivity extends AppCompatActivity {
    private static final String TAG = "InterstitialActivity";

    private Button loadButton;
    private Button showButton;
    private String placementId;
    private Ad loadedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        placementId = getIntent().getStringExtra(MainActivity.PLACEMENT_ID);

        loadButton = findViewById(R.id.button_load_interstitial);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd();
            }
        });

        showButton = findViewById(R.id.button_show_interstitial);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAd();
            }
        });
    }

    private void loadAd() {
        if (loadedAd != null) {
            loadedAd.close();
            loadedAd = null;
        }
        loadButton.setEnabled(false);

        Placement placement;
        try {
            placement = Controller.getInstance().getPlacement(placementId);
        } catch (DioSdkException e) {
            Log.e(TAG, e.getLocalizedMessage());
            return;
        }

//                final AdRequest adRequest = placement.newAdRequest(); // use default ad request
        final AdRequest adRequest = DIOAdRequestHelper.createAndPopulateAdRequest(placement); // use customised ad request
        adRequest.setAdRequestListener(new AdRequestListener() {

            @Override
            public void onAdReceived(Ad ad) {
                loadedAd = ad;
                showButton.setEnabled(true);
            }

            @Override
            public void onNoAds(DIOError error) {
                Toast.makeText(InterstitialActivity.this, "No Ads placement " + placementId, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailedToLoad(DIOError dioError) {
                Toast.makeText(InterstitialActivity.this, "Ad for placement " + placementId + " failed to load", Toast.LENGTH_LONG).show();

            }
        });
        adRequest.requestAd();
    }

    private void showAd() {
        showButton.setEnabled(false);

        loadedAd.setEventListener(new AdEventListener() {
            @Override
            public void onShown(Ad ad) {
                Log.e(TAG, "onShown");
                loadButton.setEnabled(true);
            }

            @Override
            public void onFailedToShow(Ad ad) {
                Log.e(TAG, "onFailedToShow");
                loadButton.setEnabled(true);
            }

            @Override
            public void onClicked(Ad ad) {
                Log.e(TAG, "onClicked");
            }

            @Override
            public void onClosed(Ad ad) {
                Log.e(TAG, "onClosed");
            }

            @Override
            public void onAdCompleted(Ad ad) {
                Log.e(TAG, "onAdCompleted");
            }
        });

        loadedAd.showAd(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadedAd != null) {
            loadedAd.close();
        }
    }
}
