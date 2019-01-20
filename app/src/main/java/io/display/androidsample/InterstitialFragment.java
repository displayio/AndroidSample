package io.display.androidsample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.display.androidsample.utils.FragmentHelper;

public class InterstitialFragment extends Fragment {

    Button loadButton;
    Button showButton;

    @Override
    public void onStart() {
        super.onStart();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeAsUpIndicator(0);
        actionBar.setTitle("Interstitial");

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        String placementId = args.getString("placementId");

        View contentView = inflater.inflate(R.layout.fragment_interstitial, container, false);
        loadButton = contentView.findViewById(R.id.button_load_interstitial);
        showButton = contentView.findViewById(R.id.button_show_interstitial);

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("XXX", "LOAD");
            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("XXX", "SHOW");
            }
        });

        return contentView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                FragmentHelper.performBack(getActivity());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
