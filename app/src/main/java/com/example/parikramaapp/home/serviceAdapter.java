package com.example.parikramaapp.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parikramaapp.R;

import java.util.ArrayList;
import java.util.List;
public class serviceAdapter extends RecyclerView.Adapter<serviceAdapter.ViewHolder> {
    private List<serviceItem> services;
    private List<serviceItem> servicesFull;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    // Constructor
    serviceAdapter(Context context, List<serviceItem> services) {
        this.inflater = LayoutInflater.from(context);
        this.services = services;
        this.servicesFull = new ArrayList<>(services);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        serviceItem item = services.get(position);
        holder.tvCardTitle.setText(item.getTitle());
        holder.iconImage.setImageResource(item.getIconRes());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvCardTitle;
        ImageView iconImage;

        ViewHolder(View itemView) {
            super(itemView);
            tvCardTitle = itemView.findViewById(R.id.tv_card_title);
            iconImage = itemView.findViewById(R.id.icon_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void filter(String text) {
        services.clear();
        if (text.isEmpty()) {
            services.addAll(servicesFull);
        } else {
            text = text.toLowerCase();
            for (serviceItem item : servicesFull) {
                if (item.getTitle().toLowerCase().contains(text)) {
                    services.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
