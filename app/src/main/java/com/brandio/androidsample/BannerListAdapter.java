package com.brandio.androidsample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brandio.ads.BannerPlacement;
import com.brandio.ads.Controller;
import com.brandio.ads.ads.BannerAdContainer;
import com.brandio.ads.exceptions.DioSdkException;

import java.util.ArrayList;
import java.util.List;



public class BannerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "BannerListAdapter";

    private static final int TYPE_AD = 0;
    private static final int TYPE_CONTENT = 1;

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
            case TYPE_AD:
                return new AdViewHolder(BannerAdContainer.getAdView(context));
            default:
                return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_banner, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) == null) {
            return TYPE_AD;
        } else {
            return TYPE_CONTENT;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_AD && holder instanceof AdViewHolder) {
            try {
                BannerPlacement bannerPlacement = (BannerPlacement) Controller.getInstance().getPlacement(placementId);
                bannerPlacement.setFullWidth(true);
                BannerAdContainer bannerContainer = bannerPlacement.getBannerContainer(context, requestId);
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

