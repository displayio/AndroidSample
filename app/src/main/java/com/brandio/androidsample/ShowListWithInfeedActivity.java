package com.brandio.androidsample;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.brandio.ads.Controller;
import com.brandio.ads.ads.OutStreamVideo;
import com.brandio.ads.listeners.OutStreamVideoSnapListener;

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

//        final Window window = getWindow();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION | WindowManager.LayoutParams
//                    .FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION | WindowManager
//                    .LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            window.getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//        }

        placementId = getIntent().getStringExtra(PLACEMENT_ID);
        requestId = getIntent().getStringExtra(REQUEST_ID);
        adUnitType = getIntent().getStringExtra(AD_UNIT_TYPE);


        recyclerView = findViewById(R.id.recycler_view_infeeds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        final ArrayList<Integer> items = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            items.add(i);
        }

        switch (adUnitType){
            case "INFEED" :{
                recyclerView.setAdapter(new InfeedListAdapter(items, AD_POSITION, placementId, requestId));
                break;
            }
            case "OUTSTREAMVIDEO" :{
                final OutStreamVideoListAdapter adapter = new OutStreamVideoListAdapter(items, AD_POSITION, placementId, requestId);
                recyclerView.setAdapter(adapter);

                    recyclerView.addOnScrollListener(new OutStreamVideoSnapListener(AD_POSITION) {
                        @Override
                        public void removeAdPositionFromList(int adPosition) {
                            items.remove(adPosition);
                            adapter.notifyDataSetChanged();
                        }
                    });

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
