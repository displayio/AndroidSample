package io.display.androidsample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.display.sdk.BannerPlacement;
import io.display.sdk.Controller;
import io.display.sdk.ads.BannerAdContainer;
import io.display.sdk.exceptions.DioSdkException;

public class BannerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "BannerListAdapter";

    // TODO: Use Enum
    private static final byte AD_VIEW_TYPE = 0;
    private static final byte SIMPLE_VIEW_TYPE = 1;

    private String placementId;
    private String requestId;
    private List<String> items;
    private Context context;

    public BannerListAdapter(List<String> items, int adPosition, String placementId, String requestId) {
        this.placementId = placementId;
        this.requestId = requestId;

        this.items = new ArrayList<>();
        this.items.addAll(items);
        this.items.add(adPosition, null);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext().getApplicationContext();
        switch (viewType) {
            case AD_VIEW_TYPE:
                return new AdViewHolder(BannerAdContainer.getAdView(context));
            default:
                return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_banner, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) == null) {
            return AD_VIEW_TYPE;
        } else {
            return SIMPLE_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == AD_VIEW_TYPE && holder instanceof AdViewHolder) {
            try {
                BannerAdContainer bannerContainer = ((BannerPlacement) Controller.getInstance().getPlacement(placementId)).getBannerContainer(context, requestId);
                bannerContainer.bindTo((RelativeLayout) holder.itemView);
            } catch (DioSdkException e) {
                Log.e(TAG, e.getLocalizedMessage());
            }
        } else {
            ((ItemViewHolder) holder).textLine.setText(items.get(holder.getAdapterPosition()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView textLine;

        ItemViewHolder(View itemView) {
            super(itemView);
            textLine = itemView.findViewById(R.id.text_line);
        }
    }

    class AdViewHolder extends RecyclerView.ViewHolder {

        AdViewHolder(View itemView) {
            super(itemView);
        }
    }
}

