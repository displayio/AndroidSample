package io.display.androidsample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.display.androidsample.utils.FragmentHelper;
import io.display.androidsample.utils.RecyclerTouchListener;
import io.display.sdk.Controller;
import io.display.sdk.listeners.SdkInitListener;

public class MainFragment extends Fragment {

    static String TAG = "XXX"; // XXX

    static final String APP_ID = "6494";

    static final PlacementItem[] data = {
            new PlacementItem("4654", PlacementItem.Type.INTERSTITIAL),
            new PlacementItem("3231", PlacementItem.Type.INTERSTITIAL),
            new PlacementItem("4655", PlacementItem.Type.BANNER),
    };

    View contentView;

    @Override
    public void onStart() {
        super.onStart();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);

        Controller.getInstance().init(getContext(), APP_ID, new SdkInitListener() {
            @Override
            public void onInit() {
                Log.i(TAG, "Controller initialized");
                postInit();
            }

            @Override
            public void onInitError(String msg) {
                // TODO: Show toast
                Log.e(TAG, msg);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        contentView = inflater.inflate(R.layout.fragment_main, container, false);
        return contentView;
    }

    private void postInit()
    {
        RecyclerView recyclerView = contentView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

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
    }
}
