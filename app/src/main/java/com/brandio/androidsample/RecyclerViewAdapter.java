package com.brandio.androidsample;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brandio.ads.containers.InlineContainer;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final byte AD_VIEW_TYPE = 0;
    private static final byte SIMPLE_VIEW_TYPE = 2;

    private final List<View> dataSet;
    private final boolean isViewPager;

    public RecyclerViewAdapter(List<View> dataSet, boolean isViewPager) {
        this.dataSet = dataSet;
        this.isViewPager = isViewPager;
    }

    public RecyclerViewAdapter(List<View> dataSet) {
        this(dataSet, false);
    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup viewGroup;
        if (viewType == AD_VIEW_TYPE) {
            viewGroup = InlineContainer.getAdView(parent.getContext().getApplicationContext());
        } else {
            viewGroup = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.infeed_list_item, parent, false);
        }

        if (isViewPager) {  // need because ViewPager require match_parent sizes
            viewGroup.setLayoutParams(new ViewGroup.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        }
        return new ViewHolder(viewGroup);

    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position) == null ? SIMPLE_VIEW_TYPE : AD_VIEW_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder.getItemViewType() == AD_VIEW_TYPE) {
            ((ViewGroup) holder.itemView).removeAllViews();
            View view = dataSet.get(position);
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            ((ViewGroup) holder.itemView).addView(dataSet.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
