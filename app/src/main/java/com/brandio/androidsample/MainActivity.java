package com.brandio.androidsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.brandio.ads.Controller;
import com.brandio.ads.listeners.SdkInitListener;
import com.brandio.androidsample.utils.RecyclerTouchListener;


public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private static final String APP_ID = "6494";

    private static final PlacementListItem[] data = {
            new PlacementListItem("4654", PlacementListItem.Type.INTERSTITIAL), // Html
            new PlacementListItem("3231", PlacementListItem.Type.INTERSTITIAL), // Video
            new PlacementListItem("4655", PlacementListItem.Type.BANNER),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Controller ctrl = Controller.getInstance();
        if (!ctrl.isInitialized()) {
            ctrl.getInstance().init(this, APP_ID, new SdkInitListener() {
                @Override
                public void onInit() {
                    Log.i(TAG, "Controller initialized");
                    postInit();
                }

                @Override
                public void onInitError(String msg) {
                    Log.e(TAG, msg);
                }
            });
        } else {
            postInit();
        }
    }

    private void postInit() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        PlacementListAdapter adapter = new PlacementListAdapter(data);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle args = new Bundle();
                args.putString("placementId", data[position].id);

                switch (data[position].type) {
                    case BANNER: {
                        Intent intent = new Intent(MainActivity.this, BannerActivity.class);
                        intent.putExtra("placementId", data[position].id);
                        startActivity(intent);
                        break;
                    }

                    case INTERSTITIAL: {
                        Intent intent = new Intent(MainActivity.this, InterstitialActivity.class);
                        intent.putExtra("placementId", data[position].id);
                        startActivity(intent);
                        break;
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {}
        }));
    }
}
