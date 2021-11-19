package com.brandio.androidsample;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class PlacementListAdapter extends RecyclerView.Adapter<PlacementListAdapter.MyViewHolder> {

    private PlacementListItem[] data;

    public PlacementListAdapter(PlacementListItem[] data) {
        this.data = data;
    }

    @Override
    public PlacementListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_main, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textId.setText(data[position].id);
        holder.textType.setText(getTypeAsString(position));
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    private String getTypeAsString(int position) {
        return data[position].name;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textId;
        TextView textType;

        public MyViewHolder(View v) {
            super(v);

            textId = v.findViewById(R.id.text_id);
            textType = v.findViewById(R.id.text_type);
        }
    }
}
