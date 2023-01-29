package com.yousef_shora.omniaclient.modal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.yousef_shora.omniaclient.R;
import com.yousef_shora.omniaclient.pojo.Offer;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    ArrayList<Offer> offers;
    private OnOfferClickListener listener;
    Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView offerTitle;
        TextView offerCategory;
        ImageView offerImage;
        LinearLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            offerTitle = itemView.findViewById(R.id.offer_title_textview);
            offerCategory = itemView.findViewById(R.id.offer_cateogry_textview);
            offerImage = itemView.findViewById(R.id.offer_image);
            container = itemView.findViewById(R.id.container);
        }
    }


    public RecyclerViewAdapter(ArrayList<Offer> offers, OnOfferClickListener listener, Context context) {
        this.offers = offers;
        this.listener = listener;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_offer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.offerTitle.setText(offers.get(position).getTitle());
        holder.offerCategory.setText(offers.get(position).getCategory());
        holder.offerTitle.setText(offers.get(position).getTitle());
        String picture_url = offers.get(position).getPicture_url();
        if (picture_url != null && !picture_url.isEmpty()) {
            Picasso.get().load(offers.get(position).getPicture_url()).into(holder.offerImage);
        }
        holder.container.setOnClickListener(view -> {

            listener.onItemClicked(offers.get(position));
        });
    }


    @Override
    public int getItemCount() {
        if (offers != null)
            return offers.size();
        else
            return 0;
    }
}
