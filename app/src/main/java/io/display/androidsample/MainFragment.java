package io.display.androidsample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.display.androidsample.utils.FragmentHelper;
import io.display.androidsample.utils.RecyclerTouchListener;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View contentView = inflater.inflate(R.layout.fragment_main, container, false);

        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        final PlacementItem[] data = {
                new PlacementItem("4654", PlacementItem.Type.INTERSTITIAL),
                new PlacementItem("3231", PlacementItem.Type.INTERSTITIAL),
                new PlacementItem("4655", PlacementItem.Type.BANNER),
        };

        PlacementAdapter adapter = new PlacementAdapter(data);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle args = new Bundle();
                args.putString("placementId", data[position].id);

                switch (data[position].type) {
                    case BANNER: {
                        BannerFragment fragment = new BannerFragment();
                        fragment.setArguments(args);
                        FragmentHelper.performTransaction(getActivity(), fragment);
                        break;
                    }

                    case INTERSTITIAL: {
                        InterstitialFragment fragment = new InterstitialFragment();
                        fragment.setArguments(args);
                        FragmentHelper.performTransaction(getActivity(), fragment);
                        break;
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {}
        }));

        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
    }
}
