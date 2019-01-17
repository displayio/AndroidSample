package io.display.androidsample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PlacementListAdapter extends RecyclerView.Adapter<PlacementListAdapter.MyViewHolder> {

    private PlacementItem[] data;

    public PlacementListAdapter(PlacementItem[] data) {
        this.data = data;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textId;
        TextView textType;

        public MyViewHolder(View v) {
            super(v);

            textId = v.findViewById(R.id.text_id);
            textType = v.findViewById(R.id.text_type);
        }
    }

    @Override
    public PlacementListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textId.setText(data[position].id);
        holder.textType.setText(getTypeAsString(data[position].type));
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    private String getTypeAsString(PlacementItem.Type type) {
        switch (type) {
            case BANNER:
                return "Banner";

            default:
            case INTERSTITIAL:
                return "Interstitial";
        }
    }
}
