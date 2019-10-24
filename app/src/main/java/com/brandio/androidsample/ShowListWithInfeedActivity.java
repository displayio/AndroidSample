package com.brandio.androidsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ShowListWithInfeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private String placementId;
    private String requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_with_infeeds);

        placementId = getIntent().getStringExtra("placementId");
        requestId = getIntent().getStringExtra("requestId");

        recyclerView = findViewById(R.id.recycler_view_infeeds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ArrayList<String> items = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            items.add("Content item " + i);
        }

        recyclerView.setAdapter(new InfeedListAdapter(items, 2, placementId, requestId));
    }
}
