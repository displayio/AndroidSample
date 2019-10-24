package com.brandio.androidsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.brandio.ads.AdProvider;
import com.brandio.ads.AdRequest;
import com.brandio.ads.Controller;
import com.brandio.ads.Placement;
import com.brandio.ads.ads.Ad;
import com.brandio.ads.exceptions.DioSdkException;
import com.brandio.ads.listeners.AdLoadListener;
import com.brandio.ads.listeners.AdRequestListener;

import java.util.ArrayList;


public class InfeedActivity extends AppCompatActivity {

    private static String TAG = "InfeedActivity";

    private Button loadButton;
    private Button showButton;

    private String placementId;
    private String requestId;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infeed);


        placementId = getIntent().getStringExtra("placementId");

        loadButton = findViewById(R.id.button_load_infeed);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd();
            }
        });

        showButton = findViewById(R.id.button_show_infeed);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfeedActivity.this, ShowListWithInfeedActivity.class);
                intent.putExtra("placementId", placementId);
                intent.putExtra("requestId", requestId);
                startActivity(intent);
                //showAd();
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
                    }

                    @Override
                    public void onFailedToLoad() {
                        Toast.makeText(InfeedActivity.this, "Ad for placement " + placementId + " failed to load", Toast.LENGTH_LONG).show();
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
                Toast.makeText(InfeedActivity.this, "No Ads placement " + placementId, Toast.LENGTH_LONG).show();
            }
        });

        adRequest.setDetailsRequired(true);
        adRequest.requestAd();
    }

    private void showAd() {
        showButton.setEnabled(false);
        setupRecyclerView();
    }

    private void setupRecyclerView() {

        recyclerView = findViewById(R.id.recycler_view_infeeds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ArrayList<String> items = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            items.add("Content item " + i);
        }

        recyclerView.setAdapter(new InfeedListAdapter(items, 2, placementId, requestId));
    }
}