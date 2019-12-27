package com.brandio.androidsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import static com.brandio.androidsample.MainActivity.*;

public class ShowListWithInfeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private String placementId;
    private String requestId;
    private String adUnitType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_with_infeeds);

        placementId = getIntent().getStringExtra(PLACEMENT_ID);
        requestId = getIntent().getStringExtra(REQUEST_ID);
        adUnitType = getIntent().getStringExtra(AD_UNIT_TYPE);


        recyclerView = findViewById(R.id.recycler_view_infeeds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ArrayList<Integer> items = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            items.add(i);
        }

        switch (adUnitType){
            case "INFEED" :{
                recyclerView.setAdapter(new InfeedListAdapter(items, 12, placementId, requestId));
                break;
            }
            case "FEED_INTERSTITIAL" :{
                recyclerView.setAdapter(new FeedInterstitialListAdapter(items, 12, placementId, requestId));

                break;
            }
        }



    }
}
