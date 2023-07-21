package com.brandio.androidsample;

import static com.brandio.androidsample.MainActivity.PLACEMENT_ID;
import static com.brandio.androidsample.MainActivity.REQUEST_ID;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.brandio.ads.Controller;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ViewPagerActivity extends AppCompatActivity {
    public static final String TAG = "ViewPagerActivity";
    private boolean soundEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        String placementId = getIntent().getStringExtra(PLACEMENT_ID);
        String requestId = getIntent().getStringExtra(REQUEST_ID);

        ViewPager2 viewPager = findViewById(R.id.view_pager2);
        final ArrayList<Integer> items = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            items.add(i);
        }
        viewPager.setAdapter(new InterscrollerListAdapter(items, 1, placementId, requestId, true));

        FloatingActionButton customSoundControl = findViewById(R.id.custom_sound);
        customSoundControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Controller.getInstance()
                            .getPlacement(getIntent().getStringExtra(PLACEMENT_ID))
                            .getAdRequestById(getIntent().getStringExtra(REQUEST_ID))
                            .getAdProvider().getAd().toggleSound(!soundEnabled);
                    soundEnabled = !soundEnabled;
                } catch (Exception e) {
                    Log.e(TAG, "Error. Cannot change sound settings");
                    e.printStackTrace();
                }
            }
        });
    }
}