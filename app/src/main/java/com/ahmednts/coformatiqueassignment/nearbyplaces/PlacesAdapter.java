package com.ahmednts.coformatiqueassignment.nearbyplaces;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmednts.coformatiqueassignment.R;
import com.ahmednts.coformatiqueassignment.data.UnifiedPlaceDetails;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AhmedNTS on 6/2/2017.
 */
public class PlacesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UnifiedPlaceDetails> itemList;
    private ItemClickListener itemClickListener;

    PlacesAdapter(List<UnifiedPlaceDetails> itemList, ItemClickListener itemClickListener) {
        this.itemList = itemList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView;
        layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new FollowerViewHolder(layoutView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FollowerViewHolder) holder).onBindView(this, position);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public void replaceData(List<UnifiedPlaceDetails> items) {
        setList(items);
        notifyDataSetChanged();
    }

    private void setList(List<UnifiedPlaceDetails> items) {
        itemList = items;
    }

    static class FollowerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.placeName)
        TextView placeName;

        private ItemClickListener itemClickListener;

        FollowerViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.itemClickListener = itemClickListener;
        }

        void onBindView(PlacesAdapter adapter, int position) {
            UnifiedPlaceDetails item = adapter.itemList.get(position);

            placeName.setText(item.getName());

            itemView.setOnClickListener(v -> itemClickListener.onClick(item));
        }
    }

    interface ItemClickListener {
        void onClick(UnifiedPlaceDetails unifiedPlaceDetails);
    }
}
