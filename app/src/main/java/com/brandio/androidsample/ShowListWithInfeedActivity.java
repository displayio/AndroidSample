package com.brandio.androidsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.brandio.ads.Controller;

import java.util.ArrayList;

import static com.brandio.androidsample.LoadInfeedActivity.AD_POSITION;
import static com.brandio.androidsample.MainActivity.AD_UNIT_TYPE;
import static com.brandio.androidsample.MainActivity.PLACEMENT_ID;
import static com.brandio.androidsample.MainActivity.REQUEST_ID;

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
                recyclerView.setAdapter(new InfeedListAdapter(items, AD_POSITION, placementId, requestId));
                break;
            }
            case "INTERSCROLLER" :{
                recyclerView.setAdapter(new InterscrollerListAdapter(items, AD_POSITION, placementId, requestId));

                break;
            }
        }

    }

    @Override
    protected void onDestroy() {
        Controller.getInstance().setStoredContainer(null);
        super.onDestroy();
    }
}
