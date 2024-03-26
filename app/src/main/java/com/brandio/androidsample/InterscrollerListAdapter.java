package com.brandio.androidsample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brandio.ads.Controller;
import com.brandio.ads.containers.InterscrollerContainer;
import com.brandio.ads.placements.InterscrollerPlacement;

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
    private boolean isViewPager;

    public InterscrollerListAdapter(List<Integer> items, int adPosition, String placementId, String requestId, boolean isViewPager) {
        this.placementId = placementId;
        this.requestId = requestId;
        this.isViewPager = isViewPager;

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
            if (isViewPager) {  // need because ViewPager require match_parent sizes
                adView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            return new ViewHolder(adView);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.infeed_list_item, parent, false);
        if (isViewPager) {
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
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
        if (holder.getItemViewType() == AD_VIEW_TYPE) {
            try {
                InterscrollerPlacement placement = (InterscrollerPlacement) Controller.getInstance().getPlacement(placementId);
                InterscrollerContainer container = placement.getContainer(context, requestId);
//                container.setInterscrollerHeight(1500);
//                container.setInterscrollerOffset(250);
                container.bindTo((ViewGroup) holder.itemView);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
