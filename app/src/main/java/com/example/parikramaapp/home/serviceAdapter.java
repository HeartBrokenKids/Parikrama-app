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
    private List<serviceItem> allServices; // All possible services
    private List<serviceItem> displayedServices; // Services to be displayed based on filter

    // Constructor
    serviceAdapter(Context context, List<serviceItem> services) {
        this.inflater = LayoutInflater.from(context);
        this.allServices = services;
        this.displayedServices = new ArrayList<>(services); // Initially display all
    }

    public void filterDisplayedServices(boolean[] selectedStates) {
        displayedServices.clear();
        for (int i = 0; i < allServices.size(); i++) {
            if (selectedStates[i]) {
                displayedServices.add(allServices.get(i));
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        serviceItem item = displayedServices.get(position);
        holder.tvCardTitle.setText(item.getTitle());
        holder.iconImage.setImageResource(item.getIconRes());
    }

    @Override
    public int getItemCount() {
        return displayedServices.size();
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

    public List<serviceItem> getServices() {
        return services;
    }

    public List<serviceItem> getServicesFull() {
        return servicesFull;
    }

    public void setServices(List<serviceItem> services) {
        this.services = services;
        notifyDataSetChanged();
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
