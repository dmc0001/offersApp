package com.yousef_shora.omniaclient.modal;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.yousef_shora.omniaclient.R;
import com.yousef_shora.omniaclient.pojo.Filter;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {

    private Context context;
    private final List<Filter> filters;
    private static OnFilterClickListener listener;
    int singleitem_selection_position = -1;

    public FilterAdapter(Context context, List<Filter> filters, OnFilterClickListener listener) {
        this.context = context;
        this.filters = filters;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        ImageView mImage;
        CardView container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.filter_title);
            mImage = itemView.findViewById(R.id.filter_image);
            container = itemView.findViewById(R.id.container);
            container.setOnClickListener(view -> {
                int position = getAdapterPosition();
                listener.onItemClicked(filters.get(position));
                setSingleSelection(getAdapterPosition());
            });
        }
    }

    private void setSingleSelection(int adapterPosition) {
        if (adapterPosition == RecyclerView.NO_POSITION) return;

        notifyItemChanged(singleitem_selection_position);

        singleitem_selection_position = adapterPosition;

        notifyItemChanged(singleitem_selection_position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_filter, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Filter app = filters.get(position);

        holder.mName.setText(app.getName());
        holder.mImage.setImageResource(app.getImage());

        if (singleitem_selection_position == position) {
            set_blue_card(holder);
        } else {
            set_white_card(holder);
        }
    }

    public void set_white_card(MyViewHolder holder) {
        int black_color = context.getResources().getColor(R.color.black);
        int blue_color = context.getResources().getColor(R.color.icon_color_blue);
        int white_color = context.getResources().getColor(R.color.white);
        holder.container.setCardBackgroundColor(white_color);
        holder.mImage.setColorFilter(blue_color);
        holder.mName.setTextColor(black_color);
    }

    public void set_blue_card(MyViewHolder holder) {
        int blue_color = context.getResources().getColor(R.color.icon_color_blue);
        int white_color = context.getResources().getColor(R.color.white);
        holder.container.setCardBackgroundColor(blue_color);
        holder.mImage.setColorFilter(white_color);
        holder.mName.setTextColor(white_color);
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }


}