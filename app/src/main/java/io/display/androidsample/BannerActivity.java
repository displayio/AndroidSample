package io.display.androidsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import io.display.sdk.AdProvider;
import io.display.sdk.AdRequest;
import io.display.sdk.Controller;
import io.display.sdk.Placement;
import io.display.sdk.ads.Ad;
import io.display.sdk.exceptions.DioSdkException;
import io.display.sdk.listeners.AdLoadListener;
import io.display.sdk.listeners.AdRequestListener;

public class BannerActivity extends AppCompatActivity {

    private static String TAG = "BannerActivity";

    private Button loadButton;
    private Button showButton;

    private String placementId;
    private String requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        placementId = getIntent().getStringExtra("placementId");

        loadButton = findViewById(R.id.button_load_banner);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd();
            }
        });

        showButton = findViewById(R.id.button_show_banner);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAd();
            }
        });
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
                    }

                    @Override
                    public void onFailedToLoad() {
                        Toast.makeText(BannerActivity.this, "Ad for placement " + placementId + " failed to load", Toast.LENGTH_LONG).show();
                    }
                });

                try {
                    adProvider.loadAd();
                } catch(DioSdkException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }

            @Override
            public void onNoAds() {
                Toast.makeText(BannerActivity.this, "No Ads placement " + placementId, Toast.LENGTH_LONG).show();
            }
        });

        adRequest.requestAd();
    }

    private void showAd() {
        showButton.setEnabled(false);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_banner);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<String> items = Arrays.asList(
                "content item 1",
                "content item 2",
                "content item 3",
                "content item 4",
                "content item 5",
                "content item 6",
                "content item 7",
                "content item 8");

        recyclerView.setAdapter(new BannerListAdapter(items, 2, placementId, requestId));
    }
}
