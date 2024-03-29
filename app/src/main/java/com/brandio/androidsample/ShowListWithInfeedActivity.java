package com.brandio.androidsample;

import static com.brandio.androidsample.LoadInfeedActivity.AD_POSITION;
import static com.brandio.androidsample.MainActivity.AD_UNIT_TYPE;
import static com.brandio.androidsample.MainActivity.PLACEMENT_ID;
import static com.brandio.androidsample.MainActivity.REQUEST_ID;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brandio.ads.ads.AdUnitType;
import com.brandio.ads.listeners.HeadlineSnapListener;

import java.util.ArrayList;

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

        AdUnitType type = AdUnitType.valueOf(adUnitType);
        switch (type) {
            case INFEED: {
                recyclerView.setAdapter(new InfeedListAdapter(items, AD_POSITION, placementId, requestId));
                break;
            }
            case HEADLINE: {
                final HeadlineListAdapter adapter = new HeadlineListAdapter(items, AD_POSITION, placementId, requestId);
                recyclerView.setAdapter(adapter);

                recyclerView.addOnScrollListener(new HeadlineSnapListener(AD_POSITION) {
                    @Override
                    public void removeAdPositionFromList(int adPosition) {
                        // will be called when snap was occurred
                        items.remove(adPosition);
                        adapter.notifyDataSetChanged();
                    }
                });

                break;
            }
            case INTERSCROLLER: {
                recyclerView.setAdapter(new InterscrollerListAdapter(items, AD_POSITION, placementId, requestId, false));
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
