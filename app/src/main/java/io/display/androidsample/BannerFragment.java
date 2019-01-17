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
import android.widget.TextView;

import io.display.androidsample.utils.FragmentHelper;

public class BannerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_banner, container, false);

        Bundle args = getArguments();
        String placementId = args.getString("placementId");
        Log.i("XXX", "PLACEMENT ID: " + placementId);

        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeAsUpIndicator(0);
        actionBar.setTitle("Banner");

        setHasOptionsMenu(true);
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
