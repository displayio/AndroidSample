package com.brandio.androidsample;

import static com.brandio.androidsample.tools.DIOAdViewBinder.getAdView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brandio.ads.Controller;
import com.brandio.ads.ads.Ad;
import com.brandio.ads.exceptions.DIOError;
import com.brandio.ads.listeners.AdEventListener;
import com.brandio.ads.listeners.AdRequestListener;
import com.brandio.ads.placements.Placement;
import com.brandio.ads.request.AdRequest;
import com.brandio.androidsample.tools.DIOAdRequestHelper;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    public static final String TAG = "FeedActivity";
    private final int initialAdPosition = 10;
    private final List<View> dataSet = new ArrayList<>(400);
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private Placement placement;
    private final ArrayList<Ad> receivedAds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        String placementId = getIntent().getStringExtra(MainActivity.PLACEMENT_ID);

        try {
            placement = Controller.getInstance().getPlacement(placementId);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(FeedActivity.this,
                    "DioSdkException: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        for (int i = 0; i <= 400; i++) {
            dataSet.add(null);
        }
        adapter = new RecyclerViewAdapter(dataSet);
        recyclerView.setAdapter(adapter);

        for (int i = 1; i <= 10; i++) {
            loadAd(i);
        }
    }

    private void loadAd(int num) {
//        final AdRequest adRequest = placement.newAdRequest(); // use default ad request
        final AdRequest adRequest = DIOAdRequestHelper.createAndPopulateAdRequest(placement); // use customised ad request
        adRequest.setAdRequestListener(new AdRequestListener() {
            @Override
            public void onAdReceived(Ad ad) {
                ad.setEventListener(new AdEventListener() {
                    @Override
                    public void onShown(Ad ad) {
                        Log.e(TAG, "onShown");
                    }

                    @Override
                    public void onFailedToShow(Ad ad) {
                        Log.e(TAG, "onFailedToShow");
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
                receivedAds.add(ad);
                dataSet.set(initialAdPosition * num, getAdView(ad, FeedActivity.this));
                adapter.notifyItemChanged(initialAdPosition + num);
            }

            @Override
            public void onNoAds(DIOError error) {
                Toast.makeText(FeedActivity.this,
                        "No Ads placement " + placement.getId(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailedToLoad(DIOError dioError) {
                Toast.makeText(FeedActivity.this,
                        "Ad for placement " + placement.getId() + " failed to load", Toast.LENGTH_LONG).show();

            }
        });

        adRequest.requestAd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Ad ad : receivedAds) {
            ad.close();
        }
    }
}
