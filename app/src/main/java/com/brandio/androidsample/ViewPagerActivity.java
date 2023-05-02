package com.brandio.androidsample;

import static com.brandio.androidsample.MainActivity.PLACEMENT_ID;
import static com.brandio.androidsample.MainActivity.REQUEST_ID;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class ViewPagerActivity extends AppCompatActivity {

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
        viewPager.setAdapter(new InterscrollerListAdapter(items, 3, placementId, requestId, true));
    }
}