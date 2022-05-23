package com.brandio.androidsample;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brandio.ads.Controller;
import com.brandio.ads.InfeedPlacement;
import com.brandio.ads.ShoppablePlacement;
import com.brandio.ads.containers.InfeedAdContainer;
import com.brandio.ads.exceptions.DioSdkException;

import java.util.ArrayList;
import java.util.List;




public class ShoppableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ShoppableListAdapter";
    private static final int TYPE_AD = 0;
    private static final int TYPE_CONTENT = 1;

    private String placementId;
    private String requestId;
    private List<Integer> items;
    private Context context;

    public ShoppableListAdapter(List<Integer> items, int adPosition, String placementId, String requestId) {
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
                return new AdViewHolder(InfeedAdContainer.getAdView(context));
            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.infeed_list_item, parent, false);
                return  new ItemViewHolder(view);
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
                ShoppablePlacement infeedPlacement = (ShoppablePlacement) Controller.getInstance().getPlacement(placementId);

                View shoppableAdView = infeedPlacement.getShoppableAdView(context, requestId);
                if (shoppableAdView != null) {
                    ((ViewGroup) holder.itemView).addView(shoppableAdView);
                } else {
                    ((ViewGroup) holder.itemView).removeAllViews();
                }
            } catch (DioSdkException e) {
                Log.e(TAG, e.getLocalizedMessage());
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        ItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    class AdViewHolder extends RecyclerView.ViewHolder {

        AdViewHolder(View itemView) {
            super(itemView);
        }
    }
}

