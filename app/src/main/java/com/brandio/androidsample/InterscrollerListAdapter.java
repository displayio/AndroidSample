package com.brandio.androidsample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandio.ads.Controller;
import com.brandio.ads.InterscrollerPlacement;
import com.brandio.ads.containers.InterscrollerContainer;

import java.util.ArrayList;
import java.util.List;

public class InterscrollerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "InterscrollerAdapter";
    private static final byte AD_VIEW_TYPE = 0;
    private static final byte SIMPLE_VIEW_TYPE = 2;

    private String placementId;
    private String requestId;
    private List<Integer> items;
    private Context context;

    public InterscrollerListAdapter(List<Integer> items, int adPosition, String placementId, String requestId) {
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
            ViewGroup adView = InterscrollerContainer.getAdView(context);
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
                InterscrollerPlacement placement = (InterscrollerPlacement)Controller.getInstance().getPlacement(placementId);
                InterscrollerContainer container = placement.getContainer(context, requestId, position);
//                container.setInterscrollerHeight(1500);
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
