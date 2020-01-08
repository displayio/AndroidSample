package com.brandio.androidsample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.brandio.ads.Controller;
import com.brandio.ads.FeedInterstitialPlacement;
import com.brandio.ads.containers.FeedInterstitialContainer;

import java.util.ArrayList;
import java.util.List;

public class FeedInterstitialListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "FeedInterstitialAdapter";
    private static final byte AD_VIEW_TYPE = 0;
    private static final byte SIMPLE_VIEW_TYPE = 2;

    private String placementId;
    private String requestId;
    private List<Integer> items;
    private Context context;

    public FeedInterstitialListAdapter(List<Integer> items, int adPosition, String placementId, String requestId) {
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
        if (viewType == AD_VIEW_TYPE) {
            ViewGroup adView = FeedInterstitialContainer.getAdView(context);
//            RelativeLayout.LayoutParams adViewLayoutParamsl = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            RelativeLayout adView = new RelativeLayout(context);
//            adView.setLayoutParams(adViewLayoutParamsl);

            AdHolder adHolder = new AdHolder(adView);
            adHolder.setParent(parent);
            return adHolder;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.infeed_list_item, parent, false);
        return new ViewHolder(view);

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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder.getItemViewType() == AD_VIEW_TYPE && holder instanceof AdHolder) {
            try {
//                FeedInterstitialContainer container = Controller.getInstance().getStoredContainer();
//                if (container != null) {
//                    container.bindTo((ViewGroup)holder.itemView, ((AdHolder)holder).parent);
//                } else {
//                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                            ViewGroup.LayoutParams.WRAP_CONTENT, 0);
//                    holder.itemView.setLayoutParams(params);
//                }
                FeedInterstitialPlacement placement = (FeedInterstitialPlacement)Controller.getInstance().getPlacement(placementId);
                FeedInterstitialContainer container = placement.getContainer(context, requestId, position);
                container.bindTo((ViewGroup) holder.itemView, ((AdHolder)holder).parent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class AdHolder extends RecyclerView.ViewHolder {
        public ViewGroup parent;
        AdHolder(View itemView) {
            super(itemView);
        }
        public void setParent(ViewGroup parent) {
            this.parent = parent;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
