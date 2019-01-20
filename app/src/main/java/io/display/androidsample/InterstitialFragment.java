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
import android.widget.Toast;

import io.display.androidsample.utils.FragmentHelper;
import io.display.sdk.AdProvider;
import io.display.sdk.AdRequest;
import io.display.sdk.Controller;
import io.display.sdk.Placement;
import io.display.sdk.ads.Ad;
import io.display.sdk.exceptions.DioSdkException;
import io.display.sdk.listeners.AdLoadListener;
import io.display.sdk.listeners.AdRequestListener;

public class InterstitialFragment extends Fragment {

    static String TAG = "XXX"; // XXX

    Button loadButton;
    Button showButton;

    String placementId;
    Ad loadedAd;

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
        placementId = args.getString("placementId");

        View contentView = inflater.inflate(R.layout.fragment_interstitial, container, false);
        loadButton = contentView.findViewById(R.id.button_load_interstitial);
        showButton = contentView.findViewById(R.id.button_show_interstitial);

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd();
            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAd();
            }
        });

        return contentView;
    }

    private void loadAd() {
        loadButton.setEnabled(false);

        Placement placement;
        try {
            placement = Controller.getInstance().getPlacement(placementId);
        } catch (DioSdkException e) {
            Log.e(TAG, e.getLocalizedMessage());
            return;
        }

        AdRequest adRequest = placement.newAdRequest();
        adRequest.setAdRequestListener(new AdRequestListener() {
            @Override
            public void onAdReceived(AdProvider adProvider) {

                adProvider.setAdLoadListener(new AdLoadListener() {
                    @Override
                    public void onLoaded(Ad ad) {
                        loadedAd = ad;
                        showButton.setEnabled(true);
                    }

                    @Override
                    public void onFailedToLoad() {
                        Toast.makeText(getContext(), "Ad for placement " + placementId + " failed to load", Toast.LENGTH_LONG).show();
                    }
                });

                try {
                    adProvider.loadAd();
                } catch(DioSdkException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }

            @Override
            public void onNoAds() {
                Toast.makeText(getContext(), "No Ads placement " + placementId, Toast.LENGTH_LONG).show();
            }
        });

        adRequest.requestAd();
    }

    private void showAd() {
        showButton.setEnabled(false);
        loadedAd.showAd(getContext());
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
