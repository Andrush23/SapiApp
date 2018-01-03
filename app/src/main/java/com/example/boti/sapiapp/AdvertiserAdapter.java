package com.example.boti.sapiapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Boti on 2017.11.10..
 */

public class AdvertiserAdapter extends RecyclerView.Adapter<AdvertiserAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Advertiser> items;

    public AdvertiserAdapter(Context context, ArrayList<Advertiser> items) {
        this.context=context;
        this.items = items;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    @Override
    public AdvertiserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.advertiser_item, parent, false);
        return new ViewHolder(v);
    }

    public void updateData(ArrayList<Advertiser> viewModels) {
        items.clear();
        items.addAll(viewModels);
        notifyDataSetChanged();
    }
    public void addItem(int position, Advertiser viewModel) {
        items.add(position, viewModel);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Advertiser item = items.get(position);
        holder.title.setText(item.getTitle());
        String image = item.getPhotos().get(0);
        GlideApp.with(context).load(image).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,AdvertiserDetails.class);
                intent.putExtra("title",item.getTitle());
                intent.putExtra("description",item.getDescription());
                intent.putStringArrayListExtra("photos",item.getPhotos());
                view.getContext().startActivity(intent);
            }});
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}